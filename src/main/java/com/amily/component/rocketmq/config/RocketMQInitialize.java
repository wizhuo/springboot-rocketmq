package com.amily.component.rocketmq.config;

import com.amily.component.rocketmq.RocketMqInitializeCondition;
import com.amily.component.rocketmq.consumer.RocketMqConsumerConfig;
import com.amily.component.rocketmq.producer.RocketMQProducer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

/**
 * @author lizhuo
 * @since 2019/1/4 下午10:14
 **/
@Conditional(RocketMqInitializeCondition.class)
public class RocketMQInitialize {

    @Bean
    public RocketMqConsumerConfig createConsumer(RocketMqConfiguration configuration, ApplicationContext context) throws Exception {
        return new RocketMqConsumerConfig(configuration, context);
    }

    @Bean
    public DefaultMQProducer createProducer(RocketMqConfiguration configuration) throws Exception {
        if (configuration.getNamesrvAddr() == null) {
            throw new IllegalArgumentException("rocketmq.nameSrvAddress 是必须的参数");
        }
        DefaultMQProducer producer = new RocketMQProducer(configuration.getProducerId());
        producer.setNamesrvAddr(configuration.getNamesrvAddr());
        producer.start();
        return producer;
    }

}
