package com.huanghuo.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by huangcheng on 2018/6/9.
 */
@RestController
public class AdminController {

    @RequestMapping("/index")
    @ResponseBody
    public String index() {
        return "Backend from Spring Boot!";
    }

}
