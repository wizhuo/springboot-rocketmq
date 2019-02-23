package com.amily.service;

import com.amily.dal.entity.UserEntity;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lizhuo
 * @since 2019-02-23
 */
public interface UserService extends IService<UserEntity> {
    


	 /**
     * 获取实体
     * 
     * @param id 实体ID
     * @return 实体
     */
    UserEntity selectById( Long id);

    /**
     * 新增用户
     * @param userEntity
     * @return
     */
    Boolean save(UserEntity userEntity);

    /**
     * 测试消息事务
     * @return
     */
    Boolean transMessageTest();

        
}
