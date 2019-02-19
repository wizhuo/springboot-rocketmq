package com.amily.enums;

/**
 * @author lizhuo
 * @since 2019/2/16 17:46
 */
public enum RocketQueuesEnum {

    /** 订单表 */
    ORDER_TABLE(RocketQueuesEnum.ORDER_TABLE_TOP, "ORDER", "订单表变动通知"),
    /** 用户表 */
    USER_TABLE(RocketQueuesEnum.USER_ORDER_TOPIC, "USER_ORDER_TOPIC", "用户表变更同步"),
    /** 员工表 */
    STAFF_TABLE(RocketQueuesEnum.STAFF_TOPIC, "STAFF_TOPIC", "员工变更同步")
    ;

    // TOP

    public static final String ORDER_TABLE_TOP = "ORDER_TABLE_TOP";
    public static final String USER_ORDER_TOPIC = "USER_ORDER_TOPIC";
    public static final String STAFF_TOPIC = "STAFF_TOPIC";


    //consumeGroup

    public static final String ORDER_GROUP = "ORDER_GROUP";
    public static final String USER_ORDER_GROUP = "USER_ORDER_GROUP";
    public static final String STAFF_TOPIC_GROUP = "STAFF_TOPIC_GROUP";

    private final String topic;
    private final String tag;
    private final String desc;

    RocketQueuesEnum(String topic, String tag, String desc) {
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
