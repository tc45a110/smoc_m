package com.smoc.cloud.common.smoc.customer.validator;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
public class AccountFinanceInfoValidator {
    private String id;
    @NotNull(message = "ID不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z]{1,32}", message = "ID不符合规则！")
    @Size(min = 1, max = 32, message = "ID长度不符合规则！")
    private String accountId;

    @NotNull(message = "付费方式不能为空！")
    private String payType;
    @NotNull(message = "计费方式不能为空！")
    private String chargeType;
    @NotNull(message = "额返还时间不能为空！")
    private String frozenReturnDate;
    @NotNull(message = "授信额度不能为空！")
    private BigDecimal accountCreditSum;
    private String carrierType;
    private String carrier;
    private BigDecimal carrierPrice;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;

    private List<AccountFinanceInfoValidator> prices = new ArrayList<>();
}
