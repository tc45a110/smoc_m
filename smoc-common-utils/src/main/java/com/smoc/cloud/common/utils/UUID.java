package com.smoc.cloud.common.utils;

/**
 * ID生成器，为了优化存储，替换掉ID生成器里的-
 * 2019/3/29 14:29
 */
public class UUID {

    public static String uuid32 () {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }
}
