package com.evsoft.modules.sys.utils;

import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

/**
 * Created by TWD on 2018/5/15.
 */
public class TableHelp {

    public static Map<String,Boolean> filters(String[] colsKey){
        Map<String,Boolean> tableMap = new HashedMap();
        tableMap.put("warnName",true);
        tableMap.put("activationUsername",true);
        tableMap.put("customerUsername",true);
        tableMap.put("sex",true);
        tableMap.put("mobilePhone",true);
        tableMap.put("customerId",true);
        tableMap.put("registerTime",true);
        tableMap.put("memberLevel",true);
        tableMap.put("lastLoginTime",true);
        tableMap.put("intention",true);
        tableMap.put("followRemark",true);
        tableMap.put("talkTime",true);
        tableMap.put("callThroughStatus",true);
        tableMap.put("lastContactTime",true);
        tableMap.put("rechareDayFist",true);
        tableMap.put("minAllotRechareDate",true);
        tableMap.put("lastInvestTime",true);
        tableMap.put("totalRechareAmount",true);
        tableMap.put("rechareAmount",true);
        tableMap.put("totalInvestAmount",true);
        tableMap.put("allocationTotalInvestAmount",true);
        tableMap.put("yestDueinpandi",true);
        tableMap.put("giveTime",true);
        tableMap.put("warnTime",true);
        tableMap.put("vipvaliddate",true);
        tableMap.put("customerCategory",true);
        tableMap.put("callPriority",true);
        tableMap.put("prizeUseRate",true);
        tableMap.put("visitPeriodLike",true);
        tableMap.put("smsSend",true);
        tableMap.put("unUsePrize",true);
        tableMap.put("unfinishInvestAmount",true);
        tableMap.put("withdrewAmount",true);
        tableMap.put("ntSubscribeAmountB",true);
        tableMap.put("subscribeAmountN",true);
        tableMap.put("ntSubscribeAmountN",true);
        tableMap.put("checkTerm",true);
        if(null!=colsKey){
            for(String key:colsKey) {
                tableMap.put(key,false);
            }
        }
        return tableMap;
    }

    public static Map<String,Boolean> keFuCallRecordFilters(String[] colsKey){
        Map<String,Boolean> tableMap = new HashedMap();
        tableMap.put("deptName",true);
        tableMap.put("cno",true);
        tableMap.put("kefuName",true);
        tableMap.put("queueName",true);
        tableMap.put("realName",true);
        tableMap.put("sex",true);
        tableMap.put("mobilePhone",true);
        tableMap.put("custID",true);
        tableMap.put("callType",true);
        tableMap.put("bridgeTime",true);
        tableMap.put("startTime",true);
        tableMap.put("totalDuration",true);
        tableMap.put("score",true);
        tableMap.put("recordInfo",true);
        tableMap.put("recordDesc",true);
        tableMap.put("smsStatus",true);
        tableMap.put("wName",true);
        tableMap.put("jiHuoName",true);
        if(null!=colsKey){
            for(String key:colsKey) {
                tableMap.put(key,false);
            }
        }
        return tableMap;
    }
}
