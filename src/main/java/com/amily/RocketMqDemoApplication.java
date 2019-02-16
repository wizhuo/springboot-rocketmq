package com.amily;

import com.amily.annotation.EnableRocketMq;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
/**
 *
 * @author lizhuo
 * @since 2019/2/16 17:46
 */

@SpringBootApplication
@ComponentScan(basePackages="com.amily")
@EnableRocketMq
public class RocketMqDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RocketMqDemoApplication.class, args);
	}

}

