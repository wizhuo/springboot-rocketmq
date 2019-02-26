package com.amily.mq.listener;

import com.amily.dal.entity.MessageEntity;
import com.amily.service.MessageService;
import com.amily.util.MqMsgConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Component
public class TransactionListenerImpl implements TransactionListener {


    @Autowired
    private MessageService messageService;



    @Transactional(rollbackFor = Exception.class)
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        
        System.out.println("=========executeLocalTransaction============");
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setCreateTime(new Date());
        String message = MqMsgConvertUtil.bytes2String(msg.getBody(), "UTF-8");
        messageEntity.setMessage(message);
        messageEntity.setTag(msg.getTags());
        messageEntity.setTopic(msg.getTopic());
        messageEntity.setUpdateTime(new Date());
        messageEntity.setMsgid(msg.getTransactionId());
        messageService.save(messageEntity);
      //  throw new RuntimeException();
        return LocalTransactionState.UNKNOW;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        System.out.println("=========checkLocalTransaction============");
        log.info("检查消息id={}",msg.getTransactionId());
        MessageEntity messageEntity = messageService.getByMsgId(msg.getTransactionId());
        if (null == messageEntity) {
            log.warn("消息id={}不存在",msg.getTransactionId());
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
