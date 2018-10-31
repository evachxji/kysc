package com.kysc.controller;


import com.kysc.bean.IdentifyCode;
import com.kysc.bean.User;
import com.kysc.dto.IdentifyCodeDto;
import com.kysc.service.IdentifyCodeService;
import com.kysc.utils.R;
import com.kysc.service.UserService;
import com.kysc.utils.ErrorMsg;
import com.kysc.utils.FTPUtils;
import com.kysc.utils.SMS.RandomUtils;
import com.kysc.utils.Shiro.ShiroUtils;
import com.sun.xml.internal.bind.v2.TODO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value = "用户注册",notes = "用户注册")
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
        if(identifyCodeService.hasValidSmsCode(identifyCode) != 1){
            return R.error(ErrorMsg.ERROR_MSG11.getCode(),ErrorMsg.ERROR_MSG11.getMsg());
        }
        userService.regisger(user);
        //存入session
        ShiroUtils.setLoginUser(user);
        return R.ok();
    }

    /**
     * TODO 用户名是否存在
     * @param username
     * @return
     */
    @GetMapping("/user/{username}")
    @ApiOperation(value = "用户名存在",notes = "用户名存在")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "username",value = "用户名")
    })
    public R checkUsername(@PathVariable("username") String username) {
        if (userService.checkUsername(username) > 0) {
            return R.error(ErrorMsg.ERROR_MSG2.getCode(), ErrorMsg.ERROR_MSG2.getMsg());
        }
        return R.ok();
    }

    /**
     * TODO 验证码(注册时)
     * @param mobile
     * @return
     */
    @PostMapping("/sms/{mobile}")
    @ApiOperation(value = "手机验证码",notes = "手机验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "mobile",value = "手机号码")
    })
    public R sms(@PathVariable("mobile") String mobile){
        //手机号是否已被注册
        if(userService.checkMobile(mobile) != 0){
            return R.error(ErrorMsg.ERROR_MSG4.getCode(),ErrorMsg.ERROR_MSG4.getMsg());
        }
        //已发送且有效的短信(3分钟内不能再次请求验证码)
        if(identifyCodeService.hasValidSms(mobile) > 0){
            return R.error(ErrorMsg.ERROR_MSG10.getCode(),ErrorMsg.ERROR_MSG10.getMsg());
        }
        //创建6位验证码
        Integer param = RandomUtils.getParam();
        //发送验证码
        //SMSUtils.testSendSms(mobile, param);
        IdentifyCode identifyCode = new IdentifyCode();
        identifyCode.setMobile(mobile);
        identifyCode.setCode(param);
        //向数据库插入一条验证码信息
        identifyCodeService.insert(identifyCode);
        //向前台传验证码的id
        return R.ok().put("id",identifyCode.getId());
    }

    /**
     * TODO 头像上传/修改(待完善)
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/uploadImg")
    @ApiOperation(value = "头像上传",notes = "头像上传")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "File", name = "file",value = "图片文件")
    })
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
    @PostMapping("/login")
    @ApiOperation(value = "用户注册",notes = "用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "username",value = "用户名"),
            @ApiImplicitParam(dataType = "String", name = "password",value = "密码")
    })
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

    public R update(){
        return R.ok();
    }

    /**
     * TODO 请求重置密码(输入用户名)
     * @param username
     * @return
     */
    @GetMapping("/password/reset/request")
    @ApiOperation(value = "请求重置密码",notes = "请求重置密码")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "username",value = "用户名")
    })
    public R passwordReset1(String username){
        String mobile = userService.getMobileByUsername(username);
        //用户名不存在
        if(mobile == null){
            return R.error(ErrorMsg.ERROR_MSG12.getCode(),ErrorMsg.ERROR_MSG12.getMsg());
        }
        //已发送且有效的短信(3分钟内不能再次请求验证码)
        if(identifyCodeService.hasValidSms(mobile) > 0){
            return R.error(ErrorMsg.ERROR_MSG10.getCode(),ErrorMsg.ERROR_MSG10.getMsg());
        }
        //创建6位验证码
        Integer param = RandomUtils.getParam();
        //发送验证码
        //SMSUtils.testSendSms(mobile, param);
        IdentifyCode identifyCode = new IdentifyCode();
        identifyCode.setMobile(mobile);
        identifyCode.setCode(param);
        //向数据库插入一条验证码信息
        identifyCodeService.insert(identifyCode);
        //要找回的用户名，存入session中
        ShiroUtils.getSubject().getSession().setAttribute("requestUsername",username);
        //向前台传验证码的id
        return R.ok().put("id",identifyCode.getId());
    }

    /**
     * TODO 请求重置密码(确认验证码)
     * @param identifyCode
     * @return
     */
    @PostMapping("/password/reset/request")
    @ApiOperation(value = "请求重置密码",notes = "请求重置密码")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "Integer", name = "id",value = "验证码id"),
            @ApiImplicitParam(dataType = "Integer", name = "code",value = "验证码")
    })
    public R passwordReset2(IdentifyCode identifyCode){
        //验证码是否正确
        if(identifyCodeService.hasValidSmsCode(identifyCode) != 1){
            return R.error(ErrorMsg.ERROR_MSG11.getCode(),ErrorMsg.ERROR_MSG11.getMsg());
        }
        return R.ok();
    }

    /**
     * TODO 请求重置密码(输入新密码)(待完成)
     * @param password
     * @return
     */
    @PutMapping("/password/reset/request")
    @ApiOperation(value = "请求重置密码",notes = "请求重置密码")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "password",value = "新密码")
    })
    public R passwordReset3(String password){
        String username = (String) ShiroUtils.getSubject().getSession().getAttribute("requestUsername");
        return R.ok();
    }

    /**
     * TODO 注销登录
     * @return
     */
    @GetMapping("/logout")
    public R logout(){
        ShiroUtils.getSubject().logout();
        return R.ok();
    }
}
