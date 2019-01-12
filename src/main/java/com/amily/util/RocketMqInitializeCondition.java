package com.amily.util;

import com.amily.config.RocketMqProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author lizhuo
 * @since 2019/1/4 下午10:46
 **/
@Slf4j
public class RocketMqInitializeCondition implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        String enable = conditionContext.getEnvironment().getProperty(RocketMqProperties.ENABLE_ROCKETMQ_KEE).trim();
        System.out.println("是否启用rocketmq:"+enable);
        return Boolean.valueOf(enable);
    }
}
