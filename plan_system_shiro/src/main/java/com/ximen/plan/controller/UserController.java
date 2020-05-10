package com.ximen.plan.controller;

import com.ximen.plan.constants.StatusCode;
import com.ximen.plan.dto.responsedto.ResultDTO;
import com.ximen.plan.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("sysUser")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 1.传统登录
     * 前端发送登录请求 => 接口部分获取用户名密码 => 程序员在接口部分手动控制
     * 2.shiro登录
     * 前端发送登录请求 => 接口部分获取用户名密码 => 通过subject.login =>  realm域的认证方法
     */
    //用户登录
    @PostMapping(value = "/login")
    public ResultDTO login(@RequestBody Map<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        //构造登录令牌
        try {
            UsernamePasswordToken upToken = new UsernamePasswordToken(username, password);
            //1.获取subject
            Subject subject = SecurityUtils.getSubject();
            subject.login(upToken);
            System.out.println(subject.getSession());
            System.out.println(subject.getSession().getId());
            return new ResultDTO(StatusCode.SUCCESS);
        } catch (UnknownAccountException e) {
            // 用户不存在
            return new ResultDTO(StatusCode.ERROR_USERNOTEXIST);
        } catch (IncorrectCredentialsException e) {
            // 密码不对
            return new ResultDTO(StatusCode.ERROR_PASSWORDERROR);
        } catch (Exception e) {
            throw e;
        }
    }

    @GetMapping("find")
    public ResultDTO find() {
        return new ResultDTO(true, 0, "11111");
    }

    /**
     * 认证或者鉴权失败
     *
     * @param code
     * @return
     */
    @GetMapping("autherror")
    public ResultDTO autherror(Integer code) {
        if (code == 1) {
            return new ResultDTO(StatusCode.ERROR_UNAUTHENTICATED);
        }
        return new ResultDTO(StatusCode.ERROR_UNAUTHORISE);
    }
}
