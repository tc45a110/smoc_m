package com.smoc.cloud.iot.carrier.service;

import com.smoc.cloud.common.iot.reponse.SimFlowUsedMonthlyResponse;
import com.smoc.cloud.common.iot.request.SimsFlowMonthlyRequest;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.iot.carrier.remote.SimFlowFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimFlowService {

    @Autowired
    private SimFlowFeignClient simFlowFeignClient;

    /**
     * 物联卡单月流量使用量批量查询(历史)
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<SimFlowUsedMonthlyResponse>> page(PageParams<SimsFlowMonthlyRequest> pageParams){
        try {
            ResponseData<PageList<SimFlowUsedMonthlyResponse>> data = this.simFlowFeignClient.page(pageParams);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
