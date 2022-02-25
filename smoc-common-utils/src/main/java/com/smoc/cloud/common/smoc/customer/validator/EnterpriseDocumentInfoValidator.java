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

    @NotNull(message = "签名不能为空！")
    @Length(max = 64, message = "签名长度不符合规则！")
    private String signName;

    @NotNull(message = "业务类型不能为空！")
    private String businessType;

    @NotNull(message = "信息分类不能为空！")
    private String infoType;

    @NotNull(message = "短链不能为空！")
    @Length(max = 128, message = "短链长度不符合规则！")
    private String shortLink;

    @Length(max = 128, message = "关键字长度不符合规则！")
    private String docKey;

    @NotNull(message = "签订日期不能为空！")
    @Pattern(regexp = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)", message = "日期不符合规则！")
    private String signDate;
    @NotNull(message = "到期日期不能为空！")
    @Pattern(regexp = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)", message = "日期不符合规则！")
    private String signExpireDate;
    private String docStatus;
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
