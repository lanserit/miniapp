package com.huanghuo.wechatapp.mapper;

import com.huanghuo.wechatapp.domain.model.LotteryActivity;
import com.huanghuo.wechatapp.domain.model.LotteryWinRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by huangcheng on 2018/6/3.
 */

@Mapper
public interface LotteryWinRecordMapper {
    @Insert("INSERT INTO LotteryWinRecord(userId, actId, state, ctime) VALUES(#{userId}, #{actId}, #{state}, #{ctime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(LotteryWinRecord lotteryWinRecord);

    @Select("SELECT * FROM LotteryWinRecord WHERE actId = #{actId}")
    List<LotteryWinRecord> getRecordsByActId(@Param("actId") long actId);

    @Update("UPDATE LotteryWinRecord SET state=#{state} WHERE userId=#{userId}")
    int updateStateByUserId(@Param("userId") long userId, @Param("state") int state);

    @Select("SELECT * FROM LotteryWinRecord WHERE actId = #{actId} AND state=#{state}")
    List<LotteryWinRecord> getRecordsByActIdAndState(@Param("actId") long actId, @Param("state") int state);

}
