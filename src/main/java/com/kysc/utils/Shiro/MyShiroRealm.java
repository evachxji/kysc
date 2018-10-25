package com.kysc.utils.Shiro;

import com.kysc.bean.User;
import com.kysc.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;


    //用来授权
    @Override
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


    /**
     * TODO 认证登录
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        //获取用户名、密码
        String username = token.getUsername();
        String password = String.valueOf(token.getPassword());

        //从数据库拿到User
        User user = userService.queryByUserName(username);
        //返回的password要与传入的password一致（shiro认为返回的password是正确密码，并与传入的password进行比对）
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, getName());
        //原始密码加盐加密
        Md5Hash md5 = new Md5Hash(password,user.getSalt(),2);
        password = md5.toString();
        //账号不存在
        if(user == null) {
            throw new UnknownAccountException("账号或密码不正确");
        }
        //密码错误
        if(!password.equals(user.getPassword())) {
            throw new IncorrectCredentialsException("账号或密码不正确");
        }
        //账号锁定
        /*if (user.getStatus()==1) {
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }*/
        return info;
    }

}
