package com.unicom.redis.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.unicom.redis.annotation.CacheException;
import com.unicom.redis.prefix.KeyPrefix;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author by ctf
 * @Classsname RedisService
 * @Description TODO
 * @Date 2020/5/25 9:40
 **/
@Service
@Slf4j
public class RedisService {
    @Autowired
    private JedisPool jedisPool;

    /**
     * 存值
     * @param keyPrefix
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean set(KeyPrefix keyPrefix, String key, T value){
        Jedis jedis=null;
        try {
            jedis=jedisPool.getResource();
            String str=beanToString(value);
            if (str==null||str.length()==0){
                return  false;
            }
            String realKey=keyPrefix.getPrefix()+key;
            int expireSeconds=keyPrefix.getExpireSeconds();
            if (expireSeconds<=0){
                jedis.set(realKey,str);
            }else{
                jedis.setex(realKey,expireSeconds,str);
            }
            return true;
        }
        finally {
            closeRedis(jedis);
        }
    }

    /**
     * 判断值是否存在
     * @param keyPrefix
     * @param key
     * @return
     */
    public  boolean exist(KeyPrefix keyPrefix,String key){
        Jedis jedis=null;
        try {
            jedis=jedisPool.getResource();
            String realKey=keyPrefix.getPrefix()+key;
            return  jedis.exists(realKey);
        }finally {
            closeRedis(jedis);
        }
    }

    /**
     * 递减一
     * @param keyPrefix
     * @param key
     * @return
     */
    public Long decr(KeyPrefix keyPrefix,String key){
        Jedis jedis=null;
        try {
            jedis=jedisPool.getResource();
            String realKey=keyPrefix.getPrefix()+key;
            return jedis.decr(realKey);
        }finally {
            closeRedis(jedis);
        }

    }

    /**
     * 获得值
     * @param keyPrefix
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(KeyPrefix keyPrefix,String key,Class<T> clazz){
        Jedis jedis=null;
        try {
            jedis=jedisPool.getResource();
            String realKey=keyPrefix.getPrefix()+key;
            String value=jedis.get(realKey);
            T v=stringToBean(value,clazz);
            return  v;
        }
        finally {
            closeRedis(jedis);
        }

    }

    /**
     * 删除缓存
     * @param keyPrefix
     * @param key
     * @return
     */
    public Long del(KeyPrefix keyPrefix,String key){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            String realKey=keyPrefix.getPrefix()+key;
           return    jedis.del(realKey);
        }finally {
            closeRedis(jedis);
        }
    }
    public <T> List<T> getList(KeyPrefix keyPrefix,String key,Class<T> clazz){
        Jedis jedis=null;
        try {
            jedis=jedisPool.getResource();
            String realKey=keyPrefix.getPrefix()+key;
            String value=jedis.get(realKey);
            if (value==null) return null;
            return  (List<T>) JSONArray.parseArray(value,clazz);
        }finally {
            closeRedis(jedis);
        }

    }

    /**
     * 分布式锁
     * @param keyPrefix
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean distributedLock(KeyPrefix keyPrefix, String key, T value){
        Jedis jedis=null;
        try {
            jedis=jedisPool.getResource();
            String v=beanToString(value);
            if (v==null||v.length()==0){
                return false;
            }
            long expireTime=keyPrefix.getExpireSeconds();
            String realKey=keyPrefix.getPrefix()+key;
            String result=jedis.set(realKey,v,"NX","PX",expireTime*1000);
            if ("OK".equals(result)){
                return  true;
            }
            return false;
        }finally {
            closeRedis(jedis);
        }

    }

    /**
     * 分布式锁解锁
     * @param keyPrefix
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean unDistributeLock(KeyPrefix keyPrefix,String key,T value){
        Jedis jedis=null;
        try {
            jedis=jedisPool.getResource();
            String v=beanToString(value);
            if (v==null||v.length()==0){
                return  false;
            }
            String lua="if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('del',KEYS[1]) else return 999 end";

            String realKey=keyPrefix.getPrefix()+key;
            log.info("undis {}:{}",realKey,v);
            Object result=jedis.eval(lua, Collections.singletonList(realKey),Collections.singletonList(v));

            if (("1").equals(result+"")){
                return true;
            }
            return false;
        }finally {
            closeRedis(jedis);
        }
    }

    /**
     * 字符串转bean
     * @param value
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T stringToBean(String value,Class<T> clazz){
        if (clazz==null||value==null||value.length()==0){
            return  null;
        }
        if (clazz==int.class||clazz==Integer.class){
            return (T)Integer.valueOf(value);
        }else if (clazz==long.class||clazz==Long.class){
            return (T)Long.valueOf(value);
        }else if(clazz==String.class){
            return (T)value;
        }
        return JSONObject.toJavaObject(JSONObject.parseObject(value),clazz);
    }

    /**
     * bean转换为String
     * @param value
     * @param <T>
     * @return
     */
    public <T> String beanToString(T value){
       if (value==null){
           return  null;
       }
       Class<?> clazz=value.getClass();
       if (clazz==int.class||clazz==Integer.class){
           return ""+value;
       }else if (clazz==Long.class||clazz==long.class){
           return ""+value;
       }else if(clazz==String.class){
           return (String)value;
       }
       return JSONObject.toJSONString(value);
    }

    /**
     * 关闭连接
     * @param jedis
     */
    private   void closeRedis(Jedis jedis){
        if (jedis!=null){
            jedis.close();;
        }
    }
}
