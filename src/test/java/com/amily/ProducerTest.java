package com.amily;


import com.amily.mq.command.MqConstant;
import com.amily.mq.RocketMqProducerService;
import org.apache.rocketmq.client.producer.SendResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(classes = RocketMqDemoApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ProducerTest {

    @Autowired(required = false)
    RocketMqProducerService senderService;



    @Test
    public void defaultMQProducer() {
        if(senderService==null){
            System.out.println("mq 没启用，直接返回,不进行测试");
            return;
        }

        try {


            for (int i = 0; i < 1; i++) {
                String messageBody = "我是事务消息内容test:" + i;
                System.out.println("事务消息发送前");
                SendResult result =  senderService.transSend(MqConstant.USER_ORDER_TOPIC, "aaa","111", messageBody);
                System.out.println("事务消息发送后：发送响应：MsgId:" + result.getMessageQueue().getTopic()+""+result.toString());

            }



        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

    }

}
