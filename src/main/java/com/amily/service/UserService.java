package com.amily.service;

import com.amily.dal.entity.UserEntity;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 服务类
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
    UserEntity selectById(Long id);

    /**
     * 新增用户
     */
    Boolean save(UserEntity userEntity);

    /**
     * 发送事务消息成功
     *
     * @return true/false
     */
    Boolean transMessageSuccess();

    /**
     * 发送事务消息事务
     *
     * @return true/false
     * @throws Exception 故意抛个异常回滚本地事务
     */
    Boolean transMessageError() throws Exception;


}
