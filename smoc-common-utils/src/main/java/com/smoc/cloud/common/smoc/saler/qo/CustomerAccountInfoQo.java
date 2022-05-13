package com.smoc.cloud.common.smoc.saler.qo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
public class CustomerAccountInfoQo {

    private String accountId;

    private String enterpriseName;

    private String enterpriseType;

    private String accountName;

    private String businessType;

    private String carrier;

    private String infoType;

    private String industryType;

    private String payType;

    private String enterpriseContacts;

    private String enterpriseContactsPhone;

    private String salerId;
}
