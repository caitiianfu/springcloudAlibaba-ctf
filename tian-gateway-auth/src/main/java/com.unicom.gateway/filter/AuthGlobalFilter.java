package com.unicom.gateway.filter;

import cn.hutool.core.util.StrUtil;
import com.nimbusds.jose.JWSObject;
import com.unicom.common.constant.AuthConstant;
import java.lang.annotation.Annotation;
import java.text.ParseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
/**
 *
 * @Description 将登录用户jwt转化成用户信息的全局过滤器
 * @param
 * @return
 * @date 2020/9/2
 * @author ctf
 **/

@Component
@Slf4j
public class AuthGlobalFilter implements GlobalFilter, Ordered {


  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String token=exchange.getRequest().getHeaders().getFirst(AuthConstant.JWT_TOKEN_HEADER);
    if (StrUtil.isEmpty(token)){
        return chain.filter(exchange);
    }
    //从token中解析用户信息并设置到header
    try {
      String realToken = token.replace(AuthConstant.JWT_TOKEN_PREFIX, "");
      JWSObject jwsObject = JWSObject.parse(realToken);
      String userStr=jwsObject.getPayload().toString();
      log.info("AuthGlobalFilter filer() user:{}",userStr);
      ServerHttpRequest request=exchange.getRequest().mutate().header(AuthConstant.USER_TOKEN_HEADER,userStr).build();
      exchange=exchange.mutate().request(request).build();
    }catch (ParseException e){
      e.printStackTrace();
    }
    return chain.filter(exchange);
  }

  @Override
  public int getOrder() {
    return 0;
  }
}
