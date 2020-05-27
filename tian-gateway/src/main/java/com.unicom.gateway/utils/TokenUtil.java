package com.unicom.gateway.utils;


import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.List;

public class TokenUtil {
	public  static String extractToken(ServerHttpRequest request) {
		List<String> strings = request.getHeaders().get("Authorization");
		String authToken = "";
		if((strings!=null&&strings.size()>0) && strings.get(0).contains("Bearer")){
			authToken = strings.get(0).substring("Bearer".length()).trim();
		}
		if (StringUtils.isBlank(authToken)) {
			strings = request.getQueryParams().get("access_token");
			if (strings!=null&&strings.size()>0) {
				authToken = strings.get(0);
			}
		}
		return authToken;
	}
}
