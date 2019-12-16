package com.evsoft.modules.sys.dao;

import com.evsoft.modules.sys.entity.SystTaskEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Created by AE86 on 2018/4/12.
 */
@Mapper
public interface SysTaskDao{

    List<SystTaskEntity> queryList(Map<String, Object> parms);

    SystTaskEntity queryTaskByOne(Map<String, Object> parms);

    int updateTask(Map<String, Object> map);

    int updateDel(Map<String, Object> map);

    int saveTask(SystTaskEntity systTaskEntity);

}
