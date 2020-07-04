package com.unicom.redistemplate.config;

import com.alibaba.fastjson.JSONObject;
import com.unicom.redistemplate.utils.RedisUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/** @author by ctf */
public class MyInterceptor implements HandlerInterceptor {
  @Autowired private RedisUtil redisUtil;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    String token = request.getParameter("token");
    if (StringUtils.isEmpty(token) || !redisUtil.hasKey(token)) {
      returnJson(request, response);
      return false;
    }

    return true;
  }

  private void returnJson(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    response.setContentType("application/json;charset=utf-8");
    response.setCharacterEncoding("UTF-8");
    PrintWriter out = response.getWriter();
    Map<String, Object> res = new HashMap<>();
    res.put("code", 500);
    res.put("msg", "token不存在");
    out.write(JSONObject.toJSONString(res));
  }

  @Override
  public void postHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      ModelAndView modelAndView)
      throws Exception {}

  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {}
}
