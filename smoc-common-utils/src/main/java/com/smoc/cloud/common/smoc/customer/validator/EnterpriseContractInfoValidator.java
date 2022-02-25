package com.smoc.cloud.common.smoc.customer.validator;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
public class EnterpriseContractInfoValidator {
    private String id;

    @NotNull(message = "企业不能为空！")
    private String enterpriseId;

    @NotNull(message = "合同编号不能为空！")
    @Length(min = 2, max = 48, message = "合同编号长度不符合规则！")
    private String contractNo;

    @Length(max = 48, message = "合同关键字长度不符合规则！")
    private String contractKey;

    @NotNull(message = "签订日期不能为空！")
    @Pattern(regexp = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)", message = "日期不符合规则！")
    private String contractDate;
    @NotNull(message = "到期日期不能为空！")
    @Pattern(regexp = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)", message = "日期不符合规则！")
    private String contractExpireDate;
    private String contractStatus;
    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private Date updatedTime;

    private String enterpriseType;
    private String enterpriseName;
    private String startDate;
    private String endDate;
    private String contractFiles;

    List<SystemAttachmentValidator> filesList = new ArrayList<>();
}
