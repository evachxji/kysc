package com.kysc.bean;

import java.util.Date;

public class IdentifyCode {
    private Integer id;

    private Integer userId;

    private String mobile;

    private Integer code;

    private Date createTime;

    private Date endTime;

    public IdentifyCode() {
    }

    public IdentifyCode(String mobile, Integer code) {
        this.mobile = mobile;
        this.code = code;
    }

    public IdentifyCode(String mobile, Integer code, Date createTime, Date endTime) {
        this.mobile = mobile;
        this.code = code;
        this.createTime = createTime;
        this.endTime = endTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getmobile() {
        return mobile;
    }

    public void setmobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}