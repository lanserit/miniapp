package com.huanghuo.common.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by huangcheng on 2018/6/10.
 */
public class AjaxResult {
    private long errorCode;
    private String errorMessage;
    private Object payload;

    public long getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public static AjaxResult ajaxSuccess() {
        return ajaxSuccess(null);
    }

    public static AjaxResult ajaxSuccess(Object payload) {
        AjaxResult result = new AjaxResult();
        result.errorCode = BusinessCode.SUCC;
        result.errorMessage = null;
        result.payload = payload;
        return result;
    }

    public static AjaxResult ajaxFailed(int errorCode) {
        return ajaxFailed(errorCode, null);
    }

    public static AjaxResult ajaxFailed(String errorMessage) {
        return ajaxFailed(BusinessCode.FAILED, null);
    }

    public static AjaxResult ajaxFailed(int errorCode, String errorMessage) {
        if (errorCode == BusinessCode.SUCC) {
            throw new IllegalStateException();
        }

        AjaxResult result = new AjaxResult();
        result.errorCode = errorCode;
        if (StringUtils.isNotEmpty(errorMessage)) {
            result.errorMessage = errorMessage;
        } else {
            result.errorMessage = BusinessCode.getMessage(errorCode);
        }
        result.payload = null;
        return result;
    }
}

