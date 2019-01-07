package com.amily.component.rocketmq.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * @author liz
 * @since 2019/1/4 下午10:14
 **/
@Data
@Slf4j
@ConfigurationProperties(prefix = "rocketmq")
@Order(-1)
@Component
public class RocketMqConfiguration {

    public static final String ENABLE_ROCKETMQ_KEE = "mq.enableRocketMQ";

    /**
     * rocketmq集群名称服务地址，多个使用 , 分割
     */
    private String namesrvAddr;

    /**
     * mq集群生产者id 如果不填使用实例名称
     */
    private String producerId;

    /**
     * mq集群消费者id 如果不填使用实例名称
     */
    private String consumerId;



    /**
     * 是否启动rocketmq
     */
    private boolean enableRocketMQ;


    public String getProducerId() {
        if (StringUtils.isBlank(producerId)) {
            throw new IllegalArgumentException("rocketmq.producerId 是必须的");
        }
        return producerId;
    }

    public String getConsumerId() {
        if (StringUtils.isBlank(consumerId)) {
            throw new IllegalArgumentException(" rocketmq.consumerId 是必须的");
        }
        return consumerId;
    }



}
