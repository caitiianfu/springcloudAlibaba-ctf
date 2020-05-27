package com.unicom.redis.test;

import com.unicom.redis.config.RedisService;
import com.unicom.redis.prefix.TestPrefix;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author by ctf
 * @Classsname TestRedis
 * @Description TODO
 * @Date 2020/5/25 11:32
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TestRedis {
    @Autowired
    private RedisService redisService;
    @Test
    public  void  testSetRedis(){

       boolean t=redisService.set(TestPrefix.testPrefix,"test","cs");
       System.out.println(t);
       log.info("isTrue?  {}",t);
    }
    @Test
    public void testGetRedis(){
        String value=redisService.get(TestPrefix.testPrefix,"test",String.class);
        System.out.println(value);
        log.info("value is {}",value);
    }
}
