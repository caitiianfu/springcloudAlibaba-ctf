package com.unicom.redis.interceptor;

import com.unicom.common.api.ResultCode;
import com.unicom.common.exception.Assert;
import com.unicom.redis.annotation.ApiIdempotent;
import com.unicom.redis.config.RedisService;
import com.unicom.redis.prefix.ApiIdemponentPrefix;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 接口幂等性拦截器
 * @author by ctf
 * @Classsname ApiIdempotentInterceptor
 * @Description TODO
 * @Date 2020/5/25 14:36
 **/
@Component
public class ApiIdempotentInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisService redisService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       if (!(handler instanceof HandlerMethod)){
           return true;
       }
        HandlerMethod handlerMethod= (HandlerMethod) handler;
        Method method=handlerMethod.getMethod();
        ApiIdempotent apiIdempotent=method.getAnnotation(ApiIdempotent.class);
        if (apiIdempotent!=null){
            check(request);
        }
        return true;
    }
    public void check(HttpServletRequest request){
        String token=request.getHeader("api-token");
        if (token==null||("").equals(token)){
            Assert.fail(ResultCode.REPEAT_COMMIT);
        }
        boolean ex=redisService.exist(ApiIdemponentPrefix.apiIdemponentPrefix,token);
        if (!ex){
            Assert.fail(ResultCode.REPEAT_COMMIT);
        }
        Long del=redisService.del(ApiIdemponentPrefix.apiIdemponentPrefix,token);
        //并发问题
        if (del<=0){
            Assert.fail(ResultCode.REPEAT_COMMIT);
        }
    }
}
