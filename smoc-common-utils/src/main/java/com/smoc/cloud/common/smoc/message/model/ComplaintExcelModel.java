package com.smoc.cloud.common.smoc.message.model;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ComplaintExcelModel {

    @ExcelProperty(value = "ID")
    private String handleCarrierId;

    @ExcelProperty(value = "发送类型")
    private String sendType;

    @ExcelProperty(value = "举报内容")
    private String reportContent;

    @ExcelProperty(value = "举报号码")
    private String reportNumber;

    @ExcelProperty(value = "举报运营商")
    private String carrier;

    @ExcelProperty(value = "举报时间")
    private String reportDate;

    @ExcelProperty(value = "举报来源")
    private String reportSource;

    @ExcelProperty(value = "举报渠道")
    private String reportChann;

    @ExcelProperty(value = "被举报号码")
    private String reportedNumber;

    @ExcelProperty(value = "被举报号码省分")
    private String reportedProvince;

    @ExcelProperty(value = "被举报号码地市")
    private String reportedCity;

    @ExcelProperty(value = "内容分类")
    private String contentType;

    @ExcelProperty(value = "举报号码省分")
    private String reportProvince;

    @ExcelProperty(value = "举报号码地市")
    private String reportCity;

    @ExcelProperty(value = "单位")
    private String reportUnit;

    @ExcelProperty(value = "初始状态")
    private String handleStatus;

    @ExcelProperty(value = "省分处理结果")
    private String handleResult;

    @ExcelProperty(value = "处理说明")
    private String handleRemark;

    @ExcelIgnore
    private String is12321;
    @ExcelIgnore
    private String businessAccount;
    @ExcelIgnore
    private String channelId;
    @ExcelIgnore
    private String numberCode;
    @ExcelIgnore
    private String sendDate;
    @ExcelIgnore
    private String sendRate;
}
