package com.amily.annotation;

import com.amily.service.RocketMqConsumerRunner;
import com.amily.config.RocketMqFactoryBeanConfig;
import com.amily.config.RocketMqProperties;
import com.amily.service.RocketProducerService;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        RocketMqProperties.class,
        RocketMqConsumerRunner.class,
        RocketProducerService.class,
        RocketMqProperties.class,
        RocketMqFactoryBeanConfig.class
})
public @interface EnableRocketMq {

}