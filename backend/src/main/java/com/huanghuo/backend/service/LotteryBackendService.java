package com.huanghuo.backend.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.huanghuo.common.LotteryConst;
import com.huanghuo.common.auth.WechatAuthService;
import com.huanghuo.common.mapper.LotteryActivityMapper;
import com.huanghuo.common.mapper.LotteryWinRecordMapper;
import com.huanghuo.common.model.LotteryActivity;
import com.huanghuo.common.model.LotteryWinRecord;
import com.huanghuo.common.model.User;
import com.huanghuo.common.service.LotteryService;
import com.huanghuo.common.service.UserService;
import com.huanghuo.common.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private WechatAuthService wechatAuthService;

    @Autowired
    private UserService userService;

    public List<LotteryWinRecord> getLotteryWinRecords(long actId){
        return lotteryWinRecordMapper.getRecordsByActId(actId);
    }

    @Scheduled(cron = "0 0/2 * * * ?")
    public void generateWinners() {
        List<LotteryActivity> list = lotteryActivityMapper.getListByEndtimeAndState(LotteryConst.State.ONLINE, System.currentTimeMillis());
        logger.info("start draw lottery count[{}]", list.size());
        for (LotteryActivity activity : list) {
           drawLottery(activity);
        }
    }

    public void drawLottery(LotteryActivity activity){
        try {
            logger.info("Id[{}] Name[{}]", activity.getId(), activity.getName());
            lotteryService.generateWinner(activity);
            List<LotteryWinRecord> lst = lotteryWinRecordMapper.getRecordsByActIdAndState(activity.getId(), LotteryConst.LotteryState.WIN);
            if(lst.size() > 0){
                logger.info(JsonUtil.getJsonString(lst));
            }
            for(LotteryWinRecord record : lst){
                User user = userService.findByUserId(record.getUserId());
                Map<String, Object> data = Maps.newHashMap();
                Map<String, Object> d1 = Maps.newHashMap();
                d1.put("value", activity.getName());
                data.put("keyword1",d1);
                Map<String, Object> d2 = Maps.newHashMap();
                d2.put("value", "请联系工作人员");
                data.put("keyword2", d2);
                String page = activity.getExtraattrs();
                Map<String, Object> ret = wechatAuthService.sendMessage(user.getOpenid(), page, record.getFormid(), data, "","");
                logger.info(JsonUtil.getJsonString(ret));
            }
        }catch (RuntimeException e){
            logger.error(e.getMessage(), e);
        }

    }
}
