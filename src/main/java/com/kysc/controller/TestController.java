package com.kysc.controller;

import com.kysc.utils.R;
import com.kysc.bean.User;
import com.kysc.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    TestService testService;

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public R test(){
        return R.ok(testService.test());
    }



    @RequestMapping(value = "/t1",method = RequestMethod.POST)
    public R t1(@RequestBody User user){
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        return R.ok();
    }

}
