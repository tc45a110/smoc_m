package com.smoc.cloud.common.auth.validator;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 组织管理操作类
 */
@Setter
@Getter
public class OrgValidator {

    @NotNull(message = "ID不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z]{6,32}", message = "ID不符合规则！")
    @Size(min = 1, max = 32, message = "ID长度不符合规则！")
    private String id;

    @NotNull(message = "组织名称不能为空！")
    @Size(min = 1, max = 36, message = "组织名称长度不符合规则！")
    private String orgName;

    private String orgCode;
    
    private int orgLevel;

    @NotNull(message = "组织类型不能为空！")
    private int orgType;

    @NotNull(message = "是否叶子节点不能为空！")
    private int isLeaf;

    @NotNull(message = "父ID不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z]{6,32}", message = "ID不符合规则！")
    @Size(min = 1, max = 32, message = "父ID长度不符合规则！")
    private String parentId;

    @NotNull(message = "显示顺序不能为空！")
    private Integer sort;

    @NotNull(message = "状态不能为空！")
    private Integer active;
    private Date createDate;
    private Date editDate;
}
