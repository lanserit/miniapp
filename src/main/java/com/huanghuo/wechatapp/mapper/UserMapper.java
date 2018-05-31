package com.huanghuo.wechatapp.mapper;

import com.huanghuo.wechatapp.domain.model.User;
import org.apache.ibatis.annotations.*;

/**
 * Created by huangcheng on 2018/5/26.
 */

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM USER WHERE nickname = #{nickname}")
    User findByNickName(@Param("nickname") String nickname);

    @Select("SELECT * FROM USER WHERE openid = #{openid}")
    User findByOpenId(@Param("openid") String openid);

    @Insert("INSERT INTO USER(nickname, openid, sessionkey, unionid, ctime) VALUES(#{nickname}, #{openid}, #{sessionkey}, #{unionid}, #{ctime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Update("UPDATE User SET nickname=#{nickname}, sessionkey=#{sessionkey} WHERE openid=#{openid}")
    int updateByOpenId(User user);
}