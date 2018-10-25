package com.kysc.controller;


import com.kysc.bean.IdentifyCode;
import com.kysc.bean.User;
import com.kysc.dto.IdentifyCodeDto;
import com.kysc.service.IdentifyCodeService;
import com.kysc.utils.R;
import com.kysc.service.UserService;
import com.kysc.utils.AccountValidatorUtil;
import com.kysc.utils.ErrorMsg;
import com.kysc.utils.FTPUtils;
import com.kysc.utils.SMS.RandomUtils;
import com.kysc.utils.Shiro.ShiroUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 用户
 *
 * @author yejuncheng
 * @date 2018/10/25 9:40
 */
@RestController
@RequestMapping("/user")
@Api(description = "用户接口",tags = {"用户"})
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 验证码
     */
    @Autowired
    IdentifyCodeService identifyCodeService;

    /**
     * TODO 账号注册
     * @param identifyCodeDto
     * @return
     */
    @PostMapping(value = "/user", consumes = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "username",value = "用户名"),
            @ApiImplicitParam(dataType = "String", name = "password",value = "密码"),
            @ApiImplicitParam(dataType = "String", name = "mobile",value = "手机号码"),

            @ApiImplicitParam(dataType = "Integer", name = "id",value = "验证码id"),
            @ApiImplicitParam(dataType = "String", name = "mobile",value = "手机号码"),
            @ApiImplicitParam(dataType = "Integer", name = "code",value = "验证码")

    })
    public R register(@RequestBody IdentifyCodeDto identifyCodeDto){
        User user = identifyCodeDto.getUser();
        IdentifyCode identifyCode = identifyCodeDto.getIdentifyCode();
        //验证码是否正确
        if(identifyCodeService.hasValidSmsCode(identifyCode) == 1){
            userService.regisger(user);
            ShiroUtils.setLoginUser(user);
            return R.ok("注册成功");
        }else{
            return R.error(ErrorMsg.ERROR_MSG11.getCode(),ErrorMsg.ERROR_MSG11.getMsg());
        }
    }

    /**
     * TODO 用户名是否存在
     * @param username
     * @return
     */
    @GetMapping("/user/{username}")
    public R checkUsername(@PathVariable("username") String username) {
        if (userService.checkUsername(username) > 0) {
            return R.error(ErrorMsg.ERROR_MSG2.getCode(), ErrorMsg.ERROR_MSG2.getMsg());
        }
        return R.ok();
    }

    /**
     * TODO 发送手机验证码
     * @param mobile
     * @return
     */
    @PostMapping("/sms/{mobile}")
    public R sms(@PathVariable("mobile") String mobile){
        //该账号未被注册
        if(userService.checkMobile(mobile) == 0){
            //已发送且有效的短信
            if(identifyCodeService.hasValidSms(mobile) > 0){
                return R.error(ErrorMsg.ERROR_MSG10.getCode(),ErrorMsg.ERROR_MSG10.getMsg());
            }else{
                //创建6位验证码
                Integer param = RandomUtils.getParam();
                //SMSUtils.testSendSms(mobile, param); 发送验证码
                IdentifyCode identifyCode = new IdentifyCode();
                identifyCode.setMobile(mobile);
                identifyCode.setCode(param);
                //向数据库插入一条验证码信息
                identifyCodeService.insert(identifyCode);
                //向前台传验证码的id
                return R.ok().put("id",identifyCode.getId());
            }
        }else{
            return R.error(ErrorMsg.ERROR_MSG4.getCode(),ErrorMsg.ERROR_MSG4.getMsg());
        }
    }

    /**
     * TODO 头像上传接口
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/uploadImg")
    public R uploadImg(MultipartFile file) throws IOException {
        R r = FTPUtils.MultipartFiletoFile(file);
        File file1 = (File) r.get("file");
        //转换成file失败，比如后缀不通过，或创建file失败，或文件大小非法
        if(file1 == null)   {
            return r;
        }
        else{
            FTPUtils.Upload(file1);
        }
        return R.ok();
    }

    /**
     * TODO 登录
     * @param user
     * @return
     */
    @PostMapping("login")
    public R login(User user){
        try{
            UsernamePasswordToken token =
                    new UsernamePasswordToken(user.getUsername(),user.getPassword());
            Subject subject = ShiroUtils.getSubject();
            subject.login(token);
            ShiroUtils.setLoginUser(user);
        }catch (UnknownAccountException e) {
            return R.error(e.getMessage());
        }catch (IncorrectCredentialsException e) {
            return R.error(e.getMessage());
        }catch (LockedAccountException e) {
            return R.error(e.getMessage());
        }catch (AuthenticationException e) {
            return R.error("账户验证失败");
        }
        return R.ok().put("data",user);
    }

}
