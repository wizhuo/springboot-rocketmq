package com.amily.enums;


/**
 * 消息确认枚举
 * @author lizhuo
 * @since 2019/1/5 下午8:55
 **/
public enum MqAction {

    //消费成功确认消息
    CommitMessage,

    //稍后继续消费
    ReconsumeLater;

    MqAction() {

    }
}
