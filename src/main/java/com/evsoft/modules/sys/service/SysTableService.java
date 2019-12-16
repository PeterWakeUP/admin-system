package com.evsoft.modules.sys.service;

import com.evsoft.modules.sys.entity.SysTableEntity;

//前端表格服务类
public interface SysTableService {
    /*
     * 添加或者修改表格信息
     */
    void save(SysTableEntity table);

    /*
    *根据table_key查询表格信息
    */
    SysTableEntity queryByTableKey(String tableKey, Long userId);
}
