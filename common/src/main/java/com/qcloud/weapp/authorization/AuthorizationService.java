package com.qcloud.weapp.authorization;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;
import com.huanghuo.common.util.JsonUtil;
import com.qcloud.weapp.Configuration;
import com.qcloud.weapp.ConfigurationException;
import com.qcloud.weapp.HttpRequest;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class AuthorizationService {
    private final static Logger logger = LoggerFactory.getLogger(AuthorizationService.class);

    @Autowired
    private Configuration configuration;
    private String getAPIUrl() throws ConfigurationException {
        return configuration.getAuthServerUrl();
    }

    public Map<String, Object> login(String code, String encryptedData, String iv) throws AuthorizationAPIException, ConfigurationException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("code", code);
        params.put("encrypt_data", encryptedData);
        params.put("iv", iv);
        return request("qcloud.cam.id_skey", params);
    }

    public Map<String, Object> checkLogin(String id, String skey) throws AuthorizationAPIException, ConfigurationException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        params.put("skey", skey);
        return request("qcloud.cam.auth", params);
    }

    public Map<String, Object> request(String apiName, Map<String, Object> apiParams) throws AuthorizationAPIException, ConfigurationException {
        Map<String, Object> requestBody = null;
        Map<String, Object> responseBody = null;

        try {
            HttpRequest request = new HttpRequest(getAPIUrl());

            requestBody= buildRequestBody(apiName, apiParams);
            logger.debug("==============Auth Request=============");
            System.out.println(requestBody);

            responseBody = request.post(requestBody);
            logger.debug("==============Auth Response=============");
            logger.debug(JsonUtil.getJsonString(requestBody));
        } catch (IOException e) {
            throw new AuthorizationAPIException("连接鉴权服务错误，请检查网络状态" + getAPIUrl() + e.getMessage());
        }

        int returnCode = 0;
        String returnMessage = null;


        returnCode = MapUtils.getInteger(responseBody, "returnCode");
        returnMessage = MapUtils.getString(requestBody,"returnMessage");

        if (returnCode != 0) {
            AuthorizationAPIException error = new AuthorizationAPIException(String.format("调用鉴权服务失败：#%d - %s", returnCode, returnMessage));
            error.setCode(returnCode);
            throw error;
        }

        Map<String, Object> returnData = MapUtils.getMap(responseBody, "returnData");
        return returnData;
    }

    private Map<String, Object> buildRequestBody(String apiName, Map<String, Object> apiParams) {
        Map<String, Object> ret = Maps.newHashMap();
        Map<String, Object> interfaceMap = Maps.newHashMap();
        interfaceMap.put("interfaceName", apiName);
        interfaceMap.put("para", apiParams);

        ret.put("version", 1);
        ret.put("componentName", "MA");
        ret.put("interface", interfaceMap);

        return ret;
    }
}
