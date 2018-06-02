package com.qcloud.weapp;

import com.huanghuo.util.JsonUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by huangcheng on 2018/6/3.
 */
public class HttpServletUtils {
    public static void writeToResponse(HttpServletResponse response, Map<String, Object> map) {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf8");
            response.getWriter().print(JsonUtil.getJsonString(map));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
