package com.unicom.common.config;

import com.unicom.common.interceptor.LogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
@Configuration
public class DefaultWebMvcConfig extends WebMvcConfigurationSupport {
  @Autowired
  private LogInterceptor logInterceptor;
  @Override
  protected void addInterceptors(InterceptorRegistry registry) {
    //日志拦截
    registry.addInterceptor(logInterceptor).addPathPatterns("/**");
    super.addInterceptors(registry);
  }
}
