package com.kysc.service;

import com.kysc.bean.User;
import com.kysc.dao.TestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("testService")
public class TestServiceImpl implements TestService {

    @Autowired
    private TestDao testDao;

    @Override
    public String test() {
        User user = testDao.test();
        System.out.println(user.getName());
        return "index.html";
    }
}
