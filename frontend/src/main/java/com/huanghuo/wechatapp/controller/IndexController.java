package com.huanghuo.wechatapp.controller;

import com.huanghuo.common.model.User;
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
        return "Greetings from Spring Boot!";
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error-404";
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error-500";
            }
        }
        return "error";
    }


    @MessageMapping("/user")
    @SendTo("/topic/user")
    public User sendUser(String name) throws Exception {
        Thread.sleep(1000); // simulated delay
        User user = new User();
        user.setNickname(name);
        user.setCtime(System.currentTimeMillis());
        return user;
    }
}
