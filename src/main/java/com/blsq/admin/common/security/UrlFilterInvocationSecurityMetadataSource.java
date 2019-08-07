package com.blsq.admin.common.security;


import com.blsq.admin.common.constant.SecurityConstants;
import com.blsq.admin.common.security.service.BlsqUser;
import com.blsq.admin.common.util.SecurityUtils;
import com.blsq.admin.common.vo.MenuRoleVO;
import com.blsq.admin.common.vo.SysRoleVO;
import com.blsq.admin.service.ISysRoleMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


/**
 * ***  权限资源   ***
 * 主要责任就是当访问一个url时返回这个url所需要的访问权限。
 */
@Component
@Slf4j
public class UrlFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private ISysRoleMenuService roleMenuService;
    @Autowired
    private  CacheManager cacheManager;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 方法返回本次访问需要的权限，可以有多个权限。在上面的实现中如果没有匹配的url直接返回null，
     * 也就是没有配置权限的url默认都为白名单，想要换成默认是黑名单只要修改这里即可。
     * @param o
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {

        //获取请求地址
        String requestUrls = ((FilterInvocation) o).getRequestUrl();
        String requestUrl="";
        if(requestUrls.indexOf("?")!=-1){
            requestUrl= requestUrls.substring(0,requestUrls.indexOf("?"));
        }else{
            requestUrl=requestUrls;
        }
        log.info("-------------请求接口-----------"+requestUrl);
        //((FilterInvocation) o).getRequest();

        List<MenuRoleVO> allMenu;
        Cache cache = cacheManager.getCache("menu_role_details");
        if (cache != null && cache.get("menuLists") != null) {
            allMenu = (List<MenuRoleVO>)cache.get("menuLists").get();
        }else{
            allMenu = roleMenuService.getAllMenu();
            cache.put("menuLists", allMenu);
        }

        for (MenuRoleVO menu : allMenu) {
            if(antPathMatcher.match(menu.getUrl(),requestUrl)){
                List<SysRoleVO> roles = menu.getRoleList();
                if(roles.size()<=0){
                    return SecurityConfig.createList("ROLE_NODE");
                }
                List<String> roleList = roles.stream().map(roleVO -> SecurityConstants.ROLE + roleVO.getRoleId()).collect(Collectors.toList());
                return SecurityConfig.createList(roleList.stream().toArray(String[]::new));
            }
        }
        //没有匹配上的资源，都是登录访问
        //return SecurityConfig.createList("ROLE_NODE");
       return null;
    }

    /**
     * 方法如果返回了所有定义的权限资源，Spring Security会在启动时校验每个ConfigAttribute是否配置正确，
     * 不需要校验直接返回null。
     * @return
     */
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    /**
     * 方法返回类对象是否支持校验，web项目一般使用FilterInvocation来判断，或者直接返回true
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }
}
