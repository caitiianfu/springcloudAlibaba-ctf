package com.unicom.monitor;/**
 *
 **/

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Description TODO
 * @date 2020/7/14
 * @author ctf
 **/
@EnableDiscoveryClient
@EnableAdminServer
@SpringBootApplication
public class MonitorApp {

  public static void main(String[] args) {
    SpringApplication.run(MonitorApp.class,args);
  }
}
