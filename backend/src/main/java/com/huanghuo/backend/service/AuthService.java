package com.huanghuo.backend.service;

import com.google.common.collect.Maps;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Created by huangcheng on 2018/6/26.
 */


@Service
public class AuthService {
    public static String COOKIE_NAME_AUTH="sec_backend";
    public static String COOKIE_NAME_USERNAME="sec_username";

    @Value("${admin.authinfos}")
    private String authinfos;

    private Map<String, String> authMap;

    @PostConstruct
    private void init(){
        authMap = Maps.newHashMap();
        String[] items = authinfos.split(";");
        for(String it : items){
            String[] pair = it.trim().split(":");
            authMap.put(pair[0], pair[1]);
        }
    }

    public static String md5Hex(String username, String passwd){
        return DigestUtils.md5Hex(username+":"+ passwd);
    }

    public boolean isAuthorized(String username, String secval) {
        if (!authMap.containsKey(username)) return false;
        return StringUtils.equals(md5Hex(username, authMap.get(username)), secval);
    }

}
