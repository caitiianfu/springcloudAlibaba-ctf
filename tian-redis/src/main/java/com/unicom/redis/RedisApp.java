package com.unicom.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/** @Classsname RedisApp @Description TODO @Date 2020/5/23 19:22 @Created by 10750 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.unicom"})
public class RedisApp {
  public static void main(String[] args) {
    SpringApplication.run(RedisApp.class, args);
  }
}
