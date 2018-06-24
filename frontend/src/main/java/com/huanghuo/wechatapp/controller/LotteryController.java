package com.huanghuo.wechatapp.controller;

import com.huanghuo.common.LotteryConst;
import com.huanghuo.common.auth.WechatAuthService;
import com.huanghuo.common.model.LotteryActivity;
import com.huanghuo.common.model.User;
import com.huanghuo.common.service.LotteryService;
import com.huanghuo.common.service.UserService;
import com.huanghuo.common.util.AjaxResult;
import com.huanghuo.common.util.BusinessCode;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    private UserService userService;

    @RequestMapping("/list")
    @ResponseBody
    public List<Map<String, Object>> getOpenList(@RequestParam(name = "limit", defaultValue = "5") int limit) {
        List<LotteryActivity> list = lotteryService.getListByState(LotteryConst.Activity.TIME, LotteryConst.State.ONLINE, limit);
        return list.stream().map(it -> it.getMap()).collect(Collectors.toList());
    }

    @RequestMapping("/attend")
    @ResponseBody
    public AjaxResult attendLottery(@RequestParam("actId") long actId, @RequestParam("formid") String formid, HttpServletRequest request) {
        String openId = request.getHeader(WechatAuthService.WX_HEADER_OPENID_KEY);
        LotteryActivity activity = lotteryService.getById(actId);
        User user = userService.findByOpenId(openId);
        if(activity == null){
            return AjaxResult.ajaxFailed(BusinessCode.ACTIVITY_NOT_EXIST);

        }
        else if (activity.getState() == LotteryConst.State.ONLINE) {
            try {
                if (lotteryService.attendLottery(actId, user.getId(), formid) == BusinessCode.SUCC) {
                    return AjaxResult.ajaxSuccess();
                } else {
                    return AjaxResult.ajaxFailed(BusinessCode.FAILED);
                }
            }catch (Exception e){
                return AjaxResult.ajaxFailed(e.getMessage());
            }
        } else {
            return AjaxResult.ajaxFailed(BusinessCode.ACTIVITY_IS_NOT_OPEN);
        }
    }

}
