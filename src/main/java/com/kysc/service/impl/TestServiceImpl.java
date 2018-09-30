package com.kysc.service.impl;

import com.kysc.service.TestService;
import org.springframework.stereotype.Service;

@Service("testService")
public class TestServiceImpl implements TestService {


    @Override
    public String test() {
        return "index.html";
    }
}
