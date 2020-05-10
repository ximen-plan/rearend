package com.ximen.plan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author ZhiShun.Cai
 * @date 2020/1/1 14:06
 * @note
 */
@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.ximen.plan.mapper")
public class SystemShiroApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemShiroApplication.class);
    }
}
