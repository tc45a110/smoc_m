package com.smoc.cloud.iot.carrier.remote;

import com.smoc.cloud.common.iot.reponse.SimFlowUsedMonthlyResponse;
import com.smoc.cloud.common.iot.request.SimsFlowMonthlyRequest;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "iot", path = "/iot")
public interface SimFlowFeignClient {

    /**
     * 物联卡单月流量使用量批量查询(历史)
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/sim/flow/monthly/page", method = RequestMethod.POST)
    ResponseData<PageList<SimFlowUsedMonthlyResponse>> page(@RequestBody PageParams<SimsFlowMonthlyRequest> pageParams) throws Exception;
}
