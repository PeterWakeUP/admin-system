package com.evsoft.modules.sys.controller;

import com.evsoft.common.annotation.SysLog;
import com.evsoft.common.utils.Constant;
import com.evsoft.common.utils.PageUtils;
import com.evsoft.common.utils.Query;
import com.evsoft.common.utils.R;
import com.evsoft.common.validator.Assert;
import com.evsoft.common.validator.ValidatorUtils;
import com.evsoft.common.validator.group.AddGroup;
import com.evsoft.common.validator.group.UpdateGroup;
import com.evsoft.modules.comm.controller.AbstractController;
import com.evsoft.modules.sys.dto.SysUserDto;
import com.evsoft.modules.sys.entity.SysUserEntity;
import com.evsoft.modules.sys.service.SysDeptService;
import com.evsoft.modules.sys.service.SysUserRoleService;
import com.evsoft.modules.sys.service.SysUserService;
import com.evsoft.modules.sys.utils.GetDeptTree;
import com.evsoft.modules.sys.utils.TokenDBMap;
import net.sf.json.JSONArray;
import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 * @date 2016年10月31日 上午10:40:10
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysDeptService sysDeptService;

    /**
     * 所有用户列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:user:list")
    public R list(@RequestParam Map<String, Object> params) {

        //只有超级管理员，才能查看所有管理员列表
        if (getUserId() == Constant.SUPER_ADMIN) {
            params.put("sysUserId", "sysUserId");
        }
        //得到部门id集合*
        Object deptIds = params.get("deptId");
        if(null!=deptIds && !"".equals(deptIds))
        {
            JSONArray jsonArray = JSONArray.fromObject(deptIds);
            params.put("deptId",jsonArray);
        }
        else{
            JSONArray array = JSONArray.fromObject(new GetDeptTree().getDeptIds(sysDeptService));
            params.put("deptId",array);
        }



        //查询列表数据
        Query query = new Query(params);
        List<SysUserEntity> userList = sysUserService.queryList(query);
        int total = sysUserService.queryTotal(query);
        PageUtils pageUtil = new PageUtils(userList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 获取登录的用户信息
     */
    @RequestMapping("/info")
    public R info() {
        SysUserEntity user = getUser();
        //获取用户所属的角色列表
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(user.getUserId());
        user.setRoleIdList(roleIdList);
        return R.ok().put("user", user);
    }

    /**
     * 修改登录用户密码
     */
    @SysLog("修改密码")
    @RequestMapping("/password")
    public R password(String password, String newPassword) {
        Assert.isBlank(newPassword, "新密码不为能空");
        //sha256加密
        password = new Sha256Hash(password, getUser().getSalt()).toHex();
        //sha256加密
        newPassword = new Sha256Hash(newPassword, getUser().getSalt()).toHex();

        //更新密码
        int count = sysUserService.updatePassword(getUserId(), password, newPassword);
        if (count == 0) {
            return R.error("原密码不正确");
        }

        return R.ok();
    }

    /**
     * 用户信息
     */
    @RequestMapping("/info/{userId}")
    @RequiresPermissions("sys:user:info")
    public R info(@PathVariable("userId") Long userId) {
        SysUserEntity user = sysUserService.queryObject(userId);

        //获取用户所属的角色列表
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
        user.setRoleIdList(roleIdList);
        return R.ok().put("user", user);
    }

    /*
     * 根据部门ID获取用户数据下拉列表
     * created by wangzh 2018/04/18
     * */
    @GetMapping("/getUserByDeptIdList")
    @RequiresPermissions("sys:user:getUserByDeptIdList")
    public R getUserByDeptIdList(@RequestParam("deptId") long deptId) {
        List<SysUserEntity> userList = sysUserService.getUserByDeptIdList(deptId);
        return R.ok().put("userList", userList);
    }

    /**
     * 获取会员等级数据
     */
    @RequestMapping("/getUserLevelList")
    //@RequiresPermissions("sys:user:getUserByDeptIdList")
    public R getUserLevelList(@RequestParam Map<String, Object> params) {
        String[] deptIds=new String[]{};
        java.lang.Object strDeptIds= params.get("deptIds");
        if(strDeptIds!=null&&!strDeptIds.toString().trim().equals(""))
            deptIds=strDeptIds.toString().split(",");
        params.put("deptIds",deptIds);
        List<SysUserEntity> userLevelList = sysUserService.getUserLevelList(params);
        return R.ok().put("userLevelList", userLevelList);
    }

    /*
     * 根据部门ID获取用户数据下拉列表（包括子孙部门）by xurulin
     * */
    @RequestMapping("/getAllUserByDeptId")
    public R getAllUserByDeptId(@RequestBody long[] deptIds) {
        List<SysUserEntity> userList = sysUserService.getAllUserByDeptId(deptIds);
        return R.ok().put("userList", userList);
    }

    /**
     * 保存用户
     */
    @SysLog("保存用户")
    @RequestMapping("/save")
    @RequiresPermissions("sys:user:save")
    public R save(@RequestBody SysUserEntity user) {
        ValidatorUtils.validateEntity(user, AddGroup.class);

        //查询用户在数据库是否已经存在，否则会出现重复
        SysUserEntity sysUser = sysUserService.queryByUserName(user.getUsername());
        if (null != sysUser) {
            return R.error().put("msg", "该账号已被用户“" + sysUser.getNickname() + "”注册,用户名为："+ sysUser.getUsername());
        }

        user.setCreateUserId(getUserId());
        sysUserService.save(user);

        return R.ok();
    }

    /**
     * 修改用户
     */
    @SysLog("修改用户")
    @RequestMapping("/update")
    @RequiresPermissions("sys:user:update")
    public R update(@RequestBody SysUserEntity user) {
        ValidatorUtils.validateEntity(user, UpdateGroup.class);


        user.setCreateUserId(getUserId());
        clearMoneyUserInfo(user.getUserId());
        sysUserService.update(user);
        return R.ok();
    }

    /**
     * 修改用户
     */
    @SysLog("重置用户密码")
    @RequestMapping("/resetPwd/{userId}")
    @RequiresPermissions("sys:user:resetpwd")
    public R resetPwd(@PathVariable("userId") Long userId) {
        SysUserEntity user = sysUserService.queryObject(userId);
        if (user == null) {
            return R.error("没有该用户！");
        }
        String newPassword = new Sha256Hash("888888", user.getSalt()).toHex();

        int iResult = sysUserService.resetUserPwd(userId, newPassword);
        return iResult > 0 ? R.ok() : R.error("重置密码失败！");
    }

    /**
     * 修改用户
     */
    @SysLog("修改用户基本信息")
    @RequestMapping("/updateBasicInfo")
    public R updateBasicInfo(@RequestBody SysUserEntity user) {
        ValidatorUtils.validateEntity(user, UpdateGroup.class);
        long userId = getUserId();
        if (user.getUserId() != null && user.getUserId().longValue() == userId) {
            sysUserService.updateBasicInfo(user);
            clearMoneyUserInfo(user.getUserId());
            return R.ok();
        } else {
            return R.error("修改的用户ID与当前用户不匹配");
        }
    }

    /**
     * 删除用户
     */
    @SysLog("删除用户")
    @RequestMapping("/delete")
    @RequiresPermissions("sys:user:delete")
    public R delete(@RequestBody Long[] userIds) {
        if (ArrayUtils.contains(userIds, 1L)) {
            return R.error("系统管理员不能删除");
        }

        if (ArrayUtils.contains(userIds, getUserId())) {
            return R.error("当前用户不能删除");
        }

        sysUserService.deleteBatch(userIds);

        return R.ok();
    }

    /**
     * 删除用户
     */
    @SysLog("解绑二次验证")
    @RequestMapping("/untieUser")
    @RequiresPermissions("sys:user:untie")
    public R untie(@RequestBody Long[] userIds) {
        if (ArrayUtils.contains(userIds, 1L)) {
            return R.error("系统管理员不能解绑");
        }

        if (ArrayUtils.contains(userIds, getUserId())) {
            return R.error("当前用户不能解绑");
        }

        sysUserService.batchUpdateUntieUser(userIds);

        return R.ok();
    }

    /**
     * 批量修改用户信息
     */
    @RequestMapping("/batchUpdateUser")
    @RequiresPermissions("sys:user:update")
    public R batchUpdateUser(@RequestBody List<SysUserEntity> list) {
        sysUserService.batchUpdateUser(list);
        return R.ok();
    }

    /**
     * 判断是否有查看部门的数据权限
     */
    @RequestMapping("/hasDeptRight")
    public R hasDeptRight()
    {
        List<Long> deptList=sysUserService.getDeptListByUserID(getUserId());
        if(deptList!=null&&deptList.size()>0)
            return R.ok();
        else
            return R.error();
    }

    /**
     * 通过操作人员类型查询所有人员
     */
    @RequestMapping("/getuserlistbyusertype/{opertype}")
    public R getSysUserListByUserType(@PathVariable("opertype") String operType) {
        List<SysUserEntity> sysUserList = sysUserService.getUserByOperUserTypeList(operType);
        return R.ok().put("sysUserList", sysUserList);
    }

    /**
     * 获取当前登录用户信息
     */
    @RequestMapping("/getCurrentUser")
    public R getCurrentUser() {
        return R.ok().put("user", getUser());
    }


    @RequestMapping("getAllUser")
    public R getAllUser(){
        List<SysUserDto> list = sysUserService.getAllUser();
        return R.ok().put("list", list);
    }


    /**
     * 返回当前用户的数据
     * @return
     */
    @RequestMapping("getUserIdAndDept")
    public R getUserIdAndDept(){
        return R.ok().put("user",getUser());
    }


    private void clearMoneyUserInfo(Long userId){
        TokenDBMap.userDBMap.remove(userId);
    }
}
