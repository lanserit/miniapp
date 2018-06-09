package com.huanghuo.common.model;

import java.io.Serializable;

/**
 * Created by huangcheng on 2018/6/3.
 */
public class LotteryWinRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private long userId;
    private long actId;
    private int state;
    private long ctime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getActId() {
        return actId;
    }

    public void setActId(long actId) {
        this.actId = actId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }
}
