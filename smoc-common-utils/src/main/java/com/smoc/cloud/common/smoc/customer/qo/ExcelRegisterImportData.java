package com.smoc.cloud.common.smoc.customer.qo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class ExcelRegisterImportData {

    // 设置excel表头名称
    @ExcelProperty(value = "业务账号", index = 0)
    private String account;

    @ExcelProperty(value = "账号扩展号（系统默认）", index = 1)
    private String extendNumber;

    @ExcelProperty(value = "企业签名", index = 2)
    private String sign;

    @ExcelProperty(value = "端口实际使用单位名称", index = 3)
    private String registerEnterpriseName;

    @ExcelProperty(value = "端口实际使用单位统一社会信用代码", index = 4)
    private String socialCreditCode;
    @ExcelProperty(value = "端口实际使用单位营业执照", index = 9)
    private String businessLicense;

    @ExcelProperty(value = "责任人（含法人）姓名", index = 5)
    private String personLiableName;

    private String personLiableCertificateType;
    @ExcelProperty(value = "责任人（含法人）证件号码", index = 6)
    private String personLiableCertificateNumber;
    @ExcelProperty(value = "责任人（含法人）证件", index = 10)
    private String personLiableCertificateUrl;

    @ExcelProperty(value = "经办人姓名", index = 7)
    private String personHandledName;

    private String personHandledCertificateType;
    @ExcelProperty(value = "经办人证件号码", index = 8)
    private String personHandledCertificateNumber;
    @ExcelProperty(value = "经办人证件", index = 11)
    private String personHandledCertificateUrl;

    private String authorizeCertificate;

    private String authorizeStartDate;

    private String authorizeExpireDate;
    @ExcelProperty(value = "现场拍摄留存经办人照片", index = 12)
    private String officePhotos;
}
