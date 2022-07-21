package com.smoc.cloud.api.service;

import com.smoc.cloud.api.remote.cmcc.service.CmccIotRequestService;
import com.smoc.cloud.api.request.BatchSimHandleRequest;
import com.smoc.cloud.api.request.OrderHandleRequest;
import com.smoc.cloud.api.request.SimBaseRequest;
import com.smoc.cloud.api.response.*;
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
public class IotSimQueryService {

    @Autowired
    private ApiRepository apiRepository;

    @Autowired
    private CmccIotRequestService cmccIotRequestService;

    /**
     * 单卡操作订单处理情况查询
     * 验证订单是否存在
     * 如果存在根据订单号，分辨出那个运营商
     * 判断运营商是否支持订单接口查询
     * 根据运营商，路由到对应运营商接口
     * 运营商订单查询
     * 是否查询结果本地持久化?
     * 把运营商查询结果映射为最终结果
     */
    public ResponseData<OrderHandleResponse> queryOrderHandle(OrderHandleRequest orderHandleRequest) {

        /**
         * 验证订单是否存在
         */
        Boolean isExist = apiRepository.isExistAccountOrder(orderHandleRequest.getAccount(), orderHandleRequest.getOrderNum());
        if (!isExist) {
            return ResponseDataUtil.buildError(ResponseCode.ORDER_NOT_EXIST_ERROR);
        }

        /**
         * 如果存在根据订单号，分辨出那个运营商,并路由到运营商对应的订单查询接口
         */


        /**
         * 查询，暂时只支持移动API
         */
        ResponseData<OrderHandleResponse> responseData = cmccIotRequestService.queryOrderHandle(orderHandleRequest.getOrderNum());

        return responseData;
    }

    /**
     * 物联网卡批量办理结果查询
     * 验证订单是否存在
     * 如果存在根据订单号，分辨出那个运营商
     * 判断运营商是否支持订单接口查询
     * 根据运营商，路由到对应运营商接口
     * 运营商订单查询
     * 是否查询结果本地持久化?
     * 把运营商查询结果映射为最终结果
     */
    public ResponseData<BatchSimHandleResponse> queryBatchSimHandle(BatchSimHandleRequest batchSimHandleRequest) {

        /**
         * 验证订单是否存在
         */
        Boolean isExist = apiRepository.isExistAccountOrder(batchSimHandleRequest.getAccount(), batchSimHandleRequest.getBatchId());
        if (!isExist) {
            return ResponseDataUtil.buildError(ResponseCode.ORDER_NOT_EXIST_ERROR);
        }

        /**
         * 如果存在根据订单号，分辨出那个运营商,并路由到运营商对应的订单查询接口
         */


        /**
         * 查询，暂时只支持移动API
         */
        ResponseData<BatchSimHandleResponse> responseData = cmccIotRequestService.queryBatchSimHandle(batchSimHandleRequest.getBatchId());

        return responseData;
    }

    /**
     * 单卡基本信息查询
     * 验证物联网卡是否存在
     * 如果存在根据订单号，分辨出那个运营商
     * 判断运营商是否支持订单接口查询
     * 根据运营商，路由到对应运营商接口
     * 运营商订单查询
     * 是否查询结果本地持久化?
     * 把运营商查询结果映射为最终结果
     */
    public ResponseData<SimBaseInfoResponse> querySimBaseInfo(SimBaseRequest simBaseRequest) {

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
        ResponseData<SimBaseInfoResponse> responseData = cmccIotRequestService.querySimBaseInfo(simBaseRequest.getMsisdn(), "", "");

        return responseData;
    }

    /**
     * 码号信息批量查询
     * 验证物联网卡是否存在
     * 如果存在根据订单号，分辨出那个运营商
     * 判断运营商是否支持订单接口查询
     * 根据运营商，路由到对应运营商接口
     * 运营商订单查询
     * 是否查询结果本地持久化?
     * 把运营商查询结果映射为最终结果
     */
    public ResponseData<List<BatchSimBaseInfoResponse>> queryBatchSimBaseInfo(SimBaseRequest simBaseRequest) {

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
        ResponseData<List<BatchSimBaseInfoResponse>> responseData = cmccIotRequestService.queryBatchSimBaseInfo(simBaseRequest.getMsisdn(), "", "");

        return responseData;
    }

    /**
     * 单卡状态变更历史查询
     * 验证物联网卡是否存在
     * 如果存在根据订单号，分辨出那个运营商
     * 判断运营商是否支持订单接口查询
     * 根据运营商，路由到对应运营商接口
     * 运营商订单查询
     * 是否查询结果本地持久化?
     * 把运营商查询结果映射为最终结果
     */
    public ResponseData<List<SimChangeHistoryResponse>> querySimChangeHistory(SimBaseRequest simBaseRequest) {

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
        ResponseData<List<SimChangeHistoryResponse>> responseData = cmccIotRequestService.querySimChangeHistory(simBaseRequest.getMsisdn(), "", "");

        return responseData;
    }

    /**
     * 物联卡管理停复机冻结状态查询
     * 验证物联网卡是否存在
     * 如果存在根据订单号，分辨出那个运营商
     * 判断运营商是否支持订单接口查询
     * 根据运营商，路由到对应运营商接口
     * 运营商订单查询
     * 是否查询结果本地持久化?
     * 把运营商查询结果映射为最终结果
     */
    public ResponseData<SimStartStatusResponse> querySimStartStatus(SimBaseRequest simBaseRequest) {

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
        ResponseData<SimStartStatusResponse> responseData = cmccIotRequestService.querySimStartStatus(simBaseRequest.getMsisdn(), "", "");

        return responseData;
    }

    /**
     * 物联卡状态查询（待激活、已激活、停机）
     * 验证物联网卡是否存在
     * 如果存在根据订单号，分辨出那个运营商
     * 判断运营商是否支持订单接口查询
     * 根据运营商，路由到对应运营商接口
     * 运营商订单查询
     * 是否查询结果本地持久化?
     * 把运营商查询结果映射为最终结果
     */
    public ResponseData<SimStatusResponse> querySimStatus(SimBaseRequest simBaseRequest) {

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
        ResponseData<SimStatusResponse> responseData = cmccIotRequestService.querySimStatus(simBaseRequest.getMsisdn(), "", "");

        return responseData;
    }


}
