package com.unicom.redisson.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author by ctf
 * @Classsname RedissonConfig
 * @Description TODO
 * @Date 2020/5/27 19:01
 **/
@Component
@ConfigurationProperties(prefix = "redisson")
@Data
public class RedissonConfig {
    private String host;
    private String password;
    @Bean
    public RedissonClient redissonClient(){
        Config config=new Config();
        config.useSingleServer().setAddress(host).setPassword(password);
        RedissonClient redissonClient= Redisson.create(config);
        return  redissonClient;
    }
}
