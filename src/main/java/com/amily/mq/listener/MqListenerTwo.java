package com.amily.mq.listener;


import com.amily.annotation.RocketMqListener;
import com.amily.enums.MqAction;
import com.amily.mq.command.MqConstant;
import com.amily.mq.MessageListener;
import com.amily.util.MqMsgConvertUtil;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 普通消息监听
 * @author lizhuo
 * @since 2019/1/5 下午9:27
 **/
@RocketMqListener(topic = MqConstant.ORDER_TABLE_TOP,consumerGroup= MqConstant.ORDER_GROUP)
public class MqListenerTwo implements MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MqListenerTwo.class);


    @Override
    public MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = null;
        try {
            System.out.println("消费者2======22222222");
            msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            LOGGER.info("MsgId:{},MQ消费,Topic:{},Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
        } catch (Exception e) {
            LOGGER.error("MsgId:{},应用MQ消费失败,Topic:{},Tag:{}，Body:{},异常信息:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg, e);
            throw e;
        }
        return MqAction.CommitMessage;
    }


}
