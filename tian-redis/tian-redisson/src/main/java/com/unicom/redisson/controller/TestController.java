package com.unicom.redisson.controller;

import com.unicom.common.api.ResultUtils;
import com.unicom.common.exception.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author by ctf
 * @Classsname TestController
 * @Description TODO
 * @Date 2020/5/27 19:10
 **/
@RestController
@Api(value = "redisson分布式锁测试")
@RequestMapping("/test")
public class TestController {
    @Autowired
    private RedissonClient redissonClient;
    @ApiOperation(value = "分布式锁测试方法",notes = "自己测试一下redisson好不好用")
    @ApiImplicitParams({

            @ApiImplicitParam(name="id",example = "11",value = "测试id",required = true,dataType = "LONG")
    })

    @RequestMapping("/testRedisson/{id}")
    public ResultUtils testRedisson(@PathVariable long id){
        String key="test";
        RLock rLock=redissonClient.getLock(key);
       try {
           try {
               rLock.lock(5, TimeUnit.SECONDS);
               Thread.sleep(6000);
           } catch (Exception e) {
               // e.printStackTrace();
               Assert.fail(e.getMessage());
           } finally {
               rLock.unlock();
           }
       }catch (Exception e){
           Assert.fail(e.getMessage());
       }
        return  ResultUtils.success("yes");
    }
}
