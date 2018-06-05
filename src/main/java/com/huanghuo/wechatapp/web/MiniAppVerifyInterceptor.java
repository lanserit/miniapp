package com.huanghuo.wechatapp.web;

import com.qcloud.weapp.authorization.LoginService;
import com.qcloud.weapp.authorization.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by huangcheng on 2018/5/28.
 */

@Component
public class MiniAppVerifyInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserInfo userInfo = loginService.check(request, response);
        if (userInfo != null) return true;
        return false;
    }
}
