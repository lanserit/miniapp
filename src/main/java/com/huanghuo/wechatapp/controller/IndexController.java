package com.huanghuo.wechatapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by huangcheng on 2018/5/30.
 */
@RestController
public class IndexController {
    @RequestMapping("/index")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}
