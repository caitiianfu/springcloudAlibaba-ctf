package com.unicom.oauth2.config;

import com.unicom.oauth2.component.JwtTokenEnhancer;
import com.unicom.oauth2.service.impl.UserServiceImpl;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

@AllArgsConstructor
@Configuration
@EnableAuthorizationServer
public class Oauth2ServerConfig extends AuthorizationServerConfigurerAdapter {

  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private UserServiceImpl userService;
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private JwtTokenEnhancer jwtTokenEnhancer;

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.inMemory()
        .withClient("admin-app")
        .secret(passwordEncoder.encode("123456"))
        .scopes("all")
        .authorizedGrantTypes("password", "refresh_token")
        .accessTokenValiditySeconds(3600 * 24)
        .refreshTokenValiditySeconds(3600 * 24 * 7)
        .and()
        .withClient("portal-app")
        .secret(passwordEncoder.encode("123456"))
        .scopes("all")
        .authorizedGrantTypes("password", "refresh_token")
        .accessTokenValiditySeconds(3600 * 24)
        .refreshTokenValiditySeconds(3600 * 24 * 7);
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    TokenEnhancerChain chain = new TokenEnhancerChain();
    List<TokenEnhancer> delegates = new ArrayList<>();
    delegates.add(jwtTokenEnhancer);
    delegates.add(jwtAccessTokenConverter());
    chain.setTokenEnhancers(delegates);
    endpoints.authenticationManager(authenticationManager)
        .userDetailsService(userService)
        .accessTokenConverter(jwtAccessTokenConverter())
        .tokenEnhancer(chain);
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    security.allowFormAuthenticationForClients();
  }

  @Bean
  public JwtAccessTokenConverter jwtAccessTokenConverter() {
    JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
    jwtAccessTokenConverter.setKeyPair(keyPair());
    return jwtAccessTokenConverter;
  }

  @Bean
  public KeyPair keyPair() {
    KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt-sign.jks"),
        "123456".toCharArray());
    return keyStoreKeyFactory.getKeyPair("jwt-sign", "123456".toCharArray());
  }
}
