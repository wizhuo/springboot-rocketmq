package com.amily.service.impl;


import com.amily.dal.entity.UserEntity;
import com.amily.dal.mapper.UserMapper;
import com.amily.mq.RocketMqProducerService;
import com.amily.mq.command.MqConstant;
import com.amily.service.UserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lizhuo
 * @since 2019-02-23
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RocketMqProducerService rocketMqProducerService;
	

	
    @Override
    public UserEntity selectById(Long id){
        return super.selectById(id);
    }

    @Override
    public Boolean save( UserEntity userEntity ) {
        return super.insert(userEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean transMessageTest() {
        //开始发送mq 事务消息
        UserEntity userEntity = new UserEntity();
        userEntity.setAge(11);
        userEntity.setUsername("trans");
        this.save(userEntity);
        //开始发送mq 事务消息
        LOGGER.info("begin send trans message");
        rocketMqProducerService.transSend(MqConstant.USER_ORDER_TOPIC,MqConstant.USER_TAG,"hello");
        LOGGER.info(" end send tran message");
        return true;
    }
}
