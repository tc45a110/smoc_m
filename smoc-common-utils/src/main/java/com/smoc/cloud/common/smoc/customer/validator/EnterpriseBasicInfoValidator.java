package com.smoc.cloud.common.smoc.customer.validator;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Setter
@Getter
public class EnterpriseBasicInfoValidator {
    private String enterpriseId;
    private String enterpriseParentId;

    @NotNull(message = "企业标识不能为空！")
    private String enterpriseFlag;

    @NotNull(message = "企业名称不能为空！")
    @Length(min = 2, max = 64, message = "企业名称长度不符合规则！")
    private String enterpriseName;
    private String parentEnterpriseName;

    @NotNull(message = "企业类型不能为空！")
    private String enterpriseType;

    @NotNull(message = "对接主体公司不能为空！")
    private String accessCorporation;

    @NotNull(message = "企业联系人不能为空！")
    @Length(min = 2, max = 12, message = "企业联系人长度不符合规则！")
    private String enterpriseContacts;

    @NotNull(message = "企业联系方式不能为空！")
    @Length(min = 7, max = 24, message = "企业联系方式长度不符合规则！")
    private String enterpriseContactsPhone;

    private String saler;
    private String enterpriseProcess;
    private String enterpriseStatus;
    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private Date updatedTime;

    private String flag;

    private List<EnterpriseBasicInfoValidator> enterprises = new ArrayList<>();
}
