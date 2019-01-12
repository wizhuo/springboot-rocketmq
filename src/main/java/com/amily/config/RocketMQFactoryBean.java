package com.amily.config;

import com.amily.producer.RocketMQProducer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * 创建生产者和消费者的工厂bean
 * @author lizhuo
 * @since 2019/1/4 下午10:14
 *
 **/
public class RocketMQFactoryBean {

    @Bean
    public RocketMqConsumerConfig createConsumer(RocketMqProperties configuration, ApplicationContext context) throws Exception {
        System.out.println("=======createConsumer===");
        return new RocketMqConsumerConfig(configuration, context);
    }

    @Bean
    public DefaultMQProducer createProducer(RocketMqProperties configuration) throws Exception {
        System.out.println("=======createProducer===");
        if (configuration.getNamesrvAddr() == null) {
            throw new IllegalArgumentException("rocketmq.nameSrvAddress 是必须的参数");
        }
        DefaultMQProducer producer = new RocketMQProducer(configuration.getProducerId());
        producer.setNamesrvAddr(configuration.getNamesrvAddr());
        producer.setInstanceName(System.currentTimeMillis()+"");
        producer.start();
        return producer;
    }

}
