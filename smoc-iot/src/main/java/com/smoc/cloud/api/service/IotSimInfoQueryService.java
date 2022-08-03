package com.smoc.cloud.api.service;

import com.smoc.cloud.api.remote.cmcc.service.CmccIotSimInfoService;
import com.smoc.cloud.api.request.SimBaseRequest;
import com.smoc.cloud.api.request.SimStatusChangeRequest;
import com.smoc.cloud.api.request.SimsBaseRequest;
import com.smoc.cloud.api.response.info.*;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.iot.account.repository.IotAccountPackageItemsRepository;
import com.smoc.cloud.iot.carrier.repository.IotFlowCardsPrimaryInfoRepository;
import com.smoc.cloud.iot.repository.ApiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class IotSimInfoQueryService {

    @Autowired
    private ApiRepository apiRepository;

    @Autowired
    private CmccIotSimInfoService cmccIotSimInfoService;

    @Resource
    private IotFlowCardsPrimaryInfoRepository iotFlowCardsPrimaryInfoRepository;

    @Resource
    private IotAccountPackageItemsRepository iotAccountPackageItemsRepository;

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
//    public ResponseData<OrderHandleResponse> queryOrderHandle(OrderHandleRequest orderHandleRequest) {
//
//        /**
//         * 验证订单是否存在
//         */
//        Boolean isExist = apiRepository.isExistAccountOrder(orderHandleRequest.getAccount(), orderHandleRequest.getOrderNum());
//        if (!isExist) {
//            return ResponseDataUtil.buildError(ResponseCode.ORDER_NOT_EXIST_ERROR);
//        }
//
//        /**
//         * 如果存在根据订单号，分辨出那个运营商,并路由到运营商对应的订单查询接口
//         */
//
//
//        /**
//         * 查询，暂时只支持移动API
//         */
//        ResponseData<OrderHandleResponse> responseData = cmccIotSimInfoService.queryOrderHandle(orderHandleRequest.getOrderNum());
//
//        return responseData;
//    }
//
//    /**
//     * 物联网卡批量办理结果查询
//     * 验证订单是否存在
//     * 如果存在根据订单号，分辨出那个运营商
//     * 判断运营商是否支持订单接口查询
//     * 根据运营商，路由到对应运营商接口
//     * 运营商订单查询
//     * 是否查询结果本地持久化?
//     * 把运营商查询结果映射为最终结果
//     */
//    public ResponseData<BatchSimHandleResponse> queryBatchSimHandle(BatchSimHandleRequest batchSimHandleRequest) {
//
//        /**
//         * 验证订单是否存在
//         */
//        Boolean isExist = apiRepository.isExistAccountOrder(batchSimHandleRequest.getAccount(), batchSimHandleRequest.getBatchId());
//        if (!isExist) {
//            return ResponseDataUtil.buildError(ResponseCode.ORDER_NOT_EXIST_ERROR);
//        }
//
//        /**
//         * 如果存在根据订单号，分辨出那个运营商,并路由到运营商对应的订单查询接口
//         */
//
//
//        /**
//         * 查询，暂时只支持移动API
//         */
//        ResponseData<BatchSimHandleResponse> responseData = cmccIotSimInfoService.queryBatchSimHandle(batchSimHandleRequest.getBatchId());
//
//        return responseData;
//    }

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
//        ResponseData<SimBaseInfoResponse> responseData = cmccIotSimInfoService.querySimBaseInfo(simBaseRequest.getMsisdn(), "", "");
        //本地查询
        SimBaseInfoResponse simBaseInfoResponse = this.iotAccountPackageItemsRepository.querySimBaseInfo(simBaseRequest.getAccount(), simBaseRequest.getIccid());
        return ResponseDataUtil.buildSuccess(simBaseInfoResponse);
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
    public PageList<SimBaseInfoResponse> queryBatchSimBaseInfo(SimsBaseRequest simsBaseRequest, PageParams pageParams) {

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
//        ResponseData<List<BatchSimBaseInfoResponse>> responseData = cmccIotSimInfoService.queryBatchSimBaseInfo(simsBaseRequest.getMsisdns(), simsBaseRequest.getIccids(), simsBaseRequest.getImsis());
        PageList<SimBaseInfoResponse> page = this.iotAccountPackageItemsRepository.queryBatchSimBaseInfo(simsBaseRequest.getAccount(), simsBaseRequest.getIccids(), pageParams);
        return page;
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
        //ResponseData<List<SimChangeHistoryResponse>> responseData = cmccIotSimInfoService.querySimChangeHistory(simBaseRequest.getMsisdn(), "", "");
        List<SimChangeHistoryResponse> simChangeHistoryResponses = new ArrayList<>();
        SimChangeHistoryResponse simChangeHistoryResponse1 = new SimChangeHistoryResponse();
        simChangeHistoryResponse1.setDescStatus("2");
        simChangeHistoryResponse1.setTargetStatus("4");
        simChangeHistoryResponse1.setChangeDate("2022-07-26 11:45:54");
        simChangeHistoryResponses.add(simChangeHistoryResponse1);
        SimChangeHistoryResponse simChangeHistoryResponse = new SimChangeHistoryResponse();
        simChangeHistoryResponse.setDescStatus("1");
        simChangeHistoryResponse.setTargetStatus("2");
        simChangeHistoryResponse.setChangeDate("2022-07-26 11:45:04");
        simChangeHistoryResponses.add(simChangeHistoryResponse);
        return ResponseDataUtil.buildSuccess(simChangeHistoryResponses);
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
        //ResponseData<SimStartStatusResponse> responseData = cmccIotSimInfoService.querySimStartStatus(simBaseRequest.getMsisdn(), "", "");
        SimStartStatusResponse simStartStatusResponse = new SimStartStatusResponse();
        simStartStatusResponse.setSimStatus("0");
        simStartStatusResponse.setReason("");
        return ResponseDataUtil.buildSuccess(simStartStatusResponse);
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
        //ResponseData<SimStatusResponse> responseData = cmccIotSimInfoService.querySimStatus(simBaseRequest.getMsisdn(), "", "");
        SimStatusResponse simStatusResponse = new SimStatusResponse();
        simStatusResponse.setCardStatus("2");
        simStatusResponse.setLastChangeDate("2022-07-25 19:03:04");
        return ResponseDataUtil.buildSuccess(simStatusResponse);
    }

    /**
     * 变更物联网卡状态
     * 验证物联网卡是否存在
     * 如果存在根据订单号，分辨出那个运营商
     * 判断运营商是否支持订单接口查询
     * 根据运营商，路由到对应运营商接口
     * 运营商订单查询
     * 是否查询结果本地持久化?
     * 把运营商查询结果映射为最终结果
     */
    public ResponseData<SimStatusChangeReponse> changeSimStatus(SimStatusChangeRequest simStatusChangeRequest) {

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
        //ResponseData<SimStatusResponse> responseData = cmccIotSimInfoService.querySimStatus(simBaseRequest.getMsisdn(), "", "");
        SimStatusChangeReponse simStatusChangeReponse = new SimStatusChangeReponse();
        simStatusChangeReponse.setIccid("898600D6991330004149");
        simStatusChangeReponse.setOrderNo("20220705212458357896147235");
        return ResponseDataUtil.buildSuccess(simStatusChangeReponse);
    }

    /**
     * 查询物联网卡位置信息
     * 验证物联网卡是否存在
     * 如果存在根据订单号，分辨出那个运营商
     * 判断运营商是否支持订单接口查询
     * 根据运营商，路由到对应运营商接口
     * 运营商订单查询
     * 是否查询结果本地持久化?
     * 把运营商查询结果映射为最终结果
     */
    public ResponseData<SimLocationsResponse> querySimLocations(SimBaseRequest simBaseRequest) {

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
        //ResponseData<SimStopReasonResponse> responseData = cmccIotSimInfoService.querySimStopReason(simBaseRequest.getMsisdn(), "", "");
        SimLocationsResponse simLocationsResponse = new SimLocationsResponse();
        simLocationsResponse.setIccid("898600D6991330004149");
        simLocationsResponse.setLongitude("116.823401");
        simLocationsResponse.setLatitude("39.114281");
        return ResponseDataUtil.buildSuccess(simLocationsResponse);
    }


}
