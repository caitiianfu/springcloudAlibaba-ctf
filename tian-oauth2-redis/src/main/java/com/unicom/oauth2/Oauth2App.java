package com.unicom.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/** @author by ctf */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.unicom"})
public class Oauth2App {
  public static void main(String[] args) {
    SpringApplication.run(Oauth2App.class, args);
  }
}
