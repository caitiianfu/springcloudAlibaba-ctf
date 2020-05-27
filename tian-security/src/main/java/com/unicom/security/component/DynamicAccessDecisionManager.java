package com.unicom.security.component;

import cn.hutool.core.collection.CollUtil;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Iterator;

/**
 * 权限决策管理，判断用户是否有权限
 */
public class DynamicAccessDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        //接口未配置资源则放行
        if (CollUtil.isEmpty(configAttributes)){
                return;
            }
        Iterator<ConfigAttribute> iterator=configAttributes.iterator();
        while (iterator.hasNext()){
            ConfigAttribute configAttribute=iterator.next();
            //当访问所需资源与用户资源对比
            String needAuthority=configAttribute.getAttribute();
            for (GrantedAuthority grantedAuthority:authentication.getAuthorities()){
                if (needAuthority.trim().equals(grantedAuthority.getAuthority())) {
                    return;
                }
                }
            }
            throw  new AccessDeniedException("您没有访问权限");

    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
