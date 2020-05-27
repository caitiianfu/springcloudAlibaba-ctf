package com.unicom.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

//@SpringBootApplication(scanBasePackages = {"com.unicom.common","com.unicom.admin"})
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.unicom")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class,args);
    }
}
