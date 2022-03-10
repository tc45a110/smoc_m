package com.smoc.cloud.customer.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.AccountPriceHistoryValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.customer.service.AccountPriceHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

/**
 * 业务账号价格历史
 */
@Slf4j
@Controller
@RequestMapping("/account/price/history")
public class AccountPriceHistoryController {

    @Autowired
    private AccountPriceHistoryService accountPriceHistoryService;


    /**
     * 列表
     *
     * @return
     */
    @RequestMapping(value = "/list/{accountId}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String accountId) {
        ModelAndView view = new ModelAndView("customer/account/account_price_history_list");

        //初始化数据
        PageParams<AccountPriceHistoryValidator> params = new PageParams<AccountPriceHistoryValidator>();
        params.setPageSize(31);
        params.setCurrentPage(1);
        AccountPriceHistoryValidator accountPriceHistoryValidator = new AccountPriceHistoryValidator();
        accountPriceHistoryValidator.setAccountId(accountId);
        //获取当前日期
        String today = DateTimeUtils.getDateFormat(new Date());
        String endDate = DateTimeUtils.checkOption(today, -31);
        accountPriceHistoryValidator.setStartDate(today);
        accountPriceHistoryValidator.setEndDate(endDate);
        params.setParams(accountPriceHistoryValidator);

        //查询
        ResponseData<PageList<AccountPriceHistoryValidator>> data = accountPriceHistoryService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("accountPriceHistoryValidator", accountPriceHistoryValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;
    }

    /**
     * 分页查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute AccountPriceHistoryValidator accountPriceHistoryValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("customer/account/account_price_history_list");
        //分页查询
        pageParams.setParams(accountPriceHistoryValidator);

        ResponseData<PageList<AccountPriceHistoryValidator>> data = accountPriceHistoryService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("accountPriceHistoryValidator", accountPriceHistoryValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }
}
