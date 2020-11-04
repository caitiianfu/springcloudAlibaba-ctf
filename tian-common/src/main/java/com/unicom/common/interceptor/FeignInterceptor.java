package com.unicom.common.interceptor;

import cn.hutool.core.util.StrUtil;
import com.unicom.common.constant.TraceConstant;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 *
 * @Description 下游传日志
 * @date 2020/11/3
 * @author ctf
 **/
@Component
@Slf4j
public class FeignInterceptor {
  @Bean
  public RequestInterceptor requestInterceptor() {
    RequestInterceptor requestInterceptor = template -> {
      //传递日志traceId
      String traceId = MDC.get(TraceConstant.LOG_TRACE_ID);

      log.info("feigh-traceId:{}",traceId);
      if (StrUtil.isNotEmpty(traceId)) {
        template.header(TraceConstant.LOG_TRACE_ID, traceId);
      }
    };
    return requestInterceptor;
  }
}
