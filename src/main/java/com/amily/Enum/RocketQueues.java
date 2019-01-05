package com.amily.Enum;

public enum RocketQueues {

    /** 分成账户变动通知 */
    BATCH_CHANGE_ACCOUNT(RocketQueues.USER_TOPIC, "BATCH_CHANGE_ACCOUNT", "分成账户变动通知");

    public static final String USER_TOPIC = "USER_TOPIC";

    private final String topic;
    private final String tag;
    private final String desc;

    RocketQueues(String topic, String tag, String desc) {
        this.topic = topic;
        this.tag = tag;
        this.desc = desc;
    }

    public String getTopic() {
        return topic;
    }

    public String getTag() {
        return tag;
    }

    public String getDesc() {
        return desc;
    }
}
