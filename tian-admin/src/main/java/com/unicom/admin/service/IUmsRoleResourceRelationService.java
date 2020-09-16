package com.unicom.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.unicom.generator.entity.UmsRoleResourceRelation;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ctf
 * @since 2020-05-07
 */
public interface IUmsRoleResourceRelationService extends IService<UmsRoleResourceRelation> {
  public Map<String, List<String>> initResourceRolesMap() ;

}
