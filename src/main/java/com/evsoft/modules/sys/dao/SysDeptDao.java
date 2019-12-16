package com.evsoft.modules.sys.dao;

import com.evsoft.modules.sys.dto.DeptAndUserDto;
import com.evsoft.modules.sys.entity.SysDeptEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 部门管理
 * 
 *
 *
 * @date 2017-06-20 15:23:47
 */
@Mapper
public interface SysDeptDao extends BaseDao<SysDeptEntity> {

    /**
     * 查询子部门ID列表
     * @param parentId  上级部门ID
     */
    List<Long> queryDetpIdList(Long parentId);

    /**
     * 查询子部门数据列表
     * created by wangzh 2018/04/18
     * @param parentId
     * @return
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
