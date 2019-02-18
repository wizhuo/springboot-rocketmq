package com.amily.service;

import com.amily.config.RocketMqProperties;
import com.amily.exception.MqContextException;
import com.amily.exception.MqSendException;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author lizhuo
 * @since 2019/2/16 17:46
 */

@Slf4j
public class RocketProducerService implements SendCallback {

    @Autowired
    RocketMqProperties rocketMqConfiguration;
    /**
     * 这个是自建RocketMQ的生产者
     */
    @Autowired(required = false)
    private DefaultMQProducer rocketProducer;

    private RocketSendCallback rocketSendCallback = new RocketSendCallback();

    private boolean isRocketMQ() {
        return rocketProducer != null;
    }


    /**
     * 单边发送
     *
     * @param topic   topic
     * @param tag     tag
     * @param content 字符串消息体
     */
    public void sendOneway(String topic, String tag, String content) {
        try {
            Message msg = getMessage(topic, tag, content);
            if (isRocketMQ()) {
                rocketProducer.sendOneway(msg);
            }
            this.logMsg(msg);
        } catch (Exception e) {
            log.warn("单边发送消息失败", e);
            throw new MqSendException(e);
        }
    }


    /**
     * 异步发送 默认回调函数
     *
     * @param topic   topic
     * @param tag     tag
     * @param content 字符串消息体
     */
    public void sendAsync(String topic, String tag, String content) {
        Message msg = getMessage(topic, tag, content);
        try {
            if (isRocketMQ()) {
                rocketProducer.send(msg, rocketSendCallback);
            }
        } catch (Exception e) {
            log.warn("异步发送消息失败", e);
            throw new MqSendException(e);
        }
        this.logMsg(msg);
    }


    /**
     * 同步发送
     *
     * @param topic   topic
     * @param tag     tag
     * @param content 字符串消息体
     * @return 可能返回null
     */
    public SendResult synSend(String topic, String tag, String content) {

        Message msg = getMessage(topic, tag, content);
        try {
            SendResult sendResult = rocketProducer.send(msg);
            this.logMsg(msg, sendResult);
            return sendResult;
        } catch (Exception e) {
            log.warn("同步发送消息失败", e);
            throw new MqSendException(e);
        }

    }


    /**
     * 有顺序发送
     *
     * @param topic
     * @param tag
     * @param content
     * @param orderId 相同的orderId 的消息会被有顺序的消费
     * @return
     */
    public SendResult orderSend(String topic, String tag, String content, int orderId) {

        Message msg = getMessage(topic, tag, content);
        try {
            SendResult sendResult = rocketProducer.send(msg, (List<MessageQueue> mqs, Message message, Object arg) -> {
                        Integer id = (Integer) arg;
                        int index = id % mqs.size();
                        return mqs.get(index);
                    }
                    , orderId);
            this.logMsg(msg,sendResult);
            return sendResult;
        } catch (Exception e) {
            log.error("有顺序发送消息失败", e);
            throw new MqSendException(e);
        }

    }



    /**
     * 构造message
     *
     * @param topic   topic
     * @param tag     tag
     * @param content 字符串消息体
     * @return 构造的Message对象
     */
    public Message getMessage(String topic, String tag, String content) {
        return new Message(topic, tag, content.getBytes());
    }


    @Override
    public void onSuccess(final SendResult sendResult) {
        // 消费发送成功
        log.info("send message success. topic=" + sendResult.getMessageQueue().getTopic() + ", msgId=" + sendResult.getMsgId());
    }


    /**
     * 打印消息topic等参数方便后续查找问题
     */
    private void logMsg(Message message) {
        log.info("消息队列发送完成:topic={},tag={},msgId={}", message.getTopic(), message.getTags(), message.getKeys());
    }

    /**
     * 打印消息topic等参数方便后续查找问题
     */
    private void logMsg(Message message, SendResult sendResult) {
        log.info("消息队列发送完成:topic={},tag={},msgId={},sendResult={}", message.getTopic(), message.getTags(), message.getKeys(), sendResult);
    }

    class RocketSendCallback implements SendCallback {

        @Override
        public void onSuccess(SendResult sendResult) {
            log.info("send message success. topic=" + sendResult.getMessageQueue().getTopic() + ", msgId=" + sendResult.getMsgId());
        }

        @Override
        public void onException(Throwable e) {
            log.warn("send message failed.", e);
        }
    }

    @Override
    public void onException(Throwable e) {
        if (e instanceof MqContextException) {
            MqContextException context = (MqContextException) e;
            log.info("send message failed. topic=" + context.getTopic() + ", msgId=" + context.getMessageId());
        } else {
            log.info("send message failed. =" + e);
        }

    }
}
