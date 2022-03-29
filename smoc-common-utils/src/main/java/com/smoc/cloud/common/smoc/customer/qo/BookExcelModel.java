package com.smoc.cloud.common.smoc.customer.qo;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class BookExcelModel {

    @ExcelProperty(value = "手机号码(必填项)")
    private String mobile;

    @ExcelProperty(value = "联系人")
    private String name;

    @ExcelProperty(value = "邮箱")
    private String email;

}
