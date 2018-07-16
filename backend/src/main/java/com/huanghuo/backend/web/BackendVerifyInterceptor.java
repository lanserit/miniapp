package com.huanghuo.backend.web;

import com.huanghuo.backend.service.AuthService;
import com.huanghuo.common.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by huangcheng on 2018/5/28.
 */

@Component
public class BackendVerifyInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String cookieSecVal = CookieUtil.getCookie(request, AuthService.COOKIE_NAME_AUTH);
        String cookieUsernameVal = CookieUtil.getCookie(request, AuthService.COOKIE_NAME_USERNAME);
        if(StringUtils.isNotEmpty(cookieSecVal) && StringUtils.isNotEmpty(cookieUsernameVal)) {
            return authService.isAuthorized(cookieUsernameVal, cookieSecVal);
        }else{
            return false;
        }
    }
}
