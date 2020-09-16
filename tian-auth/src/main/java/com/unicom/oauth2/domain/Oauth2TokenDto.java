package com.unicom.oauth2.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Oauth2TokenDto {
  private String token;
  private String refreshToken;
  private String tokenHead;
  private int expiresIn;
}
