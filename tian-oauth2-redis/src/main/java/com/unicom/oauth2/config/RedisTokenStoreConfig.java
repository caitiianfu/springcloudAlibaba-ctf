/**
 * Title: RedisTokenStoreConfig.java Copyright: Copyright (c) 2018 Company: Unicom
 *
 * @author ctf
 * @date 2019年11月20日
 * @version 1.0
 */
package com.unicom.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

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
public class RedisTokenStoreConfig {
  @Autowired private RedisConnectionFactory redisConnectionFactory;

  @Bean
  public TokenStore redisTokenStore() {
    return new RedisTokenStore(redisConnectionFactory);
  }
}
