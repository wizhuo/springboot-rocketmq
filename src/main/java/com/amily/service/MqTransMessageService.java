package com.amily.service;

import com.amily.dal.entity.MqTransMessageEntity;
import com.baomidou.mybatisplus.service.IService;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lizhuo
 * @since 2019-02-23
 */
public interface MqTransMessageService extends IService<MqTransMessageEntity> {

    /**
     * 获取实体
     *
     * @param id 实体ID
     * @return 实体
     */
    MqTransMessageEntity selectById(Long id);

    /**
     * 新增消息
     */
    Boolean save(MqTransMessageEntity messageEntity);

    /**
     * 根据消息id 获取对象
     */
    MqTransMessageEntity getByMsgId(String msgid);

    /**
     * 获取指定数量的消息 优先获取时间最早的消息
     *
     * @param limit 指定数量
     * @return 返回获取的消息，本方法承诺不会返回null
     * @throws IllegalArgumentException 参数必须大于0 小于1000.预防数据量太大，造成内存溢出
     */
    List<MqTransMessageEntity> list(int limit);

    /**
     * 批量删除
     *
     * @param ids 主键id 列表
     * @return true/false
     * @throws NullPointerException ids 为空的时候返回
     */
    Boolean del(Collection<Long> ids);

    /**
     * 发送本地事务消息
     *
     * @param topic topic
     * @param tag tag
     * @param content content
     * @return true/false
     * @throws IllegalArgumentException topic 或者content 为空的时候抛出参数异常
     */
    Boolean transSendMsg(String topic, String tag, String content);


}

