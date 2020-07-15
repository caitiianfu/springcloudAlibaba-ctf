package com.unicom.admin.config;


import com.unicom.admin.service.IUmsAdminService;
import com.unicom.admin.service.IUmsResourceService;
import com.unicom.admin.service.IUmsRoleService;
import com.unicom.generator.entity.UmsResource;
import com.unicom.security.component.DynamicSecurityService;
import com.unicom.security.config.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class TianSecurityConfig extends SecurityConfig {
    @Autowired
    private IUmsAdminService iUmsAdminService;
    @Autowired
    private IUmsResourceService iUmsResourceService;
    @Override
    @Bean
    @Order
    public UserDetailsService userDetailsService(){
        //获得登录信息  两种方法等价
        return new UserDetailsService() {
            @Override
           public UserDetails loadUserByUsername(String username) {
                return iUmsAdminService.loadUserByUsername(username);
            }
        };
        //return username -> iUmsAdminService.loadUserByUsername(username);
    }

    @Bean
    public DynamicSecurityService dynamicSecurityService(){
        return ()->{
            Map<String, ConfigAttribute> map=new ConcurrentHashMap<>(64);
            List<UmsResource> resourceList = iUmsResourceService.listAll();
            for (UmsResource resource : resourceList) {
                map.put(resource.getUrl(), new org.springframework.security.access.SecurityConfig(resource.getId() + ":" + resource.getName()));
            }
            return map;
        };
    }
}
