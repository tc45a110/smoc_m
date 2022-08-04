package com.smoc.cloud.common.iot.request;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Setter
@Getter
public class AccountPackageMonthPageRequest extends BaseRequest {

    private String packageId;

    private String queryMonth;

    private String currentPage;
}
