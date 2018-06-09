package com.huanghuo.common.auth;

import com.huanghuo.common.model.User;
import com.huanghuo.common.service.UserService;
import com.huanghuo.common.util.MiniAppAuthUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by huangcheng on 2018/5/27.
 */

@Service
public class WechatAuthService {
    @Value("weixin.appid")
    private String APP_ID;
    @Value("weixin.appsecret")
    private String APP_SECRET;

    @Autowired
    private UserService userService;

    @Cacheable(value = "wechatauth",key="#openId")
    public String findSesEncrytStrByOpenId(String openId){
        User user =userService.findByOpenId(openId);
        if(user != null) {
            return EncryptUtil.sha1(user.getSessionkey());
        }else {
            return "";
        }
    }

    public AuthResult auth(String code){
        AuthResult result = new AuthResult();
        Map<String, Object> ret = MiniAppAuthUtil.getAuthInfoFromMiniApp(APP_ID, APP_SECRET, code);
        String openId = MapUtils.getString(ret, "openid", "");
        if(StringUtils.isNotEmpty(openId)) {
            String sessionKey = MapUtils.getString(ret, "session_key", "");
            String uinionId = MapUtils.getString(ret, "unionid", "");
            User user = userService.findByOpenId(openId);
            if(user == null) {
                user.setOpenid(openId);
                user.setSessionkey(sessionKey);
                if (StringUtils.isNotEmpty(uinionId)) {
                    user.setUnionid(uinionId);
                }
                user.setCtime(System.currentTimeMillis());
                userService.insert(user);
            }else{
                if(!StringUtils.equals(sessionKey, user.getSessionkey())){
                    user.setSessionkey(sessionKey);
                    if (StringUtils.isNotEmpty(uinionId)) {
                        user.setUnionid(uinionId);
                    }
                    userService.updateByOpenId(user);
                }
            }
            result.setOpenId(openId);
            result.setSessionKey(sessionKey);
            result.setCode(AuthResult.SUCC);
        }else{
            result.setCode(MapUtils.getIntValue(ret,"errcode", AuthResult.FAILED));
            result.setErrMsg(MapUtils.getString(ret,"errmsg", "wechat auth failed"));
        }
        return result;
    }
}
