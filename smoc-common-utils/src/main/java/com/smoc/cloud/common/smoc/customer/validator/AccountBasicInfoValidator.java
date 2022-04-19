package com.smoc.cloud.common.smoc.customer.validator;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Objects;

@Setter
@Getter
public class AccountBasicInfoValidator {

    @NotNull(message = "业务账号不能为空！")
    @Size(min = 6, max = 6, message = "业务账号长度只能是6位！")
    private String accountId;

    @NotNull(message = "ID不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z]{1,32}", message = "ID不符合规则！")
    @Size(min = 1, max = 32, message = "ID长度不符合规则！")
    private String enterpriseId;

    @NotNull(message = "业务账号名称不能为空！")
    @Length(min = 2, max = 64, message = "业务账号名称长度不符合规则！")
    private String accountName;
    private String accountPassword;

    @NotNull(message = "业务类型不能为空！")
    private String businessType;
    @NotNull(message = "运营商不能为空！")
    private String carrier;
    @NotNull(message = "信息分类不能为空！")
    private String infoType;
    private String extendCode;
    private Integer randomExtendCodeLength;
    @NotNull(message = "设置通道方式不能为空！")
    private String accountChannelType;
    private String poistCarrier;
    private String transferType;
    private String accountProcess;
    @NotNull(message = "状态不能为空！")
    private String accountStatus;
    @NotNull(message = "账号优先级不能为空！")
    private String accountPriority;
    private String consumeType;
    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private Date updatedTime;
    private String countryCode;

    private String enterpriseName;
    private String protocol;

    private String errorAccount;
}
