package com.evsoft.modules.sys.dao;

import java.util.List;
import java.util.Map;

/**
 * Created by zhengtengfei on 2018/8/16.
 */
public interface DataBaseDAO {
    List<Map<String,Object>> selectBase(String sql);
}
