package com.smoc.cloud.common.smoc.route.qo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RouteAuditMessageAccountQo {
    private String accountId;
    private String accountName;
    private int totalNum;

}
