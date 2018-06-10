package com.qcloud.weapp.tunnel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Maps;
import com.huanghuo.common.util.JsonUtil;
import com.qcloud.weapp.Configuration;
import com.qcloud.weapp.HttpServletUtils;
import org.apache.commons.collections.MapUtils;

import com.qcloud.weapp.ConfigurationException;
import com.qcloud.weapp.Hash;
import com.qcloud.weapp.authorization.LoginService;
import com.qcloud.weapp.authorization.LoginServiceException;
import com.qcloud.weapp.authorization.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 提供信道服务
 */

@Service
public class TunnelService {
    @Autowired
    private Configuration configuration;
    @Autowired
    private LoginService loginService;
    @Autowired
    private TunnelAPIService tunnelAPIService;

    private static Map<String, Object> getMapForError(Exception error, int errorCode) {
        Map<String, Object> json = Maps.newHashMap();
        json.put("code", errorCode);
        if (error instanceof LoginServiceException) {
            json.put("error", ((LoginServiceException) error).getType());
        }
        json.put("message", error.getMessage());

        return json;
    }

    private static Map<String, Object> getMapForError(Exception error) {
        return getMapForError(error, -1);
    }


    /**
     * 处理 WebSocket 信道请求
     *
     * @param handler 指定信道处理器处理信道事件
     * @param options 指定信道服务的配置
     */
    public void handle(HttpServletRequest request, HttpServletResponse response, TunnelHandler handler, TunnelHandleOptions options) throws Exception {
        if (request.getMethod().toUpperCase() == "GET") {
            handleGet(request, response, handler, options);
        }
        if (request.getMethod().toUpperCase() == "POST") {
            handlePost(request, response, handler, options);
        }
    }

    /**
     * 处理 GET 请求
     * <p>
     * GET 请求表示客户端请求进行信道连接，此时会向 SDK 申请信道连接地址，并且返回给客户端
     * 如果配置指定了要求登陆，还会调用登陆服务来校验登陆态并获得用户信息
     */
    private void handleGet(HttpServletRequest request, HttpServletResponse response, TunnelHandler handler, TunnelHandleOptions options) throws Exception {
        Tunnel tunnel = null;
        UserInfo user = null;

        if (options != null && options.isCheckLogin()) {
            try {
                user = loginService.check(request, response);
            } catch (Exception e) {
                return;
            }
        }

        String receiveUrl = buildReceiveUrl(request);
        tunnel = tunnelAPIService.requestConnect(receiveUrl);


        Map<String, Object> result = Maps.newHashMap();

        result.put("url", tunnel.getConnectUrl());

        HttpServletUtils.writeToResponse(response, result);

        handler.onTunnelRequest(tunnel, user);
    }

    private String buildReceiveUrl(HttpServletRequest request) throws ConfigurationException {
        URI tunnelServerUri = URI.create(configuration.getTunnelServerUrl());
        String schema = tunnelServerUri.getScheme();
        String host = configuration.getServerHost();
        String path = request.getRequestURI();
        return schema + "://" + host + path;
    }

    private void handlePost(HttpServletRequest request, HttpServletResponse response, TunnelHandler handler, TunnelHandleOptions options) throws ConfigurationException, IOException {
        String requestContent = null;

        // 1. 读取报文内容
        BufferedReader requestReader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
        requestContent = "";
        for (String line; (line = requestReader.readLine()) != null; ) {
            requestContent += line;
        }

        // 2. 读取报文内容成 JSON 并保存在 body 变量中
        Map<String, Object> body = null;
        String data = null, signature = null;

        body = JsonUtil.getMapFromJson(requestContent);
        if (MapUtils.isEmpty(body)) {
            data = MapUtils.getString(body, "data");
            signature = MapUtils.getString(body, "signature");
            // String signature = body.getString("signature");
        } else {
            Map<String, Object> errMap = Maps.newHashMap();
            errMap.put("code", 9001);
            errMap.put("message", "Cant not parse the request body: invalid json");
            HttpServletUtils.writeToResponse(response, errMap);
        }

        // 3. 检查报文签名
        String computedSignature = Hash.sha1(data + configuration.getKey());
        if (!computedSignature.equals(signature)) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("code", 9003);
            map.put("message", "Bad Request - 签名错误");
            HttpServletUtils.writeToResponse(response, map);
            return;
        }

        // 4. 解析报文中携带的数据
        String tunnelId = null;
        String packetType = null;
        String packetContent = null;

        Map<String, Object> packet = JsonUtil.getMapFromJson(data);
        if (MapUtils.isEmpty(packet)) {
            tunnelId = MapUtils.getString(packet, "tunnelId");
            packetType = MapUtils.getString(packet, "type");
            if (packet.containsKey("content")) {
                packetContent = MapUtils.getString(packet, "content");
            }

            Map<String, Object> ret = Maps.newHashMap();
            ret.put("code", 0);
            ret.put("message", "OK");
            HttpServletUtils.writeToResponse(response, ret);
        } else {
            Map<String, Object> ret = Maps.newHashMap();
            ret.put("code", 9004);
            ret.put("message", "Bad Request - 无法解析的数据包");
            HttpServletUtils.writeToResponse(response, ret);
        }

        // 5. 交给客户处理实例处理报文
        Tunnel tunnel = Tunnel.getById(tunnelId);
        if (packetType.equals("connect")) {
            handler.onTunnelConnect(tunnel);
        } else if (packetType.equals("message")) {
            handler.onTunnelMessage(tunnel, new TunnelMessage(packetContent));
        } else if (packetType.equals("close")) {
            handler.onTunnelClose(tunnel);
        }
    }

}
