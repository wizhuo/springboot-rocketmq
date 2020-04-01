package com.amily.mq;

import com.amily.enums.MqAction;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.common.message.MessageExt;


/**
 * 有顺序消息监听
 *
 * @author lizhuo
 * @since 2019/1/5 下午8:55
 **/
public interface MessageOrderListener {

    /**
     * mq 消费接口
     */
    MqAction consume(MessageExt var1, ConsumeOrderlyContext context);
}
