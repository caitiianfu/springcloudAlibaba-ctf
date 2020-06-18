/**
 * Title: RedisTokenStoreConfig.java Copyright: Copyright (c) 2018 Company: Unicom
 *
 * @author ctf
 * @date 2019年11月20日
 * @version 1.0
 */
package com.unicom.oauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * 用redis存储token
 *
 * <p>Title: RedisTokenStoreConfig<／p> Copyright: Copyright (c) 2018 Company: Unicom
 *
 * @author ctf
 * @date 2019年11月20日
 * @version 1.0
 */
@Configuration
public class JwtTokenStoreConfig {

  /**
   * 对jwt增加一个秘钥 JwtAccessTokenConverter：对jwt编码以及解码的类
   *
   * @return
   */
  @Bean
  public JwtAccessTokenConverter jwtAccessTokenConverter() {
    JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
    jwtAccessTokenConverter.setSigningKey("test-jwt");
    return jwtAccessTokenConverter;
  }

  @Bean
  public TokenStore jwtTokenStore() {
    return new JwtTokenStore(jwtAccessTokenConverter());
  }
}
