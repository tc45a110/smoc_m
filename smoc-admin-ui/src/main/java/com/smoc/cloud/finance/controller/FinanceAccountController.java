package com.smoc.cloud.finance.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountRechargeValidator;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountValidator;
import com.smoc.cloud.customer.service.EnterpriseService;
import com.smoc.cloud.finance.service.FinanceAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * 财务账户
 */
@Slf4j
@Controller
@RequestMapping("/finance/account")
public class FinanceAccountController {

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private FinanceAccountService financeAccountService;

    /**
     * 财务账户列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("finance/finance_account_list");

        //初始化数据
        PageParams<FinanceAccountValidator> params = new PageParams<FinanceAccountValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        FinanceAccountValidator financeAccountValidator = new FinanceAccountValidator();
        params.setParams(financeAccountValidator);

        //查询
        ResponseData<PageList<FinanceAccountValidator>> data = financeAccountService.page(params, "1");
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        ResponseData<Map<String, Object>> count = financeAccountService.count("1");

        view.addObject("financeAccountValidator", financeAccountValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("counter", count.getData());
        return view;
    }

    /**
     * 财务账户分页
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute FinanceAccountValidator financeAccountValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("finance/finance_account_list");
        //分页查询
        pageParams.setParams(financeAccountValidator);

        ResponseData<PageList<FinanceAccountValidator>> data = financeAccountService.page(pageParams, "1");
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        ResponseData<Map<String, Object>> count = financeAccountService.count("1");

        view.addObject("financeAccountValidator", financeAccountValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("counter", count.getData());

        return view;
    }

    /**
     * 企业财务账户管理中心
     *
     * @return
     */
    @RequestMapping(value = "/center/{enterpriseId}", method = RequestMethod.GET)
    public ModelAndView center(@PathVariable String enterpriseId) {
        ModelAndView view = new ModelAndView("finance/finance_account_center");

        //查询企业信息
        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(enterpriseId);
        if (!ResponseCode.SUCCESS.getCode().equals(enterpriseData.getCode())) {
            view.addObject("error", enterpriseData.getCode() + ":" + enterpriseData.getMessage());
            return view;
        }

        //查询企业财务账户信息
        ResponseData<List<FinanceAccountValidator>> data = financeAccountService.findEnterpriseFinanceAccounts(enterpriseId);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //统计企业财务账户信息
        ResponseData<Map<String, Object>> countData = financeAccountService.countEnterpriseSum(enterpriseId);
        if (!ResponseCode.SUCCESS.getCode().equals(countData.getCode())) {
            view.addObject("error", countData.getCode() + ":" + countData.getMessage());
            return view;
        }

        view.addObject("enterprise", enterpriseData.getData());
        view.addObject("financeAccounts", data.getData());
        view.addObject("count", countData.getData());

        return view;
    }

    /**
     * 财务账户管理中心
     *
     * @return
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public ModelAndView view() {
        ModelAndView view = new ModelAndView("finance/finance_account_view");

        return view;
    }


}
