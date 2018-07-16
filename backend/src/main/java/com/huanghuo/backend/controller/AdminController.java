package com.huanghuo.backend.controller;

import com.huanghuo.backend.AdminUser;
import com.huanghuo.backend.service.AuthService;
import com.huanghuo.common.util.AjaxResult;
import com.huanghuo.common.util.BusinessCode;
import com.huanghuo.common.util.CookieUtil;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by huangcheng on 2018/6/9.
 */
@RestController
public class AdminController {
    @Autowired
    private AuthService authService;

    @RequestMapping("/index")
    @ResponseBody
    public String index() {
        return "Backend from Spring Boot!";
    }

    @RequestMapping("/auth/isLogined")
    @ResponseBody
    public AjaxResult isLogined(HttpServletRequest request){
        String cookieSecVal = CookieUtil.getCookie(request, AuthService.COOKIE_NAME_AUTH);
        String cookieUsernameVal = CookieUtil.getCookie(request, AuthService.COOKIE_NAME_USERNAME);
        if(StringUtils.isNotEmpty(cookieSecVal) && StringUtils.isNotEmpty(cookieUsernameVal)) {
            if(authService.isAuthorized(cookieUsernameVal, cookieSecVal)){
                return AjaxResult.ajaxSuccess();
            }
        }
        return AjaxResult.ajaxFailed(BusinessCode.FAILED);
    }

    @RequestMapping("/auth/logout")
    @ResponseBody
    public AjaxResult isLogined(HttpServletRequest request, HttpServletResponse response){
        CookieUtil.deleteCookie(AuthService.COOKIE_NAME_AUTH, request, response);
        CookieUtil.deleteCookie(AuthService.COOKIE_NAME_USERNAME, request, response);
        return AjaxResult.ajaxSuccess();
    }

    @PostMapping("/auth/login")
    @ResponseBody
    public AjaxResult login(@ModelAttribute AdminUser user, HttpServletRequest request,HttpServletResponse response) {
        if(user == null)return AjaxResult.ajaxFailed(BusinessCode.FAILED);
        String secVal = AuthService.md5Hex(user.getUsername(), user.getPasswd());
        boolean isOk = authService.isAuthorized(user.getUsername(), secVal);
        if(isOk) {
            CookieUtil.writeCookie(response, AuthService.COOKIE_NAME_AUTH, secVal);
            CookieUtil.writeCookie(response, AuthService.COOKIE_NAME_USERNAME, user.getUsername());
            return AjaxResult.ajaxSuccess();
        }else{
            return AjaxResult.ajaxFailed(BusinessCode.FAILED);
        }
    }

}


