package com.willjo.mq;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author willJo
 * @since 2020/12/30
 */
public class MonitorQueue implements ApplicationListener<ApplicationReadyEvent> {

    public static final int MAX_PRIORITY_QUEUE_SIZE = 1000;

    public static final int MAX_DELAY_QUEUE_SIZE = 1000;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        new Thread(() -> {
            while (true) {
                int size = MessageQueue.priorityQueue.size();
                if (size > MAX_PRIORITY_QUEUE_SIZE) {
                    System.out.println("priorityQueue size:" + size);
                }
                size = MessageQueue.delayQueue.size();
                if (size > MAX_DELAY_QUEUE_SIZE) {
                    System.out.println("delayQueue size:" + size);
                }
            }
        }, "monitor").start();
    }
}
