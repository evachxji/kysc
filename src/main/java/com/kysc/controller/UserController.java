package com.kysc.controller;


import com.kysc.bean.IdentifyCode;
import com.kysc.bean.User;
import com.kysc.dto.IdentifyCodeDto;
import com.kysc.service.IdentifyCodeService;
import com.kysc.utils.R;
import com.kysc.service.UserService;
import com.kysc.utils.ErrorMsg;
import com.kysc.utils.SMS.RandomUtils;
import com.kysc.utils.SMS.SMSUtils;
import com.kysc.utils.Shiro.ShiroUtils;
import com.kysc.utils.upload.AliyunOSSClientUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

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
        //SMSUtils.testSendSms(mobile, param.toString());
        IdentifyCode identifyCode = new IdentifyCode();
        identifyCode.setMobile(mobile);
        identifyCode.setCode(param);
        //向数据库插入一条验证码信息
        identifyCodeService.insert(identifyCode);
        //向前台传验证码的id
        return R.ok().put("id",identifyCode.getId());
    }

    /**
     * TODO 请求重置密码(用户名 => 验证码)
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
        //SMSUtils.testSendSms(mobile, param.toString());
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
     * TODO 请求重置密码(验证码 => 重置)
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
     * TODO 请求重置密码(输入新密码)
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
        userService.updatePasswordByUsername(username,password);
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
            User correctUser = userService.queryByUserName(user.getUsername());
            user.setPassword(new Md5Hash(user.getPassword(),correctUser.getSalt(),2).toString());
            UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(),user.getPassword());
            Subject subject = ShiroUtils.getSubject();
            subject.login(token);
            //最后登录时间更新
            correctUser.setLastTime(new Date());
            userService.updateSelect(correctUser);
            //放入session
            ShiroUtils.setLoginUser(correctUser);
            return R.ok();
        }catch (UnknownAccountException e) {
            return R.error(e.getMessage());
        }catch (IncorrectCredentialsException e) {
            return R.error(e.getMessage());
        }catch (LockedAccountException e) {
            return R.error(e.getMessage());
        }catch (AuthenticationException e) {
            return R.error("账户验证失败");
        }
    }

    /**
     * TODO 头像上传/修改
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
        User user = ShiroUtils.getLoginUser();
        if(user.getImg() == null){
            String url = AliyunOSSClientUtil.upload(file,"tx");
            user.setImg(url);
            userService.updateSelect(user);
        }else{
            //先去OSS删除原有的头像
            AliyunOSSClientUtil.deleteFile("tx",user.getImg());
            String url = AliyunOSSClientUtil.upload(file,"tx");
            user.setImg(url);
            userService.updateSelect(user);
        }
        ShiroUtils.setLoginUser(user);
        return R.ok();
    }

    /**
     * TODO 修改密码
     * @param password
     * @param newPassword
     * @return
     */
    @PostMapping("/password/update")
    @ApiOperation(value = "修改密码",notes = "修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "password",value = "密码"),
            @ApiImplicitParam(dataType = "String", name = "newPassword",value = "新密码"),
    })
    public R passwordUpdate(String password,String newPassword){
        User user = ShiroUtils.getLoginUser();
        if(user.getPassword().equals(new Md5Hash(password,user.getSalt(),2).toString())){
            user.setPassword(new Md5Hash(newPassword,user.getSalt(),2).toString());
            userService.updateSelect(user);
            logout();
        }else{
            R.error(ErrorMsg.ERROR_MSG13.getCode(),ErrorMsg.ERROR_MSG13.getMsg());
        }
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

    /**
     * TODO 查看用户信息
     * @return
     */
    @GetMapping("/info")
    public R info(){
        User user = ShiroUtils.getLoginUser();
        return R.ok().put("data",user);
    }
}
