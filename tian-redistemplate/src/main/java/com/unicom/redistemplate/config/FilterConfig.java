package com.unicom.redistemplate.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** @author by ctf */
@Configuration
public class FilterConfig {
  @Autowired private MyFilter myFilter;

  @Bean
  public FilterRegistrationBean registrationBean() {
    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(myFilter);
    filterRegistrationBean.addUrlPatterns("/*");
    return filterRegistrationBean;
  }
}
