package com.smoc.cloud.api.service;

import com.google.gson.Gson;
import com.smoc.cloud.api.remote.cmcc.response.info.CarrierInfo;
import com.smoc.cloud.api.remote.cmcc.service.CmccIotSimInfoService;
import com.smoc.cloud.api.request.SimBaseRequest;
import com.smoc.cloud.api.request.SimStatusChangeRequest;
import com.smoc.cloud.api.request.SimsBaseRequest;
import com.smoc.cloud.api.response.info.*;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.iot.account.repository.IotAccountPackageItemsRepository;
import com.smoc.cloud.iot.carrier.repository.IotFlowCardsPrimaryInfoRepository;
import com.smoc.cloud.iot.repository.ApiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
     * 单卡基本信息查询（移动api查询已验证）
     */
    public ResponseData<SimBaseInfoResponse> querySimBaseInfo(SimBaseRequest simBaseRequest) {

        /**
         * 移动API 查询
         */
//        ResponseData<SimBaseInfoResponse> responseData = cmccIotSimInfoService.querySimBaseInfo("", simBaseRequest.getIccid(), "");
//        log.info("[responseData]:{}",new Gson().toJson(responseData));

        //本地查询
        SimBaseInfoResponse simBaseInfoResponse = this.iotAccountPackageItemsRepository.querySimBaseInfo(simBaseRequest.getAccount(), simBaseRequest.getIccid());
        if (null == simBaseInfoResponse) {
            return ResponseDataUtil.buildError(ResponseCode.SIM_NOT_EXIST_ERROR);
        }
        return ResponseDataUtil.buildSuccess(simBaseInfoResponse);
    }

    /**
     * 码号信息批量查询（移动api查询已验证）
     */
    public PageList<SimBaseInfoResponse> queryBatchSimBaseInfo(SimsBaseRequest simsBaseRequest, PageParams pageParams) {


        /**
         * 查询，暂时只支持移动API
         */
//        ResponseData<List<SimBaseInfoResponse>> responseData = cmccIotSimInfoService.queryBatchSimBaseInfo(null, simsBaseRequest.getIccids(),  null);
//        PageList<SimBaseInfoResponse> page = new PageList<>();
//        page.setPageParams(pageParams);
//        if (!ResponseCode.SUCCESS.getCode().equals(responseData.getCode())) {
//            return page;
//        }
//        page.setList(responseData.getData());
//        log.info("[responseData]:{}",new Gson().toJson(responseData));

        //本地查询
        PageList<SimBaseInfoResponse> page = this.iotAccountPackageItemsRepository.queryBatchSimBaseInfo(simsBaseRequest.getAccount(), simsBaseRequest.getIccids(), pageParams);
        return page;
    }

    /**
     * 单卡状态变更历史查询(移动api查询已验证）
     */
    public ResponseData<List<SimChangeHistoryResponse>> querySimChangeHistory(SimBaseRequest simBaseRequest) {

        /**
         * 验证物联网卡是否存在
         */
        CarrierInfo carrierInfo = apiRepository.isExistAccountSim(simBaseRequest.getAccount(), simBaseRequest.getIccid());
        if (null == carrierInfo) {
            return ResponseDataUtil.buildError(ResponseCode.SIM_NOT_EXIST_ERROR);
        }

        /**
         * 查询，暂时只支持移动API
         */
        ResponseData<List<SimChangeHistoryResponse>> responseData = cmccIotSimInfoService.querySimChangeHistory("", simBaseRequest.getIccid(), "",carrierInfo);
        log.info("[responseData]:{}", new Gson().toJson(responseData));
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
     * 物联卡状态查询（待激活、已激活、停机）(移动api查询已验证）
     */
    public ResponseData<SimStatusResponse> querySimStatus(SimBaseRequest simBaseRequest) {

        /**
         * 验证物联网卡是否存在
         */
        CarrierInfo carrierInfo = apiRepository.isExistAccountSim(simBaseRequest.getAccount(), simBaseRequest.getIccid());
        if (null == carrierInfo) {
            return ResponseDataUtil.buildError(ResponseCode.SIM_NOT_EXIST_ERROR);
        }

        /**
         * 查询，暂时只支持移动API
         */
        ResponseData<SimStatusResponse> responseData = cmccIotSimInfoService.querySimStatus("", simBaseRequest.getIccid(), "", carrierInfo);
        log.info("[responseData]:{}", new Gson().toJson(responseData));
        return responseData;
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
     */
    public ResponseData<SimLocationsResponse> querySimLocations(SimBaseRequest simBaseRequest) {

/**
 * 验证物联网卡是否存在
 */
        CarrierInfo carrierInfo = apiRepository.isExistAccountSim(simBaseRequest.getAccount(), simBaseRequest.getIccid());
        if (null == carrierInfo) {
            return ResponseDataUtil.buildError(ResponseCode.SIM_NOT_EXIST_ERROR);
        }

        /**
         * 查询，暂时只支持移动API
         */
        ResponseData<SimLocationsResponse> responseData = cmccIotSimInfoService.querySimLocations("", simBaseRequest.getIccid(), "",carrierInfo);
        return responseData;
    }


}
