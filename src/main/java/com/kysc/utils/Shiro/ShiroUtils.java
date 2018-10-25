package com.kysc.utils.Shiro;

import com.kysc.bean.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * shiro工具类
 *
 * @author yejuncheng
 * @date 2018/10/25 10:40
 */
public class ShiroUtils {

    public static final String LOGIN_USER = "LOGIN_USER";

    public static Subject getSubject(){
        return SecurityUtils.getSubject();
    }

    public static Session getSession(){
        return SecurityUtils.getSubject().getSession();
    }

    public static void setLoginUser(User user){
        SecurityUtils.getSubject().getSession().setAttribute(LOGIN_USER,user);
    }

    public static User getLoginUser(){
        Object loginUser = SecurityUtils.getSubject().getSession().getAttribute(LOGIN_USER);
        if(loginUser!=null){
            return (User)loginUser;
        }else{
            return null;
        }
    }
}
