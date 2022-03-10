package com.smoc.cloud.customer.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.AccountPriceHistoryValidator;
import com.smoc.cloud.customer.service.AccountPriceHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

/**
 * 业务账号价格历史
 **/
@Slf4j
@RestController
@RequestMapping("account/price/history")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class AccountPriceHistoryController {

    @Autowired
    private AccountPriceHistoryService accountPriceHistoryService;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseData<PageList<AccountPriceHistoryValidator>> page(@RequestBody PageParams<AccountPriceHistoryValidator> pageParams) {

        return accountPriceHistoryService.page(pageParams);
    }
}
