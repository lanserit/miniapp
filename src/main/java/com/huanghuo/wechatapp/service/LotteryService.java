package com.huanghuo.wechatapp.service;

import com.huanghuo.wechatapp.mapper.LotteryActivityMapper;
import com.huanghuo.wechatapp.mapper.LotteryWinRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by huangcheng on 2018/6/4.
 */
public class LotteryService {
    @Autowired
    private LotteryActivityMapper lotteryActivityMapper;

    @Autowired
    private LotteryWinRecordMapper lotteryWinRecordMapper;



}
