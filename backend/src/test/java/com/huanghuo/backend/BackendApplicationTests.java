package com.huanghuo.backend;

import com.google.common.collect.Maps;
import com.huanghuo.common.auth.WechatAuthService;
import com.huanghuo.common.model.User;
import com.huanghuo.common.mapper.UserMapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.MapUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;

@RunWith(SpringRunner.class)
@MapperScan("com.huanghuo.mapper")
@SpringBootTest
@Transactional
public class BackendApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private WechatAuthService authService;

    @Test
    public void testToken() throws Exception {
       String token = authService.getAccessToken();
        System.out.println(token);
    }

    String token = "asdfasdf";

    @Test
    public void setRedis(){
        redisTemplate.opsForValue().set(WechatAuthService.ACCESS_TOKEN_KEY, token, 1000);
    }

    @Test
    public void getRedis(){
        try{
            String m = (String)redisTemplate.opsForValue().get(WechatAuthService.ACCESS_TOKEN_KEY);
            System.out.println(m);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}


