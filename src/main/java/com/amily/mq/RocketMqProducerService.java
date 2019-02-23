package com.amily.mq;

import com.amily.exception.MqContextException;
import com.amily.exception.MqSendException;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

/**
 * @author lizhuo
 * @since 2019/2/16 17:46
 */

@Slf4j
public class RocketMqProducerService implements SendCallback {
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    @Qualifier("defaultProducer")
    private DefaultMQProducer rocketProducer;

    @Autowired
    @Qualifier("transProducer")
    private TransactionMQProducer transactionMQProducer;

    private RocketSendCallback rocketSendCallback = new RocketSendCallback();


    /**
     * 单边发送
     *
     * @param topic   topic
     * @param tag     tag
     * @param content 字符串消息体
     */
    public void sendOneway(String topic, String tag, String content) {
        this.sendOneway(topic, tag, "", content);
    }

    /**
     * 单边发送
     *
     * @param topic
     * @param tag
     * @param keys
     * @param content
     */
    public void sendOneway(String topic, String tag, String keys, String content) {
        try {
            Message msg = getMessage(topic, tag, keys, content);
            rocketProducer.sendOneway(msg);
            this.logMsg(msg);
        } catch (Exception e) {
            LOGGER.error("单边发送消息失败", e);
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
    public void sendAsyncDefault(String topic, String tag, String content) {
        this.sendAsyncDefault(topic, tag, "", content);
    }

    /**
     * 异步发送 默认回调函数
     *
     * @param topic
     * @param tag
     * @param keys
     * @param content
     */
    public void sendAsyncDefault(String topic, String tag, String keys, String content) {
        Message msg = getMessage(topic, tag, keys, content);
        try {
            rocketProducer.send(msg, rocketSendCallback);
        } catch (Exception e) {
            LOGGER.error("异步发送消息失败", e);
            throw new MqSendException(e);
        }
        this.logMsg(msg);
    }

    /**
     * 异步发送
     *
     * @param topic   topic
     * @param tag     tag
     * @param content 字符串消息体
     */
    public void sendAsync(String topic, String tag, String content, SendCallback sendCallback) {
        this.sendAsync(topic, tag, content, "", sendCallback);
    }

    /**
     * 异步发送
     *
     * @param topic
     * @param tag
     * @param content
     * @param keys
     * @param sendCallback
     */
    public void sendAsync(String topic, String tag, String content, String keys, SendCallback sendCallback) {
        Message msg = getMessage(topic, tag, keys, content);
        try {
            rocketProducer.send(msg, sendCallback);
        } catch (Exception e) {
            LOGGER.error("异步发送消息失败", e);
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
        return this.synSend(topic, tag, "", content);
    }


    /**
     * 同步发送
     *
     * @param topic   topic
     * @param tag     tag
     * @param content 字符串消息体
     * @return 可能返回null
     */
    public SendResult synSend(String topic, String tag, String keys, String content) {

        Message msg = getMessage(topic, tag, keys, content);
        try {
            SendResult sendResult = rocketProducer.send(msg);
            this.logMsg(msg, sendResult);
            return sendResult;
        } catch (Exception e) {
            LOGGER.error("同步发送消息失败", e);
            throw new MqSendException(e);
        }
    }


    /**
     * 发送事务消息
     *
     * @param topic   topic
     * @param tag     tag
     * @param content 字符串消息体
     * @return 可能返回null
     */
    public SendResult transSend(String topic, String tag, String content) {

        return this.transSend(topic, tag, "", content);
    }

    /**
     * 发送事务消息
     *
     * @param topic
     * @param tag
     * @param keys
     * @param content
     * @return
     */
    public SendResult transSend(String topic, String tag, String keys, String content) {

        Message msg = getMessage(topic, tag, keys, content);
        try {
            SendResult sendResult = transactionMQProducer.sendMessageInTransaction(msg,null);
            this.logMsg(msg, sendResult);
            return sendResult;
        } catch (Exception e) {
            LOGGER.error("同步发送事务消息失败", e);
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

        return this.orderSend(topic, tag, "", content, orderId);

    }


    /**
     * 有顺序发送
     *
     * @param topic
     * @param tag
     * @param content
     * @param keys
     * @param orderId
     * @return
     */
    public SendResult orderSend(String topic, String tag, String keys, String content, int orderId) {

        Message msg = getMessage(topic, tag, keys, content);
        try {
            SendResult sendResult = rocketProducer.send(msg, (List<MessageQueue> mqs, Message message, Object arg) -> {
                        Integer id = (Integer) arg;
                        int index = id % mqs.size();
                        return mqs.get(index);
                    }
                    , orderId);
            this.logMsg(msg, sendResult);
            return sendResult;
        } catch (Exception e) {
            LOGGER.error("有顺序发送消息失败", e);
            throw new MqSendException(e);
        }

    }


    /**
     * 构造message
     *
     * @param topic
     * @param tag
     * @param keys
     * @param content
     * @return
     */
    public Message getMessage(String topic, String tag, String keys, String content) {
        return new Message(topic, tag, keys, content.getBytes());
    }


    @Override
    public void onSuccess(final SendResult sendResult) {
        // 消费发送成功
        LOGGER.info("send message success. topic=" + sendResult.getMessageQueue().getTopic() + ", msgId=" + sendResult.getMsgId());
    }


    /**
     * 打印消息topic等参数方便后续查找问题
     */
    private void logMsg(Message message) {
        LOGGER.info("消息队列发送完成:topic={},tag={},msgId={}", message.getTopic(), message.getTags(), message.getKeys());
    }

    /**
     * 打印消息topic等参数方便后续查找问题
     */
    private void logMsg(Message message, SendResult sendResult) {
        LOGGER.info("消息队列发送完成:topic={},tag={},msgId={},sendResult={}", message.getTopic(), message.getTags(), message.getKeys(), sendResult);
    }

    class RocketSendCallback implements SendCallback {

        @Override
        public void onSuccess(SendResult sendResult) {
            LOGGER.info("send message success. topic=" + sendResult.getMessageQueue().getTopic() + ", msgId=" + sendResult.getMsgId());
        }

        @Override
        public void onException(Throwable e) {
            LOGGER.error("send message failed.", e);
        }
    }

    @Override
    public void onException(Throwable e) {
        if (e instanceof MqContextException) {
            MqContextException context = (MqContextException) e;
            LOGGER.error("send message failed. topic=" + context.getTopic() + ", msgId=" + context.getMessageId());
        } else {
            LOGGER.error("send message failed. =" + e);
        }

    }
}
