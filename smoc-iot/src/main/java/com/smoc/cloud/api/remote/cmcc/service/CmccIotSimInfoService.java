package com.smoc.cloud.api.remote.cmcc.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smoc.cloud.api.remote.cmcc.response.CmccResponseData;
import com.smoc.cloud.api.remote.cmcc.response.CmccTokenResponse;
import com.smoc.cloud.api.remote.cmcc.response.info.*;
import com.smoc.cloud.api.response.info.*;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.utils.Okhttp3Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 移动物联网卡接口对接请求服务  物联网卡基本信息查询
 */
@Slf4j
@Service
public class CmccIotSimInfoService extends CmccIotSimBaseService {


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
       String requestUrl = cmccIotProperties.getUrl() + "/v5/ec/query/order-info?transid=" + this.getTransId() + "&token=" + token + "&orderNum=" + orderNum;

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
        String requestUrl = cmccIotProperties.getUrl() + "/v5/ec/query/sim-batch-result?transid=" + this.getTransId() + "&token=" + token + "&jobId=" + batchId;

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
       String requestUrl = cmccIotProperties.getUrl() + "/v5/ec/query/sim-basic-info?transid=" + this.getTransId() + "&token=" + token + "&msisdn=" + msisdn;

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
//        simBaseInfoResponse.setActiveDate(cmccSimBaseInfoResponse.getActiveDate());
//        simBaseInfoResponse.setOpenDate(cmccSimBaseInfoResponse.getOpenDate());
        //simBaseInfoResponse.setRemark(cmccSimBaseInfoResponse.getRemark());

        return ResponseDataUtil.buildSuccess(simBaseInfoResponse);
    }

    /**
     * 批量查询物联网卡基础信息
     * @param msisdns
     * @param iccids
     * @param imsis
     * @return
     */
    public ResponseData<List<BatchSimBaseInfoResponse>> queryBatchSimBaseInfo(List<String> msisdns, List<String> iccids, List<String> imsis) {

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
        String requestUrl = cmccIotProperties.getUrl() + "/v5/ec/query/sim-card-info/batch?transid=" + this.getTransId() + "&token=" + token + "&msisdns=" + msisdnsParams;

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
       String requestUrl = cmccIotProperties.getUrl() + "/v5/ec/query/sim-change-history?transid=" + this.getTransId() + "&token=" + token + "&msisdn=" + msisdn;

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
       String requestUrl = cmccIotProperties.getUrl() + "/v5/ec/query/sim-manage-stop-restart-status?transid=" + this.getTransId() + "&token=" + token + "&msisdn=" + msisdn;

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
       String requestUrl = cmccIotProperties.getUrl() + "/v5/ec/query/sim-status?transid=" + this.getTransId() + "&token=" + token + "&msisdn=" + msisdn;

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
     * 单卡停机原因查询
     *
     * @param msisdn
     * @return
     */
    public ResponseData<SimStopReasonResponse> querySimStopReason(String msisdn, String iccid, String imsi) {

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
        String requestUrl = cmccIotProperties.getUrl() + "/v5/ec/query/sim-stop-reason?transid=" + this.getTransId() + "&token=" + token + "&msisdn=" + msisdn;

        /**
         * 向移动发送请求
         */
        CmccResponseData<List<CmccSimStopReasonResponse>> cmccResponseData = new CmccResponseData<>();
        try {
            String response = Okhttp3Utils.get(requestUrl);
            Type type = new TypeToken<CmccResponseData<List<CmccSimStopReasonResponse>>>() {
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
        CmccSimStopReasonResponse cmccSimStatusResponse = cmccResponseData.getResult().get(0);
        //对外api统一数据结构
        SimStopReasonResponse simStatusResponse = new SimStopReasonResponse();
        simStatusResponse.setStopReason(cmccSimStatusResponse.getStopReason());
        simStatusResponse.setShutdownReasonDesc(cmccSimStatusResponse.getShutdownReasonDesc());

        return ResponseDataUtil.buildSuccess(simStatusResponse);
    }

}
