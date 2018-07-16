package com.huanghuo.common.util;

/**
 * Created by huangcheng on 2018/6/26.
 */
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil{
    public static String getCookie(HttpServletRequest request, String cookieName){

        Cookie[] cookies =  request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(cookieName)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private static Cookie getCookieObj(HttpServletRequest request, String cookieName){
        Cookie[] cookies =  request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(cookieName)){
                    return cookie;
                }
            }
        }
        return null;
    }

    public static void writeCookie(HttpServletResponse response, String cookieName, String value){
        Cookie cookie = new Cookie(cookieName,value);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
    }

    public static boolean deleteCookie(String cookieName, HttpServletRequest request, HttpServletResponse response) {
        if (cookieName != null) {
            Cookie cookie = getCookieObj(request, cookieName);
            if(cookie!=null){
                cookie.setMaxAge(0);//如果0，就说明立即删除
                cookie.setPath("/");//不要漏掉
                response.addCookie(cookie);
                return true;
            }
        }
        return false;
    }

}
