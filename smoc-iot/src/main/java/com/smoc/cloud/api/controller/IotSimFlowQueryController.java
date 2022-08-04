package com.smoc.cloud.api.controller;


import com.smoc.cloud.api.request.SimBaseRequest;
import com.smoc.cloud.api.request.SimsFlowMonthlyRequest;
import com.smoc.cloud.api.response.flow.SimFlowUsedMonthlyResponse;
import com.smoc.cloud.api.response.flow.SimFlowUsedThisMonthResponse;
import com.smoc.cloud.api.service.IotSimFlowQueryService;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
     * 单卡本月套餐内流量使用量实时查询
     *
     * @param simBaseRequest
     * @return
     */
    @RequestMapping(value = "/querySimFlowUsedThisMonth", method = RequestMethod.POST)
    public ResponseData<SimFlowUsedThisMonthResponse> querySimFlowUsedThisMonth(@RequestBody SimBaseRequest simBaseRequest) {
        //完成参数规则验证
        if (!MpmValidatorUtil.validate(simBaseRequest)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(simBaseRequest));
        }
        return iotSimFlowQueryService.querySimFlowUsedThisMonth(simBaseRequest);
    }

    /**
     * 物联卡单月流量使用量批量查询(历史)
     *
     * @param simsFlowMonthlyRequest
     * @return
     */
    @RequestMapping(value = "/querySimFlowUsedMonthly", method = RequestMethod.POST)
    public ResponseData<PageList<SimFlowUsedMonthlyResponse>> querySimFlowUsedMonthly(@RequestBody SimsFlowMonthlyRequest simsFlowMonthlyRequest) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(simsFlowMonthlyRequest)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(simsFlowMonthlyRequest));
        }

        //初始化数据
        PageParams pageParams = new PageParams<>();
        pageParams.setPageSize(100);
        if (null == simsFlowMonthlyRequest || null == simsFlowMonthlyRequest.getCurrentPage() || "0".equals(simsFlowMonthlyRequest.getCurrentPage())) {
            pageParams.setCurrentPage(1);
        } else {
            pageParams.setCurrentPage(new Integer(simsFlowMonthlyRequest.getCurrentPage()));
        }

        return iotSimFlowQueryService.querySimFlowUsedMonthly(simsFlowMonthlyRequest, pageParams);
    }

}
