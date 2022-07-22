package com.smoc.cloud.api.service;


import com.smoc.cloud.api.remote.cmcc.service.CmccIotSimFlowService;
import com.smoc.cloud.api.request.SimBaseRequest;
import com.smoc.cloud.api.request.SimsBaseRequest;
import com.smoc.cloud.api.request.SimsGprsFlowMonthlyRequest;
import com.smoc.cloud.api.response.flow.SimFlowUsedPoolResponse;
import com.smoc.cloud.api.response.flow.SimFlowUsedThisMonthResponse;
import com.smoc.cloud.api.response.flow.SimFlowUsedThisMonthTotalResponse;
import com.smoc.cloud.api.response.flow.SimGprsFlowUsedMonthlyBatch;
import com.smoc.cloud.api.response.info.BatchSimBaseInfoResponse;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.iot.repository.ApiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class IotSimFlowQueryService {

    @Autowired
    private ApiRepository apiRepository;

    @Autowired
    private CmccIotSimFlowService cmccIotSimFlowService;

    /**
     * 单卡本月套餐内流量使用量实时查询(加入流量池或流量共享的卡无法查询)
     * 验证物联网卡是否存在
     * 如果存在根据订单号，分辨出那个运营商
     * 判断运营商是否支持订单接口查询
     * 根据运营商，路由到对应运营商接口
     * 运营商订单查询
     * 是否查询结果本地持久化?
     * 把运营商查询结果映射为最终结果
     */
    public ResponseData<SimFlowUsedThisMonthResponse> querySimFlowUsedThisMonth(SimBaseRequest simBaseRequest) {

        /**
         * 验证物联网卡是否存在
         */
        Boolean isExist = apiRepository.isExistAccountSim(simBaseRequest.getAccount(), simBaseRequest.getMsisdn());
        if (!isExist) {
            return ResponseDataUtil.buildError(ResponseCode.SIM_NOT_EXIST_ERROR);
        }

        /**
         * 如果存在根据订单号，分辨出那个运营商,并路由到运营商对应的订单查询接口
         */


        /**
         * 查询，暂时只支持移动API
         */
        ResponseData<SimFlowUsedThisMonthResponse> responseData = cmccIotSimFlowService.querySimFlowUsedThisMonth(simBaseRequest.getMsisdn(), "", "");

        return responseData;
    }

    /**
     * 单卡本月流量累计使用量查询
     * 验证物联网卡是否存在
     * 如果存在根据订单号，分辨出那个运营商
     * 判断运营商是否支持订单接口查询
     * 根据运营商，路由到对应运营商接口
     * 运营商订单查询
     * 是否查询结果本地持久化?
     * 把运营商查询结果映射为最终结果
     */
    public  ResponseData<SimFlowUsedThisMonthTotalResponse> querySimFlowUsedThisMonthTotal(SimBaseRequest simBaseRequest) {

        /**
         * 验证物联网卡是否存在
         */
        Boolean isExist = apiRepository.isExistAccountSim(simBaseRequest.getAccount(), simBaseRequest.getMsisdn());
        if (!isExist) {
            return ResponseDataUtil.buildError(ResponseCode.SIM_NOT_EXIST_ERROR);
        }

        /**
         * 如果存在根据订单号，分辨出那个运营商,并路由到运营商对应的订单查询接口
         */


        /**
         * 查询，暂时只支持移动API
         */
        ResponseData<SimFlowUsedThisMonthTotalResponse> responseData = cmccIotSimFlowService.querySimFlowUsedThisMonthTotal(simBaseRequest.getMsisdn(), "", "");

        return responseData;
    }

    /**
     * 单卡流量池内使用量实时查询
     * 验证物联网卡是否存在
     * 如果存在根据订单号，分辨出那个运营商
     * 判断运营商是否支持订单接口查询
     * 根据运营商，路由到对应运营商接口
     * 运营商订单查询
     * 是否查询结果本地持久化?
     * 把运营商查询结果映射为最终结果
     */
    public ResponseData<List<SimFlowUsedPoolResponse>> querySimFlowUsedPool(SimBaseRequest simBaseRequest) {

        /**
         * 验证物联网卡是否存在
         */
        Boolean isExist = apiRepository.isExistAccountSim(simBaseRequest.getAccount(), simBaseRequest.getMsisdn());
        if (!isExist) {
            return ResponseDataUtil.buildError(ResponseCode.SIM_NOT_EXIST_ERROR);
        }

        /**
         * 如果存在根据订单号，分辨出那个运营商,并路由到运营商对应的订单查询接口
         */


        /**
         * 查询，暂时只支持移动API
         */
        ResponseData<List<SimFlowUsedPoolResponse>> responseData = cmccIotSimFlowService.querySimFlowUsedPool(simBaseRequest.getMsisdn(), "", "");

        return responseData;
    }

    /**
     * 物联卡单月 GPRS 流量使用量批量查询
     * 验证物联网卡是否存在
     * 如果存在根据订单号，分辨出那个运营商
     * 判断运营商是否支持订单接口查询
     * 根据运营商，路由到对应运营商接口
     * 运营商订单查询
     * 是否查询结果本地持久化?
     * 把运营商查询结果映射为最终结果
     */
    public  ResponseData<List<SimGprsFlowUsedMonthlyBatch>> querySimGprsFlowUsedMonthlyBatch(SimsGprsFlowMonthlyRequest simsGprsFlowMonthlyRequest) {

        /**
         * 验证物联网卡是否存在
         */
//        Boolean isExist = apiRepository.isExistAccountSim(simBaseRequest.getAccount(), simBaseRequest.getMsisdn());
//        if (!isExist) {
//            return ResponseDataUtil.buildError(ResponseCode.SIM_NOT_EXIST_ERROR);
//        }

        /**
         * 如果存在根据订单号，分辨出那个运营商,并路由到运营商对应的订单查询接口
         */


        /**
         * 查询，暂时只支持移动API
         */
        ResponseData<List<SimGprsFlowUsedMonthlyBatch>> responseData = cmccIotSimFlowService.querySimGprsFlowUsedMonthlyBatch(simsGprsFlowMonthlyRequest.getMsisdns(), simsGprsFlowMonthlyRequest.getIccids(), simsGprsFlowMonthlyRequest.getImsis(),simsGprsFlowMonthlyRequest.getQueryDate());

        return responseData;
    }
}
