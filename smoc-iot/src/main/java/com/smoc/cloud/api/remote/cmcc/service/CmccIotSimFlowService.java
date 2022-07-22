package com.smoc.cloud.api.remote.cmcc.service;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smoc.cloud.api.remote.cmcc.response.CmccResponseData;
import com.smoc.cloud.api.remote.cmcc.response.CmccTokenResponse;
import com.smoc.cloud.api.remote.cmcc.response.flow.CmccSimFlowUsedPoolResponse;
import com.smoc.cloud.api.remote.cmcc.response.flow.CmccSimFlowUsedThisMonthResponse;
import com.smoc.cloud.api.remote.cmcc.response.flow.CmccSimFlowUsedThisMonthTotalResponse;
import com.smoc.cloud.api.remote.cmcc.response.flow.CmccSimGprsFlowUsedMonthlyBatch;
import com.smoc.cloud.api.response.flow.*;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.utils.Okhttp3Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

/**
 * 移动物联网卡接口对接请求服务  物联网卡本月流量查询
 */
@Slf4j
@Service
public class CmccIotSimFlowService extends CmccIotSimBaseService {


    /**
     * 单卡本月套餐内流量使用量实时查询(加入流量池或流量共享的卡无法查询)
     *
     * @param msisdn
     * @return
     */
    public ResponseData<SimFlowUsedThisMonthResponse> querySimFlowUsedThisMonth(String msisdn, String iccid, String imsi) {

        /**
         * 获取token
         */
        ResponseData<CmccTokenResponse> cmccTokenResponseData = this.getToken();
        if (!ResponseCode.SUCCESS.getCode().equals(cmccTokenResponseData.getCode())) {
            return ResponseDataUtil.buildError(ResponseCode.CARRIER_TOKEN_GET_ERROR);
        }
        String token = cmccTokenResponseData.getData().getToken();

        /**
         * 组织请求参数
         */
        String transid = cmccIotProperties.getAppId() + DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmss") + cmccRedisCacheUtils.getSequence();
        String requestUrl = cmccIotProperties.getUrl() + "/v5/ec/query/sim-data-margin?transid=" + transid + "&token=" + token + "&msisdn=" + msisdn;

        /**
         * 向移动发送请求
         */
        CmccResponseData<List<CmccSimFlowUsedThisMonthResponse>> cmccResponseData = new CmccResponseData<>();
        try {
            String response = Okhttp3Utils.get(requestUrl);
            Type type = new TypeToken<CmccResponseData<List<CmccSimFlowUsedThisMonthResponse>>>() {
            }.getType();
            cmccResponseData = new Gson().fromJson(response, type);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(ResponseCode.CARRIER_DATA_GET_ERROR);
        }

        /**
         * 请求获取非正常数据
         */
        if (!"0".equals(cmccResponseData.getStatus())) {
            return ResponseDataUtil.buildError(cmccResponseData.getStatus(), cmccResponseData.getMessage());
        }

        /**
         * 返回执行结果，各个运营商，映射成统一响应格式
         */
        CmccSimFlowUsedThisMonthResponse cmccSimFlowUsedThisMonthResponse = cmccResponseData.getResult().get(0);
        //对外api统一数据结构
        Type type = new TypeToken<SimFlowUsedThisMonthResponse>() {
        }.getType();
        SimFlowUsedThisMonthResponse simFlowUsedThisMonthResponse = new Gson().fromJson(new Gson().toJson(cmccSimFlowUsedThisMonthResponse.getAccmMarginList().get(0)), type);

        return ResponseDataUtil.buildSuccess(simFlowUsedThisMonthResponse);
    }

    /**
     * 单卡本月流量累计使用量查询
     *
     * @param msisdn
     * @param iccid
     * @param imsi
     * @return
     */
    public ResponseData<SimFlowUsedThisMonthTotalResponse> querySimFlowUsedThisMonthTotal(String msisdn, String iccid, String imsi) {

        /**
         * 获取token
         */
        ResponseData<CmccTokenResponse> cmccTokenResponseData = this.getToken();
        if (!ResponseCode.SUCCESS.getCode().equals(cmccTokenResponseData.getCode())) {
            return ResponseDataUtil.buildError(ResponseCode.CARRIER_TOKEN_GET_ERROR);
        }
        String token = cmccTokenResponseData.getData().getToken();

        /**
         * 组织请求参数
         */
        String transid = cmccIotProperties.getAppId() + DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmss") + cmccRedisCacheUtils.getSequence();
        String requestUrl = cmccIotProperties.getUrl() + "/v5/ec/query/sim-data-margin?transid=" + transid + "&token=" + token + "&msisdn=" + msisdn;

        /**
         * 向移动发送请求
         */
        CmccResponseData<List<CmccSimFlowUsedThisMonthTotalResponse>> cmccResponseData = new CmccResponseData<>();
        try {
            String response = Okhttp3Utils.get(requestUrl);
            Type type = new TypeToken<CmccResponseData<List<CmccSimFlowUsedThisMonthTotalResponse>>>() {
            }.getType();
            cmccResponseData = new Gson().fromJson(response, type);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(ResponseCode.CARRIER_DATA_GET_ERROR);
        }

        /**
         * 请求获取非正常数据
         */
        if (!"0".equals(cmccResponseData.getStatus())) {
            return ResponseDataUtil.buildError(cmccResponseData.getStatus(), cmccResponseData.getMessage());
        }

        /**
         * 返回执行结果，各个运营商，映射成统一响应格式
         */
        CmccSimFlowUsedThisMonthTotalResponse cmccSimFlowUsedThisMonthResponse = cmccResponseData.getResult().get(0);
        //对外api统一数据结构
        SimFlowUsedThisMonthTotalResponse simFlowUsedThisMonthTotalResponse = new SimFlowUsedThisMonthTotalResponse();
        simFlowUsedThisMonthTotalResponse.setDataAmount(cmccSimFlowUsedThisMonthResponse.getDataAmount());
        Type type = new TypeToken<List<SimFlowUsedThisMonthTotalItemResponse>>() {
        }.getType();
        List<SimFlowUsedThisMonthTotalItemResponse> list = new Gson().fromJson(new Gson().toJson(cmccSimFlowUsedThisMonthResponse.getApnUseAmountList()), type);
        simFlowUsedThisMonthTotalResponse.setApnUseAmountList(list);

        return ResponseDataUtil.buildSuccess(simFlowUsedThisMonthTotalResponse);
    }

    /**
     * 单卡流量池内使用量实时查询
     *
     * @param msisdn
     * @param iccid
     * @param imsi
     * @return
     */
    public ResponseData<List<SimFlowUsedPoolResponse>> querySimFlowUsedPool(String msisdn, String iccid, String imsi) {

        /**
         * 获取token
         */
        ResponseData<CmccTokenResponse> cmccTokenResponseData = this.getToken();
        if (!ResponseCode.SUCCESS.getCode().equals(cmccTokenResponseData.getCode())) {
            return ResponseDataUtil.buildError(ResponseCode.CARRIER_TOKEN_GET_ERROR);
        }
        String token = cmccTokenResponseData.getData().getToken();

        /**
         * 组织请求参数
         */
        String transid = cmccIotProperties.getAppId() + DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmss") + cmccRedisCacheUtils.getSequence();
        String requestUrl = cmccIotProperties.getUrl() + "/v5/ec/query/sim-data-usage-inpool?transid=" + transid + "&token=" + token + "&msisdn=" + msisdn;

        /**
         * 向移动发送请求
         */
        CmccResponseData<List<CmccSimFlowUsedPoolResponse>> cmccResponseData = new CmccResponseData<>();
        try {
            String response = Okhttp3Utils.get(requestUrl);
            Type type = new TypeToken<CmccResponseData<List<CmccSimFlowUsedPoolResponse>>>() {
            }.getType();
            cmccResponseData = new Gson().fromJson(response, type);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(ResponseCode.CARRIER_DATA_GET_ERROR);
        }

        /**
         * 请求获取非正常数据
         */
        if (!"0".equals(cmccResponseData.getStatus())) {
            return ResponseDataUtil.buildError(cmccResponseData.getStatus(), cmccResponseData.getMessage());
        }

        /**
         * 返回执行结果，各个运营商，映射成统一响应格式
         */
        CmccSimFlowUsedPoolResponse cmccSimFlowUsedPoolResponse = cmccResponseData.getResult().get(0);
        //对外api统一数据结构
        Type type = new TypeToken<List<SimFlowUsedPoolResponse>>() {
        }.getType();
        List<SimFlowUsedPoolResponse> list = new Gson().fromJson(new Gson().toJson(cmccSimFlowUsedPoolResponse.getApnList()), type);

        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 物联卡单月 GPRS 流量使用量批量查询
     *
     * @param msisdns
     * @param iccids
     * @param imsis
     * @param queryDate
     * @return
     */
    public ResponseData<List<SimGprsFlowUsedMonthlyBatch>> querySimGprsFlowUsedMonthlyBatch(List<String> msisdns, List<String> iccids, List<String> imsis, String queryDate) {

        /**
         * 获取token
         */
        ResponseData<CmccTokenResponse> cmccTokenResponseData = this.getToken();
        if (!ResponseCode.SUCCESS.getCode().equals(cmccTokenResponseData.getCode())) {
            return ResponseDataUtil.buildError(ResponseCode.CARRIER_TOKEN_GET_ERROR);
        }
        String token = cmccTokenResponseData.getData().getToken();

        /**
         * 组织请求参数
         */
        if (msisdns.size() > 100) {
            return ResponseDataUtil.buildError(ResponseCode.SIM_TOO_MUCH_QUERY_ERROR);
        }
        String msisdnsParams = "";
        for (String mds : msisdns) {
            if (StringUtils.isEmpty(msisdnsParams)) {
                msisdnsParams = mds;
            } else {
                msisdnsParams = "_" + mds;
            }
        }
        String transid = cmccIotProperties.getAppId() + DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmss") + cmccRedisCacheUtils.getSequence();
        String requestUrl = cmccIotProperties.getUrl() + "/v5/ec/query/sim-data-usage-monthly/batch?transid=" + transid + "&token=" + token + "&msisdns=" + msisdnsParams + "&queryDate=" + queryDate;

        /**
         * 向移动发送请求
         */
        CmccResponseData<List<CmccSimGprsFlowUsedMonthlyBatch>> cmccResponseData = new CmccResponseData<>();
        try {
            String response = Okhttp3Utils.get(requestUrl);
            Type type = new TypeToken<CmccResponseData<List<CmccSimGprsFlowUsedMonthlyBatch>>>() {
            }.getType();
            cmccResponseData = new Gson().fromJson(response, type);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(ResponseCode.CARRIER_DATA_GET_ERROR);
        }

        /**
         * 请求获取非正常数据
         */
        if (!"0".equals(cmccResponseData.getStatus())) {
            return ResponseDataUtil.buildError(cmccResponseData.getStatus(), cmccResponseData.getMessage());
        }

        /**
         * 返回执行结果，各个运营商，映射成统一响应格式
         */
        CmccSimGprsFlowUsedMonthlyBatch cmccSimFlowUsedPoolResponse = cmccResponseData.getResult().get(0);
        //对外api统一数据结构
        Type type = new TypeToken<List<SimGprsFlowUsedMonthlyBatch>>() {
        }.getType();
        List<SimGprsFlowUsedMonthlyBatch> list = new Gson().fromJson(new Gson().toJson(cmccSimFlowUsedPoolResponse.getDataAmountList()), type);

        return ResponseDataUtil.buildSuccess(list);
    }


}
