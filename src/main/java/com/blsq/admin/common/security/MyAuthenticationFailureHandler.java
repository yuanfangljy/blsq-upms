package com.blsq.admin.common.security;

import com.blsq.admin.common.constant.CommonConstants;
import com.blsq.admin.common.constant.ResponseConstant;
import com.blsq.admin.common.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * 类描述：认证失败
 * </p>
 *
 * @author liujiayi
 * @version：1.0
 * @since 2018/12/22 13:14
 */

@Component
@Slf4j
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        responseBrowser(e,request,response);
    }

    public R<String> responseBrowser(AuthenticationException e,HttpServletRequest request, HttpServletResponse response) throws IOException {
        R<String> r = new R<>();
        r.setCode(CommonConstants.FAIL);
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(ResponseConstant.SC_LOGIN_SUCCESS);
        if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
            response.getWriter().println("{\"code\":\""+ CommonConstants.FAIL+"\",\"data\":{},\"message\":\"用户名或密码输入错误，登录失败!\"}");
            log.error("用户名或密码输入错误，登录失败!");
            //throw new CheckedException("用户名或密码输入错误，登录失败!");
        } else if (e instanceof DisabledException) {
            response.getWriter().println("{\"code\":\""+ CommonConstants.FAIL+"\",\"data\":{},\"message\":\"账户被禁用，登录失败，请联系管理员!\"}");
            log.error("账户被禁用，登录失败，请联系管理员!");
        } else if (e instanceof LockedException) {
            response.getWriter().println("{\"code\":\""+ CommonConstants.FAIL+"\",\"data\":{},\"message\":\"用户被锁定，登录失败，请联系管理员!\"}");
            log.error("用户被锁定，登录失败，请联系管理员!");
        } else {
            response.getWriter().println("{\"code\":\""+ CommonConstants.FAIL+"\",\"data\":{},\"message\":\"fail\"}");
            log.error("登录失败!");
        }
        return r;
    }


}
