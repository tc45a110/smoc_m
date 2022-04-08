package com.smoc.cloud.common.smoc.customer.validator;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
public class EnterpriseDocumentInfoValidator {
    private String id;

    @NotNull(message = "企业不能为空！")
    private String enterpriseId;

    @Length(max = 500, message = "签名长度不符合规则！")
    private String signName;

    @NotNull(message = "业务类型不能为空！")
    private String businessType;

    private String infoType;

    @Length(max = 128, message = "短链长度不符合规则！")
    private String shortLink;

    @Length(max = 128, message = "关键字长度不符合规则！")
    private String docKey;

    private String docStatus;
    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private Date updatedTime;
    private String checkOpinions;
    private String checkStatus;
    private String checkDate;

    private String enterpriseType;
    private String enterpriseName;
    private String startDate;
    private String endDate;

    private String contractFiles;
    List<SystemAttachmentValidator> filesList = new ArrayList<>();
}
