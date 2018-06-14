package com.huanghuo.common.model;

import com.google.common.collect.Maps;
import com.huanghuo.common.util.JsonUtil;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by huangcheng on 2018/6/3.
 */
public class LotteryActivity implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private int acttype;
    private String name;
    private int actcount;
    private int totalcount;
    private int maskcount;
    private int wincount;
    private int totalwincount;
    private long ctime;
    private long starttime;
    private long endtime;
    private String giftjson;
    private int state;
    private String extraattrs;
    private String pic1;
    private String pic2;
    private String pic3;
    private String pic4;
    private String pic5;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getActtype() {
        return acttype;
    }

    public void setActtype(int acttype) {
        this.acttype = acttype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getActcount() {
        return actcount;
    }

    public void setActcount(int actcount) {
        this.actcount = actcount;
    }

    public int getMaskcount() {
        return maskcount;
    }

    public void setMaskcount(int maskcount) {
        this.maskcount = maskcount;
    }

    public long getStarttime() {
        return starttime;
    }

    public void setStarttime(long starttime) {
        this.starttime = starttime;
    }

    public long getEndtime() {
        return endtime;
    }

    public void setEndtime(long endtime) {
        this.endtime = endtime;
    }

    public String getGiftjson() {
        return giftjson;
    }

    public Map<String, Object> getGiftJsonMap() {
        return JsonUtil.getMapFromJson(giftjson);
    }

    public void setGiftjson(String giftjson) {
        this.giftjson = giftjson;
    }

    public String getExtraattrs() {
        return extraattrs;
    }

    public Map<String, Object> getExtraAttrsJson() {
        return JsonUtil.getMapFromJson(extraattrs);
    }

    public void setExtraattrs(String extraattrs) {
        this.extraattrs = extraattrs;
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

    public String getPic1() {
        return pic1;
    }

    public void setPic1(String pic1) {
        this.pic1 = pic1;
    }

    public String getPic2() {
        return pic2;
    }

    public void setPic2(String pic2) {
        this.pic2 = pic2;
    }

    public String getPic3() {
        return pic3;
    }

    public void setPic3(String pic3) {
        this.pic3 = pic3;
    }

    public String getPic4() {
        return pic4;
    }

    public void setPic4(String pic4) {
        this.pic4 = pic4;
    }

    public int getWincount() {
        return wincount;
    }

    public void setWincount(int wincount) {
        this.wincount = wincount;
    }

    public int getTotalwincount() {
        return totalwincount;
    }

    public void setTotalwincount(int totalwincount) {
        this.totalwincount = totalwincount;
    }

    public int getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(int totalcount) {
        this.totalcount = totalcount;
    }

    public String getPic5() {
        return pic5;
    }

    public void setPic5(String pic5) {
        this.pic5 = pic5;
    }

    public Map<String, Object> getMap() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("id", id);
        map.put("name", name);
        map.put("acttype", acttype);
        map.put("ctime", ctime);
        map.put("actcount", actcount);
        map.put("totalcount", totalcount);
        map.put("totalwincount", totalwincount);
        map.put("giftjson", getGiftJsonMap());
        map.put("state", state);
        map.put("starttime", starttime);
        map.put("endtime", endtime);
        map.put("extraattrs", getExtraAttrsJson());
        map.put("pic1", pic1);
        map.put("pic2", pic2);
        map.put("pic3", pic3);
        map.put("pic4", pic4);
        map.put("pic5", pic5);

        return map;
    }
}
