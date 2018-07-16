package com.huanghuo.wechatapp.web;

import com.huanghuo.common.LotteryConst;
import com.huanghuo.common.auth.WechatAuthService;
import com.huanghuo.common.auth.WeixinSignatureUtil;
import com.huanghuo.common.model.User;
import com.huanghuo.common.service.UserService;
import com.huanghuo.common.util.AjaxResult;
import com.huanghuo.common.util.BusinessCode;
import com.huanghuo.common.util.JsonUtil;
import com.huanghuo.common.util.MiniAppAuthUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final static Logger logger = LoggerFactory.getLogger(MiniAppVerifyInterceptor.class);
    @Autowired
    private WechatAuthService wechatAuthService;
    @Autowired
    private UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String encrpt_ses_key = request.getHeader(WechatAuthService.WX_HEADER_ENCRYPTED_KEY);
        String openId = request.getHeader(WechatAuthService.WX_HEADER_OPENID_KEY);
        if (StringUtils.isNotEmpty(encrpt_ses_key) && StringUtils.isNotEmpty(openId)) {
            User user = userService.findByOpenId(openId);
            if(user != null) {
                boolean isLogined = wechatAuthService.isAuthorized(encrpt_ses_key, openId, user.getSessionkey());
                logger.info("isLogined {} client_ses  {} ", isLogined, encrpt_ses_key);
                if (isLogined) {
                    logger.info("{} login succ",user.getNickname());
                    return true;
                }else{
                    logger.info("{} login failed", user.getNickname());
                }
            }
        }
        logger.info("encrpt_ses_key {} openId {}", encrpt_ses_key, openId);

        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().print(JsonUtil.getJsonString(AjaxResult.ajaxFailed(BusinessCode.NOT_LOGINED)));
        return false;

    }
}
