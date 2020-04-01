# 优势
用原生的时候每个消费者和生产者都需要重复编写启动代码，基于这个痛点，本作者进行封装，沉淀为基础架构
让程序员专注于业务开发。
##### 使用 以下以hello 服务来作为例子讲解
1. 在启动类HelloApplication上加上注解 @EnableRocketMq <br/>
2. 在配置文件上填写相关配置
 # mq 配置
                rocketmq:
                  # NameServer地址 若是集群，用;作为地址的分隔符
                  namesrvAddr: 127.0.0.1:9876
                  # 生产者的组名
                  producerId: hello
                 
3. 发布消息，请查看ProducerTest 类，增加了自定义的类RocketProducerService 进一步封装DefaultMQProducer ，包含日志打印，也为了以后进一步扩展，比如以后直接使用阿里云的rocketmq
4. 监听消息请查看MqResultListener 类，监听消息只需要实现接口类MessageListener就可以，注意有顺序消息和无顺序消息不同，请查看注意事项说明


# 使用消息事件实现分布式事务
## 原理
为了确保发送mq 和保存本地事务要么同时成功，要么同时失败。一般思路是根据MQ的发送成功还是失败来判断本地事务是成功还是失败
但是发送MQ是走网络了，网络有个神奇的返回值：网络超时，这个没办法明确的知道MQ是成功还是失败，也就没办法决定是提交本地事务还是回滚本地事务
因此，我们这里不直接发送MQ，而是采用巧妙的方式，仅仅是保存消息到表里面，这样就可以利用数据库的事务来保证要么一起成功要么一起失败。
之后再通过定时器MqTransMessageTask来扫描消息表来发送MQ消息
## 实践
1、在每一个需要使用的消息分布式事务的库执行db.sql 中的mq_trans_message 消息表
2、调用MqTransMessageService.transSendMsg() 进行发送事务消息


#注意事项：<br/>
1、消费有顺序消息时，请使用@RocketMqOrderListener 注解，并且实现接口类MessageOrderListener<br/>
2、消费普通消息时，请使用@RocketMqListener 注解，并且实现接口类MessageListener







