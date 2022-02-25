package com.smoc.cloud.common.smoc.customer.validator;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.Objects;

@Setter
@Getter
public class EnterpriseChainInfoValidator {
    private String id;

    @NotNull(message = "企业资质不能为空！")
    private String documentId;

    @NotNull(message = "签名合同连不能为空！")
    @Length(min = 2, max = 128, message = "签名合同连长度不符合规则！")
    private String signChain;
    @NotNull(message = "签订日期不能为空！")
    @Pattern(regexp = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)", message = "日期不符合规则！")
    private String signDate;
    @NotNull(message = "到期日期不能为空！")
    @Pattern(regexp = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)", message = "日期不符合规则！")
    private String signExpireDate;
    private String signChainStatus;
    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private Date updatedTime;

}
