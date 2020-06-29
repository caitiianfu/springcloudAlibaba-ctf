package com.unicom.rocketmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** @author by ctf */
@SpringBootApplication(scanBasePackages = {"com.unicom"})
public class RocketmqConsumerApp {
  public static void main(String[] args) {
    SpringApplication.run(RocketmqConsumerApp.class, args);
  }
}
