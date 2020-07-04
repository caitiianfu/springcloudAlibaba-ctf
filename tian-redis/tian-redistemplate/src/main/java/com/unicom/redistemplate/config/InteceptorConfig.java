package com.unicom.redistemplate.config;
/** */
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description TODO
 *
 * @date 2020/6/27
 * @author ctf
 */
//@Configuration
public class InteceptorConfig implements WebMvcConfigurer {
  @Bean
  public MyInterceptor myInterceptor() {
    return new MyInterceptor();
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(myInterceptor());
  }
}
