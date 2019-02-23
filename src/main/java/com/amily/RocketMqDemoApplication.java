package com.amily;

import com.amily.annotation.EnableRocketMq;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 *
 * @author lizhuo
 * @since 2019/2/16 17:46
 */

@SpringBootApplication(scanBasePackages ="com.amily")
@EnableRocketMq
@MapperScan("com.amily.dal.mapper")
public class RocketMqDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RocketMqDemoApplication.class, args);
	}

}

