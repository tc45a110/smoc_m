package com.smoc.cloud.iot.carrier.service;


import com.smoc.cloud.common.iot.validator.IotFlowCardsChangeHistoryValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.iot.carrier.remote.IotFlowCardsChangeHistoryFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class IotFlowCardsChangeHistoryService {

    @Resource
    private IotFlowCardsChangeHistoryFeignClient iotFlowCardsChangeHistoryFeignClient;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<IotFlowCardsChangeHistoryValidator>> page(PageParams<IotFlowCardsChangeHistoryValidator> pageParams) {
        try {
            ResponseData<PageList<IotFlowCardsChangeHistoryValidator>> data = this.iotFlowCardsChangeHistoryFeignClient.page(pageParams);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 添加、修改
     *
     * @return
     */
    public ResponseData save(List<IotFlowCardsChangeHistoryValidator> histories, String msisdn, String imsi, String iccid) {
        try {
            ResponseData data = this.iotFlowCardsChangeHistoryFeignClient.save(histories, msisdn, imsi, iccid);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}
