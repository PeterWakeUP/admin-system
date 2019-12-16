package com.evsoft.modules.sys.service.impl;

import com.evsoft.common.utils.Constant;
import com.evsoft.modules.sys.dao.SysMenuDao;
import com.evsoft.modules.sys.dao.SysUserDao;
import com.evsoft.modules.sys.dao.SysUserTokenDao;
import com.evsoft.modules.sys.entity.SysMenuEntity;
import com.evsoft.modules.sys.entity.SysUserEntity;
import com.evsoft.modules.sys.entity.SysUserTokenEntity;
import com.evsoft.modules.sys.service.ShiroService;
import com.evsoft.modules.sys.utils.TokenDBMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShiroServiceImpl implements ShiroService {
    @Autowired
    private SysMenuDao sysMenuDao;
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysUserTokenDao sysUserTokenDao;



    @Override
    public Set<String> getUserPermissions(long userId) {
        List<String> permsList;

        //系统管理员，拥有最高权限
        if(userId == Constant.SUPER_ADMIN){
            List<SysMenuEntity> menuList = sysMenuDao.queryList(new HashMap<>());
            permsList = new ArrayList<>(menuList.size());
            for(SysMenuEntity menu : menuList){
                permsList.add(menu.getPerms());
            }
        }else{
            permsList = sysUserDao.queryAllPerms(userId);
        }
        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for(String perms : permsList){
            if(StringUtils.isBlank(perms)){
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
        return permsSet;
    }

    @Override
    public SysUserTokenEntity queryByToken(String token) {
        SysUserTokenEntity dbMapToken = TokenDBMap.tokenDBMap.get(token);
        if(null == dbMapToken){
            dbMapToken = sysUserTokenDao.queryByToken(token);
            TokenDBMap.tokenDBMap.put(token,dbMapToken);
        }
        return dbMapToken;
    }

    @Override
    public SysUserEntity queryUser(Long userId) {
        SysUserEntity sysUserEntity = TokenDBMap.userDBMap.get(userId);
        if(null == sysUserEntity){
            sysUserEntity = sysUserDao.queryObject(userId);
            TokenDBMap.userDBMap.put(userId,sysUserEntity);
        }
        return sysUserEntity;
    }
}
