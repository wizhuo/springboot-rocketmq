package com.amily.producer;

import com.amily.config.RocketMqProperties;
import com.amily.exception.MqClientException;
import com.amily.exception.MqContextException;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
/**
 *
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
    public void oneWaySender(String topic, String tag, String content) {
        try {
            Message msg = getMessage(topic, tag, content);
            if (isRocketMQ()) {
                rocketProducer.sendOneway(msg);
            }
            this.logMsg(msg);
        } catch (Exception e) {
            log.warn("单边发送消息失败", e);
        }
    }


    /**
     * 单边发送
     * 这方法最好不要用
     *
     * @param msg     自建的Message对象
     * @param content 字符串消息体
     */
    @Deprecated
    public void oneWaySender(Message msg, String content) {
        msg.setBody(content.getBytes());
        try {
            rocketProducer.sendOneway(msg);
        } catch (Exception e) {
            log.warn("单边发送消息失败", e);
        }
        this.logMsg(msg);
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
        }
        this.logMsg(msg);
    }

    /**
     * 指定回调处理类
     *
     * @param topic        topic
     * @param tag          tag
     * @param content      字符串消息体
     * @param sendCallBack 发送完成后的回调处理类
     */
    @Deprecated
    public void sendAsyncWithCallBack(String topic, String tag, String content, SendCallback sendCallBack) {

        Message msg = getMessage(topic, tag, content);
        try {
            if (isRocketMQ()) {
                rocketProducer.send(msg, new org.apache.rocketmq.client.producer.SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        sendCallBack.onSuccess(sendResult);
                    }

                    @Override
                    public void onException(Throwable e) {
                        MqContextException exceptionContext = new MqContextException();
                        exceptionContext.setTopic(topic);
                        exceptionContext.setMessageId("");
                        exceptionContext.setException(new MqClientException(e));
                        sendCallBack.onException(exceptionContext);
                    }
                });
            }
        } catch (Exception e) {
            log.warn("异步发送消息失败", e);
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
        SendResult result = null;
        try {
            SendResult sendResult = rocketProducer.send(msg);
            return sendResult;
        } catch (Exception e) {
            log.warn("同步发送消息失败", e);
        }

        this.logMsg(msg);
        return result;
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
        if(e instanceof MqContextException){
            MqContextException context = (MqContextException)e;
            log.info("send message failed. topic=" + context.getTopic() + ", msgId=" + context.getMessageId());
        }else{
            log.info("send message failed. =" + e);
        }

    }
}
