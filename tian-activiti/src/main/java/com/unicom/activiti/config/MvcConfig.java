package com.unicom.activiti.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/** @author by ctf @Classsname MvcConfig @Description TODO @Date 2020/6/2 18:51 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");
    super.addResourceHandlers(registry);
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/login");
    registry.addViewController("/index");
    registry.addRedirectViewController("/", "/templates/login.html");
    super.addViewControllers(registry);
  }
}
