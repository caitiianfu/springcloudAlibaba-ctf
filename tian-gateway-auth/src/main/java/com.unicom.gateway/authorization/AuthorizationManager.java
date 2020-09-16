package com.unicom.gateway.authorization;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.nimbusds.jose.JWSObject;
import com.unicom.common.constant.AuthConstant;
import com.unicom.common.domain.UserDto;
import com.unicom.common.utils.RedisUtil;
import com.unicom.gateway.config.IgnoreUrlsConfig;
import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import reactor.core.publisher.Mono;
@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {
  @Autowired
  private IgnoreUrlsConfig ignoreUrlsConfig;
  @Autowired
  private RedisUtil redisUtil;

  @Override
  public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
    ServerHttpRequest request=authorizationContext.getExchange().getRequest();
    URI uri=request.getURI();
    PathMatcher pathMatcher=new AntPathMatcher();
    // 白名单放行
    List<String> ignoreUrls=ignoreUrlsConfig.getUrls();
    for (String ignoreUrl:ignoreUrls){
      if (pathMatcher.match(ignoreUrl,uri.getPath())){
        return Mono.just(new AuthorizationDecision(true));
      }
    }
    // 对应跨域请求放行
    if (request.getMethod()== HttpMethod.OPTIONS){
      return Mono.just(new AuthorizationDecision(true));
    }
    try {
      //不同用户体系登录不允许互相访问
      String token = request.getHeaders().getFirst(AuthConstant.JWT_TOKEN_HEADER);
      if (StrUtil.isEmpty(token)) {
        return Mono.just(new AuthorizationDecision(false));
      }
      String realToken = token.replace(AuthConstant.JWT_TOKEN_PREFIX, "");
      JWSObject jwsObject = JWSObject.parse(realToken);
      String userStr=jwsObject.getPayload().toString();
      UserDto userDto= JSONUtil.toBean(userStr,UserDto.class);
      if (AuthConstant.ADMIN_CLIENT_ID.equals(userDto.getClientId())&&!pathMatcher.match(AuthConstant.ADMIN_URL_PATTERN,uri.getPath())){
        return Mono.just(new AuthorizationDecision(false));
      }
      if (AuthConstant.PORTAL_CLIENT_ID.equals(userDto.getClientId())&&pathMatcher.match(AuthConstant.ADMIN_URL_PATTERN,uri.getPath())){
        return Mono.just(new AuthorizationDecision(false));
      }
    }catch (ParseException e){
        e.printStackTrace();
        return Mono.just(new AuthorizationDecision(false));
    }
    //非管理端路径直接放行
    if (!pathMatcher.match(AuthConstant.ADMIN_URL_PATTERN,uri.getPath())){
      return Mono.just(new AuthorizationDecision(true));
    }
    //管理端路径需要校验
    Map<Object,Object> resourceRolesMap=redisUtil.hmget(AuthConstant.RESOURCE_ROLES_MAP_KEY);
    Iterator<Object> iterator=resourceRolesMap.keySet().iterator();
    List<String> authorities=new ArrayList<>();
    while (iterator.hasNext()){
      String pattern= (String) iterator.next();
      if (pathMatcher.match(pattern,uri.getPath())){
        authorities.addAll(Convert.toList(String.class,resourceRolesMap.get(pattern)));
      }
    }
    authorities=authorities.stream().map(i->i=AuthConstant.AUTHORITY_PREFIX+i).collect(Collectors.toList());
    return mono.filter(Authentication::isAuthenticated)
                .flatMapIterable(Authentication::getAuthorities)
        .map(GrantedAuthority::getAuthority)
        .any(authorities::contains)
        .map(AuthorizationDecision::new)
        .defaultIfEmpty(new AuthorizationDecision(false));
  }
}
