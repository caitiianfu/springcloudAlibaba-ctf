package com.unicom.redis.prefix;

/**
 * @author by ctf
 * @Classsname TestPrefix
 * @Description TODO
 * @Date 2020/5/25 11:28
 **/
public class TestPrefix extends BasePrefix{
    private static  final int EXPIRE_TIME=15;
    public  TestPrefix(String prefix){
        super(prefix);
    }
    public TestPrefix(String prefix,int expireSeconds){
        super(prefix,expireSeconds);
    }
    public static TestPrefix testPrefix=new TestPrefix("test",EXPIRE_TIME);
}
