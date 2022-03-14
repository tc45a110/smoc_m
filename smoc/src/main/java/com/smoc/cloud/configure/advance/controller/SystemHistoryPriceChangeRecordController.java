package com.smoc.cloud.configure.advance.controller;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.SystemHistoryPriceChangeRecordValidator;
import com.smoc.cloud.configure.advance.service.SystemHistoryPriceChangeRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

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
}
