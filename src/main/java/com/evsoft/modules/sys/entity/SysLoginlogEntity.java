package com.evsoft.modules.sys.entity;

import java.io.Serializable;
import java.util.Date;


public class SysLoginlogEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键***/
    private Long id;

    /*用户userid*/
    private Long userId;

    /***用户登录token**/
    private String token;

    /***创建时间***/
    private Date createTime;

    /***用户登录时间**/
    private Date loginTime;

    /***用户退出时间**/
    private Date logoutTime;

    /***用户登录的在线时长**/
    private Long onlineTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Date getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(Date logoutTime) {
        this.logoutTime = logoutTime;
    }

    public Long getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(Long onlineTime) {
        this.onlineTime = onlineTime;
    }
}
