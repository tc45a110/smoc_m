package com.smoc.cloud.finance.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountShareDetailValidator;
import com.smoc.cloud.finance.service.FinanceAccountShareDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 共享财务账户明细记录
 */
@Slf4j
@Controller
@RequestMapping("/finance/account")
public class FinanceAccountShareDetailController {

    @Autowired
    private FinanceAccountShareDetailService financeAccountShareDetailService;

    /**
     * 共享财务账户明细记录列表
     *
     * @return
     */
    @RequestMapping(value = "/share/record/list/{accountId}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String accountId) {
        ModelAndView view = new ModelAndView("finance/finance_account_share_record_list");

        //初始化数据
        PageParams<FinanceAccountShareDetailValidator> params = new PageParams<FinanceAccountShareDetailValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        FinanceAccountShareDetailValidator financeAccountShareDetailValidator = new FinanceAccountShareDetailValidator();
        financeAccountShareDetailValidator.setShareAccountId(accountId);
        params.setParams(financeAccountShareDetailValidator);

        //查询
        ResponseData<PageList<FinanceAccountShareDetailValidator>> data = financeAccountShareDetailService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("financeAccountShareDetailValidator", financeAccountShareDetailValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }

    /**
     * 共享财务账户明细记录分页
     *
     * @return
     */
    @RequestMapping(value = "/share/record/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute FinanceAccountShareDetailValidator financeAccountShareDetailValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("finance/finance_account_share_record_list");
        //分页查询
        pageParams.setParams(financeAccountShareDetailValidator);

        //查询
        ResponseData<PageList<FinanceAccountShareDetailValidator>> data = financeAccountShareDetailService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("financeAccountShareDetailValidator", financeAccountShareDetailValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }
}
