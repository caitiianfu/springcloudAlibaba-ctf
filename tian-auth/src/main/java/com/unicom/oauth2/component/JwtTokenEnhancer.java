package com.unicom.oauth2.component;

import com.unicom.oauth2.domain.SecurityUser;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenEnhancer implements TokenEnhancer {

  @Override
  public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
    SecurityUser securityUser=(SecurityUser) authentication.getPrincipal();
    Map<String,Object> info=new HashMap<>();
    //把用户id设置到jwt中
    info.put("id",securityUser.getId());
    info.put("client_id",securityUser.getClientId());
    ((DefaultOAuth2AccessToken)accessToken).setAdditionalInformation(info);
    return accessToken;
  }
}
