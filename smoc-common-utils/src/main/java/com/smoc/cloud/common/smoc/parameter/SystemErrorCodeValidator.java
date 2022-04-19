package com.smoc.cloud.common.smoc.parameter;

import com.smoc.cloud.common.smoc.parameter.model.ErrorCodeExcelModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class SystemErrorCodeValidator {
    private String id;

    @NotNull(message = "分类不能为空！")
    private String codeType;

    @NotNull(message = "错误码不能为空！")
    @Length(max = 32, message = "错误码最大长度为{max}个字符")
    private String errorCode;

    @NotNull(message = "错误描述不能为空！")
    @Length(max = 128, message = "错误描述最大长度为{max}个字符")
    private String errorContent;

    @Length(max = 128, message = "处理建议最大长度为{max}个字符")
    private String handleRemark;
    private String status;
    private Date createdTime;
    private String createdBy;
    private String updatedBy;
    private Date updatedTime;

    private List<ErrorCodeExcelModel> excelModelList = new ArrayList<>();
}
