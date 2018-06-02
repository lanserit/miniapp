package com.qcloud.weapp.tunnel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.huanghuo.util.JsonUtil;
import org.apache.commons.collections.MapUtils;

import com.qcloud.weapp.ConfigurationException;
import com.qcloud.weapp.ConfigurationManager;
import com.qcloud.weapp.Hash;
import com.qcloud.weapp.HttpRequest;

class TunnelAPI {
    private String getTunnelServerUrl() throws ConfigurationException {
        return ConfigurationManager.getCurrentConfiguration().getTunnelServerUrl();
    }

    public Tunnel requestConnect(String receiveUrl) throws Exception {
        Map<String, Object> data = Maps.newHashMap();
        data.put("receiveUrl", receiveUrl);
        data.put("protocolType", "wss");

        Map<String, Object> result = request("/get/wsurl", JsonUtil.getJsonString(data), true);
        Tunnel tunnel = new Tunnel(MapUtils.getString(result, "tunnelId"));
        tunnel.setConnectUrl(MapUtils.getString(result, "connectUrl"));
        return tunnel;
    }

    public EmitResult emitMessage(Tunnel[] tunnels, String messageType, Object messageContent) throws EmitError {
        String[] tunnelIds = new String[tunnels.length];
        Integer i = 0;
        for (Tunnel tunnel : tunnels) {
            tunnelIds[i++] = tunnel.getTunnelId();
        }
        return emitMessage(tunnelIds, messageType, messageContent);
    }

    public EmitResult emitMessage(String[] tunnelIds, String messageType, Object messageContent) throws EmitError {
        Map<String, Object> packet = Maps.newHashMap();

        packet.put("type", messageType);
        packet.put("content", messageContent);

        return emitPacket(tunnelIds, "message", packet);
    }

    public EmitResult emitPacket(String[] tunnelIds, String packetType, Map<String, Object> packetContent) throws EmitError {
        if (tunnelIds.length == 0) {
            return new EmitResult(new ArrayList<TunnelInvalidInfo>());
        }
        Map<String,Object> packet = Maps.newHashMap();
        packet.put("type", packetType);
        packet.put("tunnelIds", tunnelIds);
        packet.put("content", packetContent == null ? null : packetContent.toString());
        List<Object> data = Lists.newArrayList(packet);
        try {
            Map<String, Object> emitReturn = request("/ws/push", JsonUtil.getJsonString(data), false);
            List<String> invalidTunnelIds = (List<String>)MapUtils.getObject(emitReturn,"invalidTunnelIds", Lists.newArrayList());
            ArrayList<TunnelInvalidInfo> infos = new ArrayList<TunnelInvalidInfo>();
            for (int i = 0; i < invalidTunnelIds.size(); i++) {
                TunnelInvalidInfo info = new TunnelInvalidInfo();
                info.setTunnelId(invalidTunnelIds.get(i));
                infos.add(info);
            }
            EmitResult emitResult = new EmitResult(infos);
            return emitResult;
        } catch (Exception e) {
            e.printStackTrace();
            throw new EmitError("网络不可用或者信道服务器不可用", e);
        }
    }

    public Map<String, Object> request(String path, String data, Boolean isSendTcKey) throws Exception {
        String url = getTunnelServerUrl() + path;
        Map<String, Object> responseContent;

        try {
            Map<String, Object> requestContent = buildRequestContent(data, isSendTcKey);
            responseContent = new HttpRequest(url).post(requestContent);
        } catch (Exception e) {
            throw new Exception("请求信道 API 失败，网络异常或鉴权服务器错误", e);
        }


        if (MapUtils.getInteger(responseContent, "code") != 0) {
            throw new Exception(String.format("信道服务调用失败：#%d - %s", responseContent.get("code"), responseContent.get("message")));
        }
        return MapUtils.getMap(responseContent, "data");


    }

    private Map<String, Object> buildRequestContent(String data, boolean includeTckey) throws ConfigurationException {
        // data must be JsonObject or JsonArray
        Map<String, Object> requestPayload = Maps.newHashMap();

        requestPayload.put("data", data);
        requestPayload.put("dataEncode", "json");
        requestPayload.put("tcId", TunnelClient.getId());
        if (includeTckey) {
            requestPayload.put("tcKey", TunnelClient.getKey());
        }
        requestPayload.put("signature", signature(data));
        return requestPayload;
    }

    private String signature(String data) throws ConfigurationException {
        return Hash.sha1(data + TunnelClient.getKey());
    }
}
