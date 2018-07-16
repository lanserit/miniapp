package com.huanghuo.common.mapper;

import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by huangcheng on 2018/6/27.
 */
public class BatchUtilProvider {
    @SuppressWarnings("unchecked")
    public String getLotteryActivityByIds(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder("SELECT * FROM LotteryActivity WHERE ");
        sb.append(genOrSql((List<Object>) map.get("ids"), "id", "ids"));
        sb.append(" ORDER BY ctime desc");
        return sb.toString();
    }

    public String getUserByIds(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder("SELECT * FROM User WHERE ");
        sb.append(genOrSql((List<Object>) map.get("ids"), "id", "ids"));
        return sb.toString();
    }

    /**
     * 生成or语句方法，如果传入list为空，返回(1=0)
     *
     * @param list    要处理的列表项(主要用到size())
     * @param colName 数据库中列名
     * @param valName ParamMap中的key
     * @return
     */
    private StringBuffer genOrSql(List<Object> list, String colName, String valName) {
        StringBuffer sb = new StringBuffer();
        if (CollectionUtils.isEmpty(list)) {
            sb.append(" (1 = 0) ");
        } else {
            sb.append(" (");
            for (int i = 0; i < list.size() - 1; i++) {
                sb.append(String.format(" %s = #{%s[%d]} OR ", colName, valName, i));
            }
            sb.append(String.format(" %s = #{%s[%d]}", colName, valName, list.size() - 1));
            sb.append(") ");
        }
        return sb;
    }
}
