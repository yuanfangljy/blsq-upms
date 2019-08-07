/**
 * Copyright (c) 2019-2030, 云宝网络科技有限公司 All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * <p>
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the 云宝网络科技有限公司 developer nor the names of its
 * 注意：本内容仅限于云宝网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 * Author: liujiayi
 *//*


package com.blsq.admin.common.auth;

import com.blsq.admin.common.security.service.BlsqUserDetailsServiceImpl;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


*/
/**
 * <p>
 * 类描述：
 * </p>
 *
 * @author liujiayi
 * @since 2019/5/28 14:27
 * @version：1.0
 *//*


@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthParameters authParameters;

    @Autowired
    private BlsqUserDetailsServiceImpl userDetailsService;


    */
/**
     * 1.从每个请求header获取token
       2.调用前面写的validateToken方法对token进行合法性验证
       3.解析得到username，并从database取出用户相关信息权限
       4.把用户信息以UserDetail形式放进SecurityContext以备整个请求过程使用。
      （例如哪里需要判断用户权限是否足够时可以直接从SecurityContext取出去check
     *//*


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //需要设置白名单
        String contextPath = request.getRequestURI();

       */
/*if(){
            
        }*//*


        String token = getJwtFromRequest(request);
        if(token != null && jwtTokenProvider.validateToken(token)){
            String username = getUsernameFromJwt(token, authParameters.getJwtTokenSecret());
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
           Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
           SecurityContextHolder.getContext().setAuthentication(authentication);
        }else {
            log.error(request.getParameter("username") + " :Token is null");
        }
        super.doFilter(request, response, filterChain);
    }



*/
/**
     * Get Bear jwt from request header Authorization.
     *
     * @param request servlet request.
     * @return token or null.
     *//*


    private String getJwtFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer")) {
            return token.replace("Bearer ", "");
        }
        return null;
    }


*/
/**
     * Get user name from Jwt, the user name have set to jwt when generate token.
     *
     * @param token jwt token.
     * @param signKey jwt sign key, set in properties file.
     * @return user name.
     *//*


    private String getUsernameFromJwt(String token, String signKey) {
        return Jwts.parser().setSigningKey(signKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}

*/
