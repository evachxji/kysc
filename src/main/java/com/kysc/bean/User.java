package com.kysc.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;


@ApiModel(description = "用户类")
public class User {
    
     private Integer userId;

     @ApiModelProperty("用户名")
     private String username;
     @ApiModelProperty("密码")
     private String password;

     private String salt;
     @ApiModelProperty("手机号")
     private String mobile;

     private Date createTime;

     private Date lastTime;


    public Integer getUserId() {
        return userId;
    }


    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }


    public String getSalt() {
        return salt;
    }


    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }


    public String getMobile() {
        return mobile;
    }


    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }


    public Date getCreateTime() {
        return createTime;
    }


    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public Date getLastTime() {
        return lastTime;
    }


    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }
}