package com.huanghuo.common;

/**
 * Created by huangcheng on 2018/6/9.
 */
public interface LotteryConst {
    int DEFAULT_TOTAL_COUNT = 100;
    int DEFAULT_TOTAL_WIN_COUNT = 3;
    long DAY_MILLIS = 24*60*60*1000;
    interface Activity {
        int TIME = 1;
        int ATTEND_COUNT = 2;
    }

    interface LotteryState{
        int PENDING = 0;
        int WIN = 1;
    }

    interface State{
        int PREONLINE = 1;
        int ONLINE = 2;
        int LOTTERY = 3;
        int OFFLINE = 4;
    }
}
