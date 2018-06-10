package com.huanghuo.common.mapper;

import com.huanghuo.common.model.User;
import org.apache.ibatis.annotations.*;

/**
 * Created by huangcheng on 2018/5/26.
 */

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM User WHERE nickname = #{nickname}")
    User findByNickName(@Param("nickname") String nickname);

    @Select("SELECT * FROM User WHERE openid = #{openid}")
    User findByOpenId(@Param("openid") String openid);

    @Insert("INSERT INTO User(nickname, openid, sessionkey, unionid, ctime, userInfoJson) VALUES(#{nickname}, #{openid}, #{sessionkey}, #{unionid}, #{ctime}, #{userInfoJson})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Update("UPDATE User SET nickname=#{nickname}, sessionkey=#{sessionkey}, userInfoJson=#{userInfoJson} WHERE openid=#{openid}")
    int updateByOpenId(User user);

    @Update("UPDATE User SET attendcount=attendcount+1 WHERE id=#{id}")
    int incrAttendCount(@Param("id") long id);

    @Update("UPDATE User SET wincount=wincount+1 WHERE id=#{id}")
    int incrWinCount(@Param("id") long id);

}