package com.smoc.cloud.api.remote.cmcc.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smoc.cloud.api.remote.cmcc.cache.CmccRedisCacheUtils;
import com.smoc.cloud.api.remote.cmcc.configuration.CmccIotProperties;
import com.smoc.cloud.api.remote.cmcc.response.*;
import com.smoc.cloud.api.response.*;
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

/**
 * 移动物联网卡接口对接请求服务
 */
@Slf4j
@Service
public class CmccIotRequestService {

    @Autowired
    private CmccIotProperties cmccIotProperties;

    @Autowired
    private CmccRedisCacheUtils cmccRedisCacheUtils;


    /**
     * 单卡操作订单处理情况查询
     * 订单保存60天
     *
     * @return
     */
    public ResponseData<OrderHandleResponse> queryOrderHandle(String orderNum) {

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
        String requestUrl = cmccIotProperties.getUrl() + "/v5/ec/query/order-info?transid=" + transid + "&token=" + token + "&orderNum=" + orderNum;

        /**
         * 向移动发送请求
         */
        CmccResponseData<List<CmccOrderInfoResponse>> orderResponseData = new CmccResponseData<>();
        try {
            String response = Okhttp3Utils.get(requestUrl);
            Type type = new TypeToken<CmccResponseData<List<CmccOrderInfoResponse>>>() {
            }.getType();
            orderResponseData = new Gson().fromJson(response, type);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(ResponseCode.CARRIER_DATA_GET_ERROR);
        }

        /**
         * 请求获取非正常数据
         */
        if (!"0".equals(orderResponseData.getStatus())) {
            return ResponseDataUtil.buildError(orderResponseData.getStatus(), orderResponseData.getMessage());
        }

        /**
         * 返回执行结果，各个运营商，映射成统一响应格式
         */
        CmccOrderInfoResponse cmccOrderInfo = orderResponseData.getResult().get(0);
        //对外api统一数据结构
        OrderHandleResponse orderHandleResponse = new OrderHandleResponse();
        orderHandleResponse.setOrderNum(cmccOrderInfo.getOrderNum());
        orderHandleResponse.setOrderStatus(cmccOrderInfo.getStatus());
        orderHandleResponse.setHandleDate(cmccOrderInfo.getStatusDate());
        orderHandleResponse.setCreateDate(cmccOrderInfo.getCreateDate());

        return ResponseDataUtil.buildSuccess(orderHandleResponse);
    }

    /**
     * 物联网卡批量办理结果查询
     *
     * @param batchId 批次id
     * @return
     */
    public ResponseData<BatchSimHandleResponse> queryBatchSimHandle(String batchId) {

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
        String requestUrl = cmccIotProperties.getUrl() + "/v5/ec/query/sim-batch-result?transid=" + transid + "&token=" + token + "&jobId=" + batchId;

        /**
         * 向移动发送请求
         */
        CmccResponseData<List<CmccBatchSimResponse>> cmccResponseData = new CmccResponseData<>();
        try {
            String response = Okhttp3Utils.get(requestUrl);
            Type type = new TypeToken<CmccResponseData<List<CmccBatchSimResponse>>>() {
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
        CmccBatchSimResponse cmccBatchSimResponse = cmccResponseData.getResult().get(0);
        //对外api统一数据结构
        BatchSimHandleResponse batchSimHandleResponse = new BatchSimHandleResponse();
        batchSimHandleResponse.setBatchStatus(cmccBatchSimResponse.getJobStatus());
        batchSimHandleResponse.setBatchType(cmccBatchSimResponse.getResultType());
        batchSimHandleResponse.setBatchId(cmccBatchSimResponse.getResultId());
        batchSimHandleResponse.setQueryStatus(cmccBatchSimResponse.getStatus());
        batchSimHandleResponse.setMessage(cmccBatchSimResponse.getMessage());
        Type type = new TypeToken<List<BatchSimHandleItemResponse>>() {
        }.getType();
        List<BatchSimHandleItemResponse> batchList = new Gson().fromJson(new Gson().toJson(cmccBatchSimResponse.getResultList()), type);
        batchSimHandleResponse.setBatchList(batchList);

        return ResponseDataUtil.buildSuccess(batchSimHandleResponse);
    }

    /**
     * 单卡基本信息查询
     *
     * @param msisdn
     * @return
     */
    public ResponseData<SimBaseInfoResponse> querySimBaseInfo(String msisdn, String iccid, String imsi) {

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
        String requestUrl = cmccIotProperties.getUrl() + "/v5/ec/query/sim-basic-info?transid=" + transid + "&token=" + token + "&msisdn=" + msisdn;

        /**
         * 向移动发送请求
         */
        CmccResponseData<List<CmccSimBaseInfoResponse>> cmccResponseData = new CmccResponseData<>();
        try {
            String response = Okhttp3Utils.get(requestUrl);
            Type type = new TypeToken<CmccResponseData<List<CmccSimBaseInfoResponse>>>() {
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
        CmccSimBaseInfoResponse cmccSimBaseInfoResponse = cmccResponseData.getResult().get(0);
        //对外api统一数据结构
        SimBaseInfoResponse simBaseInfoResponse = new SimBaseInfoResponse();
        simBaseInfoResponse.setMsisdn(cmccSimBaseInfoResponse.getMsisdn());
        simBaseInfoResponse.setIccid(cmccSimBaseInfoResponse.getIccid());
        simBaseInfoResponse.setImsi(cmccSimBaseInfoResponse.getImsi());
        simBaseInfoResponse.setActiveDate(cmccSimBaseInfoResponse.getActiveDate());
        simBaseInfoResponse.setOpenDate(cmccSimBaseInfoResponse.getOpenDate());
        simBaseInfoResponse.setRemark(cmccSimBaseInfoResponse.getRemark());

        return ResponseDataUtil.buildSuccess(simBaseInfoResponse);
    }

    /**
     * 批量查询物联网卡基础信息
     *
     * @param msisdn
     * @return
     */
    public ResponseData<List<BatchSimBaseInfoResponse>> queryBatchSimBaseInfo(String msisdn, String iccid, String imsi) {

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
        String msisdnsParams = "";
        String[] msisdns = msisdn.split("_");
        Integer mdsLength = msisdns.length;
        if (mdsLength > 100) {
            return ResponseDataUtil.buildError(ResponseCode.SIM_TOO_MUCH_QUERY_ERROR);
        }
        for (String mds : msisdns) {
            if (StringUtils.isEmpty(msisdnsParams)) {
                msisdnsParams = mds;
            } else {
                msisdnsParams = "_" + mds;
            }
        }
        String transid = cmccIotProperties.getAppId() + DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmss") + cmccRedisCacheUtils.getSequence();
        String requestUrl = cmccIotProperties.getUrl() + "/v5/ec/query/sim-card-info/batch?transid=" + transid + "&token=" + token + "&msisdns=" + msisdnsParams;

        /**
         * 向移动发送请求
         */
        CmccResponseData<List<CmccBatchSimBaseInfoResponse>> cmccResponseData = new CmccResponseData<>();
        try {
            String response = Okhttp3Utils.get(requestUrl);
            Type type = new TypeToken<CmccResponseData<List<CmccBatchSimBaseInfoResponse>>>() {
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
        List<CmccBatchSimBaseInfoResponse> cmccBatchSimBaseInfoList = cmccResponseData.getResult();
        //对外api统一数据结构
        Type type = new TypeToken<List<BatchSimBaseInfoResponse>>() {
        }.getType();
        List<BatchSimBaseInfoResponse> list = new Gson().fromJson(new Gson().toJson(cmccBatchSimBaseInfoList), type);

        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 单卡状态变更历史查询
     *
     * @param msisdn
     * @return
     */
    public ResponseData<List<SimChangeHistoryResponse>> querySimChangeHistory(String msisdn, String iccid, String imsi) {

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
        String requestUrl = cmccIotProperties.getUrl() + "/v5/ec/query/sim-change-history?transid=" + transid + "&token=" + token + "&msisdn=" + msisdn;

        /**
         * 向移动发送请求
         */
        CmccResponseData<List<CmccSimChangeHistoryResponse>> cmccResponseData = new CmccResponseData<>();
        try {
            String response = Okhttp3Utils.get(requestUrl);
            Type type = new TypeToken<CmccResponseData<List<CmccSimChangeHistoryResponse>>>() {
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
        CmccSimChangeHistoryResponse cmccSimChangeHistoryResponse = cmccResponseData.getResult().get(0);
        //对外api统一数据结构
        Type type = new TypeToken<List<SimChangeHistoryResponse>>() {
        }.getType();
        List<SimChangeHistoryResponse> historyList = new Gson().fromJson(new Gson().toJson(cmccSimChangeHistoryResponse.getChangeHistoryList()), type);

        return ResponseDataUtil.buildSuccess(historyList);
    }

    /**
     * 物联网卡状态查询（停机、复机、冻结...）
     *
     * @param msisdn
     * @return
     */
    public ResponseData<SimStartStatusResponse> querySimStartStatus(String msisdn, String iccid, String imsi) {

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
        String requestUrl = cmccIotProperties.getUrl() + "/v5/ec/query/sim-manage-stop-restart-status?transid=" + transid + "&token=" + token + "&msisdn=" + msisdn;

        /**
         * 向移动发送请求
         */
        CmccResponseData<List<CmccSimStartStatusResponse>> cmccResponseData = new CmccResponseData<>();
        try {
            String response = Okhttp3Utils.get(requestUrl);
            Type type = new TypeToken<CmccResponseData<List<CmccSimStartStatusResponse>>>() {
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
        CmccSimStartStatusResponse cmccSimStartStatusResponse = cmccResponseData.getResult().get(0);
        //对外api统一数据结构
        SimStartStatusResponse simStartStatusResponse = new SimStartStatusResponse();
        simStartStatusResponse.setSimStatus(cmccSimStartStatusResponse.getManageStopRestartStatus());
        simStartStatusResponse.setReason(cmccSimStartStatusResponse.getReason());


        return ResponseDataUtil.buildSuccess(simStartStatusResponse);
    }

    /**
     * 物联网卡状态查询（待激活、已激活、停机...）
     *
     * @param msisdn
     * @return
     */
    public ResponseData<SimStatusResponse> querySimStatus(String msisdn, String iccid, String imsi) {

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
        String requestUrl = cmccIotProperties.getUrl() + "/v5/ec/query/sim-status?transid=" + transid + "&token=" + token + "&msisdn=" + msisdn;

        /**
         * 向移动发送请求
         */
        CmccResponseData<List<CmccSimStatusResponse>> cmccResponseData = new CmccResponseData<>();
        try {
            String response = Okhttp3Utils.get(requestUrl);
            Type type = new TypeToken<CmccResponseData<List<CmccSimStatusResponse>>>() {
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
        CmccSimStatusResponse cmccSimStatusResponse = cmccResponseData.getResult().get(0);
        //对外api统一数据结构
        SimStatusResponse simStatusResponse = new SimStatusResponse();
        simStatusResponse.setCardStatus(cmccSimStatusResponse.getCardStatus());
        simStatusResponse.setLastChangeDate(cmccSimStatusResponse.getLastChangeDate());


        return ResponseDataUtil.buildSuccess(simStatusResponse);
    }


    /**
     * 获取移动IOT服务token
     *
     * @return
     */
    public ResponseData<CmccTokenResponse> getToken() {

        /**
         * 先从本地获取token
         */
        String token = this.cmccRedisCacheUtils.getLocalToken();
        if (!StringUtils.isEmpty(token)) {
            CmccTokenResponse cmccTokenResponse = new CmccTokenResponse();
            cmccTokenResponse.setToken(token);
            ResponseDataUtil.buildSuccess(cmccTokenResponse);
        }

        /**
         * 向移动发送获取token请求
         */
        CmccResponseData<List<CmccTokenResponse>> cmccResponseData = new CmccResponseData();
        String transid = cmccIotProperties.getAppId() + DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmss") + cmccRedisCacheUtils.getSequence();
        String requestUrl = cmccIotProperties.getUrl() + "/v5/ec/get/token?appid=" + cmccIotProperties.getAppId() + "&password=" + cmccIotProperties.getPassword() + "&transid=" + transid;
        try {
            String response = Okhttp3Utils.get(requestUrl);
            Type type = new TypeToken<CmccResponseData<List<CmccTokenResponse>>>() {
            }.getType();
            cmccResponseData = new Gson().fromJson(response, type);
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
        this.cmccRedisCacheUtils.saveLocalToken(cmccTokenResponse.getToken(), new Long(cmccTokenResponse.getTtl()), TimeUnit.SECONDS);

        return ResponseDataUtil.buildSuccess(cmccTokenResponse);
    }
}
