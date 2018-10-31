package com.kysc.service.impl;

import com.kysc.bean.User;
import com.kysc.dao.UserMapper;
import com.kysc.service.UserService;
import com.kysc.utils.Shiro.ShiroUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 用户实现
 *
 * @author yejuncheng
 * @date 2018/10/25 11:34
 */
@Service("userService")
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public User regisger(User user) {
        //生成盐
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        user.setSalt(salt);
        user.setCreateTime(new Date());
        user.setLastTime(new Date());
        user.setStatus((short)0);
        //md5加盐加密2次
        Md5Hash md5 = new Md5Hash(user.getPassword(),salt,2);
        user.setPassword(md5.toString());
        //用户名、盐、加盐加密密码、创建时间、最后登陆时间、状态
        userMapper.insert(user);
        return user;
    }

    @Override
    public int checkUsername(String username) {
        return userMapper.checkUsername(username);
    }

    @Override
    public int checkMobile(String mobile) {
        return userMapper.checkMobile(mobile);
    }

    @Override
    public User queryByUserName(String username) {
        return userMapper.queryByUserName(username);
    }

    @Override
    public String getMobileByUsername(String username) {
        return userMapper.getMobileByUsername(username);
    }

    @Override
    public void updatePasswordByUsername(String username, String password) {
        User user = userMapper.queryByUserName(username);
        Md5Hash md5 = new Md5Hash(password,user.getSalt(),2);
        user.setPassword(md5.toString());
        userMapper.updateByPrimaryKey(user);
    }

    @Override
    public void updateSelect(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

}
