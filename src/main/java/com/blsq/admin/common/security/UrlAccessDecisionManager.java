package com.blsq.admin.common.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;


/**
 * 权限决策
 * 有了权限资源，知道了当前访问的url需要的具体权限，接下来就是决策当前的访问是否能通过权限验证了
 */
@Component
@Slf4j
public class UrlAccessDecisionManager implements AccessDecisionManager {

    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, AuthenticationException {

        Iterator<ConfigAttribute> iterator = collection.iterator();
        while (iterator.hasNext()) {
            if(authentication == null){
                throw new AccessDeniedException("当前访问没有权限");
            }
            ConfigAttribute ca = iterator.next();
            //当前请求需要的权限
            String needRole = ca.getAttribute();

            //当前用户所具有的权限
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
              // System.out.println("---------"+authority.getAuthority()+"----"+needRole);
                if (authority.getAuthority().equals(needRole)) {
                    return;
                }
            }
        }
        log.warn("------- 暂无权限 --------");
        throw new AccessDeniedException("权限不足!");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}