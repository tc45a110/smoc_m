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
public class CodeNumberInfoValidator {

    @NotNull(message = "ID不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z]{6,32}", message = "ID不符合规则！")
    @Length(min = 1, max = 32, message = "ID长度不符合规则！")
    private String id;

    @NotNull(message = "码号不能为空！")
    @Pattern(regexp = "^[1-9]\\d*|0$", message = "码号不符合规则！")
    @Length(min = 1, max = 32, message = "码号长度要在{min}-{max}之间！")
    private String srcId;

    @Range(min = 0, max = 1000, message = "抗诉率只能是{min}到{max}！")
    private BigDecimal maxComplaintRate;

    @NotNull(message = "运营商不能为空！")
    @Length(max = 32, message = "运营商最大长度为{max}！")
    private String carrier;

    @NotNull(message = "业务类型不能为空！")
    @Length(max = 32, message = "业务类型最大长度为{max}！")
    private String businessType;

    @Range(min = 0, max = 1, message = "资费只能是{min}到{max}！")
    private BigDecimal srcIdPrice;

    private String priceEffectiveDate;

    @NotNull(message = "码号用途不能为空！")
    @Length(max = 32, message = "码号用途最大长度为{max}！")
    private String useType;

    @NotNull(message = "码号来源不能为空！")
    @Length(max = 32, message = "码号来源最大长度为{max}！")
    private String srcIdSource;

    @NotNull(message = "接入点不能为空！")
    @Length(max = 32, message = "接入点最大长度为{max}！")
    private String accessPoint;

    private String accessTime;

    @Range(min = 0, max = 10000, message = "低消要求只能是{min}到{max}！")
    private Integer minConsumeDemand;

    private String minConsumeEffectiveDate;

    private String srcIdEffectiveDate;

    @Length(max = 32, message = "计费号码/CA编号最大长度为{max}！")
    private String caSrcId;
    @NotNull(message = "省份不能为空！")

    @Length(max = 32, message = "省份最大长度为{max}！")
    private String province;

    @Length(max = 32, message = "地市最大长度为{max}！")
    private String city;

    @Length(max = 128, message = "备注最大长度为{max}！")
    private String srcIdRemark;

    @NotNull(message = "码号状态不能为空！")
    @Length(max = 32, message = "码号状态最大长度为{max}！")
    private String srcIdStatus;

    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private Date updatedTime;

}
