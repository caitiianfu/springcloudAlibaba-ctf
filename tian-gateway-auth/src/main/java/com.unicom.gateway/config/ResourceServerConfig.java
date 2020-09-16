package com.unicom.gateway.config;

import cn.hutool.core.util.ArrayUtil;
import com.unicom.common.constant.AuthConstant;
import com.unicom.gateway.authorization.AuthorizationManager;
import com.unicom.gateway.component.RestAuthenticationEntryPoint;
import com.unicom.gateway.component.RestfulAccessDeniedHandler;
import com.unicom.gateway.filter.IgnoreUrlsRemoveJwtFilter;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties.Reactive;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Configuration
@EnableWebFluxSecurity
public class ResourceServerConfig {
    private final AuthorizationManager authorizationManager;
    private final IgnoreUrlsConfig ignoreUrlsConfig;
    private final RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final IgnoreUrlsRemoveJwtFilter ignoreUrlsRemoveJwtFilter;
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http){
      http.oauth2ResourceServer().jwt()
            .jwtAuthenticationConverter(jwtAuthenticationConverter());
      //自定义处理jwt请求头过期或签名错误的结果
      http.oauth2ResourceServer().authenticationEntryPoint(restAuthenticationEntryPoint);
      //对白名单路径直接移除请求头
      http.addFilterBefore(ignoreUrlsRemoveJwtFilter, SecurityWebFiltersOrder.AUTHENTICATION);
      http.authorizeExchange()
          .pathMatchers(ArrayUtil.toArray(ignoreUrlsConfig.getUrls(),String.class)).permitAll()  //白名单放过
          .anyExchange().access(authorizationManager)
          .and().exceptionHandling()
          .accessDeniedHandler(restfulAccessDeniedHandler)  //处理未授权
          .authenticationEntryPoint(restAuthenticationEntryPoint)  //处理未认证
          .and()
          .csrf()
          .disable();
return http.build();

    }
    @Bean
    public Converter<Jwt,? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter(){
      JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter=new JwtGrantedAuthoritiesConverter();
      jwtGrantedAuthoritiesConverter.setAuthorityPrefix(AuthConstant.AUTHORITY_PREFIX);
      jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(AuthConstant.AUTHORITY_CLAIM_NAME);
      JwtAuthenticationConverter jwtAuthenticationConverter=new JwtAuthenticationConverter();
      jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
      return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
}
