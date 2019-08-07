package com.blsq.admin.common.security;

//import com.blsq.admin.common.auth.JwtAuthenticationFilter;
import com.blsq.admin.common.constant.CommonConstants;
import com.blsq.admin.common.security.service.BlsqUserDetailsServiceImpl;
import com.blsq.admin.common.util.DecryptAES;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * 类描述： Security 配置
 * </p>
 *
 * @author liujiayi
 * @since 2019/4/16 14:18
 * @version：1.0
 */
@Component
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
@SuppressWarnings("all")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

    @Value("${security.encode.key:cooeccooeccooece}")
    private  String encodeKey;

    @Autowired
    private MyAuthenticationFailureHandler failureHandler;
    @Autowired
    private MyAuthenticationSuccessHandler successHandler;
    @Autowired
    private UrlFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource;
    @Autowired
    private UrlAccessDecisionManager urlAccessDecisionManager;
    @Autowired
    private BlsqUserDetailsServiceImpl userDetailsService;
    //@Autowired
    //private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;

    /*@Autowired
    @Qualifier("authenticationEntryPointImpl")
    private AuthenticationEntryPoint entryPoint;

    @Bean
    public JwtAuthenticationFilter getJwtAuthenticationFilter(){
        return new JwtAuthenticationFilter();
    }*/

    /**
     * 配置认证用户信息和权限
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //动态配置用户信息进行加密认证
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder(){
            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                if(rawPassword ==null || encodedPassword.length()==0){
                    log.warn("Empty encoded password");
                    return false;
                }
                String passwordsPlaintext = DecryptAES.decryptAES(rawPassword.toString(), encodeKey);
                System.out.println("passwordsPlaintext---"+passwordsPlaintext.trim()+"-----"+encodedPassword+"shifou--------");
                boolean checkpw=false;
                try {
                    checkpw = BCrypt.checkpw(passwordsPlaintext.trim(), encodedPassword);
                }catch (Exception e){
                    e.printStackTrace();
                    return false;
                }
                return checkpw;
            }

        });
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers( "/swagger-ui.html",
                "/swagger-resources/**",
                "/images/**",
                "/webjars/**",
                "/v2/api-docs",
                "/configuration/ui",
                "/configuration/security",
                " /swagger-resources/configuration/ui");
    }

    /**
     * 配置拦截请求资源
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry =
        http
                //.addFilterBefore(getJwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class).sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //.and()
                .authorizeRequests()
                .antMatchers("/v2/api-docs/**").permitAll()
                .and().formLogin().loginProcessingUrl("/sysUser/login")
                //身份验证信息来源
                //.authenticationDetailsSource(authenticationDetailsSource)
                .successHandler(successHandler).failureHandler(failureHandler)
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()//任何请求,登录后可以访问
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
                        fsi.setAccessDecisionManager(urlAccessDecisionManager);
                        fsi.setSecurityMetadataSource(urlFilterInvocationSecurityMetadataSource);
                        return fsi;
                    }
                })
                //注销行意访问任为
                .and().logout().logoutSuccessHandler(logoutSuccessHandler())
                .and().csrf().disable();
                //.exceptionHandling().authenticationEntryPoint(entryPoint);

    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() { //登出处理
        return new LogoutSuccessHandler() {
            @Override
            public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                try {
                    httpServletResponse.getWriter().println("{\"code\":\""+ CommonConstants.SUCCESS+"\",\"data\":{},\"message\":\"success\"}");
                    log.info(" **** 退出成功......");
                } catch (Exception e) {
                    log.error("LOGOUT EXCEPTION , e : " + e.getMessage() +"请先登录......");
                }
            }
        };
    }
}