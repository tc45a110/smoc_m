package com.smoc.cloud.common.smoc.parameter.model;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ErrorCodeExcelModel {

    @ExcelProperty(value = "错误码")
    private String errorCode;

    @ExcelProperty(value = "错误描述")
    private String errorContent;

    @ExcelProperty(value = "处理建议")
    private String handleRemark;

}
