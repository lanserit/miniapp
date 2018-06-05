package com.huanghuo.wechatapp.controller;

import com.huanghuo.wechatapp.service.UserService;
import com.qcloud.weapp.ConfigurationException;
import com.qcloud.weapp.authorization.LoginServiceException;
import com.qcloud.weapp.authorization.LoginService;
import com.qcloud.weapp.authorization.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by huangcheng on 2018/5/30.
 */
@RestController
public class AuthController {
    private final static Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private LoginService loginService;
    @RequestMapping("/login")
    public void login(HttpServletRequest request, HttpServletResponse response) throws LoginServiceException, ConfigurationException {
        UserInfo userInfo = loginService.login(request, response);
        String openId = userInfo.getOpenId();
    }
}
