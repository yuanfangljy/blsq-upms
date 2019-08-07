package com.blsq.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p>
 * 类描述：权限系统启动端
 * </p>
 *
 * @author liujiayi
 * @since 2019/4/14 19:54
 * @version：1.0
 */
@EnableCaching //开启缓存
@EnableSwagger2
//@EnableEurekaClient
@SpringBootApplication
public class BlAdminRun {

    public static void main(String[] args) {
        SpringApplication.run(BlAdminRun.class,args);
    }


}
