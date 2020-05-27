package com.unicom.gateway.filter;

import com.unicom.gateway.constant.TraceConstant;
import com.unicom.gateway.utils.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * *

 */
@Component
public class RequestStatsFilter implements GlobalFilter, Ordered {
	private static  final Logger log= LoggerFactory.getLogger(RequestStatsFilter.class);

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return -501;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		 System.out.println(1111111111);
		String traceId = MDC.get(TraceConstant.LOG_B3_TRACEID);
		MDC.put(TraceConstant.LOG_TRACE_ID, traceId);
				System.out.println("traceId:"+traceId);
		String accessToken = TokenUtil.extractToken(exchange.getRequest());
		
		//构建head
		ServerHttpRequest traceHead = exchange.getRequest().mutate()
				 .header(TraceConstant.LOG_TRACE_ID, traceId )
				.header("Authorization", accessToken ).build();
		//将现在的request 变成 change对象 

		log.info("request url = " + exchange.getRequest().getURI() + ", traceId = " + traceId);
		
		ServerWebExchange build = exchange.mutate().request(traceHead).build();
		
        return chain.filter(build);

		
	}

	 

}
