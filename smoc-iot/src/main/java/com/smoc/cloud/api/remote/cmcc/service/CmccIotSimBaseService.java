package com.smoc.cloud.api.remote.cmcc.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smoc.cloud.api.remote.cmcc.cache.CmccRedisCacheUtils;
import com.smoc.cloud.api.remote.cmcc.configuration.CmccIotProperties;
import com.smoc.cloud.api.remote.cmcc.response.CmccResponseData;
import com.smoc.cloud.api.remote.cmcc.response.CmccTokenResponse;
import com.smoc.cloud.api.remote.cmcc.response.info.CarrierInfo;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.utils.Okhttp3Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CmccIotSimBaseService {

    @Autowired
    public CmccIotProperties cmccIotProperties;

    @Autowired
    public CmccRedisCacheUtils cmccRedisCacheUtils;

    /**
     * 获取移动IOT服务token
     *
     * @return
     */
    public ResponseData<CmccTokenResponse> getToken(CarrierInfo carrierInfo) {

        /**
         * 先从本地获取token
         */
        String token = this.cmccRedisCacheUtils.getLocalToken(carrierInfo.getCarrierIdentifying());
        if (!StringUtils.isEmpty(token)) {
            CmccTokenResponse cmccTokenResponse = new CmccTokenResponse();
            cmccTokenResponse.setToken(token);
            return ResponseDataUtil.buildSuccess(cmccTokenResponse);
        }

        /**
         * 向移动发送获取token请求
         */
        CmccResponseData<List<CmccTokenResponse>> cmccResponseData = new CmccResponseData();
        String requestUrl = cmccIotProperties.getUrl() + "/v5/ec/get/token?appid=" + carrierInfo.getCarrierIdentifying() + "&password=" + carrierInfo.getCarrierPassword() + "&transid=" + this.getTransId();
        log.info("[requestUrl]:{}",requestUrl);
        try {
            String response = Okhttp3Utils.get(requestUrl);
            Type type = new TypeToken<CmccResponseData<List<CmccTokenResponse>>>() {
            }.getType();
            cmccResponseData = new Gson().fromJson(response, type);
            log.info("[token]:{}",new Gson().toJson(cmccResponseData));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(ResponseCode.CARRIER_TOKEN_GET_ERROR);
        }

        /**
         * 请求获取非正常数据
         */
        if (!"0".equals(cmccResponseData.getStatus())) {
            return ResponseDataUtil.buildError(cmccResponseData.getStatus(), cmccResponseData.getMessage());
        }

        /**
         * 本地缓存token
         */
        CmccTokenResponse cmccTokenResponse = cmccResponseData.getResult().get(0);
        this.cmccRedisCacheUtils.saveLocalToken(carrierInfo.getCarrierIdentifying(),cmccTokenResponse.getToken(), new Long(cmccTokenResponse.getTtl()), TimeUnit.SECONDS);
        log.info("[cmccTokenResponse]:{}",new Gson().toJson(cmccTokenResponse));
        return ResponseDataUtil.buildSuccess(cmccTokenResponse);
    }

    public String getTransId(){
        String transId = cmccIotProperties.getAppId() + DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmss") + cmccRedisCacheUtils.getSequence();
        return transId;
    }
}
