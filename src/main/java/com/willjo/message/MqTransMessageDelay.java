package com.willjo.message;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author willJo
 * @since 2020/12/29
 */
public class MqTransMessageDelay extends MqTransMessage implements Delayed {
    /**
     * 如果查不到的时候，进入延迟队列的延期时间
     */
    public static final int[] DELAY_TIME_WHEEL = {5, 20, 60, 5 * 60, 60 * 60};

    /**
     * 最大失败次数，超过这个次数，就让定时任务来发送好了
     */
    public static final int MAX_FAIL_COUNT = DELAY_TIME_WHEEL.length;


    /**
     * 单位毫秒
     */
    private long delayTime;

    public MqTransMessageDelay(Long id, String topic, String tag, String message, Date createTime, Date updateTime, Integer failCount) {
        super(id, topic, tag, message, createTime, updateTime, failCount);
        if (failCount >= MAX_FAIL_COUNT) {
            // 超过时间轮了，就以最大的等待时间
            this.delayTime = System.currentTimeMillis() + DELAY_TIME_WHEEL[MAX_FAIL_COUNT - 1] * 1000;
        } else {
            this.delayTime = System.currentTimeMillis() + DELAY_TIME_WHEEL[failCount] * 1000;
        }

    }


    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(delayTime-System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        // 按时间正序，时间越早，越先出队列
        long result = this.delayTime - o.getDelay(TimeUnit.MILLISECONDS);
        if (result < 0) {
            return 1;
        } else if (result == 0) {
            return 0;
        } else {
            return -1;
        }
    }

    public static MqTransMessageDelay instance(MqTransMessage transMessage) {
        return new MqTransMessageDelay(transMessage.getId(), transMessage.getTopic(),
                transMessage.getTag(),
                transMessage.getMessage(),
                transMessage.getCreateTime(),
                transMessage.getUpdateTime(), transMessage.getFailCount());
    }
}
