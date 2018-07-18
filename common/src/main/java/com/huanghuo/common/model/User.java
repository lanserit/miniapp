package com.huanghuo.common.model;

import com.google.common.collect.Maps;
import com.huanghuo.common.auth.UserInfo;
import com.huanghuo.common.util.JsonUtil;
import com.huanghuo.common.util.MiniAppAuthUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Base64Utils;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by huangcheng on 2018/5/26.
 */
public class User implements Serializable {
    private static final long serialVersionUID = -7468841223562642815L;
    private static long mask_offset = 89998;
    private long id;
    private String openid;
    private String nickname;
    private String userInfoJson;
    private String sessionkey;
    private String unionid;
    private long ctime;
    private int attendcount;
    private int wincount;
    private String phone;
    private String addressInfo;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSessionkey() {
        return sessionkey;
    }

    public void setSessionkey(String sessionkey) {
        this.sessionkey = sessionkey;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public int getAttendcount() {
        return attendcount;
    }

    public void setAttendcount(int attendcount) {
        this.attendcount = attendcount;
    }

    public int getWincount() {
        return wincount;
    }

    public void setWincount(int wincount) {
        this.wincount = wincount;
    }

    public String getUserInfoJson() {
        return userInfoJson;
    }

    public void setUserInfoJson(String userInfoJson) {
        this.userInfoJson = userInfoJson;
    }

    public UserInfo getUserInfo(){
        return JsonUtil.objectFromJson(userInfoJson, UserInfo.class);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(String addressInfo) {
        this.addressInfo = addressInfo;
    }

    public Map<String,Object> getMap(){
        Map<String, Object> ret = Maps.newHashMap();
        if(StringUtils.isNotEmpty(nickname)) {
            ret.put("nickName", MiniAppAuthUtil.decodeEmoji(nickname));
        }
        ret.put("attendCount", attendcount);
        ret.put("winCount", wincount);
        Map<String, Object> userInfoMap = JsonUtil.getMapFromJson(userInfoJson);
        if(MapUtils.isNotEmpty(userInfoMap)) {
            userInfoMap.put("nickName", MiniAppAuthUtil.decodeEmoji((String)userInfoMap.get("nickName")));
            userInfoMap.remove("openId");
            userInfoMap.remove("watermark");
            userInfoMap.remove("appId");
        }
        ret.put("userInfo", userInfoMap);
        ret.put("addressInfo", addressInfo);
        ret.put("maskId", id + mask_offset);
        return ret;
    }

    public static User createUser(String openid, String sessionkey, String unionid){
        User user = new User();
        user.setOpenid(openid);
        user.setSessionkey(sessionkey);
        user.setAttendcount(0);
        user.setWincount(0);
        if(StringUtils.isNotEmpty(unionid)) {
            user.setUnionid(unionid);
        }
        user.setCtime(System.currentTimeMillis());
        return user;
    }
}
