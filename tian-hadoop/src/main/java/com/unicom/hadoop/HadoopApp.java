package com.unicom.hadoop;/**
 *
 **/

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @Description TODO
 * @date 2020/7/7
 * @author ctf
 **/
@SpringBootApplication(scanBasePackages = {"com.unicom"})
public class HadoopApp {
  public static void main(String[] args) {
    SpringApplication.run(HadoopApp.class, args);
  }


}
