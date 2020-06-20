package com.unicom.sentinel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/** @author by ctf */
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication(scanBasePackages = {"com.unicom"})
public class SentinelApp {

  public static void main(String[] args) {
    SpringApplication.run(SentinelApp.class, args);
  }
}
