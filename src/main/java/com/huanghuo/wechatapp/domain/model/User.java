package com.huanghuo.wechatapp.domain.model;

import java.io.Serializable;

/**
 * Created by huangcheng on 2018/5/26.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private String openid;
    private String nickname;
    private long ctime;

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

}
