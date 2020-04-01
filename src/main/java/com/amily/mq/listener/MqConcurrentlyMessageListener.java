package com.amily.mq.listener;


import com.amily.enums.MqAction;
import com.amily.mq.command.MqConstant;
import com.amily.annotation.RocketMqListener;
import com.amily.mq.MessageListener;
import com.amily.mq.command.MqConstant.ConsumeGroup;
import com.amily.util.MqMsgConvertUtil;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lizhuo
 * @since 2019/1/5 下午9:27
 **/
@RocketMqListener(topic = MqConstant.Top.PAY_TOPIC, consumerGroup = ConsumeGroup.PAY_GROUP)
public class MqConcurrentlyMessageListener implements MessageListener {

    private static final Logger LOGGER = LoggerFactory
        .getLogger(MqConcurrentlyMessageListener.class);

    @Override
    public MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = null;
        try {
            msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");

            LOGGER.info("MsgId:{},MQ消费,Topic:{},Tag:{}，Body:{}", message.getMsgId(),
                message.getTopic(), message.getTags(), msg);

        } catch (Exception e) {
            LOGGER.error("MsgId:{},应用MQ消费失败,Topic:{},Tag:{}，Body:{},异常信息:{}", message.getMsgId(),
                message.getTopic(), message.getTags(), msg, e);
            throw e;
        }
        return MqAction.CommitMessage;
    }


}
