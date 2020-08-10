package com.unicom.monitor;/**
 *
 **/

import com.dtflys.forest.annotation.ForestScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description TODO
 * @date 2020/7/14
 * @author ctf
 **/

@SpringBootApplication
@ForestScan("com.unicom")
public class ForestApp {

  public static void main(String[] args) {
    SpringApplication.run(ForestApp.class,args);
  }
}
