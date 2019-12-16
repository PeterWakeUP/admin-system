package com.evsoft.modules.sys.service.impl;

import com.evsoft.common.exception.RRException;
import com.evsoft.modules.sys.dao.SysDictionaryDao;
import com.evsoft.modules.sys.entity.SysDictionaryEntity;
import com.evsoft.modules.sys.service.SysDitionaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lgs on 2018/4/11.
 */
@Service
public class SysDitionaryServiceImpl implements SysDitionaryService {

    private static final Logger logger = LoggerFactory.getLogger(SysDitionaryServiceImpl.class);

    @Autowired
    private SysDictionaryDao sysDictionaryDao;

    @Override
    public List<SysDictionaryEntity> getDictionaryDetail(Map<String, Object> map) {
        return sysDictionaryDao.queryList(map);
    }

    @Override
    public List<SysDictionaryEntity> getAllDictionary(Map<String, Object> map) {
        return sysDictionaryDao.queryAllDictionary(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return sysDictionaryDao.queryTotal(map);
    }

    @Override
    public int getTotalByPid(Map<String, Object> map) {
        return sysDictionaryDao.queryTotalByPid(map);
    }

    @Override
    public void saveDictionary(SysDictionaryEntity sysDictionaryEntity) {
        //判断字典类型是否存在
        HashMap<String,Object> map=new HashMap<String,Object>();
        map.put("dicKey",sysDictionaryEntity.getDicKey());
        int result = sysDictionaryDao.getCount(map);
        if(result>0){
            logger.error("新增字典失败 {},该字典类型已存在,result="+result);
            throw new RRException("新增失败,字典key已存在");
        }
        try {
            int count = sysDictionaryDao.saveDictionary(sysDictionaryEntity);
            if (count>0){
                logger.info("保存字典信息成功 {}", sysDictionaryEntity.toString());
            }
        } catch (Exception e) {
            logger.error("保存字典信息失败 {}", sysDictionaryEntity.toString());
            logger.error("保存字典信息失败 {}", e.getMessage());
        }
    }

    @Override
    public void deleteDictionary(Integer[] ids) {

        sysDictionaryDao.deleteDictionary(ids);

    }

    @Override
    public void updateDictionary(SysDictionaryEntity sysDictionaryEntity) {
        try {
            int count = sysDictionaryDao.updateDictionary(sysDictionaryEntity);
            if (count>0){
                logger.info("修改字典信息成功 {0}", sysDictionaryEntity.toString());
            }
        } catch (Exception e) {
            logger.error("修改字典信息失败 {}", sysDictionaryEntity.toString());
            logger.error("修改字典信息失败 {}", e.getMessage());
        }
    }

    @Override
    public void saveDictionaryDetail(SysDictionaryEntity sysDictionaryEntity) {
        //判断字典值是否存在
        HashMap<String,Object> map=new HashMap<String,Object>();
        map.put("pid",sysDictionaryEntity.getPid());
        map.put("dicValue",sysDictionaryEntity.getDicValue());
        int result = sysDictionaryDao.getCount(map);
        if(result>0){
            logger.error("新增字典明细失败 {0},该字典值已存在,result="+result);
            throw new RRException("新增失败,字典值已存在");
        }
        try {
            int count = sysDictionaryDao.saveDictionaryDetail(sysDictionaryEntity);
            if (count>0){
                logger.info("新增字典明细成功 {}", sysDictionaryEntity.toString());
            }
        } catch (Exception e) {
            logger.error("新增字典明细失败 {}", sysDictionaryEntity.toString());
            logger.error("新增字典明细失败 {}", e.getMessage());
        }
    }

    @Override
    public void updateDictionaryDetail(SysDictionaryEntity sysDictionaryEntity) {
        try {
            int count = sysDictionaryDao.updateDictionaryDetail(sysDictionaryEntity);
            if (count>0){
                logger.info("修改字典明细信息成功 {0}", sysDictionaryEntity.toString());
            }
        } catch (Exception e) {
            logger.error("修改字典明细信息失败 {0}", sysDictionaryEntity.toString());
            logger.error("修改字典明细信息失败 {}", e.getMessage());
        }
    }

    @Override
    public void deleteDictionaryDetail(Integer[] ids) {
        sysDictionaryDao.deleteDictionary(ids);
    }

    @Override
    public List<SysDictionaryEntity> getValueByKey(String dicKey) {
        return sysDictionaryDao.getValueByKey(dicKey);
    }

    /*
     * 根据键值获取字段属性值和文本
     * created by wangzh 2018/05/03
     * */
    @Override
    public List<SysDictionaryEntity> getDictNameValueByKey(String dictKey) {
        return sysDictionaryDao.getDictNameValueByKey(dictKey);
    }
}
