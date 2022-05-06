package com.smoc.cloud.common.smoc.configuate.validator;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.Objects;

@Setter
@Getter
public class ConfigChannelSpareChannelValidator {

    @NotNull(message = "ID不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z]{6,32}", message = "ID不符合规则！")
    @Length(min = 1, max = 32, message = "ID长度不符合规则！")
    private String id;

    @NotNull(message = "通道ID不能为空！")
    private String channelId;

    @NotNull(message = "备用通道ID不能为空！")
    private String spareChannelId;

    @NotNull(message = "状态码失败补发不能为空！")
    @Length(max = 64, message = "状态码失败补发最大长度为{max}")
    private String statusFailureSupple;

    @Length(max = 64, message = "账号失败补发最大长度为{max}")
    private String accountFailureSupple;

    @Length(max = 64, message = "内容失败补发最大长度为{max}")
    private String contentFailureSupple;

    private String spareChannelName;

    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private Date updatedTime;

}
