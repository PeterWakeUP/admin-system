package com.evsoft.modules.sys.service.impl;

import com.evsoft.modules.sys.dao.SysUserDao;
import com.evsoft.modules.sys.dto.SysUserDto;
import com.evsoft.modules.sys.entity.SysLoginlogEntity;
import com.evsoft.modules.sys.entity.SysUserEntity;
import com.evsoft.modules.sys.service.SysRoleService;
import com.evsoft.modules.sys.service.SysUserRoleService;
import com.evsoft.modules.sys.service.SysUserService;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 系统用户
 *
 * @date 2016年9月18日 上午9:46:09
 */
@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysRoleService sysRoleService;

    @Override
    public List<String> queryAllPerms(Long userId) {
        return sysUserDao.queryAllPerms(userId);
    }

    @Override
    public List<Long> queryAllMenuId(Long userId) {
        return sysUserDao.queryAllMenuId(userId);
    }

    @Override
    public SysUserEntity queryByUserName(String username) {
        return sysUserDao.queryByUserName(username);
    }

    @Override
    public SysUserEntity queryObject(Long userId) {
        return sysUserDao.queryObject(userId);
    }

    @Override
    public List<SysUserEntity> queryList(Map<String, Object> map) {
        return sysUserDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return sysUserDao.queryTotal(map);
    }

    @Override
    @Transactional
    public void save(SysUserEntity user) {
        user.setCreateTime(new Date());
        //sha256加密
        String salt = RandomStringUtils.randomAlphanumeric(20);
        user.setPassword(new Sha256Hash(user.getPassword(), salt).toHex());
        user.setSalt(salt);
        if (!StringUtils.isNotBlank(user.getNickname())) {
            user.setNickname(user.getUsername());
        }
        sysUserDao.save(user);
        //保存用户与角色关系
        sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    }

    @Override
    @Transactional
    public void update(SysUserEntity user) {
        if (StringUtils.isBlank(user.getPassword())) {
            user.setPassword(null);
        } else {
            user.setPassword(new Sha256Hash(user.getPassword(), user.getSalt()).toHex());
        }
        sysUserDao.update(user);

        //保存用户与角色关系
        sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    }

    @Override
    @Transactional
    public void updateBasicInfo(SysUserEntity user) {
        user.setStatus(null);
        user.setDeptId(null);
        user.setUsername(null);
        user.setJobNum(null);
        user.setPassword(null);
        sysUserDao.update(user);
    }

    public void updateTgKey(SysUserEntity user) {
        SysUserEntity updateUser = new SysUserEntity();
        updateUser.setUserId(user.getUserId());
        updateUser.setTgKey(user.getTgKey());
        updateUser.setTgNum(user.getTgNum());
        sysUserDao.update(updateUser);
    }

    @Override
    @Transactional
    public void deleteBatch(Long[] userId) {
        sysUserDao.deleteBatch(userId);
    }

    @Override
    public int updatePassword(Long userId, String password, String newPassword) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("password", password);
        map.put("newPassword", newPassword);
        return sysUserDao.updatePassword(map);
    }

    @Override
    public int resetUserPwd(Long userId, String newPassword) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("newPassword", newPassword);
        return sysUserDao.resetUserPwd(map);
    }

    @Override
    public SysUserEntity queryByNickName(String nickName) {
        return sysUserDao.queryByNickName(nickName);
    }

    /*
     * 根据部门ID获取用户数据列表
     * created by wangzh 2018/04/19
     * */
    @Override
    public List<SysUserEntity> getUserByDeptIdList(long deptId) {
        return sysUserDao.getUserByDeptIdList(deptId);
    }

    @Override
    public List<SysUserEntity> getAllUserByDeptId(long[] deptIds) {
        return sysUserDao.getAllUserByDeptId(deptIds);
    }


    @Override
    public List<SysUserEntity> getUserLevelList(Map<String, Object> params) {
        return sysUserDao.getUserLevelList(params);
    }

    @Override
    public void batchUpdateUser(List<SysUserEntity> list) {
        sysUserDao.batchUpdateUser(list);
    }

    @Override
    public void batchUpdateUntieUser(Long[] userId) {
        sysUserDao.batchUpdateUntieUser(userId);
    }

    /*
     * 根据工号获取用户数据
     * created by wangzh 2018/05/15
     * */
    @Override
    public SysUserDto getUserByJobNo(String jobNo) {
        return sysUserDao.getUserByJobNo(jobNo);
    }

    @Override
    public List<SysUserEntity> getUserByDeptId(Long deptId) {
        return sysUserDao.getUserByDeptId(deptId);
    }

    @Override
    public List<Long> getDeptListByUserID(Long user_id)
    {
        return sysUserDao.getDeptListByUserID(user_id);
    }

    @Override
    public List<SysUserEntity> getUserByOperUserTypeList(String operatorValue) {
        return sysUserDao.getUserByOperUserTypeList(operatorValue);
    }

    @Override
    @Transactional
    public void saveLoginTime(SysLoginlogEntity sysLoginEntity) {
        sysUserDao.saveLoginTime(sysLoginEntity);
    }

    @Override
    @Transactional
    public void updateLoginlog(SysLoginlogEntity sysLoginEntity) {
        sysUserDao.updateLoginlog(sysLoginEntity);
    }

    @Override
    public SysLoginlogEntity queryLoginTimeByToken(Map<String, Object> params) {
        return sysUserDao.queryLoginTimeByToken(params);
    }

    @Override
    public List<SysUserDto> getAllUser() {
        return sysUserDao.getAllUser();
    }
}
