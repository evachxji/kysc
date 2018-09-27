package com.kysc.service;

import com.kysc.bean.User;

public interface UserService {

    User regisger(User user);

    int checkUsername(String username);

    int checkMobile(String mobile);
}
