package com.smoc.cloud.common.auth.validator;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统资源操作类
 * 2019/3/29 14:29
 **/
@Setter
@Getter
public class MenusValidator implements Serializable {


    @NotNull(message = "ID不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z]{6,32}", message = "ID不符合规则！")
    @Size(min = 1, max = 32, message = "ID长度不符合规则！")
    private String id;

    @NotNull(message = "模块名称不能为空！")
    @Length(min = 1, max = 32, message = "模块名称长度要在{min}-{max}之间！")
    private String moduleName;

    @NotNull(message = "模块标识不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z-_]*", message = "应该为字母、数字、-、_组成")
    @Length(min = 2, max = 32, message = "模块标识长度要在{min}-{max}之间！")
    private String moduleCode;

    @Pattern(regexp = "^[0-9A-Za-z-_/*{}]*", message = "应该为字母、数字、-、_组成")
    @Length(min =0,max = 255, message = "模块路径长度要在{min}-{max}之间！")
    private String modulePath;

    @NotNull(message = "父ID不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z_-]{6,32}", message = "父ID不符合规则！")
    @Size(min = 1, max = 32, message = "父ID长度不符合规则！")
    private String parentId;

    @Pattern(regexp = "^[0-9A-Za-z-_/ ]*", message = "应该为字母、数字、-、_组成")
    private String moduleIcon;

    private String httpMethod;

    /**
     * 0 否，1 是
     */
    @NotNull(message = "是否可操作不能为空！")
    private Integer isOperating;

    /**
     * 0 否，1 是
     */
    @NotNull(message = "是否显示不能为空！")
    private Integer isDisplay;

    @NotNull(message = "排序不能为空！")
    private Integer sort;

    @NotNull(message = "系统ID不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z_-]{4,32}", message = "系统ID不符合规则！")
    @Size(min = 1, max = 32, message = "系统ID长度不符合规则！")
    private String systemId;

    @NotNull(message = "排序不能为空！")
    private Integer active;

    private Date createDate;

    private Date updateDate;

    /**
     * 1 表示本地菜单  0表示接口服务菜单
     */
    @NotNull(message = "是否本地菜单不能为空！")
    private Integer isLocal;

}
