/*
package com.blsq.admin.common.util;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

*/
/**
 * <p>
 * 类描述：
 * </p>
 *
 * @author liujiayi
 * @version：1.0
 * @since 2018/12/21 17:13
 *//*

public class Encrypted {
    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();
    public static String getPassword(String password){
        String encodePassword = ENCODER.encode(password.trim());
        System.out.println("your password..."+encodePassword);


        boolean liu = BCrypt.checkpw("123456", "$2a$10$T3wUgfsoXSYkcqeqq4iMHOV6Ip1XdL3G38OZU7LKBLekcL9CAniCC");

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean falsef = !bCryptPasswordEncoder.matches(encodePassword, "123");
        System.out.println(liu);
        return encodePassword;
    }

    public static void main(String[] args) {

        Encrypted.getPassword("123456");
    }
}
*/
