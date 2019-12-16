package com.evsoft.util.googleauth;

import com.evsoft.modules.sys.entity.SysUserEntity;
import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

/**
 * Created by TWD on 2018/5/17.
 */
public class TempTokenDB {

    public static Map<String, SysUserEntity> tempTokenMap = new HashedMap();
}
