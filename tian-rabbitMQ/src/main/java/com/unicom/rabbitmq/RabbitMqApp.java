package com.unicom.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/** @author by ctf @Classsname RabbitMqApp @Description TODO @Date 2020/5/27 22:17 */
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"com.unicom"})
public class RabbitMqApp {

  public static void main(String[] args) {
    SpringApplication.run(RabbitMqApp.class, args);
  }
}
