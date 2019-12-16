package com.evsoft.modules.sys.utils;

import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

/**
 * Created by 苏文辉 on 2018/5/22.
 */
public class TableHelpActivation {

    public static Map<String,Boolean> filters(String[] colsKey){
        Map<String,Boolean> tableMap = new HashedMap();
        tableMap.put("jobNum",true);
        tableMap.put("deptName",true);
        tableMap.put("nickname",true);
        tableMap.put("liftUsetype",true);
        tableMap.put("liftUsetypeTest",true);
        tableMap.put("userId",true);
        tableMap.put("userName",true);
        tableMap.put("userPhone",true);
        tableMap.put("distrAt",true);
        tableMap.put("minRechareDate",true);
        tableMap.put("rechareAmount",true);
        tableMap.put("subscribeAmountB",true);
        tableMap.put("ntSubscribeAmountB",true);
        tableMap.put("validInvestAmout",true);
        tableMap.put("agentSec",true);
        tableMap.put("registTime",true);
        tableMap.put("endTimeOfCheck",true);
        tableMap.put("startStamp",true);
        tableMap.put("subscribeAmountN",true);
        tableMap.put("ntSubscribeAmountN",true);
        tableMap.put("rechargeWithdrawDValue",true);
        tableMap.put("withdrewAmount",true);
        tableMap.put("performance",true);
        if(null!=colsKey){
            for(String key:colsKey) {
                tableMap.put(key,false);
            }
        }
        return tableMap;
    }

}
