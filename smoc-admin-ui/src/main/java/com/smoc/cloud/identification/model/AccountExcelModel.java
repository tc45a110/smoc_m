package com.smoc.cloud.identification.model;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

@Data
@HeadRowHeight(40)
@ContentRowHeight(30)
public class AccountExcelModel {


    @ExcelProperty(value = "数据项")
    @ColumnWidth(40)
    private String key;

    @ExcelProperty(value = "数据值")
    @ColumnWidth(120)
    private String value;
}
