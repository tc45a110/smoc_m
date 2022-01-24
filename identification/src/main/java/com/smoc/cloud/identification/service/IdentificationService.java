package com.smoc.cloud.identification.service;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.smoc.cloud.common.identification.RequestModel;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.identification.configuration.IdentificationConfigurationProperties;
import com.smoc.cloud.identification.utils.Okhttp3Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 身份验证
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class IdentificationService {

    @Autowired
    private IdentificationConfigurationProperties identificationConfigurationProperties;

    /**
     * 身份证、姓名一致性验证
     * @param requestModel  name和cardNo已经经过正则验证
     * @return
     * @throws IOException
     */
    public ResponseData identification(RequestModel requestModel){


        //请求验证
        String url = identificationConfigurationProperties.getServiceUri()+"/IdNameCheck?appKey="+identificationConfigurationProperties.getAppKey()+"&appScrect="+identificationConfigurationProperties.getAppScrect();
        Map<String,String> requestMap = new HashMap<>();
        requestMap.put("name",requestModel.getName());
        requestMap.put("cardNo",requestModel.getCardNo());
        String requestJsonData = new Gson().toJson(requestMap);
        String response = Okhttp3Utils.postJson(url,requestJsonData,new HashMap<>());
        log.info("[认证服务响应][响应数据]数据:{}" , response);
        //使用Gson解析json
        JsonObject jsonObject = (JsonObject) new JsonParser().parse(response);
        //响应返回结果RESULT 那边是根据响应计费，只要有响应就计费
        String result = jsonObject.get("RESULT").getAsString();
        if(!StringUtils.isEmpty(result)){
            if("500".equals(result)) {
                return ResponseDataUtil.buildError(ResponseCode.SYSTEM_EXCEPTION.getCode(), "认证服务异常");
            }
            String message = jsonObject.get("MESSAGE").getAsString();
            String guid = jsonObject.get("guid").getAsString();
            return ResponseDataUtil.buildSuccess(result,message,requestModel.getOrderNo());
        }

        //其他异常
        String code = jsonObject.get("code").getAsString();
        String msg = jsonObject.get("msg").getAsString();



        return ResponseDataUtil.buildError(ResponseCode.SYSTEM_EXCEPTION);
    }

    /**
     * 身份证、姓名、人的照片一致性验证
     * @param requestModel  name和cardNo已经经过正则验证
     * @return
     * @throws IOException
     */
    public ResponseData identificationFace(RequestModel requestModel){


        //请求验证
        String url = identificationConfigurationProperties.getServiceUri()+"/IdNamePhotoCheck?appKey="+identificationConfigurationProperties.getAppKey()+"&appScrect="+identificationConfigurationProperties.getAppScrect();
        Map<String,String> requestMap = new HashMap<>();
        requestMap.put("name",requestModel.getName());
        requestMap.put("cardNo",requestModel.getCardNo());
        requestMap.put("faceBase64",requestModel.getFaceBase64());
        String requestJsonData = new Gson().toJson(requestMap);
        String response = Okhttp3Utils.postJson(url,requestJsonData,new HashMap<>());
        log.info("[认证服务响应][响应数据]数据:{}" , response);
        //使用Gson解析json
        JsonObject jsonObject = (JsonObject) new JsonParser().parse(response);
        //响应返回结果RESULT 那边是根据响应计费，只要有响应就计费
        String result = jsonObject.get("RESULT").getAsString();
        if(!StringUtils.isEmpty(result)){
            if("500".equals(result)) {
                return ResponseDataUtil.buildError(ResponseCode.SYSTEM_EXCEPTION.getCode(), "认证服务异常");
            }
            String message = jsonObject.get("MESSAGE").getAsString();
            return ResponseDataUtil.buildSuccess(result,message,requestModel.getOrderNo());
        }

        //其他异常
//        String code = jsonObject.get("code").getAsString();
//        String msg = jsonObject.get("msg").getAsString();



        return ResponseDataUtil.buildError(ResponseCode.SYSTEM_EXCEPTION);
    }
}
