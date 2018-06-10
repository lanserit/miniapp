package com.qcloud.weapp.tunnel;

import com.huanghuo.common.util.JsonUtil;
import org.apache.commons.collections.MapUtils;
import java.util.Map;

/**
 * 表示一个信道消息
 */
public class TunnelMessage {
    private String type;
    private Object content;

    TunnelMessage(String messageRaw) {
        Map<String, Object> resolved = JsonUtil.getMapFromJson(messageRaw);
        this.type = MapUtils.getString(resolved, "type", "UnknownRaw");
        this.content = MapUtils.getString(resolved, "content", "messageRaw");
    }

    /**
     * 获取信道消息的类型
     */
    public String getType() {
        return type;
    }

    /**
     * 获取信道消息的内容
     */
    public Object getContent() {
        return content;
    }

    void setType(String type) {
        this.type = type;
    }

    void setContent(Object content) {
        this.content = content;
    }
}
