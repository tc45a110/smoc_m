package com.smoc.cloud.common.smoc.customer.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class EnterpriseSignCertifyValidator {

    private String id;
    private String registerEnterpriseId;
    private String registerEnterpriseName;
    private String socialCreditCode;
    private String businessLicense;
    private String personLiableName;
    private String personLiableCertificateType;
    private String personLiableCertificateNumber;
    private String personLiableCertificateUrl;
    private String personHandledName;
    private String personHandledCertificateType;
    private String personHandledCertificateNumber;
    private String personHandledCertificateUrl;
    private String authorizeCertificate;
    private String authorizeStartDate;
    private String authorizeExpireDate;
    private String position;
    private String officePhotos;
    private String certifyStatus;
    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private Date updatedTime;
}
