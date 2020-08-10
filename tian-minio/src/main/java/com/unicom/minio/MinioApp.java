package com.unicom.minio;/**
 *
 **/

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description TODO
 * @date 2020/7/14
 * @author ctf
 **/

@SpringBootApplication(scanBasePackages = "com.unicom")
public class MinioApp {

  public static void main(String[] args) {
    SpringApplication.run(MinioApp.class,args);
  }
}
