package com.huanghuo.common.util;

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
}
