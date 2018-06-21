package com.huanghuo.backend.service;

import com.google.common.collect.Lists;
import com.huanghuo.common.LotteryConst;
import com.huanghuo.common.mapper.LotteryActivityMapper;
import com.huanghuo.common.mapper.LotteryWinRecordMapper;
import com.huanghuo.common.model.LotteryActivity;
import com.huanghuo.common.service.LotteryService;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by huangcheng on 2018/6/21.
 */

@Service
public class LotteryBackendService {
    private static Logger logger = LoggerFactory.getLogger(LotteryBackendService.class);
    @Autowired
    private LotteryActivityMapper lotteryActivityMapper;
    @Autowired
    private LotteryWinRecordMapper lotteryWinRecordMapper;

    @Autowired
    private LotteryService lotteryService;

    @Scheduled(cron = "0 0/5 10,23 * * ?")
    public void generateWinners() {
        List<LotteryActivity> list = lotteryActivityMapper.getListByEndtimeAndState(LotteryConst.State.LOTTERY, System.currentTimeMillis());
        for (LotteryActivity activity : list) {
            try {
                lotteryService.generateWinner(activity);
            }catch (RuntimeException e){
                logger.error(e.getMessage(), e);
            }
        }
    }
}
