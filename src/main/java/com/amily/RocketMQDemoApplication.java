package com.amily;

import com.amily.common.annotation.EnableRocketMq;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableRocketMq
@ComponentScan("com.amily")
public class RocketMQDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RocketMQDemoApplication.class, args);
	}

}

