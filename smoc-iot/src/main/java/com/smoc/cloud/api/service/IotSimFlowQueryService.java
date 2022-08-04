package com.smoc.cloud.api.service;


import com.smoc.cloud.api.remote.cmcc.service.CmccIotSimFlowService;
import com.smoc.cloud.api.request.SimBaseRequest;
import com.smoc.cloud.api.request.SimsFlowMonthlyRequest;
import com.smoc.cloud.api.response.flow.*;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.iot.carrier.repository.IotFlowCardsFlowMonthlyRepository;
import com.smoc.cloud.iot.repository.ApiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class IotSimFlowQueryService {

    @Autowired
    private ApiRepository apiRepository;

    @Autowired
    private CmccIotSimFlowService cmccIotSimFlowService;

    @Resource
    private IotFlowCardsFlowMonthlyRepository iotFlowCardsFlowMonthlyRepository;

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
        //ResponseData<SimFlowUsedThisMonthResponse> responseData = cmccIotSimFlowService.querySimFlowUsedThisMonth(simBaseRequest.getMsisdn(), "", "");
        SimFlowUsedThisMonthResponse simFlowUsedThisMonthResponse = new SimFlowUsedThisMonthResponse();
        simFlowUsedThisMonthResponse.setIccid("898600D6991330004149");
        simFlowUsedThisMonthResponse.setUseAmount("15186.00");
        simFlowUsedThisMonthResponse.setTotalAmount("102400.00");
        simFlowUsedThisMonthResponse.setRemainAmount("87214.00");
        return ResponseDataUtil.buildSuccess(simFlowUsedThisMonthResponse);
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
        //ResponseData<SimFlowUsedThisMonthTotalResponse> responseData = cmccIotSimFlowService.querySimFlowUsedThisMonthTotal(simBaseRequest.getMsisdn(), "", "");
        SimFlowUsedThisMonthTotalResponse simFlowUsedThisMonthTotalResponse = new SimFlowUsedThisMonthTotalResponse();
        simFlowUsedThisMonthTotalResponse.setDataAmount("1024.00");

        List<SimFlowUsedThisMonthTotalItemResponse> apnUseAmountList = new ArrayList<>();
        SimFlowUsedThisMonthTotalItemResponse simFlowUsedThisMonthTotalItemResponse = new SimFlowUsedThisMonthTotalItemResponse();
        simFlowUsedThisMonthTotalItemResponse.setApnUseAmount("1024");
        simFlowUsedThisMonthTotalItemResponse.setApnName("CMIOT");

        List<SimFlowUsedThisMonthTotalPccCodeResponse> pccCodeUseAmountList = new ArrayList<>();
        SimFlowUsedThisMonthTotalPccCodeResponse simFlowUsedThisMonthTotalPccCodeResponse = new SimFlowUsedThisMonthTotalPccCodeResponse();
        simFlowUsedThisMonthTotalPccCodeResponse.setPccCode("12341234");
        simFlowUsedThisMonthTotalPccCodeResponse.setPccCodeUseAmount("1024");
        pccCodeUseAmountList.add(simFlowUsedThisMonthTotalPccCodeResponse);

        simFlowUsedThisMonthTotalItemResponse.setPccCodeUseAmountList(pccCodeUseAmountList);
        apnUseAmountList.add(simFlowUsedThisMonthTotalItemResponse);

        simFlowUsedThisMonthTotalResponse.setApnUseAmountList(apnUseAmountList);
        return ResponseDataUtil.buildSuccess(simFlowUsedThisMonthTotalResponse);
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
        //ResponseData<List<SimFlowUsedPoolResponse>> responseData = cmccIotSimFlowService.querySimFlowUsedPool(simBaseRequest.getMsisdn(), "", "");
        List<SimFlowUsedPoolResponse> list = new ArrayList<>();
        SimFlowUsedPoolResponse simFlowUsedPoolResponse = new SimFlowUsedPoolResponse();
        simFlowUsedPoolResponse.setApnName("CMIOT");
        simFlowUsedPoolResponse.setApnUseAmount("1024");
        list.add(simFlowUsedPoolResponse);
        SimFlowUsedPoolResponse simFlowUsedPoolResponse1 = new SimFlowUsedPoolResponse();
        simFlowUsedPoolResponse1.setApnName("CMIOT1");
        simFlowUsedPoolResponse1.setApnUseAmount("1024");
        list.add(simFlowUsedPoolResponse1);
        return ResponseDataUtil.buildSuccess(list);
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
    public  ResponseData<PageList<SimFlowUsedMonthlyResponse>> querySimFlowUsedMonthly(SimsFlowMonthlyRequest simsFlowMonthlyRequest, PageParams pageParams) {

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
        //ResponseData<List<SimGprsFlowUsedMonthlyBatch>> responseData = cmccIotSimFlowService.querySimGprsFlowUsedMonthlyBatch(simsGprsFlowMonthlyRequest.getMsisdns(), simsGprsFlowMonthlyRequest.getIccids(), simsGprsFlowMonthlyRequest.getImsis(),simsGprsFlowMonthlyRequest.getQueryDate());
        PageList<SimFlowUsedMonthlyResponse> page = this.iotFlowCardsFlowMonthlyRepository.page(simsFlowMonthlyRequest.getAccount(),simsFlowMonthlyRequest.getIccid(),simsFlowMonthlyRequest.getQueryMonth(),pageParams);
        return ResponseDataUtil.buildSuccess(page);
    }
}
