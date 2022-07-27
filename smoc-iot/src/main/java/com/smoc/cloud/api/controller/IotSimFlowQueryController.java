package com.smoc.cloud.api.controller;


import com.smoc.cloud.api.request.SimBaseRequest;
import com.smoc.cloud.api.request.SimsGprsFlowMonthlyRequest;
import com.smoc.cloud.api.response.flow.SimFlowUsedPoolResponse;
import com.smoc.cloud.api.response.flow.SimFlowUsedThisMonthResponse;
import com.smoc.cloud.api.response.flow.SimFlowUsedThisMonthTotalResponse;
import com.smoc.cloud.api.response.flow.SimGprsFlowUsedMonthlyBatch;
import com.smoc.cloud.api.service.IotSimFlowQueryService;
import com.smoc.cloud.common.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 物联网卡流量查询api
 */
@Slf4j
@RestController
@RequestMapping("sim/flow")
public class IotSimFlowQueryController {

    @Autowired
    private IotSimFlowQueryService iotSimFlowQueryService;

    /**
     * 单卡本月套餐内流量使用量实时查询(加入流量池或流量共享的卡无法查询)
     *
     * @param simBaseRequest
     * @return
     */
    @RequestMapping(value = "/querySimFlowUsedThisMonth", method = RequestMethod.POST)
    public ResponseData<SimFlowUsedThisMonthResponse> querySimFlowUsedThisMonth(@RequestBody SimBaseRequest simBaseRequest) {
        return iotSimFlowQueryService.querySimFlowUsedThisMonth(simBaseRequest);
    }

    /**
     * 单卡本月流量累计使用量查询
     *
     * @param simBaseRequest
     * @return
     */
    @RequestMapping(value = "/querySimFlowUsedThisMonthTotal", method = RequestMethod.POST)
    public ResponseData<SimFlowUsedThisMonthTotalResponse> querySimFlowUsedThisMonthTotal(@RequestBody SimBaseRequest simBaseRequest) {
        return iotSimFlowQueryService.querySimFlowUsedThisMonthTotal(simBaseRequest);
    }

    /**
     *  单卡流量池内使用量实时查询
     *
     * @param simBaseRequest
     * @return
     */
    @RequestMapping(value = "/querySimFlowUsedPool", method = RequestMethod.POST)
    public ResponseData<List<SimFlowUsedPoolResponse>> querySimFlowUsedPool(@RequestBody SimBaseRequest simBaseRequest) {
        return iotSimFlowQueryService.querySimFlowUsedPool(simBaseRequest);
    }

    /**
     *  物联卡单月 GPRS 流量使用量批量查询
     *
     * @param simsGprsFlowMonthlyRequest
     * @return
     */
    @RequestMapping(value = "/querySimGprsFlowUsedMonthlyBatch", method = RequestMethod.POST)
    public  ResponseData<List<SimGprsFlowUsedMonthlyBatch>> querySimGprsFlowUsedMonthlyBatch(@RequestBody SimsGprsFlowMonthlyRequest simsGprsFlowMonthlyRequest) {
        return iotSimFlowQueryService.querySimGprsFlowUsedMonthlyBatch(simsGprsFlowMonthlyRequest);
    }

}
