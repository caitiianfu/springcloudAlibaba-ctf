package com.unicom.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.unicom"})
public class EsApp {
    public static void main(String[] args) {
        SpringApplication.run(EsApp.class,args);
    }
}
