/**
 * Title: SecurityConfig.java Copyright: Copyright (c) 2018 Company: Unicom
 *
 * @author ctf
 * @date 2019年11月20日
 * @version 1.0
 */
package com.unicom.oauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Title: SecurityConfig<／p> Copyright: Copyright (c) 2018 Company: Unicom
 *
 * @author ctf
 * @date 2019年11月20日
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  /**
   * anyRequest | 匹配所有请求路径 access | SpringEl表达式结果为true时可以访问 anonymous | 匿名可以访问 denyAll | 用户不能访问
   * fullyAuthenticated | 用户完全认证可以访问（非remember-me下自动登录） hasAnyAuthority | 如果有参数，参数表示权限，则其中任何一个权限可以访问
   * hasAnyRole | 如果有参数，参数表示角色，则其中任何一个角色可以访问 hasAuthority | 如果有参数，参数表示权限，则其权限可以访问 hasIpAddress |
   * 如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问 hasRole | 如果有参数，参数表示角色，则其角色可以访问 permitAll | 用户可以任意访问
   * rememberMe | 允许通过remember-me登录的用户访问 authenticated | 用户登录后可访问
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    // 由于使用的是JWT，我们这里不需要csrf
    // 必须配置，不然OAuth2的http配置不生效----不明觉厉
    http.csrf().disable();
    // http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.requestMatchers()
        .antMatchers("/oauth/**", "/login", "/login-error")
        .and()
        .authorizeRequests()
        .antMatchers("/oauth/**")
        .authenticated()
        .and()
        .formLogin(); // .loginPage( "/login" ).failureUrl( "/login-error" );
  }
}
