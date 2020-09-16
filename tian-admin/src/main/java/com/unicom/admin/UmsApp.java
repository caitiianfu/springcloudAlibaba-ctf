package com.unicom.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.unicom.common","com.unicom.admin"})
@EnableFeignClients
@EnableDiscoveryClient
//@SpringBootApplication(scanBasePackages = "com.unicom")
public class UmsApp {
    public static void main(String[] args) {
        SpringApplication.run(UmsApp.class,args);
    }
}
