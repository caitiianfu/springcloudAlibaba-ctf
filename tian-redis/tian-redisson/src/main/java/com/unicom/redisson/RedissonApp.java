package com.unicom.redisson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author by ctf
 * @Classsname RedissonApp
 * @Description TODO
 * @Date 2020/5/27 18:51
 **/
@SpringBootApplication(scanBasePackages = {"com.unicom"})
public class RedissonApp {
    public static void main(String[] args) {
        SpringApplication.run(RedissonApp.class,args);
    }
}
