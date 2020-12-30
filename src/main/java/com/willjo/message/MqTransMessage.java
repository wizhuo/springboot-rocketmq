package com.willjo.message;

import com.baomidou.mybatisplus.annotations.TableField;
import com.willjo.dal.entity.MqTransMessageEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author willJo
 * @since 2020/12/29
 */
public class MqTransMessage implements Serializable {

    private Long id;

    private String topic;

    private String tag;

    private String message;


    private Date createTime;


    private Date updateTime;

    /**
     * 失败次数，根据这个来判断延迟时间和是否继续发送
     */
    private Integer failCount = 0;



    public MqTransMessage(Long id, String topic, String tag, String message, Date createTime, Date updateTime, Integer failCount) {
        this.id = id;
        this.topic = topic;
        this.tag = tag;
        this.message = message;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.failCount = failCount;
    }

    public static MqTransMessage instance(MqTransMessageEntity messageEntity) {
        return new MqTransMessage(messageEntity.getId(), messageEntity.getTopic(),
                messageEntity.getTag(),
                messageEntity.getMessage(),
                messageEntity.getCreateTime(),
                messageEntity.getUpdateTime(),0);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getFailCount() {
        return failCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }
}
