package com.huanghuo.common.model;

import com.google.common.collect.Maps;
import com.huanghuo.common.auth.UserInfo;
import com.huanghuo.common.util.JsonUtil;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by huangcheng on 2018/5/26.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private String openid;
    private String nickname;
    private String userInfoJson;
    private String sessionkey;
    private String unionid;
    private long ctime;
    private int attendcount;
    private int wincount;

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

    public Map<String,Object> getMap(){
        Map<String, Object> ret = Maps.newHashMap();
        ret.put("nickName", nickname);
        ret.put("attendCount", attendcount);
        ret.put("winCount", wincount);
        ret.put("userInfo", JsonUtil.getMapFromJson(userInfoJson));
        return ret;
    }
}
