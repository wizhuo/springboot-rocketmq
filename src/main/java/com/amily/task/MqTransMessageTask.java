package com.amily.task;

import com.amily.dal.entity.MqTransMessageEntity;
import com.amily.mq.RocketMqProducerService;
import com.amily.service.MqTransMessageService;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * mq 事务消息定时任务
 *
 * @author lizhuo
 * @since 2020/4/1 11:30
 */
@Component
public class MqTransMessageTask {

    private static final Logger logger = LoggerFactory.getLogger(MqTransMessageTask.class);
    @Autowired
    private MqTransMessageService messageService;

    @Autowired
    private RocketMqProducerService rocketMqProducerService;

    /**
     * 每次获取消息数量
     */
    private static final int MAX_MESSAGE_NUM = 1000;


    @Scheduled(fixedDelay = 1 * 1000)
    public void sendMessage() {
//        logger.info("====开始执行任务=====");
        List<MqTransMessageEntity> list = messageService.list(MAX_MESSAGE_NUM);
        LinkedBlockingDeque<Long> successIds = new LinkedBlockingDeque<>();
        // 如果执行期间宕机，那么这里会导致消息重发，单消费端必须要保证幂等
        list.parallelStream().forEach(messageEntity -> {
            SendResult sendResult = rocketMqProducerService
                .synSend(messageEntity.getTopic(), messageEntity.getTag(),
                    messageEntity.getMessage());
            if (SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
                successIds.add(messageEntity.getId());
            }
        });
        // 发送成功删除
        if (!CollectionUtils.isEmpty(successIds)) {
            messageService.del(successIds);
        }


    }

}
