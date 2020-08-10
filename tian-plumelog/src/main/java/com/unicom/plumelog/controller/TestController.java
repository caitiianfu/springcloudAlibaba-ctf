package com.unicom.plumelog.controller;/**
 *
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description TODO
 * @author ctf
 * @date 2020/8/8
 */
@RestController
public class TestController {
  private static final Logger log= LoggerFactory.getLogger(TestController.class);
  @GetMapping("/test")
  public String test(){
    log.info("test",123);
    return "7777";
  }

}
