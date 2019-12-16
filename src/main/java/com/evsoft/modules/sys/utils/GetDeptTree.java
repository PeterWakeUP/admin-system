package com.evsoft.modules.sys.utils;

import com.evsoft.modules.sys.entity.SysDeptEntity;
import com.evsoft.modules.sys.entity.SysUserEntity;
import com.evsoft.modules.sys.service.SysDeptService;
import org.apache.shiro.SecurityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 苏文辉 on 2018/5/18.
 */
public class GetDeptTree {

    private List<Long> listDeptId = new ArrayList<Long>();

    //获取所有子部门和本身部门
    public List<Long> getDeptIds(SysDeptService sysDeptService){
        SysUserEntity sysUserEntity = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        List<SysDeptEntity> deptList = sysDeptService.getAllList();
        long srcDeptId = sysUserEntity.getDeptId();
        SysDeptEntity node = new SysDeptEntity();
        for (SysDeptEntity mu : deptList) {
            if (mu.getDeptId().equals(sysUserEntity.getDeptId())) {
                node = mu;
                break;
            }

        }
        //=======================管理员通道(管理员可以查看到所有部门)===================================
        SysDeptEntity adminNode = new SysDeptEntity();   //管理员node
        List<SysDeptEntity> deptListTop = new ArrayList<>();  //存储顶部部门对象
        List<SysDeptEntity> listDeptTop = new ArrayList<>();
        long userId = sysUserEntity.getUserId().longValue();   //获取系统用户id
        long parentId = node.getParentId().longValue();  //获取父部门id
        if(userId == 1 && parentId == 0){   //如果是管理员 且 是管理部的
            for (SysDeptEntity mu : deptList) {   //找到和管理部同级的部门（包括管理部）
                if(mu.getParentId().longValue() == parentId){
                    listDeptTop.add(mu);
                }
            }
            for(SysDeptEntity item : listDeptTop){ //遍历找每个顶级部门的子孙部门
                item.setList(recursiveTree(deptList, item.getDeptId().longValue()));
                deptListTop.add(item);
            }
            adminNode.setList(deptListTop);
            getDeptIdList(adminNode);
            return listDeptId;
        }
        //=======================================================================================


        node.setList(recursiveTree(deptList, srcDeptId));
        getDeptIdList(node);
        return listDeptId;
    }

    /*
     //by xurulin
     //递归获取改部门下的所有子部门
      */
    private List<SysDeptEntity> recursiveTree(List<SysDeptEntity> menuList, long depid) {

        List<SysDeptEntity> childTreeNodes = new ArrayList<SysDeptEntity>();
        for (SysDeptEntity mu : menuList) {     //遍历部门List
            if (mu.getParentId() == depid) {    //如果部门id == depid（父部门id）
                childTreeNodes.add(mu);     //把部门实体加入到List中
            }
        }
        if (null == childTreeNodes || childTreeNodes.size() <= 0) return new ArrayList<SysDeptEntity>();

        for (SysDeptEntity child : childTreeNodes) {
            List<SysDeptEntity> n = recursiveTree(menuList, child.getDeptId()); //递归
            child.setList(n);
        }

        return childTreeNodes;
    }


    /**
     * 提取递归出来的所有部门的部门id
     * @param node
     */
    private void getDeptIdList(SysDeptEntity node){
        if(node.getDeptId() != null){
            listDeptId.add(node.getDeptId());
        }
        if(node.getList() != null && node.getList().size() >0){
            for(int i=0; i<node.getList().size(); i++){
                getDeptIdList((SysDeptEntity)node.getList().get(i));
            }
        }
    }


}
