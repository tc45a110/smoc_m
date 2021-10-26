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
 * 系统角色操作类
 * 2019/3/29 14:29
 **/
@Setter
@Getter
public class RoleValidator implements Serializable {

    @NotNull(message = "ID不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z]{6,32}", message = "ID不符合规则！")
    @Size(min = 1, max = 32, message = "ID长度不符合规则！")
    private String id;

    @NotNull(message = "角色编码不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z_-]{1,32}", message = "角色编码不符合规则！")
    @Length(min = 1, max = 32, message = "角色编码长度要在{min}-{max}之间！")
    private String roleCode;

    @NotNull(message = "角色名称不能为空！")
    @Length(min = 1, max = 32, message = "模块名称长度要在{min}-{max}之间！")
    private String roleName;

    private Date createDate;

    private Date updateDate;

}
