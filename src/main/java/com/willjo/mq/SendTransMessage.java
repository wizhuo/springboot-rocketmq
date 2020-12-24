package com.willjo.mq;

import com.willjo.dal.entity.MqTransMessageEntity;
import com.willjo.service.MqTransMessageService;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Objects;

/**
 * @author willJo
 * @since 2020/12/16
 */
@Component
public class SendTransMessage  implements  ApplicationListener<ApplicationReadyEvent>{

    private static final Logger logger = LoggerFactory.getLogger(SendTransMessage.class);

    @Autowired
    private RocketMqProducerService rocketMqProducerService;

    @Autowired
    private MqTransMessageService mqTransMessageService;


    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        System.out.println("run message send thread");
        new Thread(() -> {
            while (true) {
                MqTransMessageEntity message = null;
                try {
                    message = MessageQueue.queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (Objects.isNull(message)) {
                    continue;
                }
                SendResult sendResult = null;
                try {
                    String key = MessageFormat.format(MessageLock.LOCK_PREFIX, message.getId());
                    synchronized (key.intern()) {
                        // 查询数据库确保是有值
                        MqTransMessageEntity mqTransMessageEntity = mqTransMessageService.selectById(message.getId());
                        if (Objects.isNull(mqTransMessageEntity)) {

                            logger.info("can not find message in table for messageId={}", message.getId());
                            // 没有值有两种可能姓 ，一种是事务没结束，一种事务没成功
                            long time = System.currentTimeMillis() - message.getCreateTime().getTime();
                            if (time / 1000 > 30) {
                                // 超过30秒还是查不到，就直接丢弃了，后面有定时任务兜底
                                logger.info(" due to over 30 second, discard message for messageId={}", message.getId());
                                continue;

                            } else {
                                // 重新加到队尾等下一次处理
                                logger.info(" add message to queue again for messageId={}", message.getId());
                                MessageQueue.queue.put(message);
                                continue;

                            }

                        } else {
                            sendResult = rocketMqProducerService.synSend(message.getTopic(), message.getTag(),
                                    message.getMessage());
                            if (SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
                                mqTransMessageService.deleteById(message.getId());
                            } else {
                                // 如果失败了则放进第二个队列等待发送
                                MessageQueue.secondQueue.put(message);
                            }
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    MessageQueue.secondQueue.add(message);
                }


            }
        }).start();
    }
}
