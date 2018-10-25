package com.kysc.dto;

import com.kysc.bean.IdentifyCode;
import com.kysc.bean.User;

/**
 * 验证码+用户
 *
 * @author yejuncheng
 * @date 2018/10/25 10:27
 */
public class IdentifyCodeDto {

    private User user;

    private IdentifyCode identifyCode;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public IdentifyCode getIdentifyCode() {
        return identifyCode;
    }

    public void setIdentifyCode(IdentifyCode identifyCode) {
        this.identifyCode = identifyCode;
    }
}
