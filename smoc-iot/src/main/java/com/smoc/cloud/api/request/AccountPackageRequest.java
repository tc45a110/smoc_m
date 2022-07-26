package com.smoc.cloud.api.request;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountPackageRequest extends BaseRequest {

    private String packageId;
}
