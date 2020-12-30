package com.willjo.mq;

import com.willjo.dal.entity.MqTransMessageEntity;
import com.willjo.message.MqTransMessage;
import com.willjo.service.MqTransMessageService;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Objects;

/**
 * @author willJo
 * @since 2020/12/16
 */
public class TransMessageRunner implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(TransMessageRunner.class);

    @Autowired
    private RocketMqProducerService rocketMqProducerService;

    @Autowired
    private MqTransMessageService mqTransMessageService;

    /**
     * 事务最大等待时间，单位为秒
     */
    public static final int TRANS_MAX_WAITING_TIME = 30;




    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        System.out.println("run message send thread");
        new Thread(() -> {
            while (true) {
                MqTransMessage message = null;
                try {
                    message = MessageQueue.priorityQueue.take();
                } catch (InterruptedException e) {

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
                            // 没有值有三种可能姓 ，一种是事务没结束，一种事务没成功，或者已经被定时任务发送了
                            long time = System.currentTimeMillis() - message.getCreateTime().getTime();
                            if(time / 1000 > TRANS_MAX_WAITING_TIME) {
                                // 超过30秒还是查不到，就直接丢弃了，后面有定时任务兜底
                                logger.info(" due to over 30 second, discard message for messageId={}", message.getId());
                            } else {
                                // 放到延迟队列处理
                                logger.info(" add message to delayQueue  for messageId={}", message.getId());
                                MessageQueue.putInDelayQueue(message);
                            }
                            continue;

                        } else {
                            sendResult = rocketMqProducerService.synSend(message.getTopic(), message.getTag(),
                                    message.getMessage());
                            if (Objects.nonNull(sendResult) && SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
                                mqTransMessageService.deleteById(message.getId());
                            } else {
                                // 网路抖动等原因，继续放在优先队列进行发送
                                MessageQueue.priorityQueue.put(message);
                            }
                        }

                    }

                } catch (Exception e) {
                    logger.warn("mq send fail,message={}",e.getMessage(),e);
                    MessageQueue.putInDelayQueue(message);
                }


            }
        },"transMessage").start();
    }
}
