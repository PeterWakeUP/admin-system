package com.evsoft.modules.sys.controller;

import com.evsoft.common.annotation.SysLog;
import com.evsoft.common.utils.PageUtils;
import com.evsoft.common.utils.Query;
import com.evsoft.common.utils.R;
import com.evsoft.modules.comm.controller.AbstractController;
import com.evsoft.modules.sys.entity.SysDictionaryEntity;
import com.evsoft.modules.sys.service.SysDitionaryService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by lgs on 2018/4/12.
 */

@Controller
@RequestMapping("dictionary")
public class SysDictionaryController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(SysDictionaryController.class);

    @Autowired
    private SysDitionaryService sysDitionaryService;

    /**
     * 所有字典列表
     */
    @RequestMapping("/list")
    @ResponseBody
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        List<SysDictionaryEntity> projectList = sysDitionaryService.getAllDictionary(query);
        int total = sysDitionaryService.queryTotal(query);
        PageUtils pageUtil = new PageUtils(projectList, total, query.getLimit(), query.getPage());
        return R.ok().put("page", pageUtil);
    }

    /**
     * 保存字典
     */
    @SysLog("保存字典")
    @RequestMapping("/save")
    @ResponseBody
    @RequiresPermissions("sys:dictionary:save")
    public R save(@RequestBody SysDictionaryEntity sysDictionaryEntity) {
        sysDictionaryEntity.setUserId(getUserId());
        sysDictionaryEntity.setFlag(0);
        sysDictionaryEntity.setPid(0);
        try {
            sysDitionaryService.saveDictionary(sysDictionaryEntity);
            return R.ok().put("msg", "保存成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return R.error().put("msg", e.getMessage());
        }
    }


    /**
     * 逻辑删除选中字典
     */
    @SysLog("删除字典")
    @RequestMapping("/delete")
    @ResponseBody
    @RequiresPermissions("sys:dictionary:delete")
    public R delete(@RequestBody Integer[] projectIds) {
        sysDitionaryService.deleteDictionary(projectIds);
        return R.ok();
    }

    /**
     * 修改字典
     */
    @SysLog("修改字典")
    @RequestMapping("/update")
    @ResponseBody
    @RequiresPermissions("sys:dictionary:update")
    public R update(@RequestBody SysDictionaryEntity sysDictionaryEntity) {
        sysDitionaryService.updateDictionary(sysDictionaryEntity);
        return R.ok();
    }

    @SysLog("查询字典配置列表")
    @RequestMapping("/dictionaryDetail/list")
    @ResponseBody
    @RequiresPermissions("sys:dictionary:operate")
    public R dictionaryDetail(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        List<SysDictionaryEntity> projectList = sysDitionaryService.getDictionaryDetail(query);
        int total = sysDitionaryService.getTotalByPid(query);
        PageUtils pageUtil = new PageUtils(projectList, total, query.getLimit(), query.getPage());
        return R.ok().put("page", pageUtil);
    }

    @SysLog("保存字典明细")
    @RequestMapping("/dictionaryDetail/save")
    @ResponseBody
    /*@RequiresPermissions("dictionary:delete")*/
    public R dictionaryDetailSave(@RequestBody SysDictionaryEntity sysDictionaryEntity) {
        try {
            sysDictionaryEntity.setUserId(getUserId());
            if(sysDictionaryEntity.getDicSort() == null || "".equals(sysDictionaryEntity.getDicSort())){
                sysDictionaryEntity.setDicSort("0");
            }
            sysDictionaryEntity.setFlag(0);
            sysDitionaryService.saveDictionaryDetail(sysDictionaryEntity);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return R.error().put("msg", e.getMessage());
        }
        return R.ok().put("msg", "保存成功");
    }

    @SysLog("更新字典明细")
    @RequestMapping("/dictionaryDetail/update")
    @ResponseBody
    /*@RequiresPermissions("dictionary:delete")*/
    public R dictionaryDetailUpdate(@RequestBody SysDictionaryEntity sysDictionaryEntity) {
        sysDitionaryService.updateDictionaryDetail(sysDictionaryEntity);
        return R.ok().put("msg", "保存成功");
    }

    /**
     * 逻辑删除XX字典明细
     */
    @SysLog("删除字典明细")
    @RequestMapping("dictionaryDetail/delete")
    @ResponseBody
    /*@RequiresPermissions("dictionary:delete")*/
    public R dictionaryDetailDelete(@RequestBody Integer[] ids) {
        sysDitionaryService.deleteDictionaryDetail(ids);
        return R.ok();
    }


    @SysLog("查询某字典类型的列表")
    @RequestMapping("dictionaryDetail/getByKey/{dicKey}")
    @ResponseBody
    public R dictionaryGetValueByKey(@PathVariable("dicKey") String dicKey) {
        List<SysDictionaryEntity> list = sysDitionaryService.getValueByKey(dicKey);
        return R.ok().put("list", list);
    }


    //获取相关字典列表
    @RequestMapping("getBydicKey")
    @ResponseBody
    public R getBydicKey(@RequestParam(value = "dicKey") String dicKey){
        try{
            List<SysDictionaryEntity> list = sysDitionaryService.getValueByKey(dicKey);
            return R.ok().put(dicKey, list);
        }catch(Exception e){
            logger.error("查询字典报错",e);
        }
        return R.error();
    }

    /*
     * 字典值和文本集合
     * created by wangzh 2018/05/03
     * */
    @SysLog("查询某字典类型的列表")
    @GetMapping("dictionaryDetail/getDictNameValueByKey/{dictKey}")
    @ResponseBody
    public R getDictNameValueByKey(@PathVariable("dictKey") String dictKey) {
        List<SysDictionaryEntity> list = sysDitionaryService.getDictNameValueByKey(dictKey);
        if (list.isEmpty() || list.size() <= 0) return R.error("字典数据不存在");
        return R.ok().put("list", list);
    }

}
