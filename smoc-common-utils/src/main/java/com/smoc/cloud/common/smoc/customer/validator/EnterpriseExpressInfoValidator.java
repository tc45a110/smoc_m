package com.smoc.cloud.common.smoc.customer.validator;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Setter
@Getter
public class EnterpriseExpressInfoValidator {
    private String id;
    private String expressId;

    @NotNull(message = "ID不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z]{1,32}", message = "ID不符合规则！")
    @Size(min = 1, max = 32, message = "ID长度不符合规则！")
    private String enterpriseId;

    @Length(max = 90, message = "邮寄备注长度不符合规则！")
    private String postRemark;

    @NotNull(message = "姓名不能为空！")
    @Length(max = 23, message = "姓名长度不符合规则！")
    private String postContacts;

    @NotNull(message = "联系电话不能为空！")
    @Length(max = 30, message = "联系电话长度不符合规则！")
    private String postPhone;

    @NotNull(message = "地址不能为空！")
    @Length(max = 128, message = "地址长度不符合规则！")
    private String postAddress;
    private String postStatus;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;

}
