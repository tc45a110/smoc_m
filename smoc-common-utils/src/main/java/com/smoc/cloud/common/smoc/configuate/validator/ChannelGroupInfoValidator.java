package com.smoc.cloud.common.smoc.configuate.validator;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Setter
@Getter
public class ChannelGroupInfoValidator {
    private String channelGroupId;

    @NotNull(message = "通道组名称不能为空！")
    @Length(min = 1, max = 90, message = "通道组名称长度不符合规则！")
    private String channelGroupName;

    @NotNull(message = "运营商不能为空！")
    @Length(max = 32, message = "运营商最大长度为{max}！")
    private String carrier;

    @NotNull(message = "业务类型不能为空！")
    @Length(max = 32, message = "业务类型最大长度为{max}！")
    private String businessType;

    @NotNull(message = "信息类型不能为空！")
    @Length(max = 32, message = "信息类型最大长度为{max}！")
    private String infoType;

    private String maskProvince;

    @NotNull(message = "通道组业务简介不能为空！")
    @Length(max = 128, message = "通道组业务简介最大长度为{max}！")
    private String channelGroupIntroduce;
    private String channelGroupProcess;
    @NotNull(message = "状态不能为空！")
    private String channelGroupStatus;
    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private Date updatedTime;

    private Integer channelNum;
    private String accountId;
}
