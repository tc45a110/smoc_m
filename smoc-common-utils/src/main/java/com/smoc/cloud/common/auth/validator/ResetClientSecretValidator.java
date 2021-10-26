package com.smoc.cloud.common.auth.validator;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * client操作类
 * 2019/3/29 14:29
 **/
@Setter
@Getter
public class ResetClientSecretValidator implements Serializable {

    @NotNull(message = "客户端id不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z]{18,32}", message = "客户端id不符合规则！")
    @Size(min = 18, max = 32, message = "客户端id长度要在{min}-{max}之间！")
    private String clientId;

    @NotNull(message = "客户端密码不能为空！")
    @Size(min = 18, max = 32, message = "客户端密码长度要在{min}-{max}之间！")
    private String clientSecret;

}
