package com.kysc.controller;

import com.kysc.bean.R;
import com.kysc.service.TestService;
import com.kysc.utils.SMS.RandomUtils;
import com.kysc.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    @RequestMapping(value = "/sms",method = RequestMethod.POST)
    public R test(String mobile){
        //创建6位验证码
        String param = RandomUtils.getParam();
        SMSUtils.testSendSms(mobile, param);
        return R.ok(param);
    }
}
