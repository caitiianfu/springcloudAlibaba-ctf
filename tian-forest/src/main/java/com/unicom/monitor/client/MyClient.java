package com.unicom.monitor.client;/**
 *
 */

import com.dtflys.forest.annotation.Request;

/**
 * @description TODO
 * @author ctf
 * @date 2020/8/7
 */
public interface MyClient {
    @Request(url=
        "http://localhost:3360/test/testForest?name=${0}"
    ,headers = {"auth:${1}"},
    dataType = "json",
    type = "get")
    String redisJedisTest(String name,String yalu);
}
