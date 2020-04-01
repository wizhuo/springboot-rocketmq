package com.amily.service.impl;

import com.amily.dal.entity.MqTransMessageEntity;
import com.amily.dal.mapper.MessageMapper;
import com.amily.service.MqTransMessageService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lizhuo
 * @since 2019-02-23
 */
@Service
public class MqTransMessageServiceImpl extends
    ServiceImpl<MessageMapper, MqTransMessageEntity> implements
    MqTransMessageService {

    @Autowired
    private MessageMapper messageMapper;

    private static final int MAX_MESSAGE_NUM = 1000;


    @Override
    public MqTransMessageEntity selectById(Long id) {
        return super.selectById(id);
    }

    @Override
    public Boolean save(MqTransMessageEntity messageEntity) {
        return super.insert(messageEntity);
    }

    @Override
    public MqTransMessageEntity getByMsgId(String msgid) {
        Wrapper<MqTransMessageEntity> wrapper = new EntityWrapper<>();
        wrapper.where("msgid={0}", msgid);
        return super.selectOne(wrapper);
    }

    @Override
    public List<MqTransMessageEntity> list(int limit) {
        if (limit <= 0 || limit > MAX_MESSAGE_NUM) {
            throw new IllegalArgumentException("参数不合法，limit  必须在0和" + MAX_MESSAGE_NUM + "之间");
        }
        Wrapper<MqTransMessageEntity> wrapper = new EntityWrapper<>();
        wrapper.orderBy("id", true);
        wrapper.last("limit " + limit);
        List<MqTransMessageEntity> list = super.selectList(wrapper);
        return Optional.ofNullable(list).orElse(Lists.newArrayList());
    }

    @Override
    public Boolean del(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new NullPointerException("ids 不能为空");
        }
        Wrapper<MqTransMessageEntity> wrapper = new EntityWrapper<>();
        wrapper.in("id", ids);
        return super.delete(wrapper);
    }


    @Override
    public Boolean transSendMsg(String topic, String tag, String content) {
        if (StringUtils.isBlank(topic)) {
            throw new IllegalArgumentException("topic 不能为空");
        }
        if (StringUtils.isBlank(content)) {
            throw new IllegalArgumentException("content 不能为空");
        }
        MqTransMessageEntity msg = new MqTransMessageEntity();
        msg.setTopic(topic)
            .setTag(tag)
            .setMessage(content)
            .setCreateTime(new Date());
        return super.insert(msg);

    }
}
