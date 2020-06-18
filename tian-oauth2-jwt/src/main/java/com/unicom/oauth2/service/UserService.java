/**
 * Title: UserService.java Copyright: Copyright (c) 2018 Company: Unicom
 *
 * @author ctf
 * @date 2019年11月20日
 * @version 1.0
 */
package com.unicom.oauth2.service;

import com.unicom.oauth2.entity.Menu;
import com.unicom.oauth2.entity.Role;
import com.unicom.oauth2.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * Title: UserService<／p> Copyright: Copyright (c) 2018 Company: Unicom
 *
 * @author ctf
 * @date 2019年11月20日
 * @version 1.0
 */
@Service
public class UserService implements UserDetailsService {
  private List<User> userList;
  @Autowired private PasswordEncoder passwordEncoder;
  /** 定义角色 */
  public static List<Role> ALL_ROLES = new ArrayList();

  public static List<Role> ADMIN_ROLES = new ArrayList();
  public static List<Role> COMMON_ROLES = new ArrayList();
  /** 定义权限 */
  public static List<Menu> ADMIN_MENUS = new ArrayList();

  public static List<Menu> COMMON_MENUS = new ArrayList();

  static {
    // 定义菜单
    Menu commonMenu1 = new Menu(1, "首页", "/user/test1", 0);
    Menu commonMenu2 = new Menu(2, "服务", "/user/test2", 0);
    Menu commonMenu3 = new Menu(3, "公司", "/user/test3", 0);
    Menu adminMenu = new Menu(4, "系统管理", "/user/test4", 0);
    // 初始化菜单
    ADMIN_MENUS.add(adminMenu);
    ADMIN_MENUS.add(commonMenu1);
    ADMIN_MENUS.add(commonMenu2);
    ADMIN_MENUS.add(commonMenu3);
    COMMON_MENUS.add(commonMenu1);
    COMMON_MENUS.add(commonMenu2);
    COMMON_MENUS.add(commonMenu3);
    // 定义角色
    Role adminRole = new Role(1, "ROLE_ADMIN");
    Role commonRole = new Role(2, "ROLE_COMMON");
    adminRole.setMenus(ADMIN_MENUS);
    commonRole.setMenus(COMMON_MENUS);
    // 初始化角色
    ADMIN_ROLES.add(adminRole);
    ADMIN_ROLES.add(commonRole);
    COMMON_ROLES.add(commonRole);
    ALL_ROLES.add(adminRole);
    ALL_ROLES.add(commonRole);
  }

  @PostConstruct
  private void initData() {
    String password = passwordEncoder.encode("123456");
    userList = new ArrayList<User>();

    userList.add(new User(1, "zs", password, ADMIN_ROLES));
    userList.add(new User(2, "ls", password, COMMON_ROLES));
    userList.add(new User(3, "ww", password, ADMIN_ROLES));
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    List<User> users =
        userList.stream()
            .filter(user -> user.getUsername().equals(username))
            .collect(Collectors.toList());
    System.out.println(users.toString());
    if (!CollectionUtils.isEmpty(users)) {
      return users.get(0);
    }
    throw new UsernameNotFoundException("用户名或密码错误");
  }
}
