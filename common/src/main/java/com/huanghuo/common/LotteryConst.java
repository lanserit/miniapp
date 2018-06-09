package com.huanghuo.common;

/**
 * Created by huangcheng on 2018/6/9.
 */
public interface LotteryConst {
    interface Activity {
        int TIME = 1;
        int ATTEND_COUNT = 2;
    }

    interface State{
        int PREONLINE = 1;
        int ONLINE = 2;
        int OFFLINE = 3;
    }
}
