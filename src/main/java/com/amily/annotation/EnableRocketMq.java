package com.amily.annotation;

import com.amily.mq.RocketMqConsumerRunner;
import com.amily.config.RocketMqFactoryBeanConfig;
import com.amily.config.RocketMqProperties;
import com.amily.mq.RocketMqProducerService;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用mq 注解
 *
 * @author lizhuo
 * @since 2020/3/17 15:25
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
    RocketMqProperties.class,
    RocketMqConsumerRunner.class,
    RocketMqProducerService.class,
    RocketMqProperties.class,
    RocketMqFactoryBeanConfig.class
})
public @interface EnableRocketMq {

}