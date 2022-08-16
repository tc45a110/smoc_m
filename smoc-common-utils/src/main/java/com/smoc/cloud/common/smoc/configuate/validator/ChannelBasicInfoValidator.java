package com.smoc.cloud.common.smoc.configuate.validator;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
public class ChannelBasicInfoValidator {
    private String channelId;
    @NotNull(message = "通道名称不能为空！")
    @Length(min = 1, max = 32, message = "通道名称长度不符合规则！")
    private String channelName;

    @NotNull(message = "发送号码范围不能为空！")
    @Length(max = 32, message = "发送号码范围最大长度为{max}！")
    private String carrier;

    @NotNull(message = "业务类型不能为空！")
    @Length(max = 32, message = "业务类型最大长度为{max}！")
    private String businessType;

    @Range(min = 0, max = 1000, message = "抗诉率只能是{min}到{max}！")
    private BigDecimal maxComplaintRate;

    @Length(max = 32, message = "接入省份最大长度为{max}！")
    private String accessProvince;

    @NotNull(message = "通道供应商不能为空！")
    @Length(max = 32, message = "通道供应商最大长度为{max}！")
    private String channelProvder;

    @NotNull(message = "信息分类不能为空！")
    @Length(max = 64, message = "信息分类最大长度为{max}！")
    private String infoType;

    @NotNull(message = "通道区域范围不能为空！")
    private String businessAreaType;
    private String supportAreaCodes;
    private String maskProvince;

    @NotNull(message = "有无报告不能为空！")
    @Length(max = 1, message = "有无报告最大长度为{max}！")
    private String reportEnable;

    @NotNull(message = "计价方式不能为空！")
    private String priceStyle;

    @NotNull(message = "签名方式不能为空！")
    @Length(max = 1, message = "签名方式最大长度为{max}！")
    private String signType;

    @NotNull(message = "有无上行不能为空！")
    @Length(max = 1, message = "有无上行最大长度为{max}！")
    private String upMessageEnable;

    @NotNull(message = "是否支持携号转网不能为空！")
    private String transferEnable;
    private String transferType;

    @NotNull(message = "通道介绍不能为空！")
    @Length(max = 128, message = "通道介绍最大长度为{max}！")
    private String channelIntroduce;
    private String channelProcess;
    private String channelRunStatus;

    @NotNull(message = "通道状态不能为空！")
    @Length(max = 32, message = "通道状态最大长度为{max}！")
    private String channelStatus;

    private String isRegister;

    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private Date updatedTime;

    @Range(min = 0, max = 1, message = "资费只能是{min}到{max}！")
    private BigDecimal channelPrice;
    private String province;
    private String channelAccessSales;
    @Length(max = 128, message = "通道限制内容最大长度为{max}！")
    private String channelRestrictContent;

    @NotNull(message = "具体供应商不能为空！")
    private String specificProvder;
    @NotNull(message = "对账周期不能为空！")
    private String channelBill;

    private String protocol;
    private String copyChannelId;
}
