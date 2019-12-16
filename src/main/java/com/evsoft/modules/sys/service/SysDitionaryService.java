package com.evsoft.modules.sys.service;

import com.evsoft.modules.sys.entity.SysDictionaryEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by lgs on 2018/4/11.
 */
public interface SysDitionaryService {

    /**
     * 根据字典类型，获取字典明细信息
     *
     * @param id
     * @return
     */
    List<SysDictionaryEntity> getDictionaryDetail(Map<String, Object> map);


    /**
     * 获取所有字典信息
     *
     * @param dicKey
     * @return
     */
    List<SysDictionaryEntity> getAllDictionary(Map<String, Object> map);

    /**
     * 根据条件查询字典总记录
     *
     * @param map
     * @return
     */
    int queryTotal(Map<String, Object> map);

    /**
     * 根据pid查询某字典详细数据
     *
     * @param map
     * @return
     */
    int getTotalByPid(Map<String, Object> map);

    /**
     * 新增字典
     *
     * @param sysDictionaryEntity
     */
    void saveDictionary(SysDictionaryEntity sysDictionaryEntity);

    /**
     * 逻辑删除字典
     *
     * @param ids
     */
    void deleteDictionary(Integer[] ids);

    /**
     * 更新字典
     *
     * @param sysDictionaryEntity
     */
    void updateDictionary(SysDictionaryEntity sysDictionaryEntity);


    /**
     * 新增XX字典明细
     *
     * @param sysDictionaryEntity
     */
    void saveDictionaryDetail(SysDictionaryEntity sysDictionaryEntity);

    /**
     * 修改XX字典明细
     *
     * @param sysDictionaryEntity
     */
    void updateDictionaryDetail(SysDictionaryEntity sysDictionaryEntity);

    /**
     * 逻辑删除XX字典明细
     *
     * @param ids
     */
    void deleteDictionaryDetail(Integer[] ids);

    List<SysDictionaryEntity> getValueByKey(String dicKey);

    /*
     * 根据键值获取字段属性值和文本
     * created by wangzh 2018/05/03
     * */
    List<SysDictionaryEntity> getDictNameValueByKey(String dictKey);
}
