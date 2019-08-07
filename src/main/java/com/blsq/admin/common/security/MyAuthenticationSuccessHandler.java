package com.blsq.admin.common.security;

import cn.hutool.core.lang.UUID;
import com.blsq.admin.common.constant.CommonConstants;
import com.blsq.admin.service.ISysOrgService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * <p>
 * 类描述：认证成功
 * </p>
 *
 * @author liujiayi
 * @version：1.0
 * @since 2018/12/22 13:15
 */

@Slf4j
@Component
@AllArgsConstructor
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final StringRedisTemplate redisTemplate;
    //private final JwtTokenProvider tokenProvider;
    private final ISysOrgService sysOrgService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {


        String access_token = UUID.randomUUID().toString();
        //redisTemplate.opsForValue().set(SecurityConstants.BLSQ_PREFIX+SecurityConstants.ACCESS_TOKEN,access_token,200, TimeUnit.SECONDS);

        /*Set keys = redisTemplate.keys(authentication.getName());
        if(keys!=null){
            System.out.println(keys.toString());
            redisTemplate.delete(keys);
        }*/
        redisTemplate.delete(authentication.getName());
        Set<String> houseIdsByOrgList = sysOrgService.getHouseIdsByOrgList(authentication.getName());
        houseIdsByOrgList.forEach(houseId->{
            redisTemplate.opsForSet().add(authentication.getName(),houseId);
        });


        /*redisTemplate.setKeySerializer(new StringRedisSerializer());
        String key = SecurityConstants.BLSQ_PREFIX+SecurityConstants.ACCESS_TOKEN;
        Object codeObj = redisTemplate.opsForValue().get(key);*/


        log.info("User: " + authentication.getName() + " Login successfully.");
        this.returnJson(response,authentication,access_token);
    }


    private void returnJson(HttpServletResponse response,Authentication authentication,String accessToken) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println("{\"code\":\""+ CommonConstants.SUCCESS+"\",\"data\":{\"access_token\":\""+accessToken+"\"},\"message\":\""+authentication.getName()+"\"}");
    }

}
