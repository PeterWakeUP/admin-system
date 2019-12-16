package com.evsoft.modules.sys.service;

import com.evsoft.common.utils.R;
import com.evsoft.modules.sys.entity.SysUserTokenEntity;

/**
 * 用户Token
 * 
 *
 *
 * @date 2017-03-23 15:22:07
 */
public interface SysUserTokenService {

	SysUserTokenEntity queryByUserId(Long userId);

	SysUserTokenEntity queryByToken(String token);
	/**
	 *使用临时token查询对于用户Id
	 * */
	SysUserTokenEntity queryByTempToken(String tempToken);
	
	void save(SysUserTokenEntity token);
	
	void update(SysUserTokenEntity token);

	/**
	 * 生成token
	 * @param userId  用户ID
	 */
	R createToken(long userId);

	/**
	 * 生成临时token 提供二次验证使用
	 * @param userId  用户ID
	 */
	String createTempToken(long userId);

}
