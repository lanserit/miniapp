package com.huanghuo.common.mapper;

import com.huanghuo.common.model.LotteryActivity;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * Created by huangcheng on 2018/6/3.
 */

@Mapper
public interface LotteryActivityMapper {
    @Insert("INSERT INTO LotteryActivity(acttype, name, actcount, totalcount, totalwincount, maskcount, starttime, endtime, ctime, giftjson, extraattrs, state, pic1, pic2, pic3, pic4) VALUES(#{acttype}, #{name}, #{actcount}, " +
            "#{totalcount}, #{totalwincount}, #{maskcount}, #{starttime},#{endtime}, #{ctime}, #{giftjson}, #{extraattrs}, #{state}, #{pic1}, #{pic2},#{pic3},#{pic4})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(LotteryActivity lotteryActivity);

    @Update("UPDATE LotteryActivity SET state=#{newstate} WHERE id=#{id}")
    int updateStateById(@Param("id") long id, @Param("newstate") int newstate);

    @Update("UPDATE LotteryActivity SET state=#{state}, acttype=#{acttype}, name=#{name}, actcount=#{actcount}, totalcount=#{totalcount}, totalwincount=#{totalwincount}, maskcount=#{maskcount}, starttime=#{starttime}, endtime=#{endtime}, giftjson=#{giftjson}, extraattrs=#{extraattrs}, " +
            "pic1={#pic1}, pic2={#pic2}, pic3={#pic3},pic4={#pic4} WHERE id=#{id}")
    int updateById(LotteryActivity activity);

    @Delete("DELETE FROM LotteryActivity WHERE id = #{id} AND state= #{state}")
    int deleteByIdAndState(@Param("id") long id, @Param("state") int state);

    @Update("UPDATE LotteryActivity SET maskcount=maskcount+#{count} WHERE id=#{id}")
    int incrMaskCountById(@Param("id") long id, @Param("count") int count);

    @Select("SELECT * FROM LotteryActivity WHERE id = #{id}")
    LotteryActivity getById(@Param("id") long id);

    @SelectProvider(type = BatchUtilProvider.class, method = "getLotteryActivityByIds")
    List<LotteryActivity> getListByIds(@Param("ids") List<Long> ids);

    @Select("SELECT * FROM LotteryActivity WHERE id > #{id} AND state=#{state} ORDER BY id DESC LIMIT #{limit}")
    List<LotteryActivity> getListByState(@Param("id") long id, @Param("state") int state, @Param("limit") int limit);

    @Select("SELECT * FROM LotteryActivity WHERE state=#{state} AND endtime <= #{endtime} ORDER BY endtime desc")
    List<LotteryActivity> getListByEndtimeAndState(@Param("state") int state, @Param("endtime") long endtime);

    @Select("SELECT * FROM LotteryActivity WHERE id > #{id} ORDER BY id DESC LIMIT #{limit}")
    List<LotteryActivity> getList(@Param("id") long id,@Param("limit") int limit);

    @Select("SELECT * FROM LotteryActivity ORDER BY ctime DESC LIMIT #{limit}")
    List<LotteryActivity> getListByCtime(@Param("limit") int limit);

    @Update("UPDATE LotteryActivity SET actcount=actcount+1, maskcount=maskcount+1 WHERE actcount < totalcount AND id = #{id}")
    int incrActivityCount(@Param("id") long id);

    @Update("UPDATE LotteryActivity SET wincount=wincount+1 WHERE id = #{id} AND wincount < totalwincount AND state=#{state}")
    int incrActivityWinCount(@Param("id") long id, @Param("state") int state);
}
