package com.amily;


import com.amily.Enum.RocketQueues;
import com.amily.producer.RocketProducerService;
import org.apache.rocketmq.client.producer.SendResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(classes = RocketMQDemoApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ProducerTest {

    @Autowired(required = false)
    RocketProducerService senderService;


    @Test
    public void defaultMQProducer() {
        if(senderService==null){
            System.out.println("mq 没启用，直接返回,不进行测试");
            return;
        }

        try {

            for (int i = 0; i < 5; i++) {
                String messageBody = "我是消息内容:" + i;
                SendResult result =  senderService.synSend(RocketQueues.BATCH_CHANGE_ACCOUNT.getTopic(), "ccc", messageBody);
                System.out.println("发送响应：MsgId:" + result.getMessageQueue().getTopic()+""+result.toString());

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

    }

}
