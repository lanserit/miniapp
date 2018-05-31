package com.huanghuo.wechatapp.service;

import com.huanghuo.wechatapp.domain.model.User;
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

    public int insert(User user){
        return userMapper.insert(user);
    }

    public User findByOpenId(String openId){
        return userMapper.findByOpenId(openId);
    }

    public User findByNickName(String nickname){
        return userMapper.findByNickName(nickname);
    }

    public int updateByOpenId(User user){
        return userMapper.updateByOpenId(user);
    }
}
