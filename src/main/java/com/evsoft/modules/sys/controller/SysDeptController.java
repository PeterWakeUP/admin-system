package com.evsoft.modules.sys.controller;

import com.evsoft.common.utils.Constant;
import com.evsoft.common.utils.R;
import com.evsoft.modules.comm.controller.AbstractController;
import com.evsoft.modules.sys.dto.DeptAndUserDto;
import com.evsoft.modules.sys.entity.SysDeptEntity;
import com.evsoft.modules.sys.service.SysDeptService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 部门管理
 *
 * @date 2017-06-20 15:23:47
 */
@RestController
@RequestMapping("/sys/dept")
public class SysDeptController extends AbstractController {
    @Autowired
    private SysDeptService sysDeptService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//	@RequiresPermissions("sys:dept:list")
    public List<SysDeptEntity> list() {
        Map<String, Object> map = new HashMap<>();
        //如果不是超级管理员，则只能查询本部门及子部门数据
        if (getUserId() != Constant.SUPER_ADMIN) {
            map.put("deptFilter", sysDeptService.getSubDeptIdList(getDeptId()));
        }
        List<SysDeptEntity> deptList = sysDeptService.queryList(map);

        return deptList;
    }

    /*
    //by xurulin
    //加载部门树形数据
     */
    @RequestMapping("/deptTreeData")
//	@RequiresPermissions("sys:dept:list")
    public SysDeptEntity deptTreeData(@RequestParam(value="flag", required = false)Integer flag) {
        List<SysDeptEntity> deptList = sysDeptService.getAllList();
        long srcDeptId = getDeptId();
        if(flag != null){
            if(flag.intValue() == 1){   //查询所有的部门
                srcDeptId = 1;
            }else if(flag.intValue() == 2){  //查询质检部及以下部门
                srcDeptId = 105;
            }else if(flag.intValue() == 3){  //查询呼叫部门及以下部门
                srcDeptId = 2;
            }else if(flag.intValue() == 4){  //查询激活部及以下部门
                srcDeptId = 10;
            }else if(flag.intValue() == 5){  //查询客服部及以下部门
                srcDeptId = 109;
            }else if(flag.intValue() == 6){
                srcDeptId = 169;
            }

        }
        SysDeptEntity node = new SysDeptEntity();
        for (SysDeptEntity mu : deptList) {
            if (mu.getDeptId().equals(srcDeptId)) {
                node = mu;
                break;
            }

        }
        /*if (node != null && node.getParentId() > 0) {
            srcDeptId = node.getParentId();
        }*/

        //=======================管理员通道(管理员可以查看到所有部门)===================================
        SysDeptEntity adminNode = new SysDeptEntity();   //管理员node
        adminNode.setDeptId(new Long(0));
        adminNode.setName("全部");
        List<SysDeptEntity> deptListTop = new ArrayList<>();  //存储顶部部门对象
        List<SysDeptEntity> listDeptTop = new ArrayList<>();
        long userId = getUserId().longValue();   //获取系统用户id
        long parentId = node.getParentId().longValue();  //获取父部门id
        if(userId == 1 && parentId == 0 || flag != null && flag.intValue() == 1){   //如果是管理员 且 是管理部的, 或者flag == 1（质检页面）
            for (SysDeptEntity mu : deptList) {   //找到和管理部同级的部门（包括管理部）
                if(mu.getParentId().longValue() == parentId){
                    listDeptTop.add(mu);
                }
            }
            for(SysDeptEntity item : listDeptTop){  //遍历找每个顶级部门的子孙部门
                item.setList(recursiveTree(deptList, item.getDeptId().longValue()));
                deptListTop.add(item);
            }
            adminNode.setList(deptListTop);
            return adminNode;
        }
        //=======================================================================================


        node.setList(recursiveTree(deptList, srcDeptId));
        return node;
    }

    /*
     //by xurulin
     //递归获取改部门下的所有子部门
      */
    private List<SysDeptEntity> recursiveTree(List<SysDeptEntity> menuList, long depid) {

        List<SysDeptEntity> childTreeNodes = new ArrayList<SysDeptEntity>();
        for (SysDeptEntity mu : menuList) {
            if (mu.getParentId() == depid) {
                childTreeNodes.add(mu);
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
     * 选择部门(添加、修改菜单)
     */
    @RequestMapping("/select")
    @RequiresPermissions("sys:dept:select")
    public R select() {
        Map<String, Object> map = new HashMap<>();
        //如果不是超级管理员，则只能查询本部门及子部门数据
        if (getUserId() != Constant.SUPER_ADMIN) {
            map.put("deptFilter", sysDeptService.getSubDeptIdList(getDeptId()));
        }
        List<SysDeptEntity> deptList = sysDeptService.queryList(map);

        //添加一级部门
        if (getUserId() == Constant.SUPER_ADMIN) {
            SysDeptEntity root = new SysDeptEntity();
            root.setDeptId(0L);
            root.setName("一级部门");
            root.setParentId(-1L);
            root.setOpen(true);
            deptList.add(root);
        }

        return R.ok().put("deptList", deptList);
    }

    /**
     * 上级部门Id(管理员则为0)
     */
    @RequestMapping("/info")
    @RequiresPermissions("sys:dept:list")
    public R info() {
        long deptId = 0;
        if (getUserId() != Constant.SUPER_ADMIN) {
            SysDeptEntity dept = sysDeptService.queryObject(getDeptId());
            deptId = dept.getParentId();
        }

        return R.ok().put("deptId", deptId);
    }

    /*
     * 根据父ID查询子部门数据列表
     * created by wangzh 2018/04/18
     */
    @RequestMapping(value = "/getDeptByParentIdList", method = RequestMethod.GET)
   @RequiresPermissions("sys:dept:getDeptByParentIdList")
    public R getDeptByParentIdList(@RequestParam("parentId") long parentId) {
        List<SysDeptEntity> deptList = sysDeptService.getDeptByParentIdList(parentId);
        return R.ok().put("deptList", deptList);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{deptId}")
    @RequiresPermissions("sys:dept:info")
    public R info(@PathVariable("deptId") Long deptId) {
        SysDeptEntity dept = sysDeptService.queryObject(deptId);

        return R.ok().put("dept", dept);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:dept:save")
    public R save(@RequestBody SysDeptEntity dept) {
        sysDeptService.save(dept);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:dept:update")
    public R update(@RequestBody SysDeptEntity dept) {
        sysDeptService.update(dept);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:dept:delete")
    public R delete(long deptId) {
        //判断是否有子部门
        List<Long> deptList = sysDeptService.queryDetpIdList(deptId);
        if (deptList.size() > 0) {
            return R.error("请先删除子部门");
        }

        sysDeptService.delete(deptId);

        return R.ok();
    }
    @RequestMapping("/deptTreeDataByParentID/{parentID}")
//	@RequiresPermissions("sys:dept:list")
    public SysDeptEntity deptTreeDataByParentID(@PathVariable("parentID") long parentID) {
        List<SysDeptEntity> deptList = sysDeptService.getAllList();
        long srcDeptId = parentID;
        SysDeptEntity node = new SysDeptEntity();
        for (SysDeptEntity mu : deptList) {
            if (mu.getDeptId().equals(srcDeptId)) {
                node = mu;
                break;
            }

        }
        /*if (node != null && node.getParentId() > 0) {
            srcDeptId = node.getParentId();
        }*/
        node.setList(recursiveTree(deptList, srcDeptId));
        return node;
    }


    /**
     *  获得 激活、客服、大客户组部门及子部门所有id
     * @return
     */
    @RequestMapping("getCheckDept")
    public R getCheckDept(){
        Long jihuo = new Long(10);
        String deptListJ = sysDeptService.getSubDeptIdList(jihuo);  //得到激活部门及子部门所有id

        Long kefu = new Long(109);
        String deptListK = sysDeptService.getSubDeptIdList(kefu);  //得到客服部门及子部门所有id

        Long dakehu = new Long(150);
        String deptListD = sysDeptService.getSubDeptIdList(dakehu);  //得到大客户组部门及子部门所有id

        return R.ok().put("deptListJ", deptListJ).put("deptListK", deptListK).put("deptListD", deptListD);
    }


    @RequestMapping("getDeptAndUser")
    public R getDeptAndUser(){
        List<DeptAndUserDto> deptAndUserList = sysDeptService.getDeptAndUser();
        return R.ok().put("deptAndUserList", deptAndUserList);
    }

}
