package com.amily.service;

import com.amily.dal.entity.MessageEntity;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lizhuo
 * @since 2019-02-23
 */
public interface MessageService extends IService<MessageEntity> {
    
	 /**
     * 获取实体
     * 
     * @param id 实体ID
     * @return 实体
     */
    MessageEntity selectById( Long id);

    /**
     * 新增消息
     * @param messageEntity
     * @return
     */
    Boolean save(MessageEntity messageEntity);

    /**
     * 根据消息id 获取对象
     * @param msgid
     * @return
     */
    MessageEntity getByMsgId(String msgid);
    

}

