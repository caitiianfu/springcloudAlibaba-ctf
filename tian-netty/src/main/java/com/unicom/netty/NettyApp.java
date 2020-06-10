package com.unicom.netty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author by ctf
 * @Classsname NettyApp
 * @Description TODO
 * @Date 2020/6/2 15:10
 **/
@SpringBootApplication(scanBasePackages = {"com.unicom"})
public class NettyApp {
    public static void main(String[] args) {
        SpringApplication.run(NettyApp.class,args);
    }
}
