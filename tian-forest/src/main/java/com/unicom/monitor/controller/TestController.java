package com.unicom.monitor.controller;/**
 *
 */

import com.unicom.monitor.client.MyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description TODO
 * @author ctf
 * @date 2020/8/7
 */
@RestController
public class TestController {
    @Autowired
    private MyClient myClient;
    @GetMapping("/test")
    public String test(){
      return myClient.redisJedisTest("baga","yalu");
    }
}
