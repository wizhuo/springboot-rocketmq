package com.amily.mq;

import com.amily.enums.MqAction;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;


/**
 * 普通消息监听
 *
 * @author lizhuo
 * @since 2019/1/5 下午8:55
 **/
public interface MessageListener {

    /**
     * mq 消费接口
     */
    MqAction consume(MessageExt var1, ConsumeConcurrentlyContext context);
}
