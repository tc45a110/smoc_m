package com.smoc.cloud.intellect.controller;


import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountRechargeValidator;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.finance.service.FinanceAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 公共账号财务账号
 */
@Slf4j
@Controller
@RequestMapping("/web/intellect/finance")
public class IntellectFinanceController {

    @Autowired
    private FinanceAccountService financeAccountService;

    //通过这个字段，区分 变更账号类型;此处为智能短信账号
    private String businessType ="INTELLECT_ACCOUNT";

    /**
     * 账号财务账号列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("intellect/finance/finance_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //初始化数据
        PageParams<FinanceAccountValidator> params = new PageParams<FinanceAccountValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        FinanceAccountValidator financeAccountValidator = new FinanceAccountValidator();
        financeAccountValidator.setAccountType(businessType);
        financeAccountValidator.setEnterpriseId(user.getOrganization());
        params.setParams(financeAccountValidator);

        //查询
        ResponseData<PageList<FinanceAccountValidator>> data = financeAccountService.page(params,"4");
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("financeAccountValidator", financeAccountValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * 账号财务账号分页
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute FinanceAccountValidator financeAccountValidator, PageParams pageParams, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("intellect/finance/finance_list");
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //分页查询
        financeAccountValidator.setEnterpriseId(user.getOrganization());
        financeAccountValidator.setAccountType(businessType);
        pageParams.setParams(financeAccountValidator);

        ResponseData<PageList<FinanceAccountValidator>> data = financeAccountService.page(pageParams,"4");
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("financeAccountValidator", financeAccountValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;

    }

    /**
     * 财务充值列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/recharge/list", method = RequestMethod.GET)
    public ModelAndView rechargeList(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("intellect/finance/finance_recharge_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //初始化数据
        PageParams<FinanceAccountRechargeValidator> params = new PageParams<FinanceAccountRechargeValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        FinanceAccountRechargeValidator financeAccountRechargeValidator = new FinanceAccountRechargeValidator();

        Date startDate = DateTimeUtils.getFirstMonth(12);
        financeAccountRechargeValidator.setStartDate(DateTimeUtils.getDateFormat(startDate));
        financeAccountRechargeValidator.setEndDate(DateTimeUtils.getDateFormat(new Date()));
        financeAccountRechargeValidator.setRechargeSource(businessType);
        financeAccountRechargeValidator.setEnterpriseId(user.getOrganization());
        params.setParams(financeAccountRechargeValidator);

        //查询
        ResponseData<PageList<FinanceAccountRechargeValidator>> data = financeAccountService.rechargePage(params,"4");
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
     * 财务充值列表分页
     * @return
     */
    @RequestMapping(value = "/recharge/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute FinanceAccountRechargeValidator financeAccountRechargeValidator, PageParams pageParams, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("intellect/finance/finance_recharge_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //分页查询
        financeAccountRechargeValidator.setEnterpriseId(user.getOrganization());
        financeAccountRechargeValidator.setRechargeSource(businessType);
        pageParams.setParams(financeAccountRechargeValidator);

        //日期格式
        if (!StringUtils.isEmpty(financeAccountRechargeValidator.getStartDate())) {
            String[] date = financeAccountRechargeValidator.getStartDate().split(" - ");
            financeAccountRechargeValidator.setStartDate(StringUtils.trimWhitespace(date[0]));
            financeAccountRechargeValidator.setEndDate(StringUtils.trimWhitespace(date[1]));
        }

        ResponseData<PageList<FinanceAccountRechargeValidator>> data = financeAccountService.rechargePage(pageParams,"4");
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("financeAccountRechargeValidator", financeAccountRechargeValidator);
        return view;
    }
}
