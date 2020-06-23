package com.unicom.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/** @Classsname RedisApp @Description TODO @Date 2020/5/23 19:22 @Created by 10750 */
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication(scanBasePackages = {"com.unicom"})
public class AccountApp {
  public static void main(String[] args) {
    SpringApplication.run(AccountApp.class, args);
  }
}
