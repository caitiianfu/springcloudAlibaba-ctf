package com.unicom.admin.component;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import com.unicom.admin.bo.WebLog;
import freemarker.template.utility.StringUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.marker.Markers;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Aspect
@Slf4j
@Component
@Order(1)
public class WebLogAspect {
        @Pointcut("execution(public * com.unicom.admin.controller.*.*(..))")
        public void webLog(){

        }
        @Before("webLog()")
        public void doBefore(JoinPoint joinPoint){

        }
        @AfterReturning(value = "webLog()",returning = "ret")
        public  void doAfterRturning(Object ret) throws  Throwable{

        }
        @Around("webLog()")
        public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
            long startTime=System.currentTimeMillis();
            ServletRequestAttributes attributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request=attributes.getRequest();
            //记录请求信息（通过logstash传入elasticsearch）
                WebLog webLog=new WebLog();
                Object result=joinPoint.proceed();
                Signature signature=joinPoint.getSignature();
                MethodSignature methodSignature=(MethodSignature) signature;
                Method method=methodSignature.getMethod();
                if (method.isAnnotationPresent(ApiOperation.class)){
                        ApiOperation log=method.getAnnotation(ApiOperation.class);
                        webLog.setDescription(log.value());
                }
                long endTime=System.currentTimeMillis();
                String urlStr=request.getRequestURL().toString();
                webLog.setUrl(urlStr);
                webLog.setBasePath(StrUtil.removeSuffix(urlStr, URLUtil.url(urlStr).getPath()));
                webLog.setIp(request.getRemoteUser());
                webLog.setMethod(request.getMethod());
                webLog.setParameter(getParameter(method,joinPoint.getArgs()));
                webLog.setSpendTime((int)(endTime-startTime));
                webLog.setStartTime(startTime);
                webLog.setUri(request.getRequestURI());
                webLog.setResult(result);
                Map<String,Object> logMap=new HashMap<>(5);
                logMap.put("url",webLog.getUrl());
                logMap.put("parameter",webLog.getParameter());
                logMap.put("spendTime",webLog.getSpendTime());
                logMap.put("description",webLog.getDescription());
                logMap.put("method",webLog.getMethod());
                log.info(Markers.appendEntries(logMap), JSONUtil.parse(webLog).toString());
                return  result;
        }
        public  static Object getParameter(Method method,Object[] args){
                List<Object> argList=new ArrayList<>();
                Parameter[] parameters=method.getParameters();
                for (int i=0;i<parameters.length;i++){
                        //获得requestbody注解的参数
                        RequestBody requestBody=parameters[i].getAnnotation(RequestBody.class);
                        if (requestBody != null) {
                                argList.add(args[i]);
                        }
                        //获得requestParam的参数
                        RequestParam requestParam=parameters[i].getAnnotation(RequestParam.class);
                        if (requestParam!=null){
                                Map<String,Object> map=new HashMap<>(16);
                                String key=parameters[i].getName();
                                if (!StringUtils.isEmpty(requestParam.value())){
                                    key=requestParam.value();
                                }
                                map.put(key,args[i]);
                                argList.add(map);
                        }
                }
                if (argList.size()==0){
                    return null;
                }else if (argList.size()==1){
                    return argList.get(0);
                }else{
                    return argList;
                }
        }
}
