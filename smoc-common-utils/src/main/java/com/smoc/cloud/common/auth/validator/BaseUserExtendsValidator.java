package com.smoc.cloud.common.auth.validator;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 用户扩展属性校验
 * 2019/5/5 17:05
 **/
@Setter
@Getter
public class BaseUserExtendsValidator implements Serializable {

    @NotNull(message = "ID不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z]{6,32}", message = "ID不符合规则！")
    @Size(min = 1, max = 32, message = "ID长度不符合规则！")
    private String id;

    @Size(max = 18, message = "真实姓名长度不符合规则！")
    private String realName;

    @Length(max = 36, message = "公司最大长度为{max}！")
    private String corporation;

    @Length(max = 36, message = "部门最大长度为{max}！")
    private String department;

    private Integer administrator;

    private Integer teamLeader;

    @Length(max = 24, message = "员工编码最大长度为{max}！")
    private String code;

    @Length(max = 24, message = "父级编码最大长度为{max}！")
    private String parentCode;

    @NotNull(message = "用户类型不能为空！")
    private Integer type;

    @Length(max = 255, message = "头像最大长度为{max}！")
    private String header;

    @Length(max = 128, message = "EMAIL最大长度为{max}！")
    private String email;

    @Length(max = 128, message = "微信最大长度为{max}！")
    private String webChat;

    @Length(max = 36, message = "QQ最大长度为{max}！")
    private String qq;

    @Length(max = 36, message = "职位最大长度为{max}！")
    private String position;
}
