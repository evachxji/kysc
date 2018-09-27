package com.kysc.service;

import com.kysc.bean.User;
import com.kysc.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public User regisger(User user) {
        System.out.println(userMapper.insert(user));
        return null;
    }

    @Override
    public int checkUsername(String username) {
        return userMapper.checkUsername(username);
    }

    @Override
    public int checkMobile(String mobile) {
        return userMapper.checkMobile(mobile);
    }

}
