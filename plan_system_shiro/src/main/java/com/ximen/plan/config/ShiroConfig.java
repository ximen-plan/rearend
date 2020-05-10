package com.ximen.plan.config;

import com.ximen.plan.realm.UserRealm;
import com.ximen.plan.session.UserManager;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    //1.创建realm
    @Bean
    public UserRealm getRealm() {
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(new CredentialsMatcher() {
            @Override
            public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
                UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
                char[] password = token.getPassword();
                String hashed = authenticationInfo.getCredentials() == null ? null : authenticationInfo.getCredentials().toString();
                // 账户无密码
                if (password == null && hashed == null) {
                    return true;
                }
                if (password == null || hashed == null) {
                    return false;
                }
                String plaintText = String.valueOf(token.getPassword());
                return BCrypt.checkpw(plaintText, hashed);
            }
        });
        return userRealm;
    }

    //2.创建安全管理器
    @Bean
    public SecurityManager getSecurityManager(UserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);

        //将自定义的会话管理器注册到安全管理器中
        securityManager.setSessionManager(sessionManager());
        //将自定义的redis缓存管理器注册到安全管理器中
        securityManager.setCacheManager(cacheManager());

        return securityManager;
    }

    //3.配置shiro的过滤器工厂

    /**
     * 再web程序中，shiro进行权限控制全部是通过一组过滤器集合进行控制
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        //1.创建过滤器工厂
        ShiroFilterFactoryBean filterFactory = new ShiroFilterFactoryBean();
        //2.设置安全管理器
        filterFactory.setSecurityManager(securityManager);
        //3.通用配置（跳转登录页面，为授权跳转的页面）
        filterFactory.setLoginUrl("/sysUser/autherror?code=1");//跳转url地址
        filterFactory.setUnauthorizedUrl("/sysUser/autherror?code=2");//未授权的url
        //4.设置过滤器集合

        /**
         * 设置所有的过滤器：有顺序map
         *     key = 拦截的url地址
         *     value = 过滤器类型
         *
         */
        Map<String, String> filterMap = new LinkedHashMap<>();
        //filterMap.put("/user/home","anon");//当前请求地址可以匿名访问

        //具有某中权限才能访问
        //使用过滤器的形式配置请求地址的依赖权限
        //filterMap.put("/user/home","perms[user-home]"); //不具备指定的权限，跳转到setUnauthorizedUrl地址

        //使用过滤器的形式配置请求地址的依赖角色
        //filterMap.put("/user/home","roles[系统管理员]");

        filterMap.put("/sysUser/login", "anon");
        filterMap.put("/sysUser/autherror", "anon");

        filterMap.put("/**", "authc");

        filterFactory.setFilterChainDefinitionMap(filterMap);

        return filterFactory;
    }


    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;

    /**
     * 1.redis的控制器，操作redis
     */
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        return redisManager;
    }

    /**
     * 2.sessionDao
     */
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO sessionDAO = new RedisSessionDAO();
        sessionDAO.setRedisManager(redisManager());
        return sessionDAO;
    }

    /**
     * 3.会话管理器
     */
    public DefaultWebSessionManager sessionManager() {
        UserManager sessionManager = new UserManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        return sessionManager;
    }

    /**
     * 4.缓存管理器
     */
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }


    //开启对shior注解的支持
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
