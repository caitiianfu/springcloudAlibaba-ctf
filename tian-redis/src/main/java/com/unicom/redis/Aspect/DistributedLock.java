package com.unicom.redis.Aspect;

import com.unicom.common.exception.Assert;
import com.unicom.redis.annotation.RedisLock;
import com.unicom.redis.config.RedisService;
import com.unicom.redis.prefix.TestPrefix;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author by ctf
 * @Classsname DistributedLock
 * @Description TODO
 * @Date 2020/5/25 20:13
 **/
@Component
@Aspect
@Slf4j
public class DistributedLock {
    @Resource
    private RedisService redisService;
    private String key;
    private String value;
    @Pointcut("@annotation(com.unicom.redis.annotation.RedisLock)")
    public void distributedLock(){}
    @Around("distributedLock()")
    public Object around(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RedisLock redisLock = method.getAnnotation(RedisLock.class);
        key = redisLock.key();
        value = redisLock.value();
        if (value == null || value.length() == 0) {
            value = UUID.randomUUID().toString();
        }
        boolean result = redisService.distributedLock(TestPrefix.testPrefix, key, value);
        if (result) {
            try {
                return joinPoint.proceed();

            } catch (Throwable t) {
                Assert.fail("系统异常");
            }
        } else {
            log.debug("获取锁失败 {}", key);
            Assert.fail("获取锁失败");
        }
        return  null;
    }


    @After(value = "distributedLock()")
    public void after(){
        if (!redisService.unDistributeLock(TestPrefix.testPrefix,key,value)){
            log.error("解锁失败 {}:{}",key,value);
            Assert.fail("解锁失败");
        }
    }
}
