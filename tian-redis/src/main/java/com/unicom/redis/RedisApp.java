package com.unicom.redis;

import com.unicom.redis.interceptor.ApiIdempotentInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Classsname RedisApp
 * @Description TODO
 * @Date 2020/5/23 19:22
 * @Created by 10750
 **/
@SpringBootApplication(scanBasePackages = {"com.unicom"})
public class RedisApp {
    public static void main(String[] args) {
        SpringApplication.run(RedisApp.class,args);
    }


}
