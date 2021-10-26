package com.smoc.cloud.common.auth.validator;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 用户字段规则验证
 * 2019/3/29 14:29
 **/
@Setter
@Getter
public class UserValidator {

    @NotNull(message = "角色不能为空！")
    private String roleIds;

    private BaseUserValidator baseUserValidator = new BaseUserValidator();

    private BaseUserExtendsValidator baseUserExtendsValidator = new BaseUserExtendsValidator();
}
