package com.smoc.cloud.common.smoc.configuate.validator;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.Objects;

@Setter
@Getter
public class ChannelInterfaceValidator {

    @NotNull(message = "ID不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z]{6,32}", message = "ID不符合规则！")
    @Length(min = 1, max = 32, message = "ID长度不符合规则！")
    private String id;

    @NotNull(message = "通道ID不能为空！")
    private String channelId;

    @NotNull(message = "通道对接账号不能为空！")
    @Length(min = 1, max = 90, message = "通道对接账号长度不符合规则！")
    private String channelAccessAccount;

    @NotNull(message = "通道密码不能为空！")
    @Length(min = 1, max = 128, message = "通道密码长度不符合规则！")
    private String channelAccessPassword;

    @NotNull(message = "通道服务地址不能为空！")
    @Length(min = 1, max = 128, message = "通道服务地址长度不符合规则！")
    private String channelServiceUrl;

    @NotNull(message = "企业代码不能为空！")
    @Length(min = 1, max = 128, message = "企业代码长度不符合规则！")
    private String spId;

    @Length(max = 32, message = "码号最大长度为{max}")
    private String srcId;

    @Length(min = 1, max = 128, message = "业务代码长度不符合规则！")
    private String serviceCode;

    @NotNull(message = "连接数不能为空！")
    @Range(min = 1, max = 10000 , message = "连接数长度要在{min}-{max}之间！")
    private Integer connectNumber;

    @Range(max = 10000, message = "最大速率为{max}！")
    private Integer maxSendSecond;

    @Range(min = 1, max = 10000, message = "心跳间隔单位长度要在{min}-{max}之间！")
    private Integer heartbeatInterval;

    @NotNull(message = "通道协议类型不能为空！")
    private String protocol;

    @Length(max = 10, message = "版本最大长度为{max}！")
    private String version;
    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private Date updatedTime;

}
