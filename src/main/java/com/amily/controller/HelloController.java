package com.amily.controller;

import com.amily.dal.entity.UserEntity;
import com.amily.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author lizhuo
 * @since 2019/1/13 上午12:29
 **/

@RestController
public class HelloController {


    @Autowired
    private UserService userService;

    @GetMapping("/hello")
    public UserEntity hello(){
       return userService.selectById(1L);

    }

    @GetMapping("/trans/test")
    public Boolean transTest(){
        return userService.transMessageTest();

    }
}
