package com.huanghuo.common.mapper;

import com.huanghuo.common.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by huangcheng on 2018/5/26.
 */

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM User WHERE nickname = #{nickname}")
    User findByNickName(@Param("nickname") String nickname);

    @Select("SELECT * FROM User WHERE id = #{id}")
    User findByUserId(@Param("id") long id);

    @Select("SELECT * FROM User WHERE openid = #{openid}")
    User findByOpenId(@Param("openid") String openid);

    @Select("SELECT * FROM User WHERE id > #{id} ORDER BY id ASC LIMIT #{limit}")
    List<User> getListByIdByLimit(@Param("id") long id, @Param("limit") int limit);

    @SelectProvider(type = BatchUtilProvider.class, method = "getUserByIds")
    List<User> getListByIds(@Param("ids") List<Long> ids);

    @Select("SELECT COUNT(1) FROM User")
    int getCount();

    @Insert("INSERT INTO User(nickname, openid, sessionkey, unionid, ctime, userInfoJson, phone, addressInfo) VALUES(#{nickname}, #{openid}, #{sessionkey}, #{unionid}, #{ctime}, #{userInfoJson}, #{phone}, #{addressInfo})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Update("UPDATE User SET nickname=#{nickname}, sessionkey=#{sessionkey}, userInfoJson=#{userInfoJson}, phone=#{phone}, addressInfo=#{addressInfo} WHERE openid=#{openid}")
    int updateByOpenId(User user);

    @Update("UPDATE User SET attendcount=attendcount+1 WHERE id=#{id}")
    int incrAttendCount(@Param("id") long id);

    @Update("UPDATE User SET wincount=wincount+1 WHERE id=#{id}")
    int incrWinCount(@Param("id") long id);

}