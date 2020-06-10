package com.unicom.activiti.controller;

import com.unicom.activiti.entity.User;
import com.unicom.activiti.service.UserService;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/** @author Created by yawn on 2018-01-08 13:39 */
@Controller
public class LoginController {

  @Resource private UserService userService;

  @PostMapping("/login")
  @ResponseBody
  public boolean login(HttpSession session, @RequestBody User user) {
    String userName = user.getUserName();
    String password = user.getPassword();
    boolean login = userService.login(userName, password);
    if (login) {
      session.setAttribute("userName", userName);
      return true;
    }
    return false;
  }

  @GetMapping("/exit")
  public String exit(HttpSession session) {
    session.removeAttribute("userName");
    return "login";
  }
}
