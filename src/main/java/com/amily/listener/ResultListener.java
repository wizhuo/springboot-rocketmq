package com.amily.listener;


import com.amily.enums.MqAction;
import com.amily.enums.RocketQueues;
import com.amily.annotation.RocketMqListener;
import com.amily.service.MessageListener;
import com.amily.util.MqMsgConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * @author lizhuo
 * @since 2019/1/5 下午9:27
 **/
@Slf4j
@RocketMqListener(topic = RocketQueues.USER_TOPIC,tag = "aaa||bbb||ccc")
public class ResultListener implements MessageListener {


    @Override
    public MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = null;
        System.out.println("===========我是消费者==========="+message);
        try {
                msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");

            log.info("MsgId:{},MQ消费,Topic:{},Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
            if ("aaa".equals(message.getTags())) {
                System.out.println("终于等到你来了"+message.getTags());
            }else{
                System.out.println("错误过你了"+message.getTags());
            }
        } catch (Exception e) {
            log.error("MsgId:{},应用MQ消费失败,Topic:{},Tag:{}，Body:{},异常信息:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg, e);
            throw e;
        }
        return MqAction.CommitMessage;
    }



}
