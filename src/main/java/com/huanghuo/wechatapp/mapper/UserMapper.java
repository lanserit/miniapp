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

    @Insert("INSERT INTO USER(nickname, openid, ctime) VALUES(#{nickname}, #{openid}, #{ctime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(@Param("nickname") String nickname, @Param("openid") String openid, @Param("ctime") long ctime);

}