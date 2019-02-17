package com.amily.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;


/**
 * spring 启动成功后初始化消费者
 * @author lizhuo
 * @since 2019/1/4 下午10:14
 **/
@Slf4j
public class RocketMqConsumerRunner implements ApplicationRunner {

    @Autowired
    private ApplicationContext context;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
        System.out.println("hello======");
        RocketMqConsumer consumer = context.getBean(RocketMqConsumer.class);
        consumer.start();
    }
}