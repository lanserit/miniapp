package com.huanghuo.common.service;

import com.huanghuo.common.mapper.LotteryActivityMapper;
import com.huanghuo.common.mapper.LotteryWinRecordMapper;
import com.huanghuo.common.model.LotteryActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    public boolean add(LotteryActivity activity) {
        return lotteryActivityMapper.insert(activity) > 0;
    }

    public LotteryActivity getById(long id) {
        return lotteryActivityMapper.getById(id);
    }

    public List<LotteryActivity> getListByState(int acttype, int state, int limit) {
        return lotteryActivityMapper.getListByState(acttype, state, limit);
    }

}
