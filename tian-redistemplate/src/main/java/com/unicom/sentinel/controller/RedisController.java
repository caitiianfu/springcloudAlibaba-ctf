package com.unicom.sentinel.controller;

import com.unicom.sentinel.pojo.User;
import com.unicom.sentinel.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** @author by ctf */
@Api(value = "redis测试")
@RestController
public class RedisController {
  @Autowired private RedisUtil redisUtil;

  @ApiOperation(value = "测试1")
  @GetMapping("test-set-string")
  public String testSetString(String key, String value) {
    redisUtil.set(key, value, 60L);
    return "success set string";
  }

  @GetMapping("test-get-string")
  public String testGetString(String key) {
    return (String) redisUtil.get(key);
  }

  @GetMapping("test-set-string2")
  public String testSetString2(String key, String value) {
    redisUtil.set(key, value, 60L);
    return "success set string2";
  }

  @GetMapping("test-get-string2")
  public String testGetString2(String key) {
    return (String) redisUtil.get(key);
  }

  @GetMapping("test-set-obj")
  public String testSetObj() {
    User user = new User("木子", 20);
    redisUtil.set(user.getUsername(), user);
    return "success set obj";
  }

  @GetMapping("test-get-obj")
  public Object testGetObj(String key) {
    return redisUtil.get(key);
  }

  @GetMapping("test-set-list")
  public String testSetList() {
    User user1 = new User("木子1", 20);
    User user2 = new User("木子2", 22);
    List<User> users = new ArrayList<>();
    users.add(user1);
    users.add(user2);
    redisUtil.lSet("test-list", users);
    redisUtil.set(user1.getUsername(), users);
    return "success set list";
  }

  @GetMapping("test-get-list")
  public List<User> testGetlist(String key) {
    redisUtil.lGet(key, 0, 1);
    return (List<User>) redisUtil.get(key);
  }
}
