package com.amily.controller;

import com.amily.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author lizhuo
 * @since 2019/1/13 上午12:29
 **/

@RestController
public class HelloController {

    @GetMapping("/hello")
    public User hello(){
        User user = new User();
        user.setName("hello");
        user.setAge(18);
        return user;
    }
}
