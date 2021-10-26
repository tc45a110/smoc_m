package com.smoc.cloud.auth.common.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 系统统一加密方法
 * 2019/3/29 14:29
 */
public class MpmEncryptPasswordEncoder {

    public static PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
