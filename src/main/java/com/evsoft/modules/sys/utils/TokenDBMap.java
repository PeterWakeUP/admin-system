package com.evsoft.modules.sys.utils;

import com.evsoft.modules.sys.entity.SysUserEntity;
import com.evsoft.modules.sys.entity.SysUserTokenEntity;
import org.apache.commons.collections.map.HashedMap;

import java.util.Map;
import java.util.Set;

/**
 * Created by TWD on 2018/9/7.
 */
public class TokenDBMap {

    public final static Map<String, SysUserTokenEntity> tokenDBMap = new HashedMap();


    public final static Map<Long,SysUserEntity> userDBMap = new HashedMap();


    public final static Map<Long,Set<String>> permsSetDBMap = new HashedMap();
}
