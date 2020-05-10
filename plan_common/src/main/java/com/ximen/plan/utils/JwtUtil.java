package com.ximen.plan.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/11.
 */
/*
自动加载属性配置文件的key的前缀，并自动匹配setter属性，注入可以key对应的值,
如：属性声明private String key上相当于加了注解@Value("jwt.config.key")
 */
@Component
@ConfigurationProperties("jwt.config")
public class JwtUtil {

    //盐，秘钥
    private String key;

    //过期时间的时长，这里推荐一个小时
    private long ttl;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    /**
     * 生成JWT
     *
     * @param id
     * @param subject
     * @return
     */
    public String createJWT(String id, String subject, Map<String, Object> apis) {
        //1.当前时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //2.构建JwtBuilder
        JwtBuilder builder = Jwts.builder().setId(id)
                .setSubject(subject)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, key);
        //3.根据map设置claims
        for (Map.Entry<String, Object> entry : apis.entrySet()) {
            builder.claim(entry.getKey(), entry.getValue());
        }
        //3.有过期时间
        if (ttl > 0) {
            builder.setExpiration(new Date(nowMillis + ttl));
        }
        return builder.compact();
    }

    /**
     * 解析JWT
     *
     * @param jwtStr
     * @return
     */
    public Claims parseJWT(String jwtStr) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwtStr)
                .getBody();
    }

}
