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
import org.springframework.data.redis.core.RedisTemplate;
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
    @Value("${weixin.templateid}")
    private String APP_TEMPLATE_ID;

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

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Cacheable(value = "wechatauth",key="#openId")
    public String findSesEncrytStrByOpenId(String openId){
        User user =userService.findByOpenId(openId);
        if(user != null) {
            return WeixinSignatureUtil.sha1(user.getSessionkey());
        }else {
            return "";
        }
    }

    public String getAccessToken(){
        String token = (String)redisTemplate.opsForValue().get(APP_ID+APP_SECRET);
        if(StringUtils.isEmpty(token)) {
            Map<String, Object> ret = MiniAppAuthUtil.getAccessToken(APP_ID, APP_SECRET);
            if(StringUtils.isNotEmpty(MapUtils.getString(ret, "errcode"))){
                return null;
            }
            token = MapUtils.getString(ret, "access_token");
            int expireTime = MapUtils.getIntValue(ret, "expires_in", 0);
            redisTemplate.opsForValue().set(APP_ID+APP_SECRET, token, (expireTime - 10)*1000);
        }
        return token;
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
