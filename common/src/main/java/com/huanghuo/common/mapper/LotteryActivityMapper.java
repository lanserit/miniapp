package com.huanghuo.common.mapper;

import com.huanghuo.common.model.LotteryActivity;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by huangcheng on 2018/6/3.
 */

@Mapper
public interface LotteryActivityMapper {
    @Insert("INSERT INTO LotteryActivity(acttype, name, actcount, totalcount, maskcount, starttime, endtime, ctime, giftjson, extraattrs, state, pic1, pic2, pic3, pic4) VALUES(#{acttype}, #{name}, #{actcount}, #{totalcount},#{maskcount}, #{starttime}," +
            "#{endtime}, #{ctime},#{giftjson}, #{extraattrs}, #{state}, #{pic1}, #{pic2},#{pic3},#{pic4})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(LotteryActivity lotteryActivity);

    @Update("UPDATE LotteryActivity SET state=#{newstate} WHERE id=#{id}")
    int updateStateById(@Param("id") long id, @Param("newstate") int newstate);

    @Delete("DELETE FROM LotteryActivity WHERE id = #{id} AND state= #{state}")
    int deleteByIdAndState(@Param("id") long id, @Param("state") int state);

    @Update("UPDATE LotteryActivity SET maskcount=maskcount+#{count} WHERE id=#{id}")
    int incrMaskCountById(@Param("id") long id, @Param("count") int count);

    @Select("SELECT * FROM LotteryActivity WHERE id = #{id}")
    LotteryActivity getById(@Param("id") long id);

    @Select("SELECT * FROM LotteryActivity WHERE state=#{state} AND acttype=#{acttype} ORDER BY starttime DESC LIMIT #{limit}")
    List<LotteryActivity> getListByState(@Param("acttype") int acttype, @Param("state") int state, @Param("limit") int limit);

    @Select("SELECT * FROM LotteryActivity WHERE state=#{state} ORDER BY endtime > #{endtime} ")
    List<LotteryActivity> getListByEndtimeAndState(@Param("state") int state, @Param("endtime") long endtime);

    @Select("SELECT * FROM LotteryActivity ORDER BY starttime DESC LIMIT #{limit}")
    List<LotteryActivity> getList(@Param("limit") int limit);

    @Update("UPDATE LotteryActivity SET actcount=actcount+1, maskcount=maskcount+1 WHERE actcount < totalcount and id = #{id}")
    int incrActivityCount(@Param("id") long id);

    @Update("UPDATE LotteryActivity SET wincount=wincount+1 WHERE wincount < totalwincount and id = #{id}")
    int incrActivityWinCount(@Param("id") long id);
}
