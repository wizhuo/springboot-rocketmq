package com.amily;

import com.amily.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest(classes = RocketMqDemoApplication.class)
@RunWith(SpringRunner.class)
public class TransactionProducerTest {

    @Autowired
    private UserService userService;

    /**
     * 测试本地事务成功的时候发送消息也成功
     */
    @Test
    @Rollback(value = false)
    public void testSuccess() {
        userService.transMessageSuccess();
        System.out.println("发送结束");
    }

    /**
     * 测试本地事务失败的时候消息也发送失败
     */
    @Test
    @Rollback(value = false)
    public void testError() throws Exception {
        userService.transMessageError();
        System.out.println("发送结束");
    }
}
