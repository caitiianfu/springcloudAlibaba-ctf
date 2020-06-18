/**
 * Title: JsonController.java Copyright: Copyright (c) 2018 Company: Unicom
 *
 * @author ctf
 * @date 2019年2月2日
 * @version 1.0
 */
package com.unicom.activiti.controller;

import com.unicom.activiti.service.MiaoService;
import com.unicom.activiti.util.ResultInfo;
import com.unicom.generator.entity.User;
import com.unicom.generator.entity.VacationForm;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Title: JsonController<／p> Copyright: Copyright (c) 2018 Company: Unicom
 *
 * @author ctf
 * @date 2019年2月2日
 * @version 1.0
 */
@RestController
public class JsonController {
  @Autowired private MiaoService miaoService;

  @PostMapping("/login")
  public ResultInfo login(HttpServletRequest request, HttpServletResponse response) {
    ResultInfo result = new ResultInfo();
    String username = request.getParameter("username");
    User user = miaoService.loginSuccess(username);
    if (user != null) {
      result.setCode(200);
      result.setMsg("登录成功");
      result.setInfo(user);
      // 用户信息存放在Cookie中，实际情况下保存在Redis更佳
      Cookie cookie = new Cookie("userInfo", username);
      cookie.setPath("/");
      response.addCookie(cookie);
    } else {
      result.setCode(300);
      result.setMsg("登录名不存在，登录失败");
    }
    return result;
  }

  @GetMapping("/logout")
  public ResultInfo logout(HttpServletRequest request, HttpServletResponse response) {
    ResultInfo result = new ResultInfo();
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("userInfo")) {
          cookie.setValue(null);
          // 立即销毁cookie
          cookie.setMaxAge(0);
          cookie.setPath("/");
          response.addCookie(cookie);
          break;
        }
      }
    }
    result.setCode(200);
    return result;
  }

  // 添加请假单
  @GetMapping("/writeForm")
  public ResultInfo writeForm(HttpServletRequest request) {
    ResultInfo result = new ResultInfo();
    String title = request.getParameter("title");
    String content = request.getParameter("content");
    String operator = request.getParameter("operator");
    VacationForm form = miaoService.writeForm(title, content, operator);
    result.setCode(200);
    result.setMsg("填写请假条成功");
    result.setInfo(form);
    return result;
  }

  // 申请者放弃请假
  @GetMapping("/giveup")
  public ResultInfo giveup(HttpServletRequest request) {
    ResultInfo result = new ResultInfo();
    String formId = request.getParameter("formId");
    String operator = request.getParameter("operator");
    miaoService.completeProcess(formId, operator, "giveup");
    result.setCode(200);
    result.setMsg("放弃请假成功");
    return result;
  }

  // 申请者申请请假
  @GetMapping("/apply")
  public ResultInfo apply(HttpServletRequest request) {
    ResultInfo result = new ResultInfo();
    String formId = request.getParameter("formId");
    String operator = request.getParameter("operator");
    miaoService.completeProcess(formId, operator, "apply");
    result.setCode(200);
    result.setMsg("申请请假成功");
    return result;
  }

  // 审批者审核请假信息
  @GetMapping("/approve")
  public ResultInfo approve(HttpServletRequest request) {
    ResultInfo result = new ResultInfo();
    String formId = request.getParameter("formId");
    String operator = request.getParameter("operator");
    miaoService.approverVacation(formId, operator);
    result.setCode(200);
    result.setMsg("请假审核成功");
    return result;
  }

  // 获取某条请假信息当前状态
  @GetMapping("/currentState")
  public HashMap<String, String> currentState(HttpServletRequest request) {
    String formId = request.getParameter("formId");
    HashMap<String, String> map = new HashMap<String, String>();
    map = miaoService.getCurrentState(formId);
    return map;
  }

  @GetMapping("/historyState")
  public ResultInfo queryHistoricTask(HttpServletRequest request) {
    ResultInfo result = new ResultInfo();
    String formId = request.getParameter("formId");
    List process = miaoService.historyState(formId);
    result.setCode(200);
    result.setInfo(process);
    return result;
  }
}
