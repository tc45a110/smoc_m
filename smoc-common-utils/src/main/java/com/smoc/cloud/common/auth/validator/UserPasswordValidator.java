package com.smoc.cloud.common.auth.validator;

import com.smoc.cloud.common.validator.MpmIdValidator;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 用户字段规则验证
 * 2019/3/29 14:29
 **/
@Setter
@Getter
public class UserPasswordValidator extends MpmIdValidator {

    //@Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z])(?=.*[~!@#$%^&*._])[\\da-zA-Z~!@#$%^&*._]{8,}$", message = "密码不符合规则")
    private String password;

    private String oldPassword;
}
