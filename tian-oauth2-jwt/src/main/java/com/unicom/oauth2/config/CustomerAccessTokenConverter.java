package com.unicom.oauth2.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

/** @Author: ctf.th @Date: 2019/1/21 18:32 */
public class CustomerAccessTokenConverter implements TokenEnhancer {
  /**
   * 用户表添加 salt字段，每次登录后，随机生成，并用于生成token的认证数据，这样可以做到废弃旧Token，使用旧token访问将提示已失效！
   *
   * @param accessToken
   * @param authentication
   * @return
   */
  @Override
  public OAuth2AccessToken enhance(
      OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
    final Map<String, Object> additionalInfo = new HashMap<>();
    UserDetails user = (UserDetails) authentication.getUserAuthentication().getPrincipal();
    additionalInfo.put("salt", "123");
    ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
    return accessToken;
  }
}
