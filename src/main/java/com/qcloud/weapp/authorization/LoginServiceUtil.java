package com.qcloud.weapp.authorization;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Maps;
import com.huanghuo.util.JsonUtil;
import com.qcloud.weapp.ConfigurationException;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

/**
 * 提供登录服务
 */

public class LoginServiceUtil {
    private static void write(HttpServletResponse response, Map<String, Object> map) {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(JsonUtil.getJsonString(map));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Object> prepareResponseJson() {
        Map<String, Object> ret = Maps.newHashMap();
        ret.put(Constants.WX_SESSION_MAGIC_ID, 1);
        return ret;
    }

    private static Map<String, Object> getJsonForError(Exception error, int errorCode) {
        Map<String, Object> ret = prepareResponseJson();
        ret.put("code", errorCode);
        if (error instanceof LoginServiceException) {
            ret.put("error", ((LoginServiceException) error).getType());
        }
        ret.put("message", error.getMessage());

        return ret;
    }

    private static Map<String, Object> getJsonForError(Exception error) {
        return getJsonForError(error, -1);
    }

    /**
     * 处理登录请求
     *
     * @return 登录成功将返回用户信息
     */
    public static UserInfo login(HttpServletRequest request, HttpServletResponse response) throws IllegalArgumentException, LoginServiceException, ConfigurationException {
        String code = getHeader(Constants.WX_HEADER_CODE, request, response);
        String encryptedData = getHeader(Constants.WX_HEADER_ENCRYPTED_DATA, request, response);
        String iv = getHeader(Constants.WX_HEADER_IV, request, response);

        AuthorizationAPI api = new AuthorizationAPI();
        Map<String, Object> loginResult;

        try {
            loginResult = api.login(code, encryptedData, iv);
        } catch (AuthorizationAPIException apiError) {
            LoginServiceException error = new LoginServiceException(Constants.ERR_LOGIN_FAILED, apiError.getMessage(), apiError);
            write(response, getJsonForError(error));
            throw error;
        }

        Map<String, Object> json = prepareResponseJson();
        Map<String, Object> session = Maps.newHashMap();
        session.put("id", loginResult.get("id"));
        session.put("skey", loginResult.get("skey"));
        json.put("session", session);
        write(response, json);

        Map<String, Object> userInfo = MapUtils.getMap(loginResult, "user_info");

        return UserInfo.BuildFromJson(JsonUtil.getJsonString(userInfo));
    }

    /**
     * 检查当前请求的会话状态
     *
     * @return 如果包含可用会话，将会返回会话对应的用户信息
     */
    public static UserInfo check(HttpServletRequest request, HttpServletResponse response) throws LoginServiceException, ConfigurationException {
        String id = getHeader(Constants.WX_HEADER_ID, request, response);
        String skey = getHeader(Constants.WX_HEADER_SKEY, request, response);

        AuthorizationAPI api = new AuthorizationAPI();
        Map<String, Object> checkLoginResult = null;
        try {
            checkLoginResult = api.checkLogin(id, skey);
        } catch (AuthorizationAPIException apiError) {
            String errorType = Constants.ERR_CHECK_LOGIN_FAILED;
            if (apiError.getCode() == 60011 || apiError.getCode() == 60012) {
                errorType = Constants.ERR_INVALID_SESSION;
            }
            LoginServiceException error = new LoginServiceException(errorType, apiError.getMessage(), apiError);
            write(response, getJsonForError(error));
            throw error;
        }
        Map<String, Object> userInfo = MapUtils.getMap(checkLoginResult, "user_info");

        return UserInfo.BuildFromJson(JsonUtil.getJsonString(userInfo));
    }

    private static String getHeader(String key, HttpServletRequest request, HttpServletResponse response) throws LoginServiceException {
        String value = request.getHeader(key);
        if (value == null || value.isEmpty()) {
            LoginServiceException error = new LoginServiceException("INVALID_REQUEST", String.format("请求头不包含 %s，请配合客户端 SDK 使用", key));
            write(response, getJsonForError(error));
            throw error;
        }
        return value;
    }

}
