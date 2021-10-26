package com.smoc.cloud.common.auth.validator;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

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
public class ClientDetailsValidator implements Serializable {

    @NotNull(message = "客户端id不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z]{18,32}", message = "客户端id不符合规则！")
    @Size(min = 18, max = 32, message = "客户端id长度要在{min}-{max}之间！")
    private String clientId;

    private String resourceIds;

    @Length(max = 255, message = "密码最大长度为{max}！")
    private String clientSecret;

    @NotNull(message = "客户端授权范围不能为空！")
    @Size(min = 1, max = 256, message = "客户端授权范围长度要在{min}-{max}之间！")
    private String scope;

    @NotNull(message = "客户端授权类型不能为空！")
    @Size(min = 1, max = 256, message = "客户端授权类型长度要在{min}-{max}之间！")
    private String authorizedGrantTypes;

    private String webServerRedirectUri;

    private String authorities;

    private Integer accessTokenValidity;

    private Integer refreshTokenValidity;

    private String additionalInformation;

    private String autoapprove;


}
