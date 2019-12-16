package com.evsoft.modules.comm.controller;

import com.evsoft.modules.sys.entity.SysUserEntity;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Controller公共组件
 * 
 *
 *
 * @date 2016年11月9日 下午9:42:26
 */
public abstract class AbstractController {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected SysUserEntity getUser() {
		return (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
	}

	protected Long getUserId() {
		return getUser().getUserId();
	}

	protected Long getDeptId() {
		return getUser().getDeptId();
	}



	/**
	 * <p>
	 * 从request中获取参数根据键值对形成Map. <br>
	 * 注意:对于数组参数，只拷贝了第1个元素.<br>
	 * 对于全空格的数据，仍然保留，在保存修改时可能需要.
	 * </p>
	 *
	 * @param request
	 * @return map
	 * @author liuwei
	 */
	protected static Map<String, Object> getParamValues(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		@SuppressWarnings("rawtypes")
		Enumeration names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String key = (String) names.nextElement();
			String value = request.getParameter(key);
			if (value != null && !"".equals(value)) {
				map.put(key, value);
			}
		}
		return map;
	}

	protected Map getBeginAndEndDate(Map<String,Object> params){

		Object beginDate = params.get("beginDate");
		Object endDate = params.get("endDate");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();

		if(null==endDate || "".equals(endDate)){
			params.put("endDate",sdf.format(new Date()));
		}

		if(null==beginDate || "".equals(beginDate)){
			calendar.setTime(new Date());
			calendar.add(Calendar.DATE,-7);
			params.put("beginDate",sdf.format(calendar.getTime()));

		}

		return  params;
	}

	protected void splitToArray(Map<String,Object> params, String key)
	{
		Object str = params.get(key);
		if (str != null && !str.toString().trim().equals(""))
		{
			String[] array =str.toString().split(",");
			params.put(key, array);
		}
		else
			params.remove(key);
	}
}
