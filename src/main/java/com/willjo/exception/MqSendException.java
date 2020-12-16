package com.willjo.exception;

/**
 * mq 消息发送异常类
 *
 * @author lizhuo
 * @since 2019/2/17 11:42
 */
public class MqSendException extends RuntimeException {

    private static final long serialVersionUID = -1014344856325540529L;

    public MqSendException() {
        super();
    }

    public MqSendException(String message) {
        super(message);
    }

    public MqSendException(String message, Throwable cause) {
        super(message, cause);
    }

    public MqSendException(Throwable cause) {
        super(cause);
    }
}
