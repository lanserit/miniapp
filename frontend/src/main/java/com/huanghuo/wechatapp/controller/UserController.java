package com.huanghuo.wechatapp.controller;

import com.google.common.collect.Maps;
import com.huanghuo.common.auth.AuthResult;
import com.huanghuo.common.auth.UserInfo;
import com.huanghuo.common.auth.WechatAuthService;
import com.huanghuo.common.auth.WeixinSignatureUtil;
import com.huanghuo.common.model.User;
import com.huanghuo.common.service.UserService;
import com.huanghuo.common.util.AjaxResult;
import com.huanghuo.common.util.BusinessCode;
import com.huanghuo.common.util.JsonUtil;
import com.huanghuo.common.util.MiniAppAuthUtil;
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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * Created by huangcheng on 2018/5/30.
 */
@RestController
public class UserController {
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private WechatAuthService wechatAuthService;

    @RequestMapping("/login")
    @ResponseBody
    public AjaxResult login(@RequestParam("code") String code, HttpServletResponse response) {
        logger.info("login code {}", code);
        AuthResult result = wechatAuthService.auth(code);
        Map<String, Object> ret = Maps.newHashMap();
        try {
            if (result.getCode() == AuthResult.SUCC) {
                ret.put("openId", result.getOpenId());
                ret.put("encryptSesKey", wechatAuthService.encrytToken(result.getOpenId(), result.getSessionKey()));
                logger.info("login code {} succ {}", code, JsonUtil.getJsonString(result));
                return AjaxResult.ajaxSuccess(ret);
            } else {
                ret.put("errMsg", result.getErrMsg());
                logger.info("login code {} failed {}", code, JsonUtil.getJsonString(result));
                return AjaxResult.ajaxFailed(BusinessCode.NOT_LOGINED, result.getErrMsg());
            }
        } catch (Exception e) {
            logger.error("login code {} failed {}", code, e.getMessage(), e);
            return AjaxResult.ajaxFailed(e.getMessage());
        }
    }


    @RequestMapping("/user/info")
    @ResponseBody
    public AjaxResult getUserInfo(HttpServletRequest request) {
        String openId = request.getHeader(WechatAuthService.WX_HEADER_OPENID_KEY);
        User user = userService.findByOpenId(openId);
        if (user != null) {
            return AjaxResult.ajaxSuccess(user.getMap());
        } else {
            return AjaxResult.ajaxFailed(BusinessCode.USER_NOT_EXIST);
        }
    }


    @RequestMapping("/user/addr")
    @ResponseBody
    public AjaxResult updateAddr(@RequestParam("addr") String addr, HttpServletRequest request) {
        String openId = request.getHeader(WechatAuthService.WX_HEADER_OPENID_KEY);
        User user = userService.findByOpenId(openId);
        if (user != null) {
            user.setAddressInfo(addr);
            if (userService.updateByOpenId(user) > 0) {
                return AjaxResult.ajaxSuccess();
            }
        }
        return AjaxResult.ajaxFailed(BusinessCode.FAILED);
    }

    @RequestMapping("/user/update")
    @ResponseBody
    public AjaxResult updateUser(@RequestParam("rawData") String rawData, @RequestParam("encryptedData") String encryptedData, @RequestParam("iv") String iv,
                                 @RequestParam("signature") String signature, HttpServletRequest request) {
        String openId = request.getHeader(WechatAuthService.WX_HEADER_OPENID_KEY);
        try {
            rawData = URLDecoder.decode(rawData, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return AjaxResult.ajaxFailed("rawData urldecode failed");
        }
        logger.info(rawData, encryptedData, signature, iv);
        User user = userService.findByOpenId(openId);
        if (user != null) {
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
                        String nickName = MiniAppAuthUtil.encodeEmoji(userInfo.getNickName());
                        userInfo.setNickName(nickName);
                        user.setUserInfoJson(JsonUtil.getJsonString(userInfo));
                        user.setNickname(nickName);
                        if (userService.updateByOpenId(user) > 0) {
                            return AjaxResult.ajaxSuccess();
                        } else {
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
        } else {
            return AjaxResult.ajaxFailed("user = null");
        }
    }
}
