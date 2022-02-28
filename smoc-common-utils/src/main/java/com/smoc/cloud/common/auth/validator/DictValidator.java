package com.smoc.cloud.common.auth.validator;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 字典数据校验
 * 2019/5/21 17:44
 **/
@Setter
@Getter
public class DictValidator {

    @NotNull(message = "ID不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z]{6,32}", message = "ID不符合规则！")
    @Size(min = 1, max = 32, message = "ID长度不符合规则！")
    private String id;

    @NotNull(message = "名称不能为空！")
    @Size(min = 1, max = 32, message = "名称长度不符合规则！")
    private String dictName;

    @Size(min = 1, max = 24, message = "名称长度不符合规则！")
    private String dictCode;

    @NotNull(message = "类别不能为空！")
    @Size(min = 1, max = 32, message = "类别长度不符合规则！")
    private String dictType;

    @NotNull(message = "类别ID不能为空！")
    @Size(min = 1, max = 32, message = "类别ID长度不符合规则！")
    private String typeId;

    @NotNull(message = "显示顺序不能为空！")
    private Integer sort;

    @NotNull(message = "状态不能为空！")
    private Integer active;
    private Date createDate;
    private Date editDate;

    private String createDateTime;
}
