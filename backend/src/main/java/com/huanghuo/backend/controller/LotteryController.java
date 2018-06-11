package com.huanghuo.backend.controller;

import com.huanghuo.common.LotteryConst;
import com.huanghuo.common.model.LotteryActivity;
import com.huanghuo.common.service.LotteryService;
import com.huanghuo.common.util.AjaxResult;
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

    @RequestMapping("/create")
    @ResponseBody
    public AjaxResult creteActivity(@RequestBody LotteryActivity activity) {
        activity.setCtime(System.currentTimeMillis());
        if (lotteryService.add(activity)) {
            return AjaxResult.ajaxSuccess();
        } else {
            return AjaxResult.ajaxFailed("创建失败");
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
