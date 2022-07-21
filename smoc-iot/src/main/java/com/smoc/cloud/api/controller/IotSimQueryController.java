package com.smoc.cloud.api.controller;


import com.smoc.cloud.api.request.BatchSimHandleRequest;
import com.smoc.cloud.api.request.OrderHandleRequest;
import com.smoc.cloud.api.request.SimBaseRequest;
import com.smoc.cloud.api.response.*;
import com.smoc.cloud.api.service.IotSimQueryService;
import com.smoc.cloud.common.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 物联网卡查询api
 */
@Slf4j
@RestController
@RequestMapping("iot/sim")
public class IotSimQueryController {

    @Autowired
    private IotSimQueryService iotSimQueryService;

    /**
     * 单卡操作订单处理情况查询
     *
     * @param orderHandleRequest
     * @return
     */
    @RequestMapping(value = "/queryOrderHandle", method = RequestMethod.POST)
    public ResponseData<OrderHandleResponse> orderHandle(@RequestBody OrderHandleRequest orderHandleRequest) {
        return iotSimQueryService.queryOrderHandle(orderHandleRequest);
    }

    /**
     * 物联网卡批量办理结果查询
     *
     * @param batchSimHandleRequest
     * @return
     */
    @RequestMapping(value = "/queryBatchSimHandle", method = RequestMethod.POST)
    public ResponseData<BatchSimHandleResponse> queryBatchSimHandle(@RequestBody BatchSimHandleRequest batchSimHandleRequest) {
        return iotSimQueryService.queryBatchSimHandle(batchSimHandleRequest);
    }

    /**
     * 物联网卡基本信息查询
     *
     * @param simBaseRequest
     * @return
     */
    @RequestMapping(value = "/querySimBaseInfo", method = RequestMethod.POST)
    public ResponseData<SimBaseInfoResponse> querySimBaseInfo(@RequestBody SimBaseRequest simBaseRequest) {
        return iotSimQueryService.querySimBaseInfo(simBaseRequest);
    }

    /**
     * 码号信息批量查询
     *
     * @param simBaseRequest
     * @return
     */
    @RequestMapping(value = "/queryBatchSimBaseInfo", method = RequestMethod.POST)
    public ResponseData<List<BatchSimBaseInfoResponse>> queryBatchSimBaseInfo(@RequestBody SimBaseRequest simBaseRequest) {
        return iotSimQueryService.queryBatchSimBaseInfo(simBaseRequest);
    }

    /**
     * 单卡状态变更历史查询
     *
     * @param simBaseRequest
     * @return
     */
    @RequestMapping(value = "/querySimChangeHistory", method = RequestMethod.POST)
    public ResponseData<List<SimChangeHistoryResponse>> querySimChangeHistory(@RequestBody SimBaseRequest simBaseRequest) {
        return iotSimQueryService.querySimChangeHistory(simBaseRequest);
    }

    /**
     * 物联网卡状态查询（停机、复机、冻结...）
     *
     * @param simBaseRequest
     * @return
     */
    @RequestMapping(value = "/querySimStartStatus", method = RequestMethod.POST)
    public ResponseData<SimStartStatusResponse> querySimStartStatus(@RequestBody SimBaseRequest simBaseRequest) {
        return iotSimQueryService.querySimStartStatus(simBaseRequest);
    }

    /**
     * 物联网卡状态查询（停机、复机、冻结...）
     *
     * @param simBaseRequest
     * @return
     */
    @RequestMapping(value = "/querySimStatus", method = RequestMethod.POST)
    public ResponseData<SimStatusResponse> querySimStatus(@RequestBody SimBaseRequest simBaseRequest) {
        return iotSimQueryService.querySimStatus(simBaseRequest);
    }
}
