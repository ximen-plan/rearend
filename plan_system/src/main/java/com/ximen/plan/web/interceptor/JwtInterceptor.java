package com.ximen.plan.web.interceptor;

import com.ximen.plan.constants.StatusCode;
import com.ximen.plan.exception.ApiException;
import com.ximen.plan.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义拦截器
 * 继承HandlerInterceptorAdapter
 * <p>
 * preHandle:进入到控制器方法之前执行的内容
 * boolean：
 * true：可以继续执行控制器方法
 * false：拦截
 * posthandler：执行控制器方法之后执行的内容
 * afterCompletion：响应结束之前执行的内容
 * <p>
 * 1.简化获取token数据的代码编写
 * 统一的用户权限校验（是否登录）
 * 2.判断用户是否具有当前访问接口的权限
 */
@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {

    /**
     * 简化获取token数据的代码编写（判断是否登录）
     * 1.通过request获取请求token信息
     * 2.从token中解析获取claims
     * 3.将claims绑定到request域中
     */

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.通过request获取请求token信息
        String authorization = request.getHeader("JwtAuthorization");
        //判断请求头信息是否为空，或者是否已Bearer开头
        if (StringUtils.isNotBlank(authorization) && authorization.startsWith("Bearer")) {
            //获取token数据
            String token = authorization.replace("Bearer ", "");
            //解析token获取claims
            Claims claims = null;
            try {
                claims = jwtUtil.parseJWT(token);
            } catch (Exception e) {
                throw new ApiException(StatusCode.ERROR_UNAUTHENTICATED);
            }
            if (claims != null) {
                //通过claims获取到当前用户的可访问API权限字符串
                String permissions = (String) claims.get("permissions");  //api-user-delete,api-user-update
                //通过handler
                HandlerMethod h = (HandlerMethod) handler;
                //获取接口上的reqeustmapping注解
                RequestMapping annotation = h.getMethodAnnotation(RequestMapping.class);
                //获取当前请求接口中的name属性
                String name = annotation.name();
                //判断当前用户是否具有响应的请求权限
                if (StringUtils.isNotBlank(permissions) && permissions.contains(name)) {
                    request.setAttribute("user_claims", claims);
                    return true;
                } else {
                    throw new ApiException(StatusCode.ERROR_UNAUTHORISE);
                }
            }
        }
        throw new ApiException(StatusCode.ERROR_UNAUTHENTICATED);
    }
}
