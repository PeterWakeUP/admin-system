package com.evsoft.modules.sys.service.impl;

import com.evsoft.common.utils.R;
import com.evsoft.modules.sys.dao.SysUserTokenDao;
import com.evsoft.modules.sys.entity.SysUserTokenEntity;
import com.evsoft.modules.sys.oauth2.TokenGenerator;
import com.evsoft.modules.sys.service.SysUserTokenService;
import com.evsoft.modules.sys.utils.TokenDBMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service("sysUserTokenService")
public class SysUserTokenServiceImpl implements SysUserTokenService {
	@Autowired
	private SysUserTokenDao sysUserTokenDao;
	//12小时后过期
	private final static int EXPIRE = 3600 * 12;
	//10分钟后过期
	private final static int TEMP_EXPIRE = 3 * 60000;

	@Override
	public SysUserTokenEntity queryByUserId(Long userId) {
		return sysUserTokenDao.queryByUserId(userId);
	}

	@Override
	public SysUserTokenEntity queryByToken(String token) {
		return sysUserTokenDao.queryByToken(token);
	}


	@Override
	public SysUserTokenEntity queryByTempToken(String token) {
		return sysUserTokenDao.queryByTempToken(token);
	}

	@Override
	public void save(SysUserTokenEntity token){
		sysUserTokenDao.save(token);
	}
	
	@Override
	public void update(SysUserTokenEntity token){
		sysUserTokenDao.update(token);
	}

	@Override
	public R createToken(long userId) {
		//生成一个token
		String token = TokenGenerator.generateValue();

		//当前时间
		Date now = new Date();
		//过期时间
		Date expireTime = new Date(now.getTime() + EXPIRE * 1000);

		//判断是否生成过token
		SysUserTokenEntity tokenEntity = queryByUserId(userId);
		if(tokenEntity == null){
			tokenEntity = new SysUserTokenEntity();
			tokenEntity.setUserId(userId);
			tokenEntity.setToken(token);
			tokenEntity.setUpdateTime(now);
			tokenEntity.setExpireTime(expireTime);

			//保存token
			save(tokenEntity);
		}else{
			TokenDBMap.tokenDBMap.remove(tokenEntity.getToken());

			tokenEntity.setToken(token);
			tokenEntity.setUpdateTime(now);
			tokenEntity.setExpireTime(expireTime);
			//更新token
			update(tokenEntity);
		}
		R r = R.ok().put("token", token).put("expire", EXPIRE);
		return r;
	}


	@Override
	public String createTempToken(long userId) {
		//生成一个tempToken
		String tempToken = TokenGenerator.generateValue();
		//判断是否生成过token
		SysUserTokenEntity tokenEntity = queryByUserId(userId);

		//当前时间
		Date now = new Date();
		//过期时间
		Date tempExpireTime = new Date(now.getTime() + TEMP_EXPIRE);


		if(tokenEntity == null){
			tokenEntity = new SysUserTokenEntity();
			tokenEntity.setUserId(userId);
			tokenEntity.setTempToken(tempToken);
			tokenEntity.setTempExpireTime(tempExpireTime);
			//保存token
			save(tokenEntity);
		}else{
			tokenEntity.setTempToken(tempToken);
			tokenEntity.setTempExpireTime(tempExpireTime);
			//更新token
			update(tokenEntity);
		}
		return tempToken;
	}
}
