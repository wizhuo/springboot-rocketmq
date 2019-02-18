package com.amily.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 有序消息监听
 * @author lizhuo
 * @since 2019/1/4 下午10:11
 **/

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface RocketMqOrderListener {
	String value() default "";

	String topic();

	String tag() default "*";
}
