package com.huanghuo.backend.controller;

import com.huanghuo.common.LotteryConst;
import com.huanghuo.common.model.LotteryActivity;
import com.huanghuo.common.service.LotteryService;
import com.huanghuo.common.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public AjaxResult creteActivity(@RequestBody LotteryActivity activity){
        activity.setCtime(System.currentTimeMillis());
        if(lotteryService.add(activity)){
            return AjaxResult.ajaxSuccess();
        }else{
            return AjaxResult.ajaxFailed("创建失败");
        }
    }
}
