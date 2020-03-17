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

注意事项：<br/>
1、消费有顺序消息时，请使用@RocketMqOrderListener 注解，并且实现接口类MessageOrderListener<br/>
2、消费普通消息时，请使用@RocketMqListener 注解，并且实现接口类MessageListener







