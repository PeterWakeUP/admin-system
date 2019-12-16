package com.evsoft.modules.sys.service;

import com.evsoft.modules.sys.dto.SysUserDto;
import com.evsoft.modules.sys.entity.SysLoginlogEntity;
import com.evsoft.modules.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;


/**
 * 系统用户
 *
 * @date 2016年9月18日 上午9:43:39
 */
public interface SysUserService {

    /**
     * 查询用户的所有权限
     *
     * @param userId 用户ID
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
     * 根据用户ID，查询用户
     *
     * @param userId
     * @return
     */
    SysUserEntity queryObject(Long userId);

    /**
     * 查询用户列表
     */
    List<SysUserEntity> queryList(Map<String, Object> map);

    /**
     * 查询总数
     */
    int queryTotal(Map<String, Object> map);

    /**
     * 保存用户
     */
    void save(SysUserEntity user);

    /**
     * 修改用户
     */
    void update(SysUserEntity user);

    /**
     * 修改用户基本信息
     */
    void updateBasicInfo(SysUserEntity user);

    /**
     * 二次验证使用
     */
    void updateTgKey(SysUserEntity user);

    /**
     * 删除用户
     */
    void deleteBatch(Long[] userIds);

    /**
     * 修改密码
     *
     * @param userId      用户ID
     * @param password    原密码
     * @param newPassword 新密码
     */
    int updatePassword(Long userId, String password, String newPassword);

    int resetUserPwd(Long userId, String newPassword);

    /**
     * 根据用户名，查询系统用户
     */
    SysUserEntity queryByNickName(String nickName);

    /*
     * 根据部门ID获取用户数据列表
     * created by wangzh 2018/04/19
     * */
    List<SysUserEntity> getUserByDeptIdList(long deptId);

    /*
     * 根据部门ID获取用户数据列表（包括子孙部门）
     * created by xurulin 2018/04/20
     * */
    List<SysUserEntity> getAllUserByDeptId(long[] deptId);




    //查询会员等级数据
    List<SysUserEntity> getUserLevelList(Map<String, Object> params);

    //批量更新用户信息
    void batchUpdateUser(List<SysUserEntity> list);

    /**
     * 批量解绑二次验证
     * */
    void batchUpdateUntieUser(Long[] userIds);

    /*
    * 根据工号获取用户部分数据
    * created by wangzh 2018/05/15
    * */
    SysUserDto getUserByJobNo(String jobNo);


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
     */
    List<SysUserDto> getAllUser();
}
