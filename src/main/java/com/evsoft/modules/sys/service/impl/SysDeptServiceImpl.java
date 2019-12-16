package com.evsoft.modules.sys.service.impl;

import com.evsoft.modules.sys.dao.SysDeptDao;
import com.evsoft.modules.sys.dto.DeptAndUserDto;
import com.evsoft.modules.sys.entity.SysDeptEntity;
import com.evsoft.modules.sys.service.SysDeptService;
import com.qiniu.util.StringUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("sysDeptService")
public class SysDeptServiceImpl implements SysDeptService {
	@Autowired
	private SysDeptDao sysDeptDao;
	
	@Override
	public SysDeptEntity queryObject(Long deptId){
		return sysDeptDao.queryObject(deptId);
	}
	
	@Override
	public List<SysDeptEntity> queryList(Map<String, Object> map){
		return sysDeptDao.queryList(map);
	}
	
	@Override
	public void save(SysDeptEntity sysDept){
		sysDeptDao.save(sysDept);
	}
	
	@Override
	public void update(SysDeptEntity sysDept){
		sysDeptDao.update(sysDept);
	}
	
	@Override
	public void delete(Long deptId){
		sysDeptDao.delete(deptId);
	}

	@Override
	public List<Long> queryDetpIdList(Long parentId) {
		return sysDeptDao.queryDetpIdList(parentId);
	}

	@Override
	public List<Long> getTreeListData(Long srcDeptId) {
		List<Long> treeDeptIdList= new ArrayList<>();
		List<SysDeptEntity> deptList = sysDeptDao.getAllList();
		Map<Long,String> deptsIdMap = new HashedMap();
		SysDeptEntity nodes = new SysDeptEntity();
		for (SysDeptEntity mu : deptList) {
			if (mu.getDeptId().equals(srcDeptId)) {
				deptsIdMap.put(mu.getDeptId(),mu.getName());
				nodes = mu;
				break;
			}
		}


		nodes.setList(recursiveTree(deptList, srcDeptId,deptsIdMap));
		for (Map.Entry<Long, String> m : deptsIdMap.entrySet()) {
			treeDeptIdList.add(m.getKey());
		}
		return treeDeptIdList;
	}

	/*
     //递归获取改部门下的所有子部门
      */
	private List<SysDeptEntity> recursiveTree(List<SysDeptEntity> menuList, long depid, Map<Long,String> deptsIdMap) {

		List<SysDeptEntity> childTreeNodes = new ArrayList<SysDeptEntity>();
		for (SysDeptEntity mu : menuList) {
			if (mu.getParentId() == depid) {
				deptsIdMap.put(mu.getDeptId(),mu.getName());
				childTreeNodes.add(mu);
			}
		}
		if (null == childTreeNodes || childTreeNodes.size() <= 0) return new ArrayList<SysDeptEntity>();

		for (SysDeptEntity child : childTreeNodes) {
			List<SysDeptEntity> n = recursiveTree(menuList, child.getDeptId(),deptsIdMap); //递归
			for(SysDeptEntity d : n){
				deptsIdMap.put(d.getDeptId(),d.getName());
			}
			child.setList(n);
		}
		return childTreeNodes;
	}


	@Override
	public String getSubDeptIdList(Long deptId){
		//部门及子部门ID列表
		List<Long> deptIdList = new ArrayList<>();

		//获取子部门ID
		List<Long> subIdList = queryDetpIdList(deptId);
		getDeptTreeList(subIdList, deptIdList);

		//添加本部门
		deptIdList.add(deptId);

		String deptFilter = StringUtils.join(deptIdList, ",");
		return deptFilter;
	}

	/*
	*查询子部门数据列表
	* created by wangzh 2018/04/18
	**/
	@Override
	public List<SysDeptEntity> getDeptByParentIdList(long parentId) {
		return sysDeptDao.getDeptByParentIdList(parentId);
	}

	@Override
	public List<SysDeptEntity> getAllList() {
		return sysDeptDao.getAllList();
	}
	/**
	 * 递归
	 */
	public void getDeptTreeList(List<Long> subIdList, List<Long> deptIdList){
		for(Long deptId : subIdList){
			List<Long> list = queryDetpIdList(deptId);
			if(list.size() > 0){
				getDeptTreeList(list, deptIdList);
			}

			deptIdList.add(deptId);
		}
	}

	/**
	 * 通过用户id获取部门数据
	 */
	public List<SysDeptEntity> getAllListByUserId(Long user_id)
	{
		return sysDeptDao.getAllListByUserId(user_id);
	}


	@Override
	public List<DeptAndUserDto> getDeptAndUser() {
		return sysDeptDao.getDeptAndUser();
	}




}
