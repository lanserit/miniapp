package com.huanghuo.common.util;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by huangcheng on 2018/5/27.
 */
public class MiniAppAuthUtil {
    private static String MINI_APP_URL = "https://api.weixin.qq.com/sns/jscode2session";
    private static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
    private static String TEMPLATE_SEND =  "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=%s";

    public static  Map<String, Object> getAccessToken(String appid, String secret){
        List<NameValuePair> nvp = new ArrayList<NameValuePair>();
        nvp.add(new BasicNameValuePair("grant_type", "client_credential"));
        nvp.add(new BasicNameValuePair("appid", appid));
        nvp.add(new BasicNameValuePair("secret", secret));
        HttpClientUtils.HttpResult result =  HttpClientUtils.get(HttpClientUtils.generateUrl(ACCESS_TOKEN_URL, nvp, "utf-8"), null);
        return result.getJsonContent();
    }

    public static Map<String, Object> getAuthInfoFromMiniApp(String appid, String secret, String code){
        List<NameValuePair> nvp = new ArrayList<NameValuePair>();
        nvp.add(new BasicNameValuePair("appid", appid));
        nvp.add(new BasicNameValuePair("secret", secret));
        nvp.add(new BasicNameValuePair("js_code", code));
        nvp.add(new BasicNameValuePair("grant_type", "authorization_code"));
        HttpClientUtils.HttpResult result =  HttpClientUtils.get(HttpClientUtils.generateUrl(MINI_APP_URL, nvp, "utf-8"), null);
        return result.getJsonContent();
    }

    public static Map<String, Object> sendMessage(String accessToken, String openId, String templateId, String page,
                                                  String formId, Map<String, Object> data, String color, String emphasisKeyword){
        Map<String, Object> params = Maps.newHashMap();
        params.put("touser", openId);
        params.put("template_id", templateId);
        if(StringUtils.isNotEmpty(page)) {
            params.put("page", page);
        }
        params.put("form_id", formId);
        params.put("data", data);
        if(StringUtils.isNotEmpty(color)){
            params.put("color", color);
        }
        params.put("emphasis_keyword", emphasisKeyword);
        HttpClientUtils.HttpResult result = HttpClientUtils.post(String.format(TEMPLATE_SEND, accessToken), params);
        return result.getJsonContent();
    }
}
