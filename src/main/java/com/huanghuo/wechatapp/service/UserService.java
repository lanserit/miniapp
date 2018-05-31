package com.huanghuo.wechatapp.service;

import com.huanghuo.wechatapp.mapper.UserMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by huangcheng on 2018/5/31.
 */

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public int insert(String nickname, String openid){
        return userMapper.insert(nickname, openid, System.currentTimeMillis());
    }

}
