package com.kysc.controller;


import com.kysc.bean.IdentifyCode;
import com.kysc.bean.UserVo;
import com.kysc.service.IdentifyCodeService;
import com.kysc.utils.R;
import com.kysc.bean.User;
import com.kysc.service.UserService;
import com.kysc.utils.AccountValidatorUtil;
import com.kysc.utils.ErrorMsg;
import com.kysc.utils.FTPUtils;
import com.kysc.utils.SMS.RandomUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    IdentifyCodeService identifyCodeService;

    //账号注册
    @PostMapping(value = "/user", consumes = "application/json")
    public R register(@RequestBody UserVo user){
        if(user.getUsername() == null || user.getPassword() == null || user.getMobile() == null ||
                user.getUsername() == "" || user.getPassword() == "" || user.getMobile() == "" ){
           return R.error(ErrorMsg.ERROR_MSG9.getCode(),ErrorMsg.ERROR_MSG9.getMsg());
        }
        if(!AccountValidatorUtil.isPassword(user.getPassword()))     //检查密码规范
            return R.error(ErrorMsg.ERROR_MSG5.getCode(),ErrorMsg.ERROR_MSG5.getMsg());
        R r1 = checkUsername(user.getUsername());       //检查用户名
        R r2 = checkMobile(user.getMobile());           //检查手机号
        if(r1.get("code").equals(0)){ //用户名、手机号合法
            if(r2.get("code").equals(0)){
                IdentifyCode identifyCode = new IdentifyCode(user.getId(),user.getMobile(),user.getCode());
                if(identifyCodeService.hasValidSmsCode(identifyCode) == 1){
                    String salt = new SecureRandomNumberGenerator().nextBytes().toString();
                    user.setSalt(salt);
                    user.setCreateTime(new Date());
                    user.setLastTime(new Date());
                    Md5Hash md5 = new Md5Hash(user.getPassword(),salt,2);   //加密2次
                    user.setPassword(md5.toString());
                    userService.regisger(user);
                    return R.ok("注册成功");
                }else
                    return R.error(ErrorMsg.ERROR_MSG11.getCode(),ErrorMsg.ERROR_MSG11.getMsg());
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
            if(i>0){
                return R.error(ErrorMsg.ERROR_MSG4.getCode(),ErrorMsg.ERROR_MSG4.getMsg());
            }else
                return R.ok();
        }else
            return R.error(ErrorMsg.ERROR_MSG6.getCode(),ErrorMsg.ERROR_MSG6.getMsg());     //手机号不规范
    }


    //发送手机验证码
    @PostMapping("/sms/{mobile}")
    public R sms(@PathVariable("mobile") String mobile){//发送验证短信
        R r = checkMobile(mobile);
        if(r.get("code").equals(0)){        //该账号未被注册
            int count = identifyCodeService.hasValidSms(mobile);     //已发送且有效的短信
            if(count>0){
                return R.error(ErrorMsg.ERROR_MSG10.getCode(),ErrorMsg.ERROR_MSG10.getMsg());
            }else{
                //创建6位验证码
                String param = RandomUtils.getParam();
                //SMSUtils.testSendSms(mobile, param);
                IdentifyCode identifyCode = new IdentifyCode(mobile,Integer.valueOf(param));
                identifyCodeService.insert(identifyCode);
                return R.ok().put("id",identifyCode.getId());
            }
        }else
            return r;
    }

    @PostMapping("/uploadImg")
    public R uploadImg(MultipartFile file) throws IOException {

        R r = FTPUtils.MultipartFiletoFile(file);
        File file1 = (File) r.get("file");
        if(file1 == null)    //转换成file失败，比如后缀不通过，或创建file失败，或文件大小非法
            return r;
        else{
            FTPUtils.Upload(file1);
        }
        return R.ok();
    }

    @PostMapping("login")
    public R login(@RequestBody User user){
        return null;
    }

}
