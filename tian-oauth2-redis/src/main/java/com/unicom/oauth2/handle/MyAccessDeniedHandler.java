package com.unicom.oauth2.handle;

import com.unicom.oauth2.utils.ResponseUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/** @Description: 权限不足处理类 自定义权限不足需要做的业务操作 包括：处理权限不足的逻辑 @Author: zule @Date: 2019/5/6 */
@Component
@Slf4j
public class MyAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException) {
    log.error("权限不足出错 {}", accessDeniedException);
    StringBuffer msg = new StringBuffer("请求: ");
    msg.append(request.getRequestURI()).append(" 权限不足，无法访问该资源.");

    ResponseUtil.out(response, ResponseUtil.resultMap(402, "权限不足，无法访问该资源"));
  }
}
