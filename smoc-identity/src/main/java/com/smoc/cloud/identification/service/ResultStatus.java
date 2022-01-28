package com.smoc.cloud.identification.service;

import java.util.HashMap;
import java.util.Map;

public class ResultStatus {

    public static Map<String,String> code = new HashMap<>();
    public static Map<String,String> codeMessage = new HashMap<>();
    public static Map<String,String> faceMessage = new HashMap<>();
    public static Map<String,String> faceCode = new HashMap<>();

    static{
        code.put("1","0001");
        code.put("2","0002");
        code.put("3","0003");
        code.put("-1","0004");
        codeMessage.put("0001","身份证号、姓名一致");
        codeMessage.put("0002","身份证号、姓名不一致");
        codeMessage.put("0003","无身份证记录");
        codeMessage.put("0004","认证方异常");
        faceCode.put("1001","0011");
        faceCode.put("1002","0012");
        faceCode.put("1003","0013");
        faceCode.put("2001","0021");
        faceCode.put("3001","0031");
        faceCode.put("3002","0032");
        faceCode.put("4001","0041");
        faceMessage.put("0011","身份证号与姓名匹配;图像：系统判断为同一人");
        faceMessage.put("0012","身份证号与姓名匹配;图像：不能确定是否为同一人");
        faceMessage.put("0013","身份证号与姓名匹配;图像：系统判断为不同人");
        faceMessage.put("0021","身份证号与姓名匹配;图像：照片比对失败");
        faceMessage.put("0031","身份证号与姓名不匹配");
        faceMessage.put("0032","库中无此号");
        faceMessage.put("0041","上传相片质量校验不合格，请重新拍摄上传");
    }
}
