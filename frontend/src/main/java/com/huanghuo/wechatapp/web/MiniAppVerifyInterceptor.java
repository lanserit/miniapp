package com.huanghuo.wechatapp.web;

import com.huanghuo.common.auth.WechatAuthService;
import com.huanghuo.common.model.User;
import com.huanghuo.common.service.UserService;
import com.qcloud.weapp.HttpServletUtils;
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
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String encrpt_ses_key = request.getHeader(WechatAuthService.WX_HEADER_ENCRYTED_KEY);
        String openId = request.getHeader(WechatAuthService.WX_HEADER_OPENID_KEY);
        if (StringUtils.isNotEmpty(encrpt_ses_key) && StringUtils.isNotEmpty(openId)) {
            User user = userService.findByOpenId(openId);
            if(user != null) {
                if (StringUtils.equals(wechatAuthService.findSesEncrytStrByOpenId(openId), encrpt_ses_key)) {
                    return true;
                }
            }
        }
        return false;

    }
}
