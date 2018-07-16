package com.huanghuo.backend.controller;

import com.huanghuo.backend.service.LotteryBackendService;
import com.huanghuo.common.LotteryConst;
import com.huanghuo.common.model.LotteryActivity;
import com.huanghuo.common.model.LotteryWinRecord;
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
    @Autowired
    private LotteryBackendService lotteryBackendService;

    @PostMapping("/create")
    @ResponseBody
    public AjaxResult creteActivity(@ModelAttribute LotteryActivity activity) {
        activity.setCtime(System.currentTimeMillis());
        activity.setState(LotteryConst.State.PREONLINE);
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

    @PostMapping("/update")
    @ResponseBody
    public AjaxResult updaetActivity(@ModelAttribute LotteryActivity activity) {
        if(activity.getTotalcount() == 0){
            activity.setTotalcount(LotteryConst.DEFAULT_TOTAL_COUNT);
        }

        if(activity.getTotalwincount() == 0){
            activity.setTotalwincount(LotteryConst.DEFAULT_TOTAL_WIN_COUNT);
        }

        if (lotteryService.updateLottery(activity)) {
            return AjaxResult.ajaxSuccess();
        } else {
            return AjaxResult.ajaxFailed("更新失败");
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

    @RequestMapping("/delete")
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
        List<LotteryActivity> list = lotteryService.getListByCtime(limit);
        return AjaxResult.ajaxSuccess(list);
    }

    @RequestMapping("/info")
    @ResponseBody
    public AjaxResult getLotteryInfo(@RequestParam(value = "id") long id){
        LotteryActivity activity = lotteryService.getById(id);
        return AjaxResult.ajaxSuccess(activity);
    }

    @RequestMapping("/listWinRecord")
    @ResponseBody
    public AjaxResult listWinRecord(@RequestParam(value = "actId") long actId) {
        List<LotteryWinRecord> list = lotteryBackendService.getLotteryWinRecords(actId);
        return AjaxResult.ajaxSuccess(list);
    }


    @RequestMapping("/listByState")
    @ResponseBody
    public AjaxResult listActivityByState(@RequestParam(value = "id", defaultValue = "0") long id, @RequestParam("state") int state, @RequestParam(value = "limit", defaultValue = "10") int limit) {
        List<LotteryActivity> list = lotteryService.getListByState(id, state, limit);
        return AjaxResult.ajaxSuccess(list);
    }

    @RequestMapping("/draw")
    @ResponseBody
    public AjaxResult drawLottery(@RequestParam("actId") long actId){
        LotteryActivity activity = lotteryService.getById(actId);
        if(activity != null && activity.getState() == LotteryConst.State.ONLINE){
            lotteryBackendService.drawLottery(activity);
        }
        return AjaxResult.ajaxSuccess();

    }
}
