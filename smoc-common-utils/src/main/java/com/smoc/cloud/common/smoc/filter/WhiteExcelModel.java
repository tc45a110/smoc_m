package com.smoc.cloud.common.smoc.filter;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 导出excel
 */
@Setter
@Getter
public class WhiteExcelModel {

    /**
     * 第一列的数据
     */
    @ExcelProperty(value = "白词(必填项)")
    private String column1;
    /**
     * 第二列的数据
     */
    @ExcelProperty(value = "关键词")
    private String column2;

    /**
     * 第二列的数据
     */
    @ExcelProperty(value = "白词描述(可以为空)")
    private String column3;

}
