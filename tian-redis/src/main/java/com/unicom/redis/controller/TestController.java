package com.unicom.redis.controller;

import com.unicom.common.api.ResultUtils;
import com.unicom.generator.entity.UmsAdmin;
import com.unicom.redis.annotation.ApiIdempotent;
import com.unicom.redis.annotation.RedisLock;
import com.unicom.redis.config.RedisService;
import com.unicom.redis.prefix.ApiIdemponentPrefix;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author by ctf
 * @Classsname TestController
 * @Description TODO
 * @Date 2020/5/25 15:04
 **/
@RestController
@RequestMapping("/test")
@Api(value = "测试redis幂等性和分布式锁方法")
public class TestController {
    @Autowired
    private RedisService redisService;
    @RequestMapping("/createToken")
    public ResultUtils createToken(){
        String uuid= UUID.randomUUID().toString();
        redisService.set(ApiIdemponentPrefix.apiIdemponentPrefix,uuid,uuid);
        return ResultUtils.success(uuid);
    }
    @RequestMapping("/checkToken")
    @ApiIdempotent
    public ResultUtils checkToken(){
        return ResultUtils.success("只能有一个获得值");
    }

    @RequestMapping("/redisLock/{id}")
    @RedisLock(key = "redis_key")
    @ApiOperation(value = "aop非spel测试")
    public ResultUtils redisLock(@PathVariable String id){
        return ResultUtils.success(id);
    }

    @RequestMapping("/redisLock/test")
    @RedisLock(key = "redis_key",value = "'test'+#umsAdmin.username")
    @ApiOperation(value = "aop进行spel测试")
    public ResultUtils redisLockTest(@RequestBody UmsAdmin umsAdmin){
        return ResultUtils.success(umsAdmin);
    }

}
