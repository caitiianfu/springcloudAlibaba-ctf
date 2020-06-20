package com.unicom.redis.prefix;

/**
 * @author by ctf
 * @Classsname KeyPrefix
 * @Description TODO
 * @Date 2020/5/25 9:41
 **/
public interface KeyPrefix {
    public String getPrefix();
    public int getExpireSeconds();
}
