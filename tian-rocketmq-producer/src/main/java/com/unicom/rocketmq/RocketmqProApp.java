package com.unicom.rocketmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** @author by ctf */
@SpringBootApplication(scanBasePackages = {"com.unicom"})
public class RocketmqProApp {
  public static void main(String[] args) {
    SpringApplication.run(RocketmqProApp.class, args);
  }
}
