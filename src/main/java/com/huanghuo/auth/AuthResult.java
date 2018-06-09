package com.huanghuo.auth;

/**
 * Created by huangcheng on 2018/6/9.
 */
public class AuthResult {
    public final static int SUCC = 1;
    public final static int FAILED = 0;
    private String openId;
    private String sessionKey;
    private String unionId;
    private String errMsg;
    private int code;
    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
