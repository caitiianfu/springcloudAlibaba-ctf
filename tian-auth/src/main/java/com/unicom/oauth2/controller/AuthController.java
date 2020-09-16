package com.unicom.oauth2.controller;

import com.unicom.common.api.ResultUtils;
import com.unicom.common.constant.AuthConstant;
import com.unicom.oauth2.domain.Oauth2TokenDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.security.Principal;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description 自定义oauth2令牌获取接口
 * @param null
 * @return
 * @date 2020/9/6
 * @author ctf
 **/
@RestController
@Api(tags="authController",description = "认证中心授权")
@RequestMapping("/oauth")
public class AuthController {

  @Autowired
  private TokenEndpoint tokenEndpoint;
  @ApiOperation("Oauth2获取token")
  @PostMapping("/token")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "grant_type", value = "授权模式", required = true),
      @ApiImplicitParam(name = "client_id", value = "Oauth2客户端ID", required = true),
      @ApiImplicitParam(name = "client_secret", value = "Oauth2客户端秘钥", required = true),
      @ApiImplicitParam(name = "refresh_token", value = "刷新token"),
      @ApiImplicitParam(name = "username", value = "登录用户名"),
      @ApiImplicitParam(name = "password", value = "登录密码")
  })
  public ResultUtils<Oauth2TokenDto> postAccessToken(Principal principal, Map<String, String> parameters)
      throws HttpRequestMethodNotSupportedException {
    OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
    Oauth2TokenDto oauth2TokenDto = Oauth2TokenDto.builder()
        .token(oAuth2AccessToken.getValue())
        .refreshToken(oAuth2AccessToken.getRefreshToken().getValue())
        .expiresIn(oAuth2AccessToken.getExpiresIn())
        .tokenHead(AuthConstant.JWT_TOKEN_PREFIX).build();
    return ResultUtils.success(oauth2TokenDto);
  }
}
