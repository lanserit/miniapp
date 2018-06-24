package com.huanghuo.common.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangcheng on 2018/5/27.
 */
public class JsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public static String getJsonString(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            logger.error(String.format("convert to json error:%s, reason:%s", object, e.getMessage()));
        }

        return null;
    }

    public static Map<String, Object> getMapFromJson(String jsonStr) {
        return getMapFromJson(jsonStr, Object.class);
    }

    public static <T> Map<String, T> getMapFromJson(String jsonStr, Class<T> clazz) {
        if(StringUtils.isEmpty(jsonStr)){
            return null;
        }
        try {
            return mapper.readValue(jsonStr, mapper.getTypeFactory().constructMapLikeType(HashMap.class, String.class, clazz));
        } catch (Exception e) {
            logger.error(String.format("convert from json error:%s, reason:%s", jsonStr, e.getMessage()));
            return null;
        }
    }

    // Map<String, A> m = getFromJson("{\"10001\" : {\"111\" : {\"a\":1, \"b\":2}}}", new TypeReference<Map<String, Map<String, A>>>() {});
    public static <T> T getFromJson(String jsonStr, TypeReference<T> valueTypeRef) {
        if(StringUtils.isEmpty(jsonStr)){
            return null;
        }
        try {
            return mapper.readValue(jsonStr, valueTypeRef);
        } catch (Exception e) {
            logger.error(String.format("convert from json error:%s, reason:%s", jsonStr, e.getMessage()));
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Object> getListFromJson(String jsonStr) {
        return objectFromJson(jsonStr, List.class);
    }

    public static <T> List<T> getListFromJson(String jsonStr, Class<T> classType) {
        if(StringUtils.isEmpty(jsonStr)){
            return null;
        }
        try {
            return mapper.readValue(jsonStr, mapper.getTypeFactory().constructCollectionType(List.class, classType));
        } catch (JsonParseException e) {
            logger.error(String.format("convert to List error:%s, reason:%s", jsonStr, e.getMessage()));
        } catch (JsonMappingException e) {
            logger.error(e.getMessage());
            logger.error(String.format("convert to List error:%s, reason:%s", jsonStr, e.getMessage()));
        } catch (Exception e) {
            logger.error(String.format("convert to List error:%s, reason:%s", jsonStr, e.getMessage()));
        }
        return null;
    }

    public static <T> T objectFromJson(String json, Class<T> classType) {
        if(StringUtils.isEmpty(json)){
            return null;
        }
        try {
            return mapper.readValue(json, classType);
        } catch (JsonParseException e) {
            logger.error(String.format("convert to Object error:%s, reason:%s", json, e.getMessage()));
        } catch (JsonMappingException e) {
            logger.error(String.format("convert to Object error:%s, reason:%s", json, e.getMessage()));
        } catch (IOException e) {
            logger.error(String.format("convert to Object error:%s, reason:%s", json, e.getMessage()));
        }

        return null;
    }
}
