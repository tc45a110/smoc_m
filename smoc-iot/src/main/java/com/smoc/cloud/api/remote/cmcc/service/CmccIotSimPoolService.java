package com.smoc.cloud.api.remote.cmcc.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smoc.cloud.api.remote.cmcc.response.CmccResponseData;
import com.smoc.cloud.api.remote.cmcc.response.CmccTokenResponse;
import com.smoc.cloud.api.remote.cmcc.response.pool.CmccSimGroupUsedThisMonth;
import com.smoc.cloud.api.remote.cmcc.response.pool.CmccSimGroupUsedThisMonthTotal;
import com.smoc.cloud.api.response.pool.SimGroupUsedThisMonth;
import com.smoc.cloud.api.response.pool.SimGroupUsedThisMonthPool;
import com.smoc.cloud.api.response.pool.SimGroupUsedThisMonthShare;
import com.smoc.cloud.api.response.pool.SimGroupUsedThisMonthTotal;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.utils.Okhttp3Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 流量池  移动api
 */
@Slf4j
@Service
public class CmccIotSimPoolService extends CmccIotSimBaseService {

//    /**
//     * 群组本月流量累计使用量实时查询
//     *
//     * @param groupId
//     * @return
//     */
//    public ResponseData<SimGroupUsedThisMonthTotal> queryGroupUsedThisMonthTotal(String groupId) {
//
//        /**
//         * 获取token
//         */
//        ResponseData<CmccTokenResponse> cmccTokenResponseData = this.getToken();
//        if (!ResponseCode.SUCCESS.getCode().equals(cmccTokenResponseData.getCode())) {
//            return ResponseDataUtil.buildError(ResponseCode.CARRIER_TOKEN_GET_ERROR);
//        }
//        String token = cmccTokenResponseData.getData().getToken();
//
//        /**
//         * 组织请求参数
//         */
//        String requestUrl = cmccIotProperties.getUrl() + "/v5/ec/query/group-data-usage?transid=" + this.getTransId() + "&token=" + token + "&groupId=" + groupId;
//
//        /**
//         * 向移动发送请求
//         */
//        CmccResponseData<List<CmccSimGroupUsedThisMonthTotal>> cmccResponseData = new CmccResponseData<>();
//        try {
//            String response = Okhttp3Utils.get(requestUrl);
//            Type type = new TypeToken<CmccResponseData<List<CmccSimGroupUsedThisMonthTotal>>>() {
//            }.getType();
//            cmccResponseData = new Gson().fromJson(response, type);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseDataUtil.buildError(ResponseCode.CARRIER_DATA_GET_ERROR);
//        }
//
//        /**
//         * 请求获取非正常数据
//         */
//        if (!"0".equals(cmccResponseData.getStatus())) {
//            return ResponseDataUtil.buildError(cmccResponseData.getStatus(), cmccResponseData.getMessage());
//        }
//
//        /**
//         * 返回执行结果，各个运营商，映射成统一响应格式
//         */
//        CmccSimGroupUsedThisMonthTotal cmccSimGroupUsedThisMonthTotal = cmccResponseData.getResult().get(0);
//        //对外api统一数据结构
//        SimGroupUsedThisMonthTotal simGroupUsedThisMonthTotal = new SimGroupUsedThisMonthTotal();
//        simGroupUsedThisMonthTotal.setUseAmount(cmccSimGroupUsedThisMonthTotal.getUseAmount());
//
//        return ResponseDataUtil.buildSuccess(simGroupUsedThisMonthTotal);
//    }

//    /**
//     * 群组本月套餐内流量使用量实时查询
//     *
//     * @param groupId
//     * @return
//     */
//    public ResponseData<SimGroupUsedThisMonth> queryGroupUsedThisMonth(String groupId) {
//
//        /**
//         * 获取token
//         */
//        ResponseData<CmccTokenResponse> cmccTokenResponseData = this.getToken();
//        if (!ResponseCode.SUCCESS.getCode().equals(cmccTokenResponseData.getCode())) {
//            return ResponseDataUtil.buildError(ResponseCode.CARRIER_TOKEN_GET_ERROR);
//        }
//        String token = cmccTokenResponseData.getData().getToken();
//
//        /**
//         * 组织请求参数
//         */
//        String requestUrl = cmccIotProperties.getUrl() + "/v5/ec/query/group-data-usage?transid=" + this.getTransId() + "&token=" + token + "&groupId=" + groupId;
//
//        /**
//         * 向移动发送请求
//         */
//        CmccResponseData<List<CmccSimGroupUsedThisMonth>> cmccResponseData = new CmccResponseData<>();
//        try {
//            String response = Okhttp3Utils.get(requestUrl);
//            Type type = new TypeToken<CmccResponseData<List<CmccSimGroupUsedThisMonth>>>() {
//            }.getType();
//            cmccResponseData = new Gson().fromJson(response, type);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseDataUtil.buildError(ResponseCode.CARRIER_DATA_GET_ERROR);
//        }
//
//        /**
//         * 请求获取非正常数据
//         */
//        if (!"0".equals(cmccResponseData.getStatus())) {
//            return ResponseDataUtil.buildError(cmccResponseData.getStatus(), cmccResponseData.getMessage());
//        }
//
//        /**
//         * 返回执行结果，各个运营商，映射成统一响应格式
//         */
//        CmccSimGroupUsedThisMonth cmccSimGroupUsedThisMonthTotal = cmccResponseData.getResult().get(0);
//        //对外api统一数据结构
//        SimGroupUsedThisMonth simGroupUsedThisMonth = new SimGroupUsedThisMonth();
//        SimGroupUsedThisMonthPool simGroupUsedThisMonthPool = new SimGroupUsedThisMonthPool();
//        SimGroupUsedThisMonthShare simGroupUsedThisMonthShare = new SimGroupUsedThisMonthShare();
//        if (null != cmccSimGroupUsedThisMonthTotal.getFlowPoolInfo() && cmccSimGroupUsedThisMonthTotal.getFlowPoolInfo().size() > 0) {
//            BeanUtils.copyProperties(cmccSimGroupUsedThisMonthTotal.getFlowPoolInfo().get(0), simGroupUsedThisMonthPool);
//        }
//        if (null != cmccSimGroupUsedThisMonthTotal.getFlowPoolSharingInfo() && cmccSimGroupUsedThisMonthTotal.getFlowPoolSharingInfo().size() > 0) {
//            BeanUtils.copyProperties(cmccSimGroupUsedThisMonthTotal.getFlowPoolSharingInfo().get(0), simGroupUsedThisMonthShare);
//        }
//        simGroupUsedThisMonth.setFlowPoolInfo(simGroupUsedThisMonthPool);
//        simGroupUsedThisMonth.setFlowPoolSharingInfo(simGroupUsedThisMonthShare);
//        return ResponseDataUtil.buildSuccess(simGroupUsedThisMonth);
//    }
}
