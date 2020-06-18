/**
 * Title: AuthorizationServerConfig.java Copyright: Copyright (c) 2018 Company: Unicom
 *
 * @author ctf
 * @date 2019年11月20日
 * @version 1.0
 */
package com.unicom.oauth2.config;

import com.unicom.oauth2.service.UserService;
import java.util.Arrays;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * Title: AuthorizationServerConfig<／p> Copyright: Copyright (c) 2018 Company: Unicom
 *
 * @author ctf
 * @date 2019年11月20日
 * @version 1.0
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerRedisConfig extends AuthorizationServerConfigurerAdapter {

  @Autowired private PasswordEncoder passwordEncoder;

  @Autowired private AuthenticationManager authenticationManager;

  @Autowired private UserService userService;

  @Autowired
  /*1.redis配置*/
  @Qualifier("jwtTokenStore")
  private TokenStore tokenStore;

  @Autowired private DataSource dataSource;

  @Autowired private AuthorizationCodeServices authorizationCodeServices;
  @Autowired private WebResponseExceptionTranslator webResponseExceptionTranslator;

  @Autowired private JwtAccessTokenConverter jwtAccessTokenConverter;

  // 这个是定义授权的请求的路径的Bean  从数据库读取
  @Bean
  public ClientDetailsService clientDetails() {
    JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
    jdbcClientDetailsService.setPasswordEncoder(passwordEncoder);
    return jdbcClientDetailsService;
  }

  /**
   * 授权码修改 jdbc形式
   *
   * @param dataSource
   * @return
   */
  @Bean
  public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource) {
    return new JdbcAuthorizationCodeServices(dataSource);
  }

  /* 密码	模式配置
   *  * 密码模式下配置认证管理器 AuthenticationManager,并且设置 AccessToken的存储介质tokenStore,如果不设置，则会默认使用内存当做存储介质。
   * 而该AuthenticationManager将会注入 2个Bean对象用以检查(认证)
   * 1、ClientDetailsService的实现类 JdbcClientDetailsService (检查 ClientDetails 对象)
   * 2、UserDetailsService的实现类 CustomUserDetailsService (检查 UserDetails 对象)
   * */
  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    // 添加JWT的额外信息
    TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
    tokenEnhancerChain.setTokenEnhancers(
        Arrays.asList(new CustomerAccessTokenConverter(), jwtAccessTokenConverter));
    endpoints.tokenEnhancer(tokenEnhancerChain);
    /*jwt配置*/
    endpoints
        // 密码模式需要
        .authenticationManager(authenticationManager)
        .accessTokenConverter(jwtAccessTokenConverter)
        .tokenEnhancer(tokenEnhancerChain)
        // 授权码从jdbc获得
        .authorizationCodeServices(authorizationCodeServices)
        // refresh_token需要
        .userDetailsService(userService)
        // token存储jwt
        .tokenStore(tokenStore);
    // 自定义登录或者鉴权失败时的返回信息
    endpoints.exceptionTranslator(webResponseExceptionTranslator);
  }

  /**
   * 配置 oauth_client_details【client_id和client_secret等】信息的认证【检查ClientDetails的合法性】服务 设置 认证信息的来源：数据库
   * (可选项：数据库和内存,使用内存一般用来作测试) 自动注入：ClientDetailsService的实现类 JdbcClientDetailsService (检查
   * ClientDetails 对象)
   */
  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    /*  clients
            .inMemory()
            .withClient("client")
            .secret(passwordEncoder.encode("123456"))
            // token存一天，默认12个小时
            .accessTokenValiditySeconds(60 * 60 * 12)
            // refresh_token存一周，默认30天
            .refreshTokenValiditySeconds(60 * 60 * 24 * 7)
            .redirectUris("http://www.baidu.com")
            .autoApprove(true) // 自动授权配置
            .scopes("all")
            .authorizedGrantTypes("authorization_code", "password", "refresh_token");
    */
    // 默认值InMemoryTokenStore对于单个服务器是完全正常的（即，在发生故障的情况下，低流量和热备份备份服务器）。大多数项目可以从这里开始，也可以在开发模式下运行，以便轻松启动没有依赖关系的服务器。
    // 这JdbcTokenStore是同一件事的JDBC版本，它将令牌数据存储在关系数据库中。如果您可以在服务器之间共享数据库，则可以使用JDBC版本，如果只有一个，则扩展同一服务器的实例，或者如果有多个组件，则授权和资源服务器。要使用JdbcTokenStore你需要“spring-jdbc”的类路径。

    // 这个地方指的是从jdbc查出数据来存储
    clients.jdbc(dataSource);
  }

  /**
   * 注意，自定义TokenServices的时候，需要设置@Primary，否则报错，
   *
   * @return
   */
  /*@Primary
  @Bean
  public DefaultTokenServices defaultTokenServices() {
    DefaultTokenServices tokenServices = new DefaultTokenServices();
    tokenServices.setTokenStore(tokenStore);
    tokenServices.setSupportRefreshToken(true);
    // token有效期自定义设置，默认12小时
    tokenServices.setAccessTokenValiditySeconds(60 * 60 * 12);
    // refresh_token默认30天
    tokenServices.setRefreshTokenValiditySeconds(60 * 60 * 24 * 7);
    return tokenServices;
  }*/

  /**
   * 配置：安全检查流程 默认过滤器：BasicAuthenticationFilter
   * 1、oauth_client_details表中clientSecret字段加密【ClientDetails属性secret】 2、CheckEndpoint类的接口
   * oauth/check_token 无需经过过滤器过滤，默认值：denyAll()
   */
  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    security.allowFormAuthenticationForClients(); // 允许客户表单认证
    security.tokenKeyAccess("permitAll()");
    security.passwordEncoder(passwordEncoder); // 设置oauth_client_details中的密码编码器
    security.checkTokenAccess("permitAll()");
  }
}
