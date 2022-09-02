package com.smoc.cloud.configure.channel.service;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExcelPriceData {

    // 设置excel表头名称
    @ExcelProperty(value = "国家码", index = 0)
    private String countriesNo;

    @ExcelProperty(value = "国家名称", index = 1)
    private String countriesName;

    @ExcelProperty(value = "单价/元(最多精确到小数点后四位)", index = 2)
    private String price;
}
