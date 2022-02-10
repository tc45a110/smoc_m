package com.smoc.cloud.common.smoc.customer.validator;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Setter
@Getter
public class AccountChannelInfoValidator {
    private String id;
    @NotNull(message = "账号ID不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z]{1,32}", message = "账号ID不符合规则！")
    @Size(min = 1, max = 32, message = "账号ID长度不符合规则！")
    private String accountId;
    private String configType;
    @NotNull(message = "运营商不能为空！")
    private String carrier;
    private String channelGroupId;
    private String channelId;
    private String channelPriority;
    private int channelWeight;
    private String channelSource;
    private String changeSource;
    private String channelStatus;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;

    private String accountChannelType;
    private String channelName;
    private String channelGroupName;
    private String createdTimeStr;
}
