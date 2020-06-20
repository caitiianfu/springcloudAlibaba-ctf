package com.unicom.redis.controller;

import com.unicom.common.api.ResultUtils;
import com.unicom.generator.entity.UmsAdmin;
import com.unicom.redis.annotation.ApiIdempotent;
import com.unicom.redis.annotation.RedisLock;
import com.unicom.redis.config.RedisService;
import com.unicom.redis.prefix.ApiIdemponentPrefix;
import com.unicom.redis.prefix.TestPrefix;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** @author by ctf @Classsname TestController @Description TODO @Date 2020/5/25 15:04 */
@RestController
@RequestMapping("/test")
@Api(value = "测试redis幂等性和分布式锁方法")
public class TestController {
  @Autowired private RedisService redisService;

  @RequestMapping("/createToken")
  public ResultUtils createToken(HttpServletRequest request) {
    String uuid = UUID.randomUUID().toString();
    redisService.set(ApiIdemponentPrefix.apiIdemponentPrefix, uuid, uuid);
    Map<String, Object> map = (Map<String, Object>) request.getAttribute("map");
    System.out.println(map.toString());
    return ResultUtils.success(uuid);
  }

  @RequestMapping("/checkToken")
  @ApiIdempotent
  public ResultUtils checkToken() {
    return ResultUtils.success("只能有一个获得值");
  }

  @RequestMapping("/redisLock/{id}")
  @RedisLock(key = "redis_key")
  @ApiOperation(value = "aop非spel测试")
  public ResultUtils redisLock(@PathVariable String id) {
    return ResultUtils.success(id);
  }

  @RequestMapping("/redisLock/test")
  @RedisLock(key = "redis_key", value = "'test'+#umsAdmin.username")
  @ApiOperation(value = "aop进行spel测试")
  public ResultUtils redisLockTest(@RequestBody UmsAdmin umsAdmin) {
    return ResultUtils.success(umsAdmin);
  }

  @RequestMapping("/redisList1")
  @ApiOperation(value = "测试存储list1")
  public ResultUtils redisList1() {
    List<UmsAdmin> umsAdmin = new ArrayList<>();
    UmsAdmin u1 = new UmsAdmin();
    u1.setNickName("cs1");
    UmsAdmin u2 = new UmsAdmin();
    u2.setNickName("cs2");
    umsAdmin.add(u1);
    umsAdmin.add(u2);
    redisService.setList(TestPrefix.testPrefix, "list", umsAdmin);

    return ResultUtils.success(redisService.getList(TestPrefix.testPrefix, "list"));
  }

  @GetMapping("/redisList2")
  @ApiOperation(value = "测试存储list2")
  public ResultUtils redisList2() {
    System.out.println(555);
    List<UmsAdmin> umsAdmin = new ArrayList<>();
    UmsAdmin u1 = new UmsAdmin();
    u1.setNickName("cs1");
    u1.setCreateTime(LocalDateTime.now());
    UmsAdmin u2 = new UmsAdmin();
    u2.setNickName("cs2");
    u2.setCreateTime(LocalDateTime.now());
    umsAdmin.add(u1);
    umsAdmin.add(u2);
    redisService.setList(TestPrefix.testPrefix, "list", umsAdmin);

    return ResultUtils.success(redisService.getList(TestPrefix.testPrefix, "list"));
  }

  @GetMapping("/tt")
  @ApiOperation(value = "测试tt")
  public String tt() {
    System.out.println(1111);
    return "tt";
  }
}
