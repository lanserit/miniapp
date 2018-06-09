package com.huanghuo.wechatapp.web;

import com.huanghuo.auth.AuthResult;
import com.huanghuo.auth.WechatAuthService;
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
public class MiniAppVerifyInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private WechatAuthService wechatAuthService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String encrpt_ses_key = request.getParameter("encrpt_ses_key");
        String openId = request.getParameter("openid");
        if(StringUtils.isNotEmpty(encrpt_ses_key)&& StringUtils.isNotEmpty(openId)) {
            if(StringUtils.equals(wechatAuthService.findSesEncrytStrByOpenId(openId), encrpt_ses_key)) {
                return true;
            }
        }
        return false;

    }
}
