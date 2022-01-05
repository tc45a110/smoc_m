package com.smoc.cloud.common.smoc.customer.validator;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class EnterpriseBasicInfoValidator {
    private String enterpriseId;
    private String enterpriseParentId;
    private String enterpriseName;
    private String enterpriseType;
    private String accessCorporation;
    private String enterpriseContacts;
    private String enterpriseContactsPhone;
    private String saler;
    private String enterpriseProcess;
    private String enterpriseStatus;
    private String createdBy;
    private String createdTime;


}
