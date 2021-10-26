package com.smoc.cloud.common.auth.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
/**
 * 系统菜单
 * 2019/3/29 14:29
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class  Menu implements Serializable {

    private String id;

    private String moduleName;

    private String moduleCode;

    private String modulePath;

    private String parentId;

    private String moduleIcon;

    private String httpMethod;

    /**
     * 0 否，1 是
     */
    private Integer isOperating;

    private Integer sort;

    private String systemId;

    private Integer active;

    private Date createDate;

    private Date updateDate;

}
