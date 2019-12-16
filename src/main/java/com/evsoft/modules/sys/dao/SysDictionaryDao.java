package com.evsoft.modules.sys.dao;

import com.evsoft.modules.sys.entity.SysDictionaryEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Created by liguisen on 2018/4/11.
 */
@Mapper
public interface SysDictionaryDao extends BaseDao<SysDictionaryEntity> {

    /**
     * 查询所有字典
     *
     * @param map
     * @return
     */
    List<SysDictionaryEntity> queryAllDictionary(Map<String, Object> map);


    /**
     * 新增字典
     *
     * @param sysDictionaryEntity
     */
    int saveDictionary(SysDictionaryEntity sysDictionaryEntity);

    /**
     * 删除字典
     *
     * @param dictionaryIds
     */
    void deleteDictionary(Integer[] dictionaryIds);

    /**
     * 更新字典
     *
     * @param sysDictionaryEntity
     * @return
     */
    int updateDictionary(SysDictionaryEntity sysDictionaryEntity);

    /**
     * 更新字典
     *
     * @param
     * @return
     */
    int queryTotalByPid(Map<String, Object> map);

    /**
     * 新增XX字典明细
     *
     * @param sysDictionaryEntity
     * @return
     */
    int saveDictionaryDetail(SysDictionaryEntity sysDictionaryEntity);

    /**
     * 修改XX字典明细信息
     *
     * @param sysDictionaryEntity
     * @return
     */
    int updateDictionaryDetail(SysDictionaryEntity sysDictionaryEntity);


    /**
     * 修改XX字典明细信息
     *
     * @param
     * @return
     */
    int getCount(Map<String, Object> map);

    List<SysDictionaryEntity> getValueByKey(String dicKey);

    /*
     * 根据键值获取字段属性值和文本
     * created by wangzh 2018/05/03
     * */
    List<SysDictionaryEntity> getDictNameValueByKey(String dictKey);


    /**
     * 根据条件，查询字典最大的值divValue
     * @param map
     * @return
     */
    int getMaxDicValue(Map<String, Object> map);

    /**
     * 根据条件，查询XX字典明细的pid
     * @param map
     * @return
     */
    int getPid(Map<String, Object> map);


}
