package com.unicom.redistemplate.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** @author by ctf */
@Controller
public class TestController {

  @GetMapping("/login/callback")
  public String login(HttpServletRequest request) {
    String code = request.getParameter("code");
    return "1";
  }
}
