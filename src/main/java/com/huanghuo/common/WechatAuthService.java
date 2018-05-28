package com.huanghuo.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by huangcheng on 2018/5/27.
 */

@Service
public class WechatAuthService {
    @Value("weixin.appid")
    private String APP_ID;
    @Value("weixin.appsecret")
    private String APP_SECRET;
}
