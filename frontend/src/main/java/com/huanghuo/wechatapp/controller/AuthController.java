package com.huanghuo.wechatapp.controller;

import com.google.common.collect.Maps;
import com.huanghuo.common.auth.AuthResult;
import com.huanghuo.common.auth.EncryptUtil;
import com.huanghuo.common.auth.WechatAuthService;
import com.huanghuo.common.service.UserService;
import com.qcloud.weapp.ConfigurationException;
import com.qcloud.weapp.authorization.LoginServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    public Map<String, Object> login(@RequestParam("code") String code, HttpServletResponse response) throws LoginServiceException, ConfigurationException {
        AuthResult result = wechatAuthService.auth(code);
        Map<String, Object> ret = Maps.newHashMap();
        if(result.getCode() == AuthResult.SUCC){
            ret.put("opendId", result.getOpenId());
            ret.put("encryptSesKey", EncryptUtil.sha1(result.getSessionKey()));
            ret.put("code", AuthResult.SUCC);
        }else{
            ret.put("code", AuthResult.FAILED);
            ret.put("errMsg", result.getErrMsg());
        }
        return ret;
    }
}
