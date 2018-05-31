package com.huanghuo.wechatapp.controller;

import com.huanghuo.wechatapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by huangcheng on 2018/5/30.
 */
@RestController
public class AuthController {
    @Autowired
    UserService userService;

    @RequestMapping("/login")
    public boolean login() {
        int count = userService.insert("hello", "12131");
        return count > 0;
    }
}
