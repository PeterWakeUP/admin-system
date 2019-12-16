package com.evsoft.modules.sys.service;



import com.evsoft.modules.sys.entity.SystTaskEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by TWD on 2018/7/2.
 */
public interface SysTaskService {


    List<SystTaskEntity> queryList(Map<String, Object> map);

    SystTaskEntity queryTaskByOne(Map<String, Object> parms);


    /**
     * 返回true 表示开启定时任务，
     * 返回false表示已关闭该定时任务
     * */
    boolean queryTaskStatus(String taskKey);

    int updateTask(Map<String, Object> map);

    int saveTask(SystTaskEntity systTaskEntity);


    int delete(Map<String, Object> params);


}
