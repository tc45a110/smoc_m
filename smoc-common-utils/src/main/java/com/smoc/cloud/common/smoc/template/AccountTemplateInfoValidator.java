package com.smoc.cloud.common.smoc.template;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class AccountTemplateInfoValidator {

    private String templateId;

    private String templateType;

    private String businessAccount;

    private String templateContent;

    private String checkDate;

    private String checkBy;

    private String checkOpinions;

    private String checkStatus;

    private String templateStatus;

    private String templateAgreementType;

    private String createdBy;

    private String createdTime;

    private String updatedBy;

    private Date updatedTime;

    private String enterpriseName;
    private String accountName;
    private String startDate;
    private String endDate;
    private String enterpriseId;
}
