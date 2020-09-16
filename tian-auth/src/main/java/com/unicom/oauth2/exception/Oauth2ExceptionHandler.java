package com.unicom.oauth2.exception;

import com.unicom.common.api.ResultUtils;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice public class Oauth2ExceptionHandler {
  @ExceptionHandler(value = OAuth2Exception.class)
  public ResultUtils handleOauth2(OAuth2Exception e){
      return ResultUtils.failedMsg(e.getMessage());
  }

}
