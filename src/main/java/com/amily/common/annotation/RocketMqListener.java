package com.amily.common.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author lizhuo
 * @since 2019/1/4 下午10:11
 **/

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface RocketMqListener {
	String value() default "";

	String topic();

	String tag() default "*";
}
