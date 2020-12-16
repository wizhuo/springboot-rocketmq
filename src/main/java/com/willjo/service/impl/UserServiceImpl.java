package com.willjo.service.impl;


import com.willjo.dal.entity.UserEntity;
import com.willjo.dal.mapper.UserMapper;
import com.willjo.mq.command.MqConstant;
import com.willjo.service.MqTransMessageService;
import com.willjo.service.UserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
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
    private MqTransMessageService mqTransMessageService;


    @Override
    public UserEntity selectById(Long id) {
        return super.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean save(UserEntity userEntity) {
        return super.insert(userEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean transMessageSuccess() {
        //保存用户信息
        saveUser();
        LOGGER.info("begin send trans message");
        mqTransMessageService.transSendMsg(MqConstant.Top.USER_ORDER_TOPIC, MqConstant.Tag.USER_TAG,
            "{\"userName\": \"WillJoSuccess\"}");
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
        LOGGER.info(" end send tran message");
        return Boolean.TRUE;
    }

    private void saveUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setAge(11);
        userEntity.setUsername("trans");
        this.save(userEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean transMessageError() throws Exception {
        //保存用户信息
        saveUser();
        LOGGER.info("begin send trans message");
        mqTransMessageService.transSendMsg(MqConstant.Top.USER_ORDER_TOPIC, MqConstant.Tag.USER_TAG,
            "{\"userName\": \"WillJoError\"}");
        TimeUnit.SECONDS.sleep(10);
        LOGGER.info(" end send tran message");
        throw new RuntimeException();
    }
}
