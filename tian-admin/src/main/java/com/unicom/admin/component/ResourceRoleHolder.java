package com.unicom.admin.component;

import com.unicom.admin.service.IUmsRoleResourceRelationService;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResourceRoleHolder {
    @Autowired
    private IUmsRoleResourceRelationService iUmsRoleResourceRelationService;
    @PostConstruct
    public  void  initResourceRolesMap(){
      iUmsRoleResourceRelationService.initResourceRolesMap();
    }
}
