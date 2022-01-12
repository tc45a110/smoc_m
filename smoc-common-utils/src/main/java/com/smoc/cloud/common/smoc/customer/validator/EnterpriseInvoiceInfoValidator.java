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
public class EnterpriseInvoiceInfoValidator {
    private String id;

    @NotNull(message = "ID不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z]{1,32}", message = "ID不符合规则！")
    @Size(min = 1, max = 32, message = "ID长度不符合规则！")
    private String enterpriseId;
    private String invoiceType;

    @NotNull(message = "发票抬头不能为空！")
    @Length(max = 128, message = "发票抬头长度不符合规则！")
    private String invoiceTitle;

    @NotNull(message = "纳税人税号不能为空！")
    @Length(min = 15, max = 25, message = "纳税人税号长度不符合规则！")
    private String taxPayerNumber;

    @Length(max = 64, message = "开户银行长度不符合规则！")
    private String openBank;

    @Length(max = 64, message = "开户账号长度不符合规则！")
    private String openAccount;

    @Length(max = 11, message = "注册电话长度不符合规则！")
    private String registerPhone;

    @Length(max = 64, message = "注册地址长度不符合规则！")
    private String registerAddress;

    @Length(max = 128, message = "备注长度不符合规则！")
    private String invoiceMark;
    private String invoiceStatus;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;

    //普票id，页面做区分
    private String commonId;

    //专票id，页面做区分
    private String specialId;

}
