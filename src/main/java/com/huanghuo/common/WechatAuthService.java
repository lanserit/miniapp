package com.huanghuo.common;

import com.google.common.collect.Maps;
import com.huanghuo.util.MiniAppAuthUtil;
import com.huanghuo.wechatapp.domain.model.User;
import com.huanghuo.wechatapp.service.UserService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    public Map<String, Object> auth(String code){
        Map<String, Object> ret = MiniAppAuthUtil.getAuthInfoFromMiniApp(APP_ID, APP_SECRET, code);
        String openId = MapUtils.getString(ret, "openid", "");
        if(StringUtils.isNotEmpty(openId)) {
            String sessionKey = MapUtils.getString(ret, "session_key", "");
            String uinionId = MapUtils.getString(ret, "unionid", "");
            Map<String, Object> result = Maps.newHashMap();
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
            result.put("openid", openId);
            result.put("sessionkey", sessionKey);
            return result;
        }else{
           return ret;
        }
    }
}
