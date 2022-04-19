package com.smoc.cloud.common.smoc.customer.validator;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Objects;

@Setter
@Getter
public class AccountInterfaceInfoValidator {

    @NotNull(message = "ID不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z]{1,32}", message = "ID不符合规则！")
    @Size(min = 1, max = 32, message = "ID长度不符合规则！")
    private String accountId;

    @NotNull(message = "接口类型不能为空！")
    private String protocol;
    private String accountPassword;

    private Integer maxSubmitSecond;

    @NotNull(message = "发送速率不能为空！")
    @Range(min = 0, max = 100000 , message = "发送速率长度要在{min}-{max}之间！")
    private Integer maxSendSecond;

    @Length(max = 32, message = "接入码号最大长度为{max}！")
    private String srcId;

    private String identifyIp;

    private Integer maxConnect;
    private String executeCheck;

    @Length(max = 128, message = "上行短信推送地址最大长度为{max}！")
    private String moUrl;

    @Length(max = 128, message = "状态报告推送地址最大长度为{max}！")
    private String statusReportUrl;
    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private Date updatedTime;


}
