package com.huanghuo.common.service;

import com.huanghuo.common.LotteryConst;
import com.huanghuo.common.mapper.LotteryActivityMapper;
import com.huanghuo.common.mapper.LotteryWinRecordMapper;
import com.huanghuo.common.model.LotteryActivity;
import com.huanghuo.common.model.LotteryWinRecord;
import com.huanghuo.common.util.BusinessCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by huangcheng on 2018/6/4.
 */
@Service
public class LotteryService {
    @Autowired
    private LotteryActivityMapper lotteryActivityMapper;

    @Autowired
    private LotteryWinRecordMapper lotteryWinRecordMapper;

    @Autowired
    private UserService userService;

    public boolean add(LotteryActivity activity) {
        return lotteryActivityMapper.insert(activity) > 0;
    }

    public LotteryActivity getById(long id) {
        return lotteryActivityMapper.getById(id);
    }

    public List<LotteryActivity> getListByState(int acttype, int state, int limit) {
        return lotteryActivityMapper.getListByState(acttype, state, limit);
    }

    @Transactional
    public int attendLottery(long actId, long userId) {
        if (lotteryActivityMapper.incrActivityCount(actId) > 0) {
            if (userService.incrAttendCount(userId) > 0) {
                LotteryWinRecord lw = new LotteryWinRecord();
                lw.setActId(actId);
                lw.setUserId(userId);
                lw.setState(LotteryConst.LotteryState.PENDING);
                lw.setCtime(System.currentTimeMillis());
                if (lotteryWinRecordMapper.insert(lw) > 0) {
                    return BusinessCode.SUCC;
                } else {
                    throw new RuntimeException("insert winrecord failed");
                }
            } else {
                throw new RuntimeException("incr activity count failed");
            }
        }
        return BusinessCode.FAILED;
    }

    @Transactional
    public int winLottery(long actId, long userId) {
        LotteryActivity activity = getById(actId);
        if (activity != null && activity.getState() == LotteryConst.State.LOTTERY) {
            if (lotteryActivityMapper.incrActivityWinCount(actId) > 0) {
                if (userService.incrWinCount(userId) > 0) {
                    if (lotteryWinRecordMapper.updateStateByUserId(userId, LotteryConst.LotteryState.WIN) > 0) {
                        return BusinessCode.SUCC;
                    } else {
                        throw new RuntimeException("win lottery failed");
                    }
                } else {
                    throw new RuntimeException("incr activity wincount failed");
                }
            }
        }
        return BusinessCode.FAILED;
    }

}
