package com.unicom.sentinel.config;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author by ctf
 * @description 服务调用,通过@SentinelRestTemplate包装使用RestTemplate
 */
@Configuration
public class RibbonConfig {
  @Bean
  @SentinelRestTemplate
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
