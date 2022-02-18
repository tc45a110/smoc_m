package com.smoc.cloud.common.smoc.filter;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Setter
@Getter
public class FilterWhiteListValidator {
    private String id;
    private String enterpriseId;
    private String groupId;

    @Length(max = 25, message = "姓名最大长度为{max}个字符")
    private String name;

    @NotNull(message = "手机号不能为空！")
    @Pattern(regexp = "^(13[0-9]|14[0-9]|15[0-9]|16[0-9]|17[0-9]|18[0-9]|19[0-9])\\d{8}$", message = "手机号不符合规则！")
    private String mobile;
    private String isSync;
    private String status;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;
    private String createdTimeStr;
}
