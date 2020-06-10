package com.unicom.activiti.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** @author Created by yawn on 2018-01-07 20:21 */
@RestController
public class TestController {

  @GetMapping("/test")
  public Object test() {
    return "okay!";
  }
}
