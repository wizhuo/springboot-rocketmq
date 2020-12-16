package com.willjo.exception;


/**
 * mq 客户端异常
 *
 * @author lizhuo
 * @since 2019/1/5 下午9:14
 **/

public class MqClientException extends RuntimeException {


    private static final long serialVersionUID = -8131365538823989295L;

    public MqClientException() {
    }

    public MqClientException(String message) {
        super(message);
    }

    public MqClientException(Throwable cause) {
        super(cause);
    }

    public MqClientException(String message, Throwable cause) {
        super(message, cause);
    }
}