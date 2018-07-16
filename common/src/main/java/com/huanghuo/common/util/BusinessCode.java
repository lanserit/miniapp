package com.huanghuo.common.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangcheng on 2018/6/10.
 */
public class BusinessCode {
    @Message("成功")
    public static final int SUCC = 0;
    @Message("失败")
    public static final int FAILED = -1;

    @Message("没有登陆")
    public static final int NOT_LOGINED = -2;

    @Message("用户不存在")
    public static final int USER_NOT_EXIST = 100;

    @Message("活动不存在")
    public static final int ACTIVITY_NOT_EXIST = 10001;

    @Message("活动没有开启")
    public static final int ACTIVITY_IS_NOT_OPEN = 10002;

    @Message("活动已经结束")
    public static final int ACTIVITY_IS_END = 10003;

    @Message("活动参与失败")
    public static final int ACTIVITY_ATTEND_FAILED = 10004;

    private static final Map<Integer, String> messageMap;

    static {
        Map<Integer, String> m = new HashMap<Integer, String>();

        for (Field field : BusinessCode.class.getDeclaredFields()) {
            Message annotation = field.getAnnotation(Message.class);
            if (annotation != null) {
                try {
                    m.put(field.getInt(null), annotation.value());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        messageMap = Collections.unmodifiableMap(m);
    }

    public static String getMessage(int errorCode) {
        return messageMap.get(errorCode);
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Message {
        String value();
    }
}
