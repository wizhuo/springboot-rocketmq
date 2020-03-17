package com.amily.mq.listener;


import com.amily.annotation.RocketMqOrderListener;
import com.amily.enums.MqAction;
import com.amily.mq.command.MqConstant;
import com.amily.mq.MessageOrderListener;
import com.amily.util.MqMsgConvertUtil;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lizhuo
 * @since 2019/1/5 下午9:27
 **/
@RocketMqOrderListener(topic = MqConstant.USER_ORDER_TOPIC,consumerGroup= MqConstant.USER_ORDER_GROUP)
public class MqOrderMessageRestListener implements MessageOrderListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MqOrderMessageRestListener.class);


    @Override
    public MqAction consume(MessageExt message, ConsumeOrderlyContext context) {
        String msg = null;
        try {
            msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");

            LOGGER.info("MsgId:{},MQ消费,Topic:{},Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);

        } catch (Exception e) {
            LOGGER.error("MsgId:{},应用MQ消费失败,Topic:{},Tag:{}，Body:{},异常信息:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg, e);
            throw e;
        }
        return MqAction.CommitMessage;
    }


}
