package com.amily.service.impl;

import com.amily.dal.entity.MessageEntity;
import com.amily.dal.mapper.MessageMapper;
import com.amily.service.MessageService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lizhuo
 * @since 2019-02-23
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, MessageEntity> implements MessageService {

    @Autowired
    private MessageMapper messageMapper;
	

	
    @Override
    public MessageEntity selectById(Long id){
        return super.selectById(id);
    }

    @Override
    public Boolean save(MessageEntity messageEntity) {
        return super.insert(messageEntity);
    }

    @Override
    public MessageEntity getByMsgId(String msgid) {
        Wrapper<MessageEntity> wrapper = new EntityWrapper<>();
        wrapper.where("msgid={0}",msgid);
        return super.selectOne(wrapper);
    }
}
