package com.huanghuo.common.auth;

import com.huanghuo.common.model.User;
import com.huanghuo.common.service.UserService;
import com.huanghuo.common.util.JsonUtil;
import com.huanghuo.common.util.MiniAppAuthUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.BagUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by huangcheng on 2018/5/27.
 */

@Service
public class WechatAuthService {
    private final static Logger logger = LoggerFactory.getLogger(WechatAuthService.class);

    @Value("${weixin.appid}")
    public String APP_ID;

    @Value("${weixin.appsecret}")
    public String APP_SECRET;

    @Value("${weixin.templateid}")
    private String APP_TEMPLATE_ID;

    @Value("${weixin.salt}")
    private String APP_SALT;

    private long EXPIRE_TIME = 7*24*60*60*1000;

    public static final String WX_HEADER_SKEY = "X-WX-Skey";
    public static final String WX_HEADER_ENCRYPTED_KEY = "X-WX-ENCRYPTED-KEY";
    public static final String WX_HEADER_OPENID_KEY = "X-WX-ENCRYPTED-OPENID";

    public final static String ACCESS_TOKEN_KEY = "access_token";

    public String getAPP_ID() {
        return APP_ID;
    }

    public String getAPP_SECRET() {
        return APP_SECRET;
    }

    public String getAPP_SALT() {
        return APP_SALT;
    }

    private String accessToken = "";
    private long tokenExpireTime = 0;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    public String getAccessToken() {
        long currentTime = System.currentTimeMillis();
        if (currentTime > tokenExpireTime || StringUtils.isEmpty(accessToken)) {
            Map<String, Object> ret = MiniAppAuthUtil.getAccessToken(APP_ID, APP_SECRET);
            if (StringUtils.isNotEmpty(MapUtils.getString(ret, "errcode"))) {
                return null;
            }
            String token = MapUtils.getString(ret, "access_token");

            int expireTime = MapUtils.getIntValue(ret, "expires_in", 0);
            tokenExpireTime = currentTime + (expireTime - 10) * 1000;
            accessToken = token;
        }
        return accessToken;
    }

    public Map<String, Object> sendMessage(String tmplId, String openId, String page,
                                           String formId, Map<String, Object> data, String color, String emphasisKeyword) {
        Map<String, Object> ret = MiniAppAuthUtil.sendMessage(getAccessToken(), openId,
                tmplId, page, formId, data, color, emphasisKeyword);
        return ret;
    }

    public Map<String, Object> sendMessage(String openId, String page,
                                           String formId, Map<String, Object> data, String color, String emphasisKeyword) {
        return sendMessage(APP_TEMPLATE_ID, openId, page, formId, data, color, emphasisKeyword);
    }

    public static String getRawToken(String openId, String sessionKey){
        return String.format("%s.%s", openId, sessionKey);
    }

    public boolean isAuthorized(String encrytedData, String openId, String sessionKey){
        String token = getRawToken(openId, sessionKey);
        String data = decrpytToken(encrytedData);
        logger.info("client {} encrypted {} , server {}", data, encrytedData, token);
        return StringUtils.equals(token, data);
    }

    public String encrytToken(String openId, String sessionKey){
        String data = getRawToken(openId, sessionKey);
        String token = null;
        try {
            token = EncryptUtil.fastEncrypt(data, APP_SALT);
        }catch (Exception e){
            e.printStackTrace();
        }
        return token;
    }

    public String decrpytToken(String data){
        String raw = null;
        try {
            raw = EncryptUtil.fastDecrypt(data, APP_SALT);
        }catch (Exception e){
            e.printStackTrace();
        }
        return raw;
    }

    public AuthResult auth(String code) {
        AuthResult result = new AuthResult();
        Map<String, Object> ret = MiniAppAuthUtil.getAuthInfoFromMiniApp(APP_ID, APP_SECRET, code);
        logger.info("auth {}", JsonUtil.getJsonString(ret));
        String openId = MapUtils.getString(ret, "openid", "");
        if (StringUtils.isNotEmpty(openId)) {
            String sessionKey = MapUtils.getString(ret, "session_key", "");
            String uinionId = MapUtils.getString(ret, "unionid", "");
            logger.info("sessionkey[{}] openId[{}] unionId[{}]", sessionKey, openId, uinionId);
            User user = userService.findByOpenId(openId);
            if (user == null) {
                user = User.createUser(openId, sessionKey, uinionId);
                if (userService.insert(user) > 0) {
                    result.setOpenId(openId);
                    result.setSessionKey(sessionKey);
                    result.setCode(AuthResult.SUCC);
                    return result;
                } else {
                    logger.info("insert failed");
                }
            } else {
                if (!StringUtils.equals(sessionKey, user.getSessionkey())) {
                    user.setSessionkey(sessionKey);
                    if (StringUtils.isNotEmpty(uinionId)) {
                        user.setUnionid(uinionId);
                    }
                    if (userService.updateByOpenId(user) > 0) {
                        logger.info(openId, " udpate sessionkey ", sessionKey);
                    } else {
                        logger.info("update sessionkey failed");
                    }
                }
                result.setOpenId(openId);
                result.setSessionKey(sessionKey);
                result.setCode(AuthResult.SUCC);
                return result;
            }
            result.setCode(AuthResult.FAILED);
        } else {
            result.setCode(AuthResult.FAILED);
            result.setErrMsg(MapUtils.getString(ret, "errmsg", "wechat auth failed"));
        }
        return result;
    }
}
