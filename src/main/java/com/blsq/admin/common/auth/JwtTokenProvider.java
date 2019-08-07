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


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


*/
/**
 * <p>
 * 类描述：生成以及验证token的方法。
 * </p>
 *
 * @author liujiayi
 * @since 2019/5/28 14:26
 * @version：1.0
 *//*


@Slf4j
@Component
public class JwtTokenProvider {
    @Autowired
    private AuthParameters authParameters;

*/
/**
     * Generate token for user login.
     *
     * @param authentication
     * @return return a token string.
     *//*


    public String createJwtToken(Authentication authentication) {
        //user name
        String username = ((org.springframework.security.core.userdetails
                .User) authentication.getPrincipal()).getUsername();
        //expire time
        Date expireTime = new Date(System.currentTimeMillis() + authParameters.getTokenExpiredMs());
        //create token
        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(expireTime)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, authParameters.getJwtTokenSecret())
                .compact();
        return token;
    }


*/
/**
     * validate token eligible.
     * if Jwts can parse the token string and no throw any exception, then the token is eligible.
     *
     * @param token a jws string.
     *//*


    public boolean validateToken(String token) {
        String VALIDATE_FAILED = "validate failed : ";
        //ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException
        try {
            Jwts.parser().setSigningKey(authParameters.getJwtTokenSecret()).parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            log.error(VALIDATE_FAILED + ex.getMessage());
            return false;
        }
    }
}

*/
