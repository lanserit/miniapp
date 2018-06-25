package com.huanghuo.backend.controller;

import com.huanghuo.common.LotteryConst;
import com.huanghuo.common.model.LotteryActivity;
import com.huanghuo.common.service.LotteryService;
import com.huanghuo.common.util.AjaxResult;
import com.huanghuo.common.util.BusinessCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by huangcheng on 2018/6/10.
 */
@RestController
@RequestMapping("/lottery")
public class LotteryController {
    @Autowired
    private LotteryService lotteryService;

    @PostMapping("/create")
    @ResponseBody
    public AjaxResult creteActivity(@RequestBody LotteryActivity activity) {
        activity.setCtime(System.currentTimeMillis());
        if(activity.getTotalcount() == 0){
            activity.setTotalcount(LotteryConst.DEFAULT_TOTAL_COUNT);
        }

        if(activity.getTotalwincount() == 0){
            activity.setTotalwincount(LotteryConst.DEFAULT_TOTAL_WIN_COUNT);
        }

        if (lotteryService.addLottery(activity)) {
            return AjaxResult.ajaxSuccess();
        } else {
            return AjaxResult.ajaxFailed("创建失败");
        }
    }

    @RequestMapping("/updateState")
    @ResponseBody
    public AjaxResult updateActivity(@RequestParam(value="id") long id, @RequestParam(value = "newstate") int newstate){
        if(lotteryService.updateLotteryState(id, newstate)) {
            return AjaxResult.ajaxSuccess();
        }else{
            return AjaxResult.ajaxFailed(BusinessCode.FAILED);
        }
    }

    @RequestMapping("/deleteLottery")
    @ResponseBody
    public AjaxResult deleteActivity(@RequestParam(value="id") long id){
        if(lotteryService.deleteLottery(id, LotteryConst.State.PREONLINE)){
            return AjaxResult.ajaxSuccess();
        }else {
            return AjaxResult.ajaxFailed(BusinessCode.FAILED);
        }
    }

    @RequestMapping("/list")
    @ResponseBody
    public AjaxResult listActivity(@RequestParam(value = "limit", defaultValue = "10") int limit) {
        List<LotteryActivity> list = lotteryService.getList(limit);
        return AjaxResult.ajaxSuccess(list);
    }


    @RequestMapping("/listByState")
    @ResponseBody
    public AjaxResult listActivityByState(@RequestParam("acttype") int acttype, @RequestParam("state") int state, @RequestParam(value = "limit", defaultValue = "10") int limit) {
        List<LotteryActivity> list = lotteryService.getListByState(acttype, state, limit);
        return AjaxResult.ajaxSuccess(list);
    }
}
