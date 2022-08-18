package com.smoc.cloud.common.smoc.customer.qo;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExportModel {

    private String id;
    //报备码号
    private String regCodeNum;
    //接入省份
    private String accessProvince;
    //报备完整码号
    private String numberSegment;
    //报备签名
    private String registerSign;
    private String isSign;
    private String isGreen;
    private String blackList;
    //报备运营商
    private String registerCarrier;

    //报备app
    private String appName;
    //服务类型
    private String serviceType;
    //短信用途
    private String mainApplication;

    private String registerEnterprise;
    //端口实际使用单位名称
    private String enterpriseName;
    //企业社会信用代码
    private String socialCreditCode;
    //营业执照/组织机构代码证
    private String businessLicense;
    //责任人（含法人）姓名
    private String liableName;
    //责任人（含法人）证件类型
    private String liableCertType;
    //责任人（含法人）证件号码
    private String liableCertNum;
    //责任人（含法人）证件
    private String liableCertUrl;
    //经办人姓名
    private String handledName;
    //经办人挣钱类型
    private String handledCertType;
    //经办人证件号码
    private String handledCertNum;
    //经办人证件
    private String handledCertUrl;
    //授权书
    private String authorizeCert;
    private String isAuthorize;
    //授权开始日期
    private String authorizeStart;
    //授权结束日期
    private String authorizeEnd;
    //机房位置
    private String position;
    //机房现场拍照
    private String officePhotos;

    private String operate;
}
