package com.amily.config;

import com.amily.Enum.MqAction;
import com.amily.annotation.RocketMqListener;
import com.amily.util.MessageListener;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.context.ApplicationContext;

import java.util.Map;


/**
 * @author lizhuo
 * @since 2019/1/4 下午10:15
 **/
@Slf4j
public class RocketMqConsumerConfig {
    public ApplicationContext context;
    private volatile boolean init = false;
    private RocketMqProperties configuration;
    private Map<String, DefaultMQPushConsumer> consumerMap;


    public RocketMqConsumerConfig(RocketMqProperties configuration, ApplicationContext context) {
        this.context = context;
        this.configuration = configuration;
    }



    public synchronized void start() throws Exception {
        if (this.init) {
            log.warn("请不要重复初始化RocketMQ消费者");
            return;
        }
        this.consumerMap = Maps.newConcurrentMap();
        initializeConsumer(this.consumerMap);
        init = true;
    }

    /**
     * 初始化消费者，同项目内不允许对同一个topic多次加载
     *
     * @param map 存储消费者
     */
    private void initializeConsumer(Map<String, DefaultMQPushConsumer> map) throws Exception {
        Map<String, Object> beansWithAnnotationMap = context.getBeansWithAnnotation(RocketMqListener.class);
        Map<String, String> topicMap = Maps.newHashMap();
        for (Map.Entry<String, Object> entry : beansWithAnnotationMap.entrySet()) {
            // 获取到实例对象的class信息
            Class<?> classIns = entry.getValue().getClass();
            RocketMqListener rocketMqListenerAnnotaion = classIns.getDeclaredAnnotation(RocketMqListener.class);
            String topic = rocketMqListenerAnnotaion.topic();
            String tag = rocketMqListenerAnnotaion.tag();
            if (StringUtils.isBlank(topic)) {
                throw new RuntimeException(classIns.getSimpleName() + ":topic不能为空");
            }

            if (topicMap.containsKey(topic)) {
                throw new RuntimeException(String.format("Topic:%s 已经由%s监听 请勿重复监听同一Topic", topic, classIns.getSimpleName()));
            }

            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(this.configuration.getConsumerId());
            //设置名称服务器地址
            consumer.setNamesrvAddr(this.configuration.getNamesrvAddr());
            consumer.subscribe(topic, tag);
            //注册消费回调
            consumer.registerMessageListener((MessageListenerConcurrently) (msgList, context) -> {

                for (MessageExt msg : msgList) {
                    System.out.println("=======msg="+msg);
                    MessageListener listener = (MessageListener) entry.getValue();
                    MqAction action = listener.consume(msg,context);
                    switch (action) {
                        case ReconsumeLater:
                            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                        default:
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });

            topicMap.put(topic, classIns.getSimpleName());
            map.put(String.format("%s-%s", topic, tag), consumer);
        }

        consumerMap.forEach((key, consumer) -> {
            try {
                consumer.setInstanceName(System.currentTimeMillis()+"");
                consumer.start();
                log.info(String.format("自建RocketMQ 成功加载 Topic-tag:%s", key));
            } catch (MQClientException e) {
                log.warn(String.format("自建RocketMQ 加载失败 Topic-tag:%s", key), e);
            }
        });
        log.info("--------------成功初始化所有消费者到自建mq--------------");

    }
}
