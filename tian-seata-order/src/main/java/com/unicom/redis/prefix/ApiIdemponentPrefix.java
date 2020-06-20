package com.unicom.redis.prefix;

/**
 * @author by ctf
 * @Classsname ApiIdemponentPrefix
 * @Description TODO
 * @Date 2020/5/25 14:48
 **/
public class ApiIdemponentPrefix extends BasePrefix {
    public ApiIdemponentPrefix(String prefix){
        super(prefix);
    }
    public ApiIdemponentPrefix(String prefix,int expireSeconds){
        super(prefix, expireSeconds);
    }
    public  static ApiIdemponentPrefix apiIdemponentPrefix=new ApiIdemponentPrefix("token",30*60);
}
