package com.smoc.cloud.common.smoc.customer.validator;

import com.smoc.cloud.common.smoc.customer.qo.BookExcelModel;
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
public class EnterpriseBookInfoValidator {
    private String id;
    private String enterpriseId;

    @NotNull(message = "分组不能为空！")
    private String groupId;

    @Length(max = 32, message = "姓名最大长度为{max}个字符")
    private String name;

    @NotNull(message = "手机号不能为空！")
    @Pattern(regexp = "^(13[0-9]|14[0-9]|15[0-9]|16[0-9]|17[0-9]|18[0-9]|19[0-9])\\d{8}$", message = "手机号不符合规则！")
    private String mobile;

    @Length(max = 128, message = "邮箱最大长度为{max}个字符")
    private String email;
    private String remark;
    private String isSync;
    private String status;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;

    private String createdTimeStr;
    private List<BookExcelModel> excelModelList = new ArrayList<>();
}
