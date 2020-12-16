package com.willjo;

import com.willjo.annotation.EnableRocketMq;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author lizhuo
 * @since 2019/2/16 17:46
 */

@SpringBootApplication(scanBasePackages = "com.willjo")
@EnableRocketMq
@MapperScan("com.willjo.dal.mapper")
@EnableScheduling
public class RocketMqDemoApplication {

    public static void main(String[] args) {

        SpringApplication.run(RocketMqDemoApplication.class, args);

    }

}

