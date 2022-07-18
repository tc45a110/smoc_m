package com.smoc.cloud.iot.carrier.controller;


import com.smoc.cloud.common.iot.validator.IotFlowCardsChangeHistoryValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.iot.carrier.service.IotFlowCardsChangeHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("iot/carrier/card/history")
public class IotFlowCardsChangeHistoryController {

    @Autowired
    private IotFlowCardsChangeHistoryService iotFlowCardsChangeHistoryService;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseData<PageList<IotFlowCardsChangeHistoryValidator>> page(@RequestBody PageParams<IotFlowCardsChangeHistoryValidator> pageParams) {
        ResponseData<PageList<IotFlowCardsChangeHistoryValidator>> data = iotFlowCardsChangeHistoryService.page(pageParams);
        return data;
    }

    /**
     * 添加、修改
     *
     * @return
     */
    @RequestMapping(value = "/save/{msisdn}/{imsi}/{iccid}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody List<IotFlowCardsChangeHistoryValidator> histories, @PathVariable String msisdn, @PathVariable String imsi, @PathVariable String iccid) {

        //保存操作
        ResponseData data = iotFlowCardsChangeHistoryService.save(msisdn, imsi, iccid, histories);

        return data;
    }
}
