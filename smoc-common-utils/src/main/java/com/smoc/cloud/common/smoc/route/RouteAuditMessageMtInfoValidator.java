package com.smoc.cloud.common.smoc.route;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Setter
@Getter
public class RouteAuditMessageMtInfoValidator {
    private long id;
    private String accountId;
    private String accountName;
    private String infoType;
    private String phoneNumber;
    private String accountSubmitTime;
    private String messageContent;
    private String channelId;
    private String reason;
    private String messageMd5;
    private String messageJson;
    private Integer auditFlag;
    private Long auditId;
    private String createdTime;

}
