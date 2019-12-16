package com.evsoft.modules.sys.entity;


import java.io.Serializable;

public class SysTableEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    //表格标识（格式为sys:user:list，sys为模块名，user为菜单名，list为前端表格ID）
    private String tableKey;

    //表格列是否显示的json字符串
    private String displayInfo;

    //创建人ID
    private Long createUser;

    //创建时间
    private String createTime;

    //更新人
    private Long updateUser;

    //更新时间
    private String updateTime;

    public String getTableKey() {
        return tableKey;
    }

    public void setTableKey(String tableKey) {
        this.tableKey = tableKey;
    }

    public String getDisplayInfo() {
        return displayInfo;
    }

    public void setDisplayInfo(String displayInfo) {
        this.displayInfo = displayInfo;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

}
