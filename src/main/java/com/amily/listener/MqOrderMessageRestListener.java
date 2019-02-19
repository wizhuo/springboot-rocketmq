package com.amily.listener;


import com.amily.annotation.RocketMqOrderListener;
import com.amily.enums.MqAction;
import com.amily.enums.RocketQueuesEnum;
import com.amily.service.MessageOrderListener;
import com.amily.util.MqMsgConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * @author lizhuo
 * @since 2019/1/5 下午9:27
 **/
@Slf4j
@RocketMqOrderListener(topic = RocketQueuesEnum.USER_ORDER_TOPIC,consumerGroup=RocketQueuesEnum.USER_ORDER_GROUP)
public class MqOrderMessageRestListener implements MessageOrderListener {


    @Override
    public MqAction consume(MessageExt message, ConsumeOrderlyContext context) {
        String msg = null;
        System.out.println("===========我是有顺序的消费者===========" + message);
        try {
            msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");

            log.info("MsgId:{},MQ消费,Topic:{},Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);

        } catch (Exception e) {
            log.error("MsgId:{},应用MQ消费失败,Topic:{},Tag:{}，Body:{},异常信息:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg, e);
            throw e;
        }
        return MqAction.CommitMessage;
    }


}
