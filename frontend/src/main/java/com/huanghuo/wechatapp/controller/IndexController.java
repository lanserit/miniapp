package com.huanghuo.wechatapp.controller;

import com.huanghuo.common.model.User;
import com.huanghuo.common.util.AjaxResult;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by huangcheng on 2018/5/30.
 */
@RestController
public class IndexController {
    @RequestMapping("/index")
    @ResponseBody
    public String index() {
        return "Panda Lottery!";
    }

    @RequestMapping("/error")
    @ResponseBody
    public AjaxResult handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return AjaxResult.ajaxFailed("error-404");
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return AjaxResult.ajaxFailed("error-500");
            }
        }
        return AjaxResult.ajaxFailed("error");
    }
}
