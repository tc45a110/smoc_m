package com.smoc.cloud.common.smoc.message.model;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class MessageTemplateExcelModel {

    @ExcelProperty(value = "手机号")
    private String mobile;

    @ExcelProperty(value = "${1}")
    private String column1;

    @ExcelProperty(value = "${2}")
    private String column2;

    @ExcelProperty(value = "${3}")
    private String column3;

    @ExcelProperty(value = "${4}")
    private String column4;

    @ExcelProperty(value = "${5}")
    private String column5;

    @ExcelProperty(value = "${6}")
    private String column6;

    @ExcelProperty(value = "${7}")
    private String column7;

    @ExcelProperty(value = "${8}")
    private String column8;

    @ExcelProperty(value = "${9}")
    private String column9;

    @ExcelProperty(value = "${10}")
    private String column10;

}
