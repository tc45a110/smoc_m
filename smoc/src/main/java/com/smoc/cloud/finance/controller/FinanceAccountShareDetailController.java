package com.smoc.cloud.finance.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountShareDetailValidator;
import com.smoc.cloud.finance.service.FinanceAccountShareDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

/**
 * 共享财务账户明细记录
 */
@Slf4j
@RestController
@RequestMapping("finance/account/share")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class FinanceAccountShareDetailController {

    @Autowired
    private FinanceAccountShareDetailService financeAccountShareDetailService;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/record/page", method = RequestMethod.POST)
    public ResponseData<PageList<FinanceAccountShareDetailValidator>> page(@RequestBody PageParams<FinanceAccountShareDetailValidator> pageParams) {

        return financeAccountShareDetailService.page(pageParams);
    }
}
