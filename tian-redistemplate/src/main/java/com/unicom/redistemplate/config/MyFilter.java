package com.unicom.redistemplate.config;

import com.alibaba.fastjson.JSONObject;
import com.unicom.redistemplate.utils.RedisUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/** @author by ctf */
@Component
public class MyFilter implements Filter {

  // 过滤器无法获得ioc
  @Autowired private RedisUtil redisUtil;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    String token = request.getParameter("token");
    if (StringUtils.isEmpty(token) || !redisUtil.hasKey(token)) {
      returnJson(request, response);
      return;
    }

    chain.doFilter(request, response);
  }

  private void returnJson(ServletRequest request, ServletResponse response) throws IOException {
    response.setContentType("application/json;charset=utf-8");
    response.setCharacterEncoding("UTF-8");
    PrintWriter out = response.getWriter();
    Map<String, Object> res = new HashMap<>();
    res.put("code", 500);
    res.put("msg", "token不存在");
    out.write(JSONObject.toJSONString(res));
  }
}
