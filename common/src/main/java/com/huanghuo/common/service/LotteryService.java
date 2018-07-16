package com.huanghuo.common.service;

import com.google.common.collect.Maps;
import com.huanghuo.common.LotteryConst;
import com.huanghuo.common.mapper.LotteryActivityMapper;
import com.huanghuo.common.mapper.LotteryWinRecordMapper;
import com.huanghuo.common.model.LotteryActivity;
import com.huanghuo.common.model.LotteryWinRecord;
import com.huanghuo.common.model.User;
import com.huanghuo.common.util.BusinessCode;
import com.huanghuo.common.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by huangcheng on 2018/6/4.
 */
@Service
public class LotteryService {
    private final static Logger logger = LoggerFactory.getLogger(LotteryService.class);

    @Autowired
    private LotteryActivityMapper lotteryActivityMapper;

    @Autowired
    private LotteryWinRecordMapper lotteryWinRecordMapper;

    @Autowired
    private UserService userService;

    private final String DEFAULT_IMG = "https://wxh5.oss-cn-hangzhou.aliyuncs.com/miniapp/hd.jpg";

    public boolean updateLotteryState(long id, int newstate) {
        return lotteryActivityMapper.updateStateById(id, newstate) > 0;
    }

    public LotteryWinRecord getRecordByActIdAndUserId(long actId, long userId){
        return lotteryWinRecordMapper.getRecordByActIdAndUserId(actId, userId);
    }

    @Cacheable(value = "lottery-imgs")
    public Map<String, Object> getLotteryHeadImages(long actId, long recordId, int limit) {
        List<LotteryWinRecord> records = lotteryWinRecordMapper.getRecordsByActIdByLimit(actId, recordId, limit);
        List<Long> userIds = records.stream().map(i -> i.getUserId()).collect(Collectors.toList());
        List<User> users = userService.getListByIds(userIds);
        List<String> imgs = users.stream().map(u -> {
            if(u.getUserInfo() == null){
                return DEFAULT_IMG;
            }else {
                String img = u.getUserInfo().getAvatarUrl();
                return StringUtils.isNotEmpty(img) ? img : DEFAULT_IMG;
            }
        }).collect(Collectors.toList());
        Map<String, Object> ret = Maps.newHashMap();
        int size = records.size();
        ret.put("recordId", size > 0 ? records.get(0).getId(): -1);
        ret.put("imgs", imgs);
        return ret;
    }

    public boolean addLottery(LotteryActivity activity) {
        return lotteryActivityMapper.insert(activity) > 0;
    }

    public boolean updateLottery(LotteryActivity activity) {
        return lotteryActivityMapper.updateById(activity) > 0;
    }

    public boolean deleteLottery(long id, int state) {
        return lotteryActivityMapper.deleteByIdAndState(id, state) > 0;
    }

    public LotteryActivity getById(long id) {
        return lotteryActivityMapper.getById(id);
    }

    public List<LotteryActivity> getListByState(long id, int state, int limit) {
        return lotteryActivityMapper.getListByState(id, state, limit);
    }

    public List<LotteryActivity> getListByEndtimeAndState(int state, long limit) {
        return lotteryActivityMapper.getListByEndtimeAndState(state, limit);
    }

    public List<Long> getActIdByUserId(long userId) {
        return lotteryWinRecordMapper.getActIdsByUserId(userId);
    }

    public List<LotteryActivity> getList(long id, int limit) {
        return lotteryActivityMapper.getList(id, limit);
    }

    public List<LotteryActivity> getListByCtime(int limit) {
        return lotteryActivityMapper.getListByCtime(limit);
    }

    public List<Map<String, Object>> getActivityInfoByUserId(long userId) {
        List<LotteryWinRecord> winRecords = lotteryWinRecordMapper.getByUserId(userId);
        Map<Long, LotteryWinRecord> winRecordMap = Maps.newHashMap();
        for (LotteryWinRecord r : winRecords) {
            winRecordMap.put(r.getActId(), r);
        }
        List<Long> ids = winRecords.stream().map(w -> w.getActId()).collect(Collectors.toList());
        List<LotteryActivity> activities = lotteryActivityMapper.getListByIds(ids);
        List<Map<String, Object>> ret = activities.stream().map(i -> {
            Map<String, Object> m = i.getMap();
            m.put("userstate", winRecordMap.get(i.getId()).getState());
            return m;
        }).collect(Collectors.toList());
        return ret;
    }

    @CacheEvict(value = "lottery-imgs", allEntries=true)
    @Transactional
    public int attendLottery(long actId, User user, String formid) {
        if (lotteryActivityMapper.incrActivityCount(actId) > 0) {
            if (userService.incrAttendCount(user) > 0) {
                LotteryWinRecord lw = new LotteryWinRecord();
                lw.setActId(actId);
                lw.setUserId(user.getId());
                lw.setFormid(formid);
                lw.setState(LotteryConst.LotteryState.PENDING);
                lw.setCtime(System.currentTimeMillis());
                if (lotteryWinRecordMapper.insert(lw) > 0) {
                    return BusinessCode.SUCC;
                } else {
                    throw new RuntimeException("insert winrecord failed");
                }
            } else {
                throw new RuntimeException("incr activity count failed");
            }
        }
        return BusinessCode.FAILED;
    }

    private int winLottery(LotteryActivity activity, User user) {
        if (lotteryActivityMapper.incrActivityWinCount(activity.getId(), LotteryConst.State.LOTTERY) > 0) {
            if (userService.incrWinCount(user) > 0) {
                if (lotteryWinRecordMapper.updateStateByUserId(user.getId(), activity.getId(), LotteryConst.LotteryState.WIN) > 0) {
                    return BusinessCode.SUCC;
                } else {
                    throw new RuntimeException(String.format("userId[%d] activity[%d] win lottery failed", user.getId(), activity.getId()));
                }
            } else {
                throw new RuntimeException(String.format("incr userId[%d] activity[%d] wincount failed", user.getId(), activity.getId()));
            }
        }
        return BusinessCode.FAILED;
    }

    @Transactional
    public void generateWinner(LotteryActivity activity) {
        if (activity.getState() == LotteryConst.State.ONLINE) {
            if (lotteryActivityMapper.updateStateById(activity.getId(), LotteryConst.State.LOTTERY) > 0) {
                List<Long> userIds = lotteryWinRecordMapper.getUserIdsByActId(activity.getId());
                logger.info(JsonUtil.getJsonString(userIds));
                Collections.shuffle(userIds);
                int totalwincount = activity.getTotalwincount();
                for (int i = 0; i < totalwincount && i < userIds.size(); i++) {
                    long userId = userIds.get(i);
                    User user = userService.findByUserId(userId);
                    if (user != null) {
                        int ret = winLottery(activity, user);
                        if (ret == BusinessCode.SUCC) {
                            logger.info("userId[{}] win acitivty[{}][{}]", userId, activity.getId(), activity.getName());
                        } else {
                            logger.info("userId[{}] fail acitivty[{}][{}]", userId, activity.getId(), activity.getName());
                        }
                    }
                }
            }
        }
    }


}
