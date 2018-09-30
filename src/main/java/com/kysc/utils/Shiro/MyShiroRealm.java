package com.kysc.utils.Shiro;

import com.kysc.bean.User;
import com.kysc.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;


    //用来授权
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
//        String username = (String) principalCollection.getPrimaryPrincipal();   //获取用户名
//        Set<String> roles = getRolesByUserName(username);                   //获取角色
//        Set<String> permissions = getPermissionsByUserName(username);       //获取权限
//        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();       //创建返回对象
//        info.setRoles(roles);                                               //设置角色
//        info.setStringPermissions(permissions);                             //设置权限
//        return info;                                                        //返回
        return null;
    }



    //用来认证
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authenticationToken) throws AuthenticationException {

        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        //String username = (String) authenticationToken.getPrincipal();
        String username = token.getUsername();
        String password = String.valueOf(token.getPassword());  //提交的号码密码

        User user = userService.queryByUserName(username);

        //账号不存在
        if(user == null) {
            throw new UnknownAccountException("账号或密码不正确");
        }

        //密码错误
        if(!password.equals(user.getPassword())) {
            throw new IncorrectCredentialsException("账号或密码不正确");
        }



        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, getName());
        return info;
    }

}
