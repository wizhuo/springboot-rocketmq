package com.amily.mq.listener;

import com.amily.dal.entity.MessageEntity;
import com.amily.service.MessageService;
import com.amily.util.MqMsgConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 事务消息回调
 * @author lizhuo
 * @since 2020/3/17 15:27
 */
@Component
public class TransactionListenerImpl implements TransactionListener {

private static final Logger LOGGER = LoggerFactory.getLogger(TransactionListenerImpl.class);

    @Autowired
    private MessageService messageService;



    @Transactional(rollbackFor = Exception.class)
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setCreateTime(new Date());
        String message = MqMsgConvertUtil.bytes2String(msg.getBody(), "UTF-8");
        messageEntity.setMessage(message);
        messageEntity.setTag(msg.getTags());
        messageEntity.setTopic(msg.getTopic());
        messageEntity.setUpdateTime(new Date());
        messageEntity.setMsgid(msg.getTransactionId());
        messageService.save(messageEntity);
        return LocalTransactionState.UNKNOW;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        LOGGER.info("检查消息id={}",msg.getTransactionId());
        MessageEntity messageEntity = messageService.getByMsgId(msg.getTransactionId());
        if (null == messageEntity) {
            LOGGER.warn("消息id={}不存在",msg.getTransactionId());
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
