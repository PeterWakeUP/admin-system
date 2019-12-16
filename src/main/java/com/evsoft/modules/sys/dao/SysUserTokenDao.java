package com.evsoft.modules.sys.dao;

import com.evsoft.modules.sys.entity.SysUserTokenEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统用户Token
 * 
 *
 *
 * @date 2017-03-23 15:22:07
 */
@Mapper
public interface SysUserTokenDao extends BaseDao<SysUserTokenEntity> {
    
    SysUserTokenEntity queryByUserId(Long userId);

    SysUserTokenEntity queryByToken(String token);


    /**
     *使用临时token查询对于用户Id
     * */
    SysUserTokenEntity queryByTempToken(String tempToken);

}
