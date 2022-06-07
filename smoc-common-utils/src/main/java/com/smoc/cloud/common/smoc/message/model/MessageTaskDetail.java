package com.smoc.cloud.common.smoc.message.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Setter
@Getter
public class MessageTaskDetail {

    @ExcelProperty(value = "手机号")
    @ColumnWidth(20)
    private String mobile;

    @ExcelProperty(value = "发送内容")
    @ColumnWidth(40)
    private String messageContent;

    @ExcelProperty(value = "计费条数")
    @ColumnWidth(15)
    private int chargeNumber;

    @ExcelProperty(value = "归属地")
    @ColumnWidth(15)
    private String area;

    @ExcelProperty(value = "运营商")
    @ColumnWidth(15)
    private String carrier;

    @ExcelProperty(value = "接收状态")
    @ColumnWidth(20)
    private String customerStatus;

    @ExcelProperty(value = "接收时间")
    @ColumnWidth(20)
    private String sendTime;


    @ExcelIgnore
    private String taskId;

    @ExcelIgnore
    private String enterpriseFlag;

}
