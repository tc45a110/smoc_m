package com.smoc.cloud.api.request;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Setter
@Getter
public class AccountPackageMonthPageRequest extends BaseRequest {

    @Pattern(regexp = "^[0-9A-Za-z]{0,32}", message = "套餐包ID不符合规则不符合规则！")
    private String packageId;

    @Pattern(regexp = "^(20[2-9]{1}[0-9]{1}[0-1]{1}[0-9]{1})", message = "查询月份不符合规则！")
    private String queryMonth;

    //当前页
    @Pattern(regexp = "^\\d+$", message = "当前页不符合规则！")
    private String currentPage;
}
