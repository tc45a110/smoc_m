package com.smoc.cloud.common.smoc.template;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Setter
@Getter
public class AccountTemplateInfoValidator {

    private String templateId;

    private String enterpriseId;

    private String templateType;

    private String businessAccount;

    private String infoType;

    private String signName;

    private String templateTitle;

    @NotNull(message = "模板不能为空！")
    @Size(min = 1, max = 2000, message = "模板长度不符合规则！")
    private String templateContent;

    private String forbiddenContent;

    private String templateFlag;

    private String mmAttchment;

    private String checkDate;

    private String checkBy;

    private String checkOpinions;

    private String checkStatus;

    private String templateStatus;

    private String templateAgreementType;

    private String isFilter;

    private String createdBy;

    private String createdTime;

    private String updatedBy;

    private Date updatedTime;

    private String templateClassify;

    private String enterpriseName;
    private String accountName;
    private String startDate;
    private String endDate;
    private String multimediaAttachmentData;
}
