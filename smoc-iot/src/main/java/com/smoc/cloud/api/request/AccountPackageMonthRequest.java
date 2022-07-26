package com.smoc.cloud.api.request;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountPackageMonthRequest extends BaseRequest {

    private String packageId;

    private String queryMonth;
}
