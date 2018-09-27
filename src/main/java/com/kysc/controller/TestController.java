package com.kysc.controller;

import com.kysc.bean.R;
import com.kysc.service.TestService;
import com.kysc.utils.SMS.RandomUtils;
import com.kysc.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    TestService testService;

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public R test(){
        return R.ok(testService.test());
    }


}
