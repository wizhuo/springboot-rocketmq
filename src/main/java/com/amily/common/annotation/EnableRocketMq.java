package com.amily.common.annotation;

import com.amily.component.rocketmq.config.RocketApplicationListener;
import com.amily.component.rocketmq.config.RocketMqConfiguration;
import com.amily.component.rocketmq.config.RocketMQInitialize;
import com.amily.component.rocketmq.producer.RocketProducerService;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        RocketMqConfiguration.class,
        RocketApplicationListener.class,
        RocketProducerService.class,
        RocketMqConfiguration.class,
        RocketMQInitialize.class
})
public @interface EnableRocketMq {

}