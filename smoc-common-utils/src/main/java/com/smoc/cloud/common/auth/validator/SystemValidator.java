package com.smoc.cloud.common.auth.validator;

import com.smoc.cloud.common.validator.MpmIdValidator;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * System参数正则校验
 * 2019/4/23 16:49
 **/
@Setter
@Getter
public class SystemValidator extends MpmIdValidator {

    @NotNull(message = "系统名称不能为空！")
    @Length(min = 3, max = 32, message = "系统名称长度要在{min}-{max}之间！")
    private String systemName;

    @NotNull(message = "系统标识不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z-_]*", message = "应该为字母、数字、-、_组成")
    @Length(min = 3, max = 32, message = "系统标识长度要在{min}-{max}之间！")
    private String projectName;

    @Length(min = 0, max = 80, message = "URL长度要在{min}-{max}之间！")
    private String url;

    @Length(min = 0, max = 45, message = "图标长度要在{min}-{max}之间！")
    private String icon;

    private String styleClass;

    @NotNull(message = "是否有效不能为空！")
    private Integer active;

    @NotNull(message = "显示顺序不能为空！")
    private Integer sort;

    private Date createDate;

    private Date updateDate;
}
