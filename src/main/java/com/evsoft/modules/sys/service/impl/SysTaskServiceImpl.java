package com.evsoft.modules.sys.service.impl;

import com.evsoft.common.globals.Conts;
import com.evsoft.modules.sys.dao.SysTaskDao;
import com.evsoft.modules.sys.entity.SystTaskEntity;
import com.evsoft.modules.sys.service.SysTaskService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by TWD on 2018/7/2.
 */
@Service
public class SysTaskServiceImpl implements SysTaskService {


    @Autowired
    SysTaskDao sysTaskDao;


    @Override
    public List<SystTaskEntity> queryList(Map<String, Object> map) {
        return sysTaskDao.queryList(map);
    }

    @Override
    public SystTaskEntity queryTaskByOne(Map<String, Object> parms) {
        return sysTaskDao.queryTaskByOne(parms);
    }

    /**
     * 返回true 表示开启定时任务，
     * 返回false表示已关闭该定时任务
     * */
    @Override
    public boolean queryTaskStatus(String taskKey) {
        Map<String, Object> params = new HashedMap();
        params.put(Conts.TaskKey,taskKey);
        SystTaskEntity systTaskEntity =  sysTaskDao.queryTaskByOne(params);
        if(null!=systTaskEntity && systTaskEntity.getStatus()==1){
            return true;
        }
        return false;
    }

    @Override
    public int updateTask(Map<String, Object> map) {
        return sysTaskDao.updateTask(map);
    }

    @Override
    public int saveTask(SystTaskEntity systTaskEntity) {
        return sysTaskDao.saveTask(systTaskEntity);
    }

    @Override
    public int delete(Map<String, Object> params) {
        return sysTaskDao.updateDel(params);
    }


}
