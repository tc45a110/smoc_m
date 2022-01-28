package com.smoc.cloud.identification.service;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.smoc.cloud.common.gateway.utils.AESUtil;
import com.smoc.cloud.common.gateway.utils.ValidatorUtil;
import com.smoc.cloud.common.identification.RequestModel;
import com.smoc.cloud.common.redis.smoc.identification.KeyEntity;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationOrdersInfoValidator;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationRequestDataValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
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
import java.math.BigDecimal;
import java.util.Date;
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
    private DataService dataService;

    @Autowired
    private IdentificationOrdersService identificationOrdersService;

    @Autowired
    private IdentificationConfigurationProperties identificationConfigurationProperties;

    /**
     * 身份证、姓名一致性验证
     *
     * @param requestModel name和cardNo已经经过正则验证
     * @return
     * @throws IOException
     */
    public ResponseData identification(RequestModel requestModel) {

        //log.info("[请求数据]{}", new Gson().toJson(requestModel));

        KeyEntity keyEntity = dataService.getKey(requestModel.getIdentifyAccount());
        if (null == keyEntity) {
            return ResponseDataUtil.buildError(ResponseCode.USER_NOT_EXIST.getCode(), ResponseCode.USER_NOT_EXIST.getMessage());
        }

        String cardNo;
        try {
            cardNo = AESUtil.decrypt(requestModel.getCardNo(), keyEntity.getAesKey(), keyEntity.getAesIv());
            requestModel.setCardNo(cardNo);
        } catch (Exception e) {
            return ResponseDataUtil.buildError(ResponseCode.EXCEPTION_ID_CARD.getCode(), ResponseCode.EXCEPTION_ID_CARD.getMessage());
        }

        log.info("[请求订单]{}", new Gson().toJson(requestModel));
        //身份证规则验证
        if (!ValidatorUtil.validate(requestModel)) {
            String errorMessage = ValidatorUtil.validateMessage(requestModel);
            return ResponseDataUtil.buildError(ResponseCode.EXCEPTION_ID_CARD.getCode(), errorMessage);
        }

        //订单号查重
        boolean orderNoRepeat = dataService.orderNoRepeat(requestModel.getOrderNo());
        if (orderNoRepeat) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ORDER_ERROR.getCode(), ResponseCode.PARAM_ORDER_ERROR.getMessage());
        }


        //生成消费订单
        IdentificationOrdersInfoValidator order = new IdentificationOrdersInfoValidator();
        order.setId(UUID.uuid32());
        order.setOrderNo(requestModel.getOrderNo());
        order.setIdentificationAccount(requestModel.getIdentifyAccount());
        order.setOrderType("NAME");
        order.setIdentificationPrice(new BigDecimal("0.00"));
        order.setIdentificationPriceStatus("01");
        order.setCreatedBy("系统");
        order.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));

        log.info("[消费订单]：{}", new Gson().toJson(order));
        //保存订单
        ResponseData data = identificationOrdersService.save(order);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return ResponseDataUtil.buildError(data.getCode(), data.getMessage(),requestModel.getOrderNo());
        }



        //保存原数据
        IdentificationRequestDataValidator identificationRequestDataValidator = new IdentificationRequestDataValidator();
        identificationRequestDataValidator.setId(UUID.uuid32());
        identificationRequestDataValidator.setIdentificationAccount(requestModel.getIdentifyAccount());
        identificationRequestDataValidator.setOrderNo(requestModel.getOrderNo());
        identificationRequestDataValidator.setOrderType("NAME");
        identificationRequestDataValidator.setCreatedBy("系统");
        identificationRequestDataValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
        identificationRequestDataValidator.setRequestData(new Gson().toJson(requestModel));


        //请求验证
        String url = identificationConfigurationProperties.getServiceUri() + "/IdNameCheck?appKey=" + identificationConfigurationProperties.getAppKey() + "&appScrect=" + identificationConfigurationProperties.getAppScrect();
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("name", requestModel.getName());
        requestMap.put("cardNo", requestModel.getCardNo());
        String requestJsonData = new Gson().toJson(requestMap);
        String response = Okhttp3Utils.postJson(url, requestJsonData, new HashMap<>());

        log.info("[认证服务响应][响应数据]数据:{}", response);
        identificationRequestDataValidator.setResponceData(response);
        //异步保存
        identificationOrdersService.save(identificationRequestDataValidator);

        //使用Gson解析json
        JsonObject jsonObject = (JsonObject) new JsonParser().parse(response);
        //响应返回结果RESULT 那边是根据响应计费，只要有响应就计费
        String result = jsonObject.get("RESULT").getAsString();
        if (!StringUtils.isEmpty(result)) {
            if ("500".equals(result) || "501".equals(result)) {
                order.setIdentificationStatus(result);
                order.setIdentificationPriceStatus("00");
                order.setIdentificationMessage("认证服务异常");
                identificationOrdersService.update(order);
                return ResponseDataUtil.buildError(ResponseCode.SYSTEM_EXCEPTION.getCode(), "认证服务异常");
            }


            order.setIdentificationStatus(ResultStatus.code.get(result));
            order.setIdentificationPriceStatus("02");
            order.setIdentificationMessage(jsonObject.get("MESSAGE").getAsString());
            dataService.saveOrderNo(order.getOrderNo());
            if ("-1".equals(result)) {

                identificationOrdersService.update(order);
                return ResponseDataUtil.buildSuccess("0004", order.getIdentificationMessage(), requestModel.getOrderNo());
            }

            order.setIdentificationOrderNo(jsonObject.get("guid").getAsString());
            identificationOrdersService.update(order);
            return ResponseDataUtil.buildSuccess(ResultStatus.code.get(result), order.getIdentificationMessage(), requestModel.getOrderNo());
        }

        //其他异常
        String code = jsonObject.get("code").getAsString();
        String msg = jsonObject.get("msg").getAsString();
        order.setIdentificationStatus(code);
        order.setIdentificationPriceStatus("00");
        order.setIdentificationMessage(msg);
        identificationOrdersService.update(order);

        return ResponseDataUtil.buildError(ResponseCode.SYSTEM_EXCEPTION);

    }

    /**
     * 身份证、姓名、人的照片一致性验证
     *
     * @param requestModel name和cardNo已经经过正则验证
     * @return
     * @throws IOException
     */
    public ResponseData identificationFace(RequestModel requestModel) {


        KeyEntity keyEntity = dataService.getKey(requestModel.getIdentifyAccount());
        if (null == keyEntity) {
            return ResponseDataUtil.buildError(ResponseCode.USER_NOT_EXIST.getCode(), ResponseCode.USER_NOT_EXIST.getMessage());
        }

        String cardNo;
        try {
            cardNo = AESUtil.decrypt(requestModel.getCardNo(), keyEntity.getAesKey(), keyEntity.getAesIv());
            requestModel.setCardNo(cardNo);
        } catch (Exception e) {
            return ResponseDataUtil.buildError(ResponseCode.EXCEPTION_ID_CARD.getCode(), ResponseCode.EXCEPTION_ID_CARD.getMessage());
        }

        //log.info("[解析后数据]{}", new Gson().toJson(requestModel));
        //身份证规则验证
        if (!ValidatorUtil.validate(requestModel)) {
            String errorMessage = ValidatorUtil.validateMessage(requestModel);
            return ResponseDataUtil.buildError(ResponseCode.EXCEPTION_ID_CARD.getCode(), errorMessage);
        }

        //判断头像照片是否为空
        if (StringUtils.isEmpty(requestModel.getFaceBase64())) {
            return ResponseDataUtil.buildError(ResponseCode.EXCEPTION_FACE.getCode(), ResponseCode.EXCEPTION_FACE.getMessage());
        }

        //订单号查重
        boolean orderNoRepeat = dataService.orderNoRepeat(requestModel.getOrderNo());
        if (orderNoRepeat) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ORDER_ERROR.getCode(), ResponseCode.PARAM_ORDER_ERROR.getMessage());
        }

        //生成消费订单
        IdentificationOrdersInfoValidator order = new IdentificationOrdersInfoValidator();
        order.setId(UUID.uuid32());
        order.setOrderNo(requestModel.getOrderNo());
        order.setIdentificationAccount(requestModel.getIdentifyAccount());
        order.setOrderType("FACE");
        order.setIdentificationPrice(new BigDecimal("0.00"));
        order.setIdentificationPriceStatus("01");
        order.setCreatedBy("系统");
        order.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));

        log.info("[消费订单]：{}", new Gson().toJson(order));
        //保存订单
        ResponseData data = identificationOrdersService.save(order);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return ResponseDataUtil.buildError(data.getCode(), data.getMessage(),requestModel.getOrderNo());
        }


        //保存原数据
        IdentificationRequestDataValidator identificationRequestDataValidator = new IdentificationRequestDataValidator();
        identificationRequestDataValidator.setId(UUID.uuid32());
        identificationRequestDataValidator.setIdentificationAccount(requestModel.getIdentifyAccount());
        identificationRequestDataValidator.setOrderNo(requestModel.getOrderNo());
        identificationRequestDataValidator.setOrderType("FACE");
        identificationRequestDataValidator.setCreatedBy("系统");
        identificationRequestDataValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));

        //请求验证
        String url = identificationConfigurationProperties.getServiceUri() + "/IdNamePhotoCheck?appKey=" + identificationConfigurationProperties.getAppKey() + "&appScrect=" + identificationConfigurationProperties.getAppScrect();
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("name", requestModel.getName());
        requestMap.put("cardNo", requestModel.getCardNo());
        requestMap.put("faceBase64", requestModel.getFaceBase64());
        String requestJsonData = new Gson().toJson(requestMap);
        String response = Okhttp3Utils.postJson(url, requestJsonData, new HashMap<>());

        log.info("[认证服务响应][响应数据]数据:{}", response);
        identificationRequestDataValidator.setResponceData(response);
        requestModel.setFaceBase64(null);//放弃保存人脸照片
        identificationRequestDataValidator.setRequestData(new Gson().toJson(requestModel));
        //异步保存
        identificationOrdersService.save(identificationRequestDataValidator);

        //使用Gson解析json
        JsonObject jsonObject = (JsonObject) new JsonParser().parse(response);
        //响应返回结果RESULT 那边是根据响应计费，只要有响应就计费
        String result = jsonObject.get("RESULT").getAsString();
        if (!StringUtils.isEmpty(result)) {
            if ("500".equals(result) || "501".equals(result)) {
                order.setIdentificationStatus(result);
                order.setIdentificationPriceStatus("00");
                order.setIdentificationMessage("认证服务异常");
                identificationOrdersService.update(order);
                return ResponseDataUtil.buildError(ResponseCode.SYSTEM_EXCEPTION.getCode(), "认证服务异常");
            }

            order.setIdentificationPriceStatus("02");
            order.setIdentificationStatus(result);
            order.setIdentificationMessage(jsonObject.get("MESSAGE").getAsString());
            dataService.saveOrderNo(order.getOrderNo());
            String resultCode = "";
            String resultMsg = "";
            JsonObject detailObject = jsonObject.get("detail").getAsJsonObject();
            if (null != detailObject) {
                resultCode = detailObject.get("resultCode").getAsString();
                resultMsg = detailObject.get("resultMsg").getAsString();
            }

            if ("-1".equals(result)) {
                order.setIdentificationStatus("5000");
                if(!StringUtils.isEmpty(resultMsg)){
                    order.setIdentificationMessage(resultMsg);
                }
                identificationOrdersService.update(order);
                return ResponseDataUtil.buildSuccess("5000", order.getIdentificationMessage(), requestModel.getOrderNo());
            }


            if(StringUtils.isEmpty(resultCode) || null == ResultStatus.faceCode.get(resultCode)){
                resultCode = "4001";
            }

            order.setIdentificationStatus(ResultStatus.faceCode.get(resultCode));
            order.setIdentificationMessage(ResultStatus.faceMessage.get(ResultStatus.faceCode.get(resultCode)));
            identificationOrdersService.update(order);

            return ResponseDataUtil.buildSuccess(ResultStatus.faceCode.get(resultCode), ResultStatus.faceMessage.get(ResultStatus.faceCode.get(resultCode)), requestModel.getOrderNo());
        }

        //其他异常
        String code = jsonObject.get("code").getAsString();
        String msg = jsonObject.get("msg").getAsString();
        order.setIdentificationStatus(code);
        order.setIdentificationPriceStatus("00");
        order.setIdentificationMessage(msg);
        identificationOrdersService.update(order);

        return ResponseDataUtil.buildError(ResponseCode.SYSTEM_EXCEPTION);
    }
}
