package com.unicom.activiti.controller;

import com.unicom.activiti.entity.User;
import com.unicom.activiti.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/** @author Created by yawn on 2018-01-11 10:02 */
@RestController
public class UserController {

  @Resource private UserService userService;

  @GetMapping("/getAllUser")
  public Object getAllUser() {
    return userService.getAllUser();
  }

  @GetMapping("/getAllGroup")
  public Object getAllGroup() {
    return userService.getAllGroup();
  }

  @GetMapping("/getUserGroup")
  public Object getUserGroup(String groupId) {
    return userService.getUserGroup(groupId);
  }

  @PostMapping("/addUser")
  public Object addUser(@RequestBody User user) {
    return userService.addUser(user);
  }
}
