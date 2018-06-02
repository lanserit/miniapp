package com.huanghuo.wechatapp.controller;

import com.huanghuo.wechatapp.domain.model.User;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

/**
 * Created by huangcheng on 2018/5/30.
 */
@RestController
public class IndexController {
    @RequestMapping("/index")
    public String index() {
        return "Greetings from Spring Boot!";
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
