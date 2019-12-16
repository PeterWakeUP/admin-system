package com.evsoft.modules.sys.service;

import com.evsoft.modules.sys.dto.DeptAndUserDto;
import com.evsoft.modules.sys.entity.SysDeptEntity;

import java.util.List;
import java.util.Map;

/**
 * 部门管理
 *
 * @date 2017-06-20 15:23:47
 */

public interface SysDeptService {

    SysDeptEntity queryObject(Long deptId);

    List<SysDeptEntity> queryList(Map<String, Object> map);

    void save(SysDeptEntity sysDept);

    void update(SysDeptEntity sysDept);

    void delete(Long deptId);

    /**
     * 查询子部门ID列表
     *
     * @param parentId 上级部门ID
     */
    List<Long> queryDetpIdList(Long parentId);

    /**
     * 获取包括本部门以及本部门的所有子部门
     *
     * @param parentId 上级部门ID
     */
    List<Long> getTreeListData(Long parentId);

    /**
     * 获取子部门ID(包含本部门ID)，用于数据过滤
     */
    String getSubDeptIdList(Long deptId);

    /*
     * 查询子部门数据列表
     * created by wangzh 2018/04/18
     */
    List<SysDeptEntity> getDeptByParentIdList(long parentId);

    List<SysDeptEntity> getAllList();

    /**
     * 通过用户id获取部门数据
     */
    List<SysDeptEntity> getAllListByUserId(Long user_id);


    /**
     * 查询所有的部门和用户
     * @return
     */
    List<DeptAndUserDto> getDeptAndUser();
}
