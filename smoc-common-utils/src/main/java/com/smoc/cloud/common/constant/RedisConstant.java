package com.smoc.cloud.common.constant;

/**
 * Redis常量
 * 2019/4/4 12:50
 **/
public class RedisConstant {

    /**
     * Redis key模块module前缀
     */
    //auth模块
    public static String MODULE_AUTH_PREFIX = "auth";

    /**
     * Redis key业务前缀
     */
    //用户
    public static String AUTH_USERS_PREFIX = MODULE_AUTH_PREFIX + ":u:user";

    //用户角色
    public static String AUTH_USER_ROLES = MODULE_AUTH_PREFIX +":u:u-roles";

    //用户菜单
    public static String AUTH_USER_MENUS = MODULE_AUTH_PREFIX +":u:u-menus";

    //客户端
    public static String AUTH_USER_CLIENT = MODULE_AUTH_PREFIX +":u:u-client";


    //菜单
    public static String AUTH_MENUS_PREFIX = ":m";

    /**
     * Redis key 数据KEY
     */
    //角色、菜单 auth-role-menus缩写
    public static String RROLE_MENUS = MODULE_AUTH_PREFIX + AUTH_MENUS_PREFIX + ":a-r-m";

    //用户菜单 KEY   auth-user-menus缩写 + userID
    public static String USER_MENUS = MODULE_AUTH_PREFIX + AUTH_MENUS_PREFIX + ":a-u-m";
    public static String USER_ROLES = MODULE_AUTH_PREFIX + AUTH_MENUS_PREFIX + ":a-u-r";

}
