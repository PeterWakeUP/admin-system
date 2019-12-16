package com.evsoft.modules.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.evsoft.common.utils.R;
import com.evsoft.modules.comm.controller.AbstractController;
import com.evsoft.modules.sys.entity.SysTableEntity;
import com.evsoft.modules.sys.service.SysTableService;
import com.evsoft.modules.sys.utils.TableHelp;
import com.evsoft.modules.sys.utils.TableHelpActivation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/table")
public class SysTableController extends AbstractController {
    @Autowired
    private SysTableService sysTableService;

    /**
     * 保存表格信息
     */
    @RequestMapping("/save")
    //@RequiresPermissions("sys:user:save")
    public R save(SysTableEntity table){
        Long userId = this.getUserId();
        table.setCreateUser(userId);
        table.setUpdateUser(userId);
        String str = table.getDisplayInfo();
        String[] cols = str.split(",");
        Map<String,Boolean> tablesInfo = null;
        if(table.getTableKey().equals("customerTable")){
            tablesInfo = TableHelp.filters(cols);
        }else if(table.getTableKey().equals("activationTable")){
            tablesInfo = TableHelpActivation.filters(cols);
        }
        else if(table.getTableKey().equals("keFuCallRecordTable")){
            tablesInfo = TableHelp.keFuCallRecordFilters(cols);
        }
        String displayInfo = JSONObject.toJSONString(tablesInfo);
        table.setDisplayInfo(displayInfo);
        sysTableService.save(table);
        return R.ok();
    }

    /**
     *查询表格信息
     */
    //@SysLog("查询表格信息")
    @RequestMapping("/queryByTableKey")
    public R queryByTableKey(String tableKey){
        SysTableEntity sysTable = sysTableService.queryByTableKey(tableKey,getUserId());
        if(null==sysTable){
            SysTableEntity table = new SysTableEntity();
            table.setCreateUser(getUserId());
            table.setUpdateUser(getUserId());
            table.setTableKey(tableKey);
            Map<String,Boolean> tablesInfoMap = null;
            if(table.getTableKey().equals("customerTable")){
                tablesInfoMap = TableHelp.filters(null);
            }else if(table.getTableKey().equals("activationTable")){
                tablesInfoMap = TableHelpActivation.filters(null);
            }
            else if(table.getTableKey().equals("keFuCallRecordTable")){
                tablesInfoMap = TableHelp.keFuCallRecordFilters(null);
            }
            for(String key:tablesInfoMap.keySet()){
                tablesInfoMap.put(key,false);
            }
            String displayInfo = JSONObject.toJSONString(tablesInfoMap);
            table.setDisplayInfo(displayInfo);
            sysTableService.save(table);
            sysTable = sysTableService.queryByTableKey(tableKey,getUserId());
        }
        return R.ok().put("sysTable", sysTable);
    }
}
