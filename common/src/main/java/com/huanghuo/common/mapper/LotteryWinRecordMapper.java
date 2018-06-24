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

    @Select("SELECT userId FROM LotteryWinRecord WHERE actId = #{actId}")
    List<Long> getUserIdsByActId(@Param("actId") long actId);

    @Update("UPDATE LotteryWinRecord SET state=#{state} WHERE userId=#{userId} AND state=0")
    int updateStateByUserId(@Param("userId") long userId, @Param("state") int state);

    @Select("SELECT * FROM LotteryWinRecord WHERE actId = #{actId} AND state=#{state}")
    List<LotteryWinRecord> getRecordsByActIdAndState(@Param("actId") long actId, @Param("state") int state);

}
