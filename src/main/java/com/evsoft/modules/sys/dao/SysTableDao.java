package com.evsoft.modules.sys.dao;

import com.evsoft.modules.sys.entity.SysTableEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by AE86 on 2018/4/12.
 */
@Mapper
public interface SysTableDao extends BaseDao<SysTableEntity> {
    //根据table_key查询表格信息
    SysTableEntity queryByTableKey(@Param("tableKey") String tableKey, @Param("userId") Long userId);

}
