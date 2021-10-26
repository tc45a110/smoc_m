package com.smoc.cloud.common.auth.validator;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 角色授权 验证类
 */

@Setter
@Getter
public class RoleAuthValidator {


    @NotNull(message = "角色ID不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z]{6,32}", message = "角色ID不符合规则！")
    @Size(min = 1, max = 32, message = "角色ID长度不符合规则！")
    private String roleId;

    @NotNull(message = "菜单ID不能为空！")
    private String menusId;
}
