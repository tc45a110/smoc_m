package com.smoc.cloud.iot.carrier.controller;


import com.smoc.cloud.api.request.SimsFlowMonthlyRequest;
import com.smoc.cloud.api.response.flow.SimFlowUsedMonthlyResponse;
import com.smoc.cloud.api.service.IotSimFlowQueryService;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 物联网卡流量查询
 */
@Slf4j
@RestController
@RequestMapping("sim/flow")
public class SimFlowController {

    @Autowired
    private IotSimFlowQueryService iotSimFlowQueryService;

    /**
     * 物联卡单月流量使用量批量查询(历史)
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/monthly/page", method = RequestMethod.POST)
    public ResponseData<PageList<SimFlowUsedMonthlyResponse>> page(@RequestBody  PageParams<SimsFlowMonthlyRequest> pageParams) {

        SimsFlowMonthlyRequest simsFlowMonthlyRequest = pageParams.getParams();
        return iotSimFlowQueryService.querySimFlowUsedMonthly(simsFlowMonthlyRequest, pageParams);
    }

}
