package com.willjo.mq;

import com.willjo.dal.entity.MqTransMessageEntity;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author willJo
 * @since 2020/12/16
 */
public class MessageQueue {

    /**
     * 优先级最高的
     */
    public static Queue<MqTransMessageEntity> queue = new LinkedBlockingDeque<>();

    /**
     * 优先级别比较低的
     */
    public static Queue<MqTransMessageEntity> secondQueue = new LinkedBlockingDeque<>();



}
