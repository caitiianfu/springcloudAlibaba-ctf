package com.unicom.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unicom.admin.service.IUmsRoleResourceRelationService;
import com.unicom.common.constant.AuthConstant;
import com.unicom.common.utils.RedisUtil;
import com.unicom.generator.entity.UmsResource;
import com.unicom.generator.entity.UmsRole;
import com.unicom.generator.entity.UmsRoleResourceRelation;
import com.unicom.generator.mapper.UmsResourceMapper;
import com.unicom.generator.mapper.UmsRoleMapper;
import com.unicom.generator.mapper.UmsRoleResourceRelationMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ctf
 * @since 2020-05-07
 */
@Service
public class UmsRoleResourceRelationServiceImpl extends ServiceImpl<UmsRoleResourceRelationMapper, UmsRoleResourceRelation> implements IUmsRoleResourceRelationService {
  @Resource
  private UmsResourceMapper umsResourceMapper;
  @Resource
  private UmsRoleMapper umsRoleMapper;
  @Resource
  private UmsRoleResourceRelationMapper umsRoleResourceRelationMapper;
  @Autowired
  private RedisUtil redisUtil;
  @Value("${spring.application.name}")
  private String applicationName;

  @Override
  public Map<String, List<String>> initResourceRolesMap() {
    Map<String,List<String>> resourceRoleMap=new HashMap<>();
    List<UmsRole> umsRoles=umsRoleMapper.selectList(null);
    List<UmsResource> umsResources=umsResourceMapper.selectList(null);
    List<UmsRoleResourceRelation> umsRoleResourceRelations=umsRoleResourceRelationMapper.selectList(null);
    for (UmsResource resource:umsResources){
      Set<Long> roleIds=
          umsRoleResourceRelations.stream()
              .filter(item->resource.getId().equals(item.getResourceId()))
              .map(UmsRoleResourceRelation::getRoleId).collect(Collectors.toSet());
      List<String> roleList=umsRoles.stream()
          .filter(item->roleIds.contains(item.getId())).map(UmsRole::getName).collect(Collectors.toList());
      resourceRoleMap.put("/"+applicationName+resource.getUrl(),roleList);
    }
    redisUtil.del(AuthConstant.RESOURCE_ROLES_MAP_KEY);
    redisUtil.hmset(AuthConstant.RESOURCE_ROLES_MAP_KEY,resourceRoleMap);
    return resourceRoleMap;
  }
}
