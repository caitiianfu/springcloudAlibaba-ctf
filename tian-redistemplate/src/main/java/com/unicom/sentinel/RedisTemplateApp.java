package com.unicom.sentinel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** @author by ctf */
@SpringBootApplication(scanBasePackages = {"com.unicom"})
public class RedisTemplateApp {
  public static void main(String[] args) {
    SpringApplication.run(RedisTemplateApp.class, args);
  }
}
