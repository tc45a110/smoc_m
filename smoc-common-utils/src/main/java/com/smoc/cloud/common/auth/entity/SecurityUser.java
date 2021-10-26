package com.smoc.cloud.common.auth.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 系统管理用户  基本信息
 * 2019/3/29 14:29
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecurityUser implements Serializable {

    private String id;

    //用户名
    private String userName;

    //真实姓名
    private String realName;

    private String password;

    //手机号
    private String phone;

    //组织id
    private String organization;

    //组织名称
    private String corporation;

    //部门
    private String department;

    //黑白名单控制   0表示白名单控制  1表示黑名单控制
    private String parentCode;

    //企业编码
    private String code;

    //是否管理员
    private Integer administrator;

    //是否为团队领导
    private Integer teamLeader;

    //用户类型
    private Integer type;

    private String authScope;

    //角色权限
    private List<String> authorities;


}
