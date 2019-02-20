package com.amily;

import com.amily.enums.RocketQueuesEnum;
import com.amily.util.DateUtilsPlus;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.Date;

public class DefaultTest {

    public static void main(String[] args) throws Exception {
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new
                DefaultMQProducer("please_rename_unique_group_name");
        // Specify name server addresses.
        producer.setNamesrvAddr("127.0.0.1:9876");
        //Launch the instance.
        producer.start();
        for (int i = 0; i < 2; i++) {
            //Create a message instance, specifying topic, tag and message body.
            Message msg = new Message(RocketQueuesEnum.ORDER_TABLE_TOP /* Topic */,
                    "aaa" /* Tag */,"111",
                    ("Hello RocketMQ 11111111111111111111" +DateUtilsPlus.formatDateByStyle(new Date())+
                            i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
            );

            //Call send message to deliver message to one of brokers.
            SendResult sendResult = producer.send(msg);
            System.out.printf("%s%n", sendResult);
        }
//
//        for (int i = 0; i < 1; i++) {
//            int orderId = i % 10;
//            //Create a message instance, specifying topic, tag and message body.
//            Message msg = new Message(RocketQueuesEnum.USER_TABLE.getTopic(), "aaa", "KEY" + i,
//                    ("Hello RocketMQ " + i+DateUtilsPlus.formatDateByStyle(new Date())).getBytes(RemotingHelper.DEFAULT_CHARSET));
//            SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
//                @Override
//                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
//                    Integer id = (Integer) arg;
//                    int index = id % mqs.size();
//                    return mqs.get(index);
//                }
//            }, orderId);
//
//            System.out.printf("%s%n", sendResult);
//        }
//
//        for (int i = 0; i < 1; i++) {
//            //Create a message instance, specifying topic, tag and message body.
//            Message msg = new Message(RocketQueuesEnum.STAFF_TABLE.getTopic() /* Topic */,
//                    "ccc" /* Tag */,
//                    ("Hello RocketMQ 3333333333333 " + DateUtilsPlus.formatDateByStyle(new Date())+
//                            i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
//            );
//            //Call send message to deliver message to one of brokers.
//            SendResult sendResult = producer.send(msg);
//            System.out.printf("%s%n", sendResult);
//        }
        //Shut down once the producer instance is not longer in use.
        producer.shutdown();
    }
}
