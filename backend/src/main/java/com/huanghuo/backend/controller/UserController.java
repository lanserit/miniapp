package com.huanghuo.backend.controller;

import com.huanghuo.common.model.User;
import com.huanghuo.common.service.UserService;
import com.huanghuo.common.util.AjaxResult;
import com.huanghuo.common.util.BusinessCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by huangcheng on 2018/7/3.
 */

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/info")
    @ResponseBody
    public AjaxResult getUserInfo(@RequestParam(value="id") long id){
        User user = userService.findByUserId(id);
        if(user != null) {
            return AjaxResult.ajaxSuccess(user.getMap());
        }
        return AjaxResult.ajaxFailed(BusinessCode.USER_NOT_EXIST);
    }

    @RequestMapping("/list")
    @ResponseBody
    public AjaxResult getListByLimit(@RequestParam(value="id") long id,@RequestParam(value="limit") int limit){
        List<User> list = userService.getListByLimit(id, limit);
        return AjaxResult.ajaxSuccess(list);
    }

}
