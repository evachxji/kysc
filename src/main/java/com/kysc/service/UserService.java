package com.kysc.service;

import com.kysc.bean.User;

/**
 * 用户接口
 *
 * @author yejuncheng
 * @date 2018/10/25 9:50
 */
public interface UserService {

    /**
     * 新建用户
     * @param user
     * @return
     */
    User regisger(User user);

    /**
     * 检查用户名
     * @param username
     * @return
     */
    int checkUsername(String username);

    /**
     * 检查手机号码是否被注册
     * @param mobile
     * @return
     */
    int checkMobile(String mobile);

    /**
     * 登录  凭用户名获取User
     * @param username
     * @return
     */
    User queryByUserName(String username);
}
