package com.huanghuo.wechatapp.controller;

import com.huanghuo.common.LotteryConst;
import com.huanghuo.common.model.LotteryActivity;
import com.huanghuo.common.service.LotteryService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by huangcheng on 2018/6/9.
 */

@RestController
@RequestMapping("/lottery")
public class LotteryController {
    @Autowired
    private LotteryService lotteryService;

    @RequestMapping("/list")
    @ResponseBody
    public List<Map<String, Object>> getOpenList(@RequestParam(name = "limit", defaultValue = "5") int limit) {
        List<LotteryActivity> list = lotteryService.getListByState(LotteryConst.Activity.TIME, LotteryConst.State.ONLINE, limit);
        return list.stream().map(it -> it.getMap()).collect(Collectors.toList());
    }
}
