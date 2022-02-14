package com.smoc.cloud.identification.controller;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountRechargeValidator;
import com.smoc.cloud.finance.service.FinanceAccountRechargeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


/**
 * 认账账号充值记录
 */
@Slf4j
@Controller
@RequestMapping("/identification/finance/recharge")
public class FinanceAccountRechargeController {

    @Autowired
    private FinanceAccountRechargeService financeAccountRechargeService;

    /**
     * 认账账号财务账号列表
     *
     * @return
     */
    @RequestMapping(value = "/list/{accountId}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String accountId) {
        ModelAndView view = new ModelAndView("identification/finance/identification_finance_recharge_list");

        //初始化数据
        PageParams<FinanceAccountRechargeValidator> params = new PageParams<FinanceAccountRechargeValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        FinanceAccountRechargeValidator financeAccountRechargeValidator = new FinanceAccountRechargeValidator();
        financeAccountRechargeValidator.setAccountId(accountId);
        params.setParams(financeAccountRechargeValidator);

        //查询
        ResponseData<PageList<FinanceAccountRechargeValidator>> data = financeAccountRechargeService.page(params,"2");
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("financeAccountRechargeValidator", financeAccountRechargeValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * 认账账号财务账号分页
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute FinanceAccountRechargeValidator financeAccountRechargeValidator, PageParams pageParams) {

        ModelAndView view = new ModelAndView("identification/finance/identification_finance_recharge_list");
        //分页查询
        pageParams.setParams(financeAccountRechargeValidator);

        ResponseData<PageList<FinanceAccountRechargeValidator>> data = financeAccountRechargeService.page(pageParams,"2");
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }
        view.addObject("financeAccountRechargeValidator", financeAccountRechargeValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;

    }

}
