package com.amily;

import com.amily.annotation.EnableRocketMq;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="com.amily")
@EnableRocketMq
public class RocketMQDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RocketMQDemoApplication.class, args);
	}

}

