package com.kysc.api;

import com.kysc.utils.R;
import com.kysc.bean.User;
import com.kysc.service.UserService;
import com.kysc.utils.ErrorMsg;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Api("用户管理接口")
public class ApiUserController {

    @Autowired
    UserService userService;

    @PostMapping("/user")
    @ApiOperation(value = "账号注册", notes = "后台检查用户名密码手机号")
    public R register(@RequestBody User user){
        /*if(user.getPassword().length()<5 && user.getPassword().length()>15)     //检查密码
            return R.error(ErrorMsg.ERROR_MSG5.getCode(),ErrorMsg.ERROR_MSG5.getMsg());
        R r1 = checkUsername(user.getUsername());       //检查用户名
        R r2 = checkMobile(user.getMobile());           //检查手机号
        if(r1.get("code").equals(0)){ //用户名、手机号合法
            if(r2.get("code").equals(0)){
                String salt = new SecureRandomNumberGenerator().nextBytes().toString();
                user.setSalt(salt);
                user.setCreateTime(new Date());
                user.setLastTime(new Date());
                Md5Hash md5 = new Md5Hash(user.getPassword(),salt,2);   //加密2次
                user.setPassword(md5.toString());
                userService.regisger(user);*/
                return R.ok("注册成功");
        /*    }else
                return r2;
        }else
            return r1;*/
    }


    @GetMapping("/user/{username}")
    @ApiOperation(value = "检查用户名")
    public R checkUsername(@PathVariable("username") String username){      //检查用户名是否重复
        /*if(username != null && username != ""){         //为空
            //4到15位（字母，数字，下划线，减号）
            if(Pattern.matches("^[a-zA-Z0-9_-]{4,15}$", username)){     //用户名符合规范
                if(userService.checkUsername(username)>0)
                    return R.error(ErrorMsg.ERROR_MSG2.getCode(),ErrorMsg.ERROR_MSG2.getMsg());
                else
                    return R.ok();
            }else
                return R.error(ErrorMsg.ERROR_MSG1.getCode(),ErrorMsg.ERROR_MSG1.getMsg());
        }else*/
            return R.error(ErrorMsg.ERROR_MSG.getCode(),ErrorMsg.ERROR_MSG.getMsg());
    }



    //检查手机号
    public R checkMobile(@PathVariable("mobile") String mobile){
        /*if(Pattern.matches("^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$", mobile)){
            int i = userService.checkMobile(mobile);       //手机已注册
            if(i>0){
                return R.error(ErrorMsg.ERROR_MSG4.getCode(),ErrorMsg.ERROR_MSG4.getMsg());
            }else
                return R.ok();
        }else*/
            return R.error(ErrorMsg.ERROR_MSG6.getCode(),ErrorMsg.ERROR_MSG6.getMsg());     //手机号不规范
    }


    //发送手机验证码
    @PostMapping(value = "/sms/{mobile}")
    @ApiOperation(value = "发送手机验证码")
    public R sms(@PathVariable("mobile") String mobile){//发送验证短信
        R r = checkMobile(mobile);
        /*if(r.get("code").equals(0)){
            //创建6位验证码
            String param = RandomUtils.getParam();
            SMSUtils.testSendSms(mobile, param);
            return R.ok(param);
        }else*/
            return r;
    }


}
