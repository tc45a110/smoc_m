package com.smoc.cloud.common.auth.validator;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户字段规则验证
 * 2019/3/29 14:29
 **/
@Setter
@Getter
public class BaseUserValidator implements Serializable {

    @NotNull(message = "ID不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z]{6,32}", message = "ID不符合规则！")
    @Size(min = 1, max = 32, message = "ID长度不符合规则！")
    public String id;

    @NotNull(message = "用户名不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z_-]{4,20}$", message = "用户名不符合规则！")
    @Length(min = 4, max = 20, message = "字段长度要在{min}-{max}之间！")
    private String userName;

    @Length(max = 255, message = "密码最大长度为{max}！")
    private String password;

    @NotNull(message = "手机号不能为空！")
    @Pattern(regexp = "^(13[0-9]|14[0-9]|15[0-9]|16[0-9]|17[0-9]|18[0-9]|19[0-9])\\d{8}$", message = "手机号不符合规则！")
    private String phone;

    private String organization;

    /**
     * male 男、female 女
     */
    @NotNull(message = "性别不能为空！")
    @Length(min = 1, max = 64, message = "性别长度要在{min}-{max}之间！")
    private String gender;

    @Range(min = 18, max = 120, message = "年龄只能是{min}到{max}！")
    private Integer age;

    /**
     * 0 禁用、1 启用
     */
    @NotNull(message = "状态不能为空！")
    private Integer active;

    private Date createDate;

    private Date updateDate;

}
