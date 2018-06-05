package com.huanghuo.wechatapp.mapper;

import com.huanghuo.wechatapp.domain.model.LotteryActivity;
import com.huanghuo.wechatapp.domain.model.User;
import org.apache.ibatis.annotations.*;

/**
 * Created by huangcheng on 2018/6/3.
 */

@Mapper
public interface LotteryActivityMapper {
    @Insert("INSERT INTO LotteryActivity(acttype, name, actcount, totalcount, maskcount, starttime, endtime, ctime, giftjson,extraattrs, state) VALUES(#{acttype}, #{name}, #{actcount}, #{totalcount},#{maskcount}, #{starttime},#{endtime}, #{ctime},#{giftjson}, #{extraattrs}, #{state})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(LotteryActivity lotteryActivity);

    @Update("UPDATE LotteryActivity SET state=#{newstate} WHERE id=#{id} AND state = #{oldstate}")
    int updateStateById(@Param("id") long id, @Param("newstate") int newstate, @Param("oldstate") int oldstate);

    @Update("UPDATE LotteryActivity SET maskcount=maskcount+#{count} WHERE id=#{id}")
    int incrMaskCountById(@Param("id") long id, @Param("count") int count);

    @Select("SELECT * FROM LotteryActivity WHERE id = #{id}")
    LotteryActivity getById(@Param("id") long id);

    @Update("UPDATE LotteryActivity SET actcount=actcount+1, maskcount=maskcount+1 WHERE actcount < totalcount and id = #{id}")
    int incrActivityCount(@Param("id") long id);


}
