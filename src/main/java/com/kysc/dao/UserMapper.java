package com.kysc.dao;

import com.kysc.bean.User;

/**
 * 用户
 * 
 * @author yejuncheng
 * @date 2018/10/25 10:25
 */
public interface UserMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUsername(String username);

    int checkMobile(String mobile);

    /**
     * 登录，凭用户名得到User
     * @param username
     * @return
     */
    User queryByUserName(String username);
}