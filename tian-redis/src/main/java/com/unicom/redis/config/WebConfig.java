package com.unicom.redis.config;

import com.unicom.redis.interceptor.ApiIdempotentInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author by ctf
 * @Classsname WebConfig
 * @Description TODO
 * @Date 2020/5/25 15:41
 **/

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private ApiIdempotentInterceptor apiIdempotentInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //接口幂等性拦截器
        registry.addInterceptor(apiIdempotentInterceptor);
        super.addInterceptors(registry);
    }
}
