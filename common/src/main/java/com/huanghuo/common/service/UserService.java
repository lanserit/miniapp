package com.huanghuo.common.service;

import com.huanghuo.common.mapper.UserMapper;
import com.huanghuo.common.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by huangcheng on 2018/5/31.
 */

@Service
public class UserService {
    private final static Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    UserMapper userMapper;

    @Caching(evict = {@CacheEvict(value = "user", key = "'id_' + #user.getId()"),
            @CacheEvict(value = "user", key = "'openId_' + #user.getOpenid()")})
    public int insert(User user){
        return userMapper.insert(user);
    }

    @Cacheable(key = "'openId_'+#openId", value = "user", unless="#result == null")
    public User findByOpenId(String openId){
        logger.debug(openId);
        return userMapper.findByOpenId(openId);
    }

    @Cacheable(key = "'id_'+#id", value = "user", unless="#result == null")
    public User findByUserId(long id){
        return userMapper.findByUserId(id);
    }

    public List<User> getListByLimit(long id, int limit){
        return userMapper.getListByIdByLimit(id, limit);
    }

    public List<User> getListByIds(List<Long> ids){
        return userMapper.getListByIds(ids);
    }

    public User findByNickName(String nickname){
        return userMapper.findByNickName(nickname);
    }

    @Caching(evict = {@CacheEvict(value = "user", key = "'id_' + #user.getId()"),
            @CacheEvict(value = "user", key = "'openId_' + #user.getOpenid()")})
    public int updateByOpenId(User user){
        return userMapper.updateByOpenId(user);
    }

    @Caching(evict = {@CacheEvict(value = "user", key = "'id_' + #user.getId()"),
            @CacheEvict(value = "user", key = "'openId_' + #user.getOpenid()")})
    public int incrAttendCount(User user){
        return userMapper.incrAttendCount(user.getId());
    }

    @Caching(evict = {@CacheEvict(value = "user", key = "'id_' + #user.getId()"),
            @CacheEvict(value = "user", key = "'openId_' + #user.getOpenid()")})
    public int incrWinCount(User user){
        return userMapper.incrWinCount(user.getId());
    }


}
