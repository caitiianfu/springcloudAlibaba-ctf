package com.unicom.activiti;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** @author by ctf @Classsname ActApp @Description TODO @Date 2020/6/2 18:50 */
@SpringBootApplication(
    exclude = {SecurityAutoConfiguration.class},
    scanBasePackages = {"com.unicom"})
public class ActApp {
  public static void main(String[] args) {
    SpringApplication.run(ActApp.class, args);
  }
}
