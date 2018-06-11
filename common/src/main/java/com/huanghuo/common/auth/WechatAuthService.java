package com.huanghuo.common.auth;

import com.huanghuo.common.model.User;
import com.huanghuo.common.service.UserService;
import com.huanghuo.common.util.MiniAppAuthUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final static Logger logger = LoggerFactory.getLogger(WechatAuthService.class);
    @Value("${weixin.appid}")
    private String APP_ID;
    @Value("${weixin.appsecret}")
    private String APP_SECRET;

    public static final String WX_HEADER_SKEY = "X-WX-Skey";
    public static final String WX_HEADER_ENCRYTED_KEY = "X-WX-ENCRYPTED-KEY";
    public static final String WX_HEADER_OPENID_KEY = "X-WX-ENCRYPTED-OPENID";

    public String getAPP_ID() {
        return APP_ID;
    }

    public String getAPP_SECRET() {
        return APP_SECRET;
    }

    @Autowired
    private UserService userService;

    @Cacheable(value = "wechatauth",key="#openId")
    public String findSesEncrytStrByOpenId(String openId){
        User user =userService.findByOpenId(openId);
        if(user != null) {
            return WeixinSignatureUtil.sha1(user.getSessionkey());
        }else {
            return "";
        }
    }

    public Map<String, Object> getAccessToken(){
        Map<String, Object> ret =MiniAppAuthUtil.getAccessToken(APP_ID, APP_SECRET);
        return ret;
    }

    public AuthResult auth(String code){
        AuthResult result = new AuthResult();
        Map<String, Object> ret = MiniAppAuthUtil.getAuthInfoFromMiniApp(APP_ID, APP_SECRET, code);
        String openId = MapUtils.getString(ret, "openid", "");
        if(StringUtils.isNotEmpty(openId)) {
            String sessionKey = MapUtils.getString(ret, "session_key", "");
            String uinionId = MapUtils.getString(ret, "unionid", "");
            logger.info(String.format("skey %s openId %s unionId %s", sessionKey, openId, uinionId));
            User user = userService.findByOpenId(openId);
            if(user == null) {
                user = new User();
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
                    if(userService.updateByOpenId(user)> 0){
                        logger.info(openId, " udpate sessionkey ", sessionKey);
                    }
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
