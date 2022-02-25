package com.smoc.cloud.common.smoc.customer.validator;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Setter
@Getter
public class SystemAttachmentValidator {
    private String id;
    private String moudleId;
    private String moudleInentification;
    private String attachmentName;
    private String attachmentUri;
    private String docType;
    private BigDecimal docSize;
    private String attachmentStatus;
    private String createdBy;
    private Date createdTime;

}
