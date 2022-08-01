package com.smoc.cloud.api.request;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountPackageMonthPageRequest extends BaseRequest {

    private String packageId;

    private String queryMonth;

    //当前页
    private Integer currentPage;
}
