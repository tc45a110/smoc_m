package com.smoc.cloud.common.smoc.customer.validator;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Setter
@Getter
public class EnterpriseWebAccountInfoValidator {

    private String id;

    @NotNull(message = "ID不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z]{1,32}", message = "ID不符合规则！")
    @Size(min = 1, max = 32, message = "ID长度不符合规则！")
    private String enterpriseId;

    private String accountType;

    @NotNull(message = "用户名不能为空！")
    @Length(min = 4, max = 30, message = "用户名长度不符合规则！")
    private String webLoginName;
    private String webRealName;
    @NotNull(message = "密码不能为空！")
    @Length(min = 6, max = 20, message = "密码长度不符合规则！")
    private String webLoginPassword;
    private String aesPassword;
    private String accountStatus;
    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private Date updatedTime;

    private String enterpriseName;
}
