package com.smoc.cloud.configure.advance.controller;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.validator.SystemHistoryPriceChangeRecordValidator;
import com.smoc.cloud.common.smoc.customer.validator.AccountChannelInfoValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.configure.advance.service.SystemHistoryPriceChangeRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("configure/price/history")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class SystemHistoryPriceChangeRecordController {

    @Autowired
    private SystemHistoryPriceChangeRecordService systemHistoryPriceChangeRecordService;

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseData<PageList<SystemHistoryPriceChangeRecordValidator>> page(@RequestBody PageParams<SystemHistoryPriceChangeRecordValidator> pageParams) {

        return systemHistoryPriceChangeRecordService.page(pageParams);
    }

    /**
     * 历史价格调整
     * @param validators
     * @param changeType
     * @return
     */
    @RequestMapping(value = "/save/{changeType}/{taskType}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody List<SystemHistoryPriceChangeRecordValidator> validators,@PathVariable String changeType,@PathVariable String taskType) {

        //保存操作
        ResponseData data = systemHistoryPriceChangeRecordService.save(validators, changeType,taskType);

        return data;
    }
}
