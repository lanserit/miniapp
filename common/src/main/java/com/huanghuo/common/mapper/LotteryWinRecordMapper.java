package com.huanghuo.common.mapper;

import com.huanghuo.common.model.LotteryWinRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by huangcheng on 2018/6/3.
 */

@Mapper
public interface LotteryWinRecordMapper {
    @Insert("INSERT INTO LotteryWinRecord(userId, actId, formid, state, ctime) VALUES(#{userId}, #{actId}, #{formid}, #{state}, #{ctime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(LotteryWinRecord lotteryWinRecord);

    @Select("SELECT * FROM LotteryWinRecord WHERE actId = #{actId}")
    List<LotteryWinRecord> getRecordsByActId(@Param("actId") long actId);

    @Select("SELECT * FROM LotteryWinRecord WHERE actId = #{actId} AND userId = #{userId}")
    LotteryWinRecord getRecordByActIdAndUserId(@Param("actId") long actId, @Param("userId") long userId);

    @Select("SELECT * FROM LotteryWinRecord WHERE actId = #{actId} AND id > #{id} ORDER BY id DESC LIMIT #{limit}")
    List<LotteryWinRecord> getRecordsByActIdByLimit(@Param("actId") long actId, @Param("id") long id, @Param("limit") int limit);

    @Select("SELECT userId FROM LotteryWinRecord WHERE actId = #{actId}")
    List<Long> getUserIdsByActId(@Param("actId") long actId);

    @Select("SELECT actId FROM LotteryWinRecord WHERE userId = #{userId}")
    List<Long> getActIdsByUserId(@Param("userId") long userId);

    @Select("SELECT * FROM LotteryWinRecord WHERE userId = #{userId}")
    List<LotteryWinRecord> getByUserId(@Param("userId") long userId);

    @Update("UPDATE LotteryWinRecord SET state=#{state} WHERE userId=#{userId} AND actId=#{actId} AND state=0")
    int updateStateByUserId(@Param("userId") long userId, @Param("actId") long actId, @Param("state") int state);

    @Select("SELECT * FROM LotteryWinRecord WHERE actId = #{actId} AND state=#{state}")
    List<LotteryWinRecord> getRecordsByActIdAndState(@Param("actId") long actId, @Param("state") int state);

}
