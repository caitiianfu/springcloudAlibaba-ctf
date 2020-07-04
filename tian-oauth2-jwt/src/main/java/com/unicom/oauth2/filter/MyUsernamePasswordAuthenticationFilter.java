/**
 * Title: MyUsernamePasswordAuthenticationFilter.java Copyright: Copyright (c) 2018 Company: Unicom
 *
 * @author ctf
 * @date 2020年2月12日
 * @version 1.0
 */
package com.unicom.oauth2.filter;

import cn.hutool.json.JSONObject;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

/**
 * Title: MyUsernamePasswordAuthenticationFilter<／p> Copyright: Copyright (c) 2018 Company: Unicom
 *
 * @author ctf
 * @date 2020年2月12日
 * @version 1.0
 */
public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  public MyUsernamePasswordAuthenticationFilter(
      AuthenticationManager aManager,
      AuthenticationSuccessHandler successHandler,
      AuthenticationFailureHandler failureHandler) {
    this.setFilterProcessesUrl("/security/login"); // 这句代码很重要，设置登陆的url 要和 WebSecurityConfig
    // 配置类中的.loginProcessingUrl("/auth/v1/api/login/entry") 一致，如果不配置则无法执行
    // 重写的attemptAuthentication
    // 方法里面而是执行了父类UsernamePasswordAuthenticationFilter的attemptAuthentication（）
    this.setAuthenticationManager(aManager); // AuthenticationManager 是必须的
    this.setAuthenticationSuccessHandler(successHandler); // 设置自定义登陆成功后的业务处理
    this.setAuthenticationFailureHandler(failureHandler); // 设置自定义登陆失败后的业务处理
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    // 校验验证码
    String verifyCode = request.getParameter("verifyCode");
    if (!checkValidateCode(verifyCode)) {
      try {
        PrintWriter out = response.getWriter();
        JSONObject json = new JSONObject();
        json.put("code", 402);
        json.put("msg", "验证码出错");
        out.write(json.toString());

      } catch (IOException e) {
        e.printStackTrace();
      }
      return null;
    }
    // 设置username的字段,js传后端的字段
    this.setUsernameParameter("uname");
    // 设置password的字段，js传后端的字段
    this.setPasswordParameter("pwd");
    return super.attemptAuthentication(request, response);
  }
  /** 校验验证码 */
  private boolean checkValidateCode(String verifyCode) {
    if (StringUtils.isEmpty(verifyCode) || !"1234".equals(verifyCode)) {
      return false;
    }
    return true;
  }
}
