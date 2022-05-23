package com.smoc.cloud.common.smoc.customer.qo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ServiceAuthInfo {

    private String roleIds;

    private String roleName;

    private Boolean checkedStatus;

    private String userId;
}
