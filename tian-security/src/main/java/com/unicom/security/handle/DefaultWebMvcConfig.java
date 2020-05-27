package com.unicom.security.handle;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
@Component
public class DefaultWebMvcConfig extends WebMvcConfigurationSupport {
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        //日志链路追踪拦截器
        registry.addInterceptor(new TraceInterceptor()).addPathPatterns("/**");

        super.addInterceptors(registry);
    }
}
