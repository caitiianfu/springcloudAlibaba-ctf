package com.unicom.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unicom.admin.bo.AdminUserDetails;
import com.unicom.admin.service.IUmsAdminService;
import com.unicom.admin.service.IUmsResourceService;
import com.unicom.common.exception.Assert;
import com.unicom.generator.entity.UmsAdmin;
import com.unicom.generator.entity.UmsResource;
import com.unicom.generator.mapper.UmsAdminMapper;
import com.unicom.redis.config.RedisService;
import com.unicom.redis.prefix.UserAdminPrefix;
import com.unicom.security.util.JwtTokenUtil;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 *
 * @author ctf
 * @since 2020-05-07
 */
@Slf4j
@Service
public class UmsAdminServiceImpl extends ServiceImpl<UmsAdminMapper, UmsAdmin>
    implements IUmsAdminService {
  @Autowired private UmsAdminMapper umsAdminMapper;
  @Autowired private IUmsResourceService iUmsResourceService;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private JwtTokenUtil jwtTokenUtil;
  @Autowired private RedisService redisService;

  @Override
  public UmsAdmin getAdminByUsername(String username) {
    UmsAdmin umsAdmin =
        redisService.get(UserAdminPrefix.userAdminUsr, username + ":detail", UmsAdmin.class);
    if (umsAdmin != null) {
      return umsAdmin;
    }
    QueryWrapper<UmsAdmin> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("username", username);
    List<UmsAdmin> adminList = umsAdminMapper.selectList(queryWrapper);
    if (adminList != null && adminList.size() > 0) {
      UmsAdmin admin = adminList.get(0);
      redisService.set(UserAdminPrefix.userAdminUsr, admin.getUsername() + ":detail", admin);
      return admin;
    }
    return null;
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    // 获取用户信息

    UmsAdmin umsAdmin = getAdminByUsername(username);
    if (umsAdmin != null) {
      List<UmsResource> resourceList =
          redisService.getList(UserAdminPrefix.userAdminResource, username + ":resource");
      if (resourceList != null && resourceList.size() != 0) {
        return new AdminUserDetails(umsAdmin, resourceList);
      }
      // 获取资源信息
      List<UmsResource> resources = iUmsResourceService.getResourceList(umsAdmin.getId());
      redisService.set(UserAdminPrefix.userAdminResource, username + ":resource", resources);
      return new AdminUserDetails(umsAdmin, resources);
    }
    // 该异常会在EntryPoint进行返回
    throw new UsernameNotFoundException("用户名" + username + "不存在");
  }

  @Override
  public String login(String username, String password) {
    String token = null;

    UserDetails userDetails = loadUserByUsername(username);
    if (!passwordEncoder.matches(password, userDetails.getPassword())) {
      throw new BadCredentialsException("密码错误");
    }
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    token = jwtTokenUtil.generateToken(userDetails);

    return token;
  }

  @Override
  public List<Map<String, Object>> selectByColumn(List<String> ips) {
    Field f;
    for (String s : ips) {
      Class clazz = UmsAdmin.class;
      try {
        clazz.getDeclaredField(s);
      } catch (NoSuchFieldException e) {

        Assert.fail("传参错误");
      }
    }
    return umsAdminMapper.selectByColumn(ips);
  }

  @Override
  public List<Object> selectByColumnObject(List<String> ips) {
    Field f;
    for (String s : ips) {
      Class clazz = UmsAdmin.class;
      try {
        clazz.getDeclaredField(s);
      } catch (NoSuchFieldException e) {
        Assert.fail("传参错误");
      }
    }
    System.out.println("wwww");

    return umsAdminMapper.selectByColumnObject(ips);
  }
}
