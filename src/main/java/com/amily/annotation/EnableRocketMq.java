package com.amily.annotation;

import com.amily.config.RocketMqConsumerInitialize;
import com.amily.config.RocketMQFactoryBean;
import com.amily.config.RocketMqProperties;
import com.amily.producer.RocketProducerService;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        RocketMqProperties.class,
        RocketMqConsumerInitialize.class,
        RocketProducerService.class,
        RocketMqProperties.class,
        RocketMQFactoryBean.class
})
public @interface EnableRocketMq {

}