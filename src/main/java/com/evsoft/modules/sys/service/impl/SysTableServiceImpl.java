package com.evsoft.modules.sys.service.impl;

import com.evsoft.modules.sys.dao.SysTableDao;
import com.evsoft.modules.sys.entity.SysTableEntity;
import com.evsoft.modules.sys.service.SysTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by AE86 on 2018/4/12.
 */
@Service("sysTableService")
public class SysTableServiceImpl implements SysTableService {
    @Autowired
    private SysTableDao sysTableDao;

    @Override
    public void save(SysTableEntity table) {
        SysTableEntity oriEntity=sysTableDao.queryByTableKey(table.getTableKey(),table.getCreateUser());
        if(oriEntity==null){
            sysTableDao.save(table);
        }
        else {
            table.setCreateUser(oriEntity.getCreateUser());
            sysTableDao.update(table);
        }
    }

    @Override
    public SysTableEntity queryByTableKey(String tableKey,Long userId){
        return sysTableDao.queryByTableKey(tableKey,userId);
    }
}
