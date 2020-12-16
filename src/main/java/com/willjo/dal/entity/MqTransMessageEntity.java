package com.willjo.dal.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author lizhuo
 * @since 2019-02-23
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("mq_trans_message")
public class MqTransMessageEntity extends Model<MqTransMessageEntity> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String topic;

    private String tag;

    private String message;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;



    /**
     * 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
