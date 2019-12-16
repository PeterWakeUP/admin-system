package com.evsoft.modules.sys.dto;

/**
 * @Description:
 * @CreatedDate:2018-05-09 15:45
 * @Author:wangzh
 */
public class SysUserDto {
    private Long userID;
    private Long deptID;
    private String userName;
    private String nickName;
    private String jobNum;
    private String email;
    private String mobile;

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getDeptID() {
        return deptID;
    }

    public void setDeptID(Long deptID) {
        this.deptID = deptID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getJobNum() {
        return jobNum;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
