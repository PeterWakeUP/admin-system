package com.evsoft.modules.sys.controller;

import com.evsoft.common.globals.AuthConst;
import com.evsoft.common.globals.Globals;
import com.evsoft.common.utils.R;
import com.evsoft.common.utils.ShiroUtils;
import com.evsoft.modules.comm.controller.AbstractController;
import com.evsoft.modules.sys.entity.SysLoginlogEntity;
import com.evsoft.modules.sys.entity.SysUserEntity;
import com.evsoft.modules.sys.entity.SysUserTokenEntity;
import com.evsoft.modules.sys.service.SysUserService;
import com.evsoft.modules.sys.service.SysUserTokenService;
import com.evsoft.util.googleauth.GoogleAuthenticator;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录相关
 * 
 *
 *
 * @date 2016年11月10日 下午1:15:31
 */
@RestController
public class SysLoginController extends AbstractController {

	private org.slf4j.Logger logger= LoggerFactory.getLogger(SysLoginController.class);


	@Autowired
	private Producer producer;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserTokenService sysUserTokenService;

	@Autowired
	private GoogleAuthenticator googleAuthenticator;


	@Autowired
	Globals globals;

	@RequestMapping("captcha.jpg")
	public void captcha(HttpServletResponse response)throws ServletException, IOException {
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");

		//生成文字验证码
		String text = producer.createText();
		//生成图片验证码
		BufferedImage image = producer.createImage(text);
		//保存到shiro session
		ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);

		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);
		IOUtils.closeQuietly(out);
	}
	/**
	 * 登录
	 */
	@RequestMapping(value = "/sys/login", method = RequestMethod.POST)
	public Map<String, Object> login(String username, String password, String captcha)throws IOException {
		//用户信息
		logger.info("{}请求了登录，二次验证开启状态:{}",username,globals.eagle2fa);
		String yanzhengma = (String)ShiroUtils.getSessionAttribute(Constants.KAPTCHA_SESSION_KEY);
		if(!captcha.equals(yanzhengma)){
			return R.error().put("msg", "验证码不正确");
		}

		SysUserEntity user = sysUserService.queryByUserName(username);
		//账号不存在、密码错误
		if(user == null || !user.getPassword().equals(new Sha256Hash(password, user.getSalt()).toHex())) {
			return R.error("账号或密码不正确");
		}
		//账号锁定
		if(user.getStatus() == 0){
			return R.error("账号已被锁定,请联系管理员");
		}


		if(true){
			//如果没有开启二次验证
			//生成token，并保存到数据库
			R r = sysUserTokenService.createToken(user.getUserId());
			try {
				//20180720 add by Tdw12329 begin 登录成功后插入一条数据记录登录时间和token到数据库
				String loginToken = r.get("token").toString();
				insertLogTime(user.getUserId(),loginToken,new Date());
				//20180720 add by Tdw12329 end
			}catch (Exception e){
			}


			return r.put("eagle2fa","false");
		}
		//生成一个临时Token 用户并临时存储 后续验证使用
		String tempToken= sysUserTokenService.createTempToken(user.getUserId());

		return R.ok().put(AuthConst.DYNAMIC_AUTH_TOKEN_KEY,tempToken).put("tgNum",user.getTgNum()).put("userName",user.getUsername());

	}


	/**
	 * 生成二维码
	 * @param request
	 * @return
	 */
	@RequestMapping("/sys/generateCode")
	public R generateCode(HttpServletRequest request) {
		String dynamicAuthToken = request.getParameter(AuthConst.DYNAMIC_AUTH_TOKEN_KEY);
		SysUserTokenEntity sysUserTokenEntity = sysUserTokenService.queryByTempToken(dynamicAuthToken);
		if (sysUserTokenEntity == null) {
			logger.warn("请先登录后在进行二次验证");
			return R.error("请先登录");
		}
		SysUserEntity user  = sysUserService.queryObject(sysUserTokenEntity.getUserId());
		if (user == null) {
			logger.warn("员工不存在");
			return R.error("员工不存在");
		}
		if(user.getTgNum()<1){
			//防止直接访问该链接导致二次验证被解绑，而被非法操作绑定
			String secret = googleAuthenticator.generateSecretKey();//生成的key赋值给secret
			user.setTgKey(secret);
			String qrBarcode = googleAuthenticator.getQRBarcode(user.getUsername(),secret);
			sysUserService.updateTgKey(user);
			return R.ok().put("authUrl", qrBarcode);
		}
		return R.ok().put("error", "[非法操作]已经登录过，解绑需要管理员操作！");
	}


	/**
	 * 二次验证登录
	 * @param request
	 * @return
	 */
	@RequestMapping("/sys/auth/login")
	public R authLogin(HttpServletRequest request,HttpServletResponse response) {
		long authCode;
		try {
			authCode = Long.valueOf(request.getParameter("authCode"));//谷歌身份验证器上输入的验证码
		}catch (Exception e) {
			logger.warn("输入二维码格式不对",e);
			return R.error("输入数字验证码");
		}
		String dynamicAuthToken = request.getParameter(AuthConst.DYNAMIC_AUTH_TOKEN_KEY);
		SysUserTokenEntity sysUserTokenEntity = sysUserTokenService.queryByTempToken(dynamicAuthToken);
		if (sysUserTokenEntity == null) {
			logger.warn("请先登录");
			return R.error("请先登录");
		}
		Long oldTime = sysUserTokenEntity.getTempExpireTime().getTime();
		Long nowTime = new Date().getTime();
		if(nowTime>oldTime){
			//防止直接访问generateCode 导致二次验证被解绑，而被非法操作绑定
			logger.warn("二次验证已过期，请返回上一步重新登陆");
			return R.error("二次验证已过期，请返回上一步重新登陆");
		}

		SysUserEntity user = null;
		try{
			user = sysUserService.queryObject(sysUserTokenEntity.getUserId());
			if(null == user){
				logger.error("员工不存在");
				return R.error("员工不存在");
			}
			boolean authResult = googleAuthenticator.checkCode(authCode,user.getTgKey());//检验用户输入的验证码是否正确
			logger.info("{}请求二次验证,验证码是{},否正确：{}",user.getUsername(),authCode,authResult);
			if(!authResult) {
				logger.warn("{}验证码错误:{}",user.getUsername(),authCode);
				return R.error("验证码错误");
			}
		}catch (Exception e) {
			logger.error("二次验证登录系统异常",e);
			return R.error();
		}
		try{
			if(null!=user){
				user.setTgNum(user.getTgNum()+1);//修改登录次数
				sysUserService.updateTgKey(user);
			}
		}catch(Exception e){
			logger.error("update TgNum次数出错:{} user:{}",e,user);
		}
		//这里才真正创建可访问系统的token;
		R ret = sysUserTokenService.createToken(user.getUserId());

		//20180720 add by Tdw12329 登录成功后插入一条数据记录登录时间和token到数据库
		String loginToken = ret.get("token").toString();
		insertLogTime(user.getUserId(),loginToken,new Date());

		return ret;
	}


	/*****
	 * 登录成功后插入一条数据记录登录时间和token到数据库
	 *
	 */
	private void insertLogTime(Long userId,String token,Date d){
		SysLoginlogEntity sysLogin=new SysLoginlogEntity();
		sysLogin.setUserId(userId);
		sysLogin.setToken(token);
		sysLogin.setCreateTime(d);
		sysLogin.setLoginTime(d);
		try{
			sysUserService.saveLoginTime(sysLogin);
		}catch (Exception e){
			logger.error("insertLogTime error{}",e.getMessage());
		}


	}

	/***
	 * update退出时间，在线登录时长
	 */
	@RequestMapping("/sys/logout/logoutUpdateTime")
	public R logoutUpdateTime(HttpServletRequest request) throws IOException{
		try {
           //根据token去更新退出时间，登录的在线时长
			SysLoginlogEntity sysLogin=new SysLoginlogEntity();
			//退出时间
			Date logoutTime = new Date();
			//根据token去查询登录时间，拿到登录时间计算登录的在线时长
			String token = request.getParameter("token");

			//查询登录时间
			Map<String, Object> params=new HashMap<String, Object>();
			params.put("token",token);
			params.put("userId",getUserId());
			SysLoginlogEntity logEntity = sysUserService.queryLoginTimeByToken(params);
			if(null!=logEntity){
				Date loginTime = logEntity.getLoginTime();

				//计算登录在线时间：毫秒
				long haomilliTime =(logoutTime.getTime() - loginTime.getTime());

				sysLogin.setToken(token);
				sysLogin.setLogoutTime(logoutTime);
				sysLogin.setOnlineTime(haomilliTime);
				sysLogin.setUserId(getUserId());
				sysUserService.updateLoginlog(sysLogin);
			}
		}catch (Exception e){
			logger.error("退出时出现异常{}",e);
		}

		return R.ok();
	}
}
