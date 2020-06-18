/**
 * Title: ResourceServerConfig.java Copyright: Copyright (c) 2018 Company: Unicom
 *
 * @author ctf
 * @date 2019年11月20日
 * @version 1.0
 */
package com.unicom.oauth2.config;

import com.unicom.oauth2.filter.MyAccessDecisionManager;
import com.unicom.oauth2.filter.MyInvocationSecurityMetadataSourceService;
import com.unicom.oauth2.handle.MyAccessDeniedHandler;
import com.unicom.oauth2.handle.MyAuthenticationEntryPointHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * Title: ResourceServerConfig<／p> Copyright: Copyright (c) 2018 Company: Unicom 配置会报错，目前没有用
 *
 * @author ctf
 * @date 2019年11月20日
 * @version 1.0
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
  @Autowired private AuthenticationManager authenticationManager;
  @Autowired private MyAccessDecisionManager accessDecisionManager;
  @Autowired private MyInvocationSecurityMetadataSourceService securityMetadataSourceService;
  @Autowired private MyAuthenticationEntryPointHandler myAuthenticationEntryPointHandler;
  @Autowired private MyAccessDeniedHandler myAccessDeniedHandler;

  @Autowired private PasswordEncoder passwordEncoder;

  @Autowired private TokenStore jwtTokenStore;
  /**
   * 资源服务器认证的配置： 1、设置资源服务器的标识，从配置文件中读取自定义资源名称 2、设置Access Token的数据源(默认内存中)，本项目使用 redis，所以需要配置
   *
   * @param resources
   */
  @Override
  public void configure(ResourceServerSecurityConfigurer resources) {
    resources.resourceId("resource").tokenStore(jwtTokenStore);
  }

  /**
   * 这里设置需要token验证的url * 这些url可以在WebSecurityConfigurerAdapter中排查掉， * 对于相同的url，如果二者都配置了验证 *
   * 则优先进入ResourceServerConfigurerAdapter,进行token验证。而不会进行 * WebSecurityConfigurerAdapter 的 basic
   * auth或表单认证。
   *
   * @param http
   * @throws Exception
   */
  @Override
  public void configure(HttpSecurity http) throws Exception {

    http.requestMatchers()
        .antMatchers("/user/**")
        .and()
        .authorizeRequests()
        .antMatchers("/user/**")
        .authenticated();
    /*  .and()
            .exceptionHandling()
            .authenticationEntryPoint(myAuthenticationEntryPointHandler)
            .accessDeniedHandler(myAccessDeniedHandler);
    */
    // 配置过滤器的顺序
    //  http.addFilterAfter(filterSecurityInterceptor(), FilterSecurityInterceptor.class);
  }

  /* private MyFilterSecurityInterceptor filterSecurityInterceptor() {
    MyFilterSecurityInterceptor interceptor = new MyFilterSecurityInterceptor();
    interceptor.setAuthenticationManager(authenticationManager);
    interceptor.setAccessDecisionManager(accessDecisionManager);
    interceptor.setSecurityMetadataSource(securityMetadataSourceService);
    return interceptor;
  }*/
}
