package com.ximen.plan;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.ximen.plan.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 后台管理的zuul过滤器
 */
@Component
public class ManagerZuulFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    //注入JwtUtil
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Object run() {
        System.out.println("经过了后台管理网站的网关过滤器");

        //1.获取jwt的token的header
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String authorizationHeader = request.getHeader("Authorization");

        String upgradeHeader = request.getHeader("Upgrade");
        if (null == upgradeHeader) {
            upgradeHeader = request.getHeader("upgrade");
        }
        if (null != upgradeHeader && "websocket".equalsIgnoreCase(upgradeHeader)) {
            requestContext.addZuulRequestHeader("connection", "Upgrade");
            return null;
        }

        //2. 其他请求处理
        //1）处理CORS跨域请求，因为跨域请求的第一次请求是预请求，不带头信息的，因此要过滤掉。
        if (request.getMethod().equals("OPTIONS")) {
            System.out.println("跨域的第一次请求，直接放行");
            return null;
        }
        //2）处理一些特殊请求，比如登录请求，就直接放行
        //获取请求的URL
        String url = request.getRequestURL().toString();
        //如果是登录
        if (url.indexOf("/sysUser/login") > 0) {
            System.out.println("的登录请求，直接放行：" + url);
            return null;
        }

        //3. 判断头信息的合法性
        if (null != authorizationHeader && authorizationHeader.startsWith("Bearer ")) {
            //获取令牌，The part after "Bearer "
            final String token = authorizationHeader.substring(7);
            //获取载荷
            Claims claims = null;
            try {
                claims = jwtUtil.parseJWT(token);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //判断，是否有载荷内容
            if (null != claims) {
                //是管理员
                //转发头信息
                requestContext.addZuulRequestHeader("JwtAuthorization", authorizationHeader);
                System.out.println("token 验证通过，添加了头信息:" + authorizationHeader);
                //放行
                return null;
            }
        }

        //无权访问微服务
        //1）终止网关的代码的继续运行,默认是true（继续向下走）
        requestContext.setSendZuulResponse(false);
        //2）给响应中写点内容
        requestContext.getResponse().setContentType("application/json;charset=UTF-8");
        //友好提示（建议将来写成json）
        Map resMap = new HashMap<>();
        resMap.put("messge", "您无权访问");
        resMap.put("code", 401);
        resMap.put("flag", false);
        requestContext.setResponseBody(JSON.toJSONString(resMap));
        //设置http状态码，401代表没有访问资源的权限
        requestContext.setResponseStatusCode(401);

        //放行
        return null;
    }
}
