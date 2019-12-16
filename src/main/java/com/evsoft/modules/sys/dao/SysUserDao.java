package com.evsoft.modules.sys.dao;

import com.evsoft.modules.sys.dto.SysUserDto;
import com.evsoft.modules.sys.entity.SysLoginlogEntity;
import com.evsoft.modules.sys.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 系统用户
 * 
 *
 *
 * @date 2016年9月18日 上午9:34:11
 */
@Mapper
public interface SysUserDao extends BaseDao<SysUserEntity> {
	
	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */
	List<String> queryAllPerms(Long userId);
	
	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(Long userId);
	
	/**
	 * 根据用户名，查询系统用户
	 */
	SysUserEntity queryByUserName(String username);

	/**
	 * 根据中文名，查询系统用户
	 */
	SysUserEntity queryByNickName(String nickName);

	/**
	 * 修改密码
	 */
	int updatePassword(Map<String, Object> map);

	int resetUserPwd(Map<String, Object> map);

	/*
	* 根据部门ID获取用户数据列表
	* created by wangzh 2018/04/18
	* */
	List<SysUserEntity> getUserByDeptIdList(long deptId);

	/*
	* 获取会员等级数据
	* created by hjz 2018/04/27
	* */
	List<SysUserEntity> getUserLevelList(Map<String, Object> map);

	/*
     * 根据部门ID获取用户数据列表（包括子孙部门）
     * created by xurulin 2018/04/20
     * */
	List<SysUserEntity> getAllUserByDeptId(long[] deptIds);


    /**
	 * 批量更新用户信息
	 */
	void batchUpdateUser(List<SysUserEntity> list);


	void batchUpdateUntieUser(Long[] id);



	/*
	* 根据工号查询用户信息
	* created by wangzh 2018/05/09
	* */
	SysUserDto getUserByJobNo(String jobNo);


	/*
	* 查询一个用户信息
	* created by wangzh 2018/05/09
	* */
	SysUserDto getOneUser(Map<String, Object> params);


	/**
	 * 通过部门id查询所有人员
	 * @param deptId
	 * @return
	 */
	List<SysUserEntity> getUserByDeptId(Long deptId);

	/**
	 * 通过用户id获取可以查看的部门
	 * @param user_id
	 * @return
	 */
	List<Long> getDeptListByUserID(Long user_id);

	/**
	 * 通过操作人员类型查询所有人员
	 * @param operatorValue
	 * @return
	 */
	List<SysUserEntity> getUserByOperUserTypeList(String operatorValue);

	/***
	 * 记录登录时间，token到数据库
	 */
	void saveLoginTime(SysLoginlogEntity loginTime);

	/***
	 * update退出时间，在线登录时长
	 */
	void updateLoginlog(SysLoginlogEntity loginTime);

	/***
	 * 根据token去查询登录时间，拿到登录时间计算登录的在线时长
	 */
	SysLoginlogEntity queryLoginTimeByToken(Map<String, Object> params);

	/**
	 * 获取所有用户
	 * @return
	 */
	List<SysUserDto> getAllUser();

	List<SysUserDto> queryBatchId(@Param("ids") List<Long> ids);
}
