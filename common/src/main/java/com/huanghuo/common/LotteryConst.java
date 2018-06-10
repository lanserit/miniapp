package com.huanghuo.common;

/**
 * Created by huangcheng on 2018/6/9.
 */
public interface LotteryConst {

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
