package com.smoc.cloud.common.auth.validator;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 字典类型数据校验
 * 2019/5/21 17:44
 **/
@Setter
@Getter
public class DictTypeValidator {

    @NotNull(message = "ID不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z]{6,32}", message = "ID不符合规则！")
    @Size(min = 1, max = 32, message = "ID长度不符合规则！")
    private String id;

    @NotNull(message = "类别名称不能为空！")
    @Size(min = 1, max = 32, message = "类别名称长度不符合规则！")
    private String dictTypeName;

    @NotNull(message = "类别编码不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z]{1,24}", message = "类别编码不符合规则！")
    @Size(min = 1, max = 24, message = "类别编码长度不符合规则！")
    private String dictTypeCode;

    @NotNull(message = "所属系统不能为空！")
    @Size(min = 2, max = 128, message = "所属系统长度不符合规则！")
    private String dictTypeSystem;

    @NotNull(message = "图标不能为空！")
    private String icon;

    @NotNull(message = "显示顺序不能为空！")
    private Integer sort;

    @NotNull(message = "状态不能为空！")
    private Integer active;
    private Date createDate;
    private Date editDate;
}
