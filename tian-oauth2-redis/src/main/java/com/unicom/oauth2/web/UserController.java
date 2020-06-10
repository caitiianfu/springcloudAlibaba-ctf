/**
 * Title: UserController.java Copyright: Copyright (c) 2018 Company: Unicom
 *
 * @author ctf
 * @date 2019年11月21日
 * @version 1.0
 */
package com.unicom.oauth2.web;

import cn.hutool.core.util.StrUtil;
import com.unicom.common.api.ResultUtils;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Title: UserController<／p> Copyright: Copyright (c) 2018 Company: Unicom
 *
 * @author ctf
 * @date 2019年11月21日
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {
  // jwt解析用户
  @GetMapping("/getCurrentUser")
  public Object getCurrentUser(HttpServletRequest request, Authentication authentication) {
    String header = request.getHeader("Authorization");
    String token = StrUtil.subAfter(header, "bearer ", false);
    System.out.println(token);
    return Jwts.parser()
        .setSigningKey("test-key".getBytes(StandardCharsets.UTF_8))
        .parseClaimsJws(token)
        .getBody();
  }
  // 该模式用redis获得是用户详细信息，用jwt获得的是用户名
  @GetMapping("/getCurrentUserRedisOrJwt")
  public Object getCurrentUserRedisOrJwt(
      HttpServletRequest request, Authentication authentication) {
    return authentication.getPrincipal();
  }

  @GetMapping("/test1")
  public ResultUtils test1() {
    return ResultUtils.success("test1");
  }

  @GetMapping("/test2")
  public ResultUtils test2() {
    return ResultUtils.success("test2");
  }

  @GetMapping("/test3")
  public ResultUtils test3() {
    return ResultUtils.success("test3");
  }

  @GetMapping("/test4")
  public ResultUtils test4() {
    return ResultUtils.success("test4");
  }

  @GetMapping("/login")
  public ResultUtils login() {
    return ResultUtils.success("test4");
  }
}
