package com.smoc.cloud.common.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统角色
 * 2019/3/29 14:29
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role implements Serializable {

    private String id;

    private String roleCode;

    private String roleName;

    private String organization;

    private Date createDate;

    private Date updateDate;

    /**
     * 角色资源
     */
    private List<Menu> menus;


}
