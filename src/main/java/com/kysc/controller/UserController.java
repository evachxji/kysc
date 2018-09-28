package com.kysc.controller;


import com.kysc.bean.R;
import com.kysc.bean.User;
import com.kysc.service.UserService;
import com.kysc.utils.AccountValidatorUtil;
import com.kysc.utils.ErrorMsg;
import com.kysc.utils.SMS.RandomUtils;
import com.kysc.utils.SMSUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    //账号注册
    @PostMapping("/user")
    public R register(@RequestBody User user){
        if(AccountValidatorUtil.isPassword(user.getPassword()))     //检查密码
            return R.error(ErrorMsg.ERROR_MSG5.getCode(),ErrorMsg.ERROR_MSG5.getMsg());
        R r1 = checkUsername(user.getUsername());       //检查用户名
        R r2 = checkMobile(user.getMobile());           //检查手机号
        if(r1.isEmpty()){ //用户名、手机号合法
            if(r2.isEmpty()){
                String salt = new SecureRandomNumberGenerator().nextBytes().toString();
                user.setSalt(salt);
                user.setCreateTime(new Date());
                user.setLastTime(new Date());
                Md5Hash md5 = new Md5Hash(user.getPassword(),salt,2);   //加密2次
                user.setPassword(md5.toString());
                userService.regisger(user);
                return R.ok("注册成功");
            }else
                return r2;
        }else
            return r1;
    }

    //检查用户名
    @GetMapping("/user/{username}")
    public R checkUsername(@PathVariable("username") String username){      //检查用户名是否重复
        if(username != null && username != ""){         //为空
            //4到15位（字母，数字，下划线，减号）
            if(AccountValidatorUtil.isUsername(username)){     //用户名符合规范
                if(userService.checkUsername(username)>0)
                    return R.error(ErrorMsg.ERROR_MSG2.getCode(),ErrorMsg.ERROR_MSG2.getMsg());
                else
                    return R.ok();
            }else
                return R.error(ErrorMsg.ERROR_MSG1.getCode(),ErrorMsg.ERROR_MSG1.getMsg());
        }else
            return R.error(ErrorMsg.ERROR_MSG.getCode(),ErrorMsg.ERROR_MSG.getMsg());
    }


    //检查手机号
    public R checkMobile(String mobile){
        if(AccountValidatorUtil.isMobile(mobile)){
            int i = userService.checkMobile(mobile);       //手机已注册
            System.out.println(i);
            if(i>0){
                return R.error(ErrorMsg.ERROR_MSG4.getCode(),ErrorMsg.ERROR_MSG4.getMsg());
            }else
                return R.ok();
        }else
            return R.error(ErrorMsg.ERROR_MSG6.getCode(),ErrorMsg.ERROR_MSG6.getMsg());     //手机号不规范
    }


    //发送手机验证码
    @PostMapping(value = "/sms/{mobile}")
    public R sms(@PathVariable("mobile") String mobile){//发送验证短信
        R r = checkMobile(mobile);
        System.out.println(r.isEmpty());
        if(r.isEmpty()){
            //创建6位验证码
            String param = RandomUtils.getParam();
            SMSUtils.testSendSms(mobile, param);
            return R.ok(param);
        }else
            return r;
    }

}
