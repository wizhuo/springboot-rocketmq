package com.amily.component.rocketmq;

import com.amily.component.rocketmq.config.RocketMqConfiguration;
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
        String enable = conditionContext.getEnvironment().getProperty(RocketMqConfiguration.ENABLE_ROCKETMQ_KEE, "false").trim();
        log.debug("是否启用rocketmq:{}", enable);
        return true;
    }
}
