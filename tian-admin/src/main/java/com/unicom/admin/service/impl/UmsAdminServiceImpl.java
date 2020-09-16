package com.unicom.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.BeanProperty;
import com.unicom.admin.service.AuthService;
import com.unicom.admin.service.IUmsAdminRoleRelationService;
import com.unicom.admin.service.IUmsAdminService;
import com.unicom.admin.service.IUmsResourceService;
import com.unicom.admin.service.IUmsRoleService;
import com.unicom.common.api.ResultUtils;
import com.unicom.common.constant.AuthConstant;
import com.unicom.common.domain.UserDto;
import com.unicom.common.exception.Assert;
import com.unicom.generator.entity.UmsAdmin;
import com.unicom.generator.entity.UmsAdminRoleRelation;
import com.unicom.generator.entity.UmsResource;
import com.unicom.generator.entity.UmsRole;
import com.unicom.generator.mapper.UmsAdminMapper;
import com.unicom.generator.mapper.UmsAdminRoleRelationMapper;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
  @Autowired private IUmsRoleService umsRoleService;
  @Autowired private IUmsResourceService iUmsResourceService;
  //@Autowired private RedisService redisService;
  @Autowired private IUmsAdminRoleRelationService iUmsAdminRoleRelationService;
  @Autowired private AuthService authService;
  @Override
  public UmsAdmin getAdminByUsername(String username) {
//    UmsAdmin umsAdmin =
//        redisService.get(UserAdminPrefix.userAdminUsr, username + ":detail", UmsAdmin.class);
//    if (umsAdmin != null) {
//      return umsAdmin;
//    }
    QueryWrapper<UmsAdmin> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("username", username);
    List<UmsAdmin> adminList = umsAdminMapper.selectList(queryWrapper);
    if (adminList != null && adminList.size() > 0) {
      UmsAdmin admin = adminList.get(0);
     // redisService.set(UserAdminPrefix.userAdminUsr, admin.getUsername() + ":detail", admin);
      return admin;
    }
    return null;
  }

  @Override
  public UserDto loadUserByUsername(String username) {
    //获得用户信息
    UmsAdmin umsAdmin=getAdminByUsername(username);
    if (umsAdmin!=null){
      Wrapper<UmsAdminRoleRelation> umsAdminRoleRelationWrapper=
      Wrappers.
          <UmsAdminRoleRelation>lambdaQuery()
          .eq(UmsAdminRoleRelation::getAdminId,umsAdmin.getId());
      List<UmsAdminRoleRelation> adminRoleRelations=iUmsAdminRoleRelationService.list(umsAdminRoleRelationWrapper);
      List<Long> roleIds=adminRoleRelations.stream().map(UmsAdminRoleRelation::getId).collect(Collectors.toList());
      List<UmsRole> roleList=umsRoleService.list(null);
      List<String> roleNames=roleList.stream().
          filter(item->roleIds.contains(item.getId()))
          .map(item->item.getId()+":"+item.getName()).collect(Collectors.toList());
      UserDto userDto=new UserDto();
      BeanUtils.copyProperties(umsAdmin,userDto);
      userDto.setRoles(roleNames);
      return userDto;
    }
    return null;
  }

 /* @Override
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
  }*/

//  @Override
//  public String login(String username, String password) {
//    String token = null;
//
//    UserDetails userDetails = null;
//        //loadUserByUsername(username);
//    if (!passwordEncoder.matches(password, userDetails.getPassword())) {
//      throw new BadCredentialsException("密码错误");
//    }
//    UsernamePasswordAuthenticationToken authenticationToken =
//        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//    token = jwtTokenUtil.generateToken(userDetails);
//
//    return token;
//  }

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

  @Override
  public ResultUtils login(String username, String password) {
    if (StrUtil.isEmpty(username)||StrUtil.isEmpty(password)){
      Assert.fail("用户名或密码不能为空");
    }
    Map<String,String> params=new HashMap<>();
    params.put("client_id", AuthConstant.ADMIN_CLIENT_ID);
    params.put("client_secret","123456");
    params.put("grant_type","password");
    params.put("username",username);
    params.put("password",password);
    ResultUtils result=authService.getAccessToken(params);
    return result;
  }
}
