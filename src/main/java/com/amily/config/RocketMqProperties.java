package com.amily.config;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;


/**
 * rocketmq 的配置文件
 * @author liz
 * @since 2019/1/4 下午10:14
 **/
@Data
@ConfigurationProperties(prefix = "rocketmq")
@Order(-1)
public class RocketMqProperties {


    /**
     * rocketmq集群名称服务地址，用;作为地址的分隔符
     */
    private String namesrvAddr;

    /**
     * mq集群生产者id 如果不填使用实例名称
     */
    private String producerId;


    public String getProducerId() {
        if (StringUtils.isBlank(producerId)) {
            throw new IllegalArgumentException("rocketmq.producerId 是必须的");
        }
        return producerId;
    }




}
