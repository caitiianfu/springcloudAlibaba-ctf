package com.unicom.oauth2.handle;

import com.unicom.oauth2.utils.ResponseUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/** @Description: 认证失败处理类 在访问一个受保护的资源，用户没有通过登录认证 @Author: zule @Date: 2019/5/7 */
@Component
@Slf4j
public class MyAuthenticationEntryPointHandler implements AuthenticationEntryPoint {
  @Override
  public void commence(
      HttpServletRequest httpServletRequest,
      HttpServletResponse response,
      AuthenticationException e)
      throws IOException, ServletException {
    log.info("认证失败处理： {}", e);
    StringBuffer msg = new StringBuffer("请求访问: ");
    msg.append(httpServletRequest.getRequestURI()).append(" 接口，因为认证失败，无法访问系统资源.");
    ResponseUtil.out(response, ResponseUtil.resultMap(403, msg.toString()));
  }
}
