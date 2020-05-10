package com.ximen.plan.realm;

import com.ximen.plan.entity.SysUser;
import com.ximen.plan.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ZhiShun.Cai
 * @date 2020/1/1 14:43
 * @note
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1.获取登录的用户名密码（token）
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();
        String password = new String(upToken.getPassword());
        //2.根据用户名查询数据库
        SysUser user = userService.findByName(username);
        if (user == null) {
            throw new UnknownAccountException();
        }
        //3.判断用户是否存在或者密码是否一致
        //4.如果一致返回安全数据
        //构造方法：安全数据，密码，realm域名
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), this.getName());
        return info;
    }
}
