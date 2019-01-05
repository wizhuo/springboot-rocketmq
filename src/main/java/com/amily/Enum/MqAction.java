package com.amily.Enum;


/**
 * @author lizhuo
 * @since 2019/1/5 下午8:55
 **/
public enum MqAction {
    //稍后继续消费
    CommitMessage,
    //消费成功确认消息
    ReconsumeLater;

    private MqAction(){

    }
}
