package com.amily.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;

/**
 * @author lizhuo
 * @since 2019/1/4 下午10:19
 **/
public class RocketMQProducer extends DefaultMQProducer {


    public RocketMQProducer(String producerGroup) {
        super(producerGroup);
    }

}
