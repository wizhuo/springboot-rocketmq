package com.amily.config;

import com.amily.mq.RocketMqConsumer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.*;

/**
 * 创建生产者和消费者的工厂bean
 *
 * @author lizhuo
 * @since 2019/1/4 下午10:14
 **/
public class RocketMqFactoryBeanConfig {

    @Bean
    public RocketMqConsumer createConsumer(RocketMqProperties configuration, ApplicationContext context) throws Exception {
        return new RocketMqConsumer(configuration, context);
    }

    @Bean
    public DefaultMQProducer defaultProducer(RocketMqProperties configuration) throws Exception {
        if (configuration.getNamesrvAddr() == null) {
            throw new IllegalArgumentException("rocketmq.nameSrvAddress 是必须的参数");
        }
        DefaultMQProducer producer = new DefaultMQProducer(configuration.getProducerId());
        producer.setNamesrvAddr(configuration.getNamesrvAddr());
        producer.setInstanceName(System.currentTimeMillis() + "");
        producer.start();
        return producer;
    }


    @Bean
    public TransactionMQProducer transProducer(RocketMqProperties configuration, TransactionListener transactionListener) throws Exception {
        if (configuration.getNamesrvAddr() == null) {
            throw new IllegalArgumentException("rocketmq.nameSrvAddress 是必须的参数");
        }
        TransactionMQProducer producer = new TransactionMQProducer(configuration.getProducerId());
        producer.setNamesrvAddr(configuration.getNamesrvAddr());
        producer.setInstanceName(System.currentTimeMillis() + "");
        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("client-transaction-msg-check-thread");
                return thread;
            }
        });

        producer.setExecutorService(executorService);
        producer.setTransactionListener(transactionListener);
        producer.start();
        return producer;
    }

}
