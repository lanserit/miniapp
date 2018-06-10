package com.huanghuo.wechatapp.controller;

import com.google.common.collect.Maps;
import com.huanghuo.common.auth.*;
import com.huanghuo.common.model.User;
import com.huanghuo.common.service.UserService;
import com.huanghuo.common.util.AjaxResult;
import com.huanghuo.common.util.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by huangcheng on 2018/5/30.
 */
@RestController
public class AuthController {
    private final static Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private WechatAuthService wechatAuthService;

    @RequestMapping("/login")
    @ResponseBody
    public AjaxResult login(@RequestParam("code") String code, HttpServletResponse response) {
        AuthResult result = wechatAuthService.auth(code);
        Map<String, Object> ret = Maps.newHashMap();
        try{
        if (result.getCode() == AuthResult.SUCC) {
            ret.put("openId", EncryptUtil.encrypt(result.getOpenId(), wechatAuthService.getAPP_SECRET()));
            ret.put("encryptSesKey", WeixinSignatureUtil.sha1(result.getSessionKey()));
            return AjaxResult.ajaxSuccess(ret);
        } else {
            ret.put("errMsg", result.getErrMsg());
            return AjaxResult.ajaxFailed(result.getCode(), result.getErrMsg());
        }
        }catch (Exception e){
            return AjaxResult.ajaxFailed(e.getMessage());
        }
    }

    @RequestMapping("/user/update")
    @ResponseBody
    public AjaxResult updateUser(@RequestParam("rawData") String rawData, @RequestParam("encryptedData") String encryptedData, @RequestParam("iv") String iv, @RequestParam("signature") String signature, HttpServletRequest request) {
        String openId = request.getHeader(WechatAuthService.WX_HEADER_OPENID_KEY);
        logger.info(rawData, encryptedData, signature, iv);
        User user = userService.findByOpenId(openId);
        String sessionKey = user.getSessionkey();
        if (StringUtils.isNotEmpty(sessionKey)) {
            String signature2 = WeixinSignatureUtil.signature(rawData, sessionKey);
            if (StringUtils.equals(signature, signature2)) {
                String data = WeixinSignatureUtil.decryptUtf8(sessionKey, encryptedData, iv);
                Map<String, Object> dataObj = JsonUtil.getMapFromJson(data);
                String watermarkAppId = MapUtils.getString((Map) dataObj.get("watermark"), "appid");
                logger.info(data);
                if (StringUtils.equals(wechatAuthService.getAPP_ID(), watermarkAppId)) {
                    UserInfo userInfo = UserInfo.buildFromJson(data);
                    user.setUserInfoJson(data);
                    if(userService.updateByOpenId(user) > 0) {
                        return AjaxResult.ajaxSuccess();
                    }else{
                        return AjaxResult.ajaxFailed("update userInfo failed!");
                    }
                } else {
                    return AjaxResult.ajaxFailed("appid is not correct!");
                }
            }
            return AjaxResult.ajaxFailed("signature is not correct!");
        } else {
            return AjaxResult.ajaxFailed("update failed");
        }
    }
}
