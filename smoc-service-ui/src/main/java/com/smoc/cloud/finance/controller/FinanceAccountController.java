package com.smoc.cloud.finance.controller;

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
import java.util.List;

/**
 * 财务账户
 */
@Slf4j
@Controller
@RequestMapping("/web/finance")
public class FinanceAccountController {

    @Autowired
    private FinanceAccountService financeAccountService;

    /**
     * 财务账户列表
     *
     * @return
     */
    @RequestMapping(value = "/account/list/{businessType}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String businessType, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("finance/finance_account_list");

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
        ResponseData<PageList<FinanceAccountValidator>> data = financeAccountService.page(params, "1");
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
     * 财务账户分页
     *
     * @return
     */
    @RequestMapping(value = "/account/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute FinanceAccountValidator financeAccountValidator, PageParams pageParams,HttpServletRequest request) {
        ModelAndView view = new ModelAndView("finance/finance_account_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //分页查询
        financeAccountValidator.setEnterpriseId(user.getOrganization());
        pageParams.setParams(financeAccountValidator);

        ResponseData<PageList<FinanceAccountValidator>> data = financeAccountService.page(pageParams, "1");
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
     * @return
     */
    @RequestMapping(value = "/recharge/list/{businessType}", method = RequestMethod.GET)
    public ModelAndView rechargeList(@PathVariable String businessType, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("finance/finance_recharge_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //初始化数据
        PageParams<FinanceAccountRechargeValidator> params = new PageParams<FinanceAccountRechargeValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        FinanceAccountRechargeValidator financeAccountRechargeValidator = new FinanceAccountRechargeValidator();

        Date startDate = DateTimeUtils.getFirstMonth(12);
        financeAccountRechargeValidator.setStartDate(DateTimeUtils.getDateFormat(startDate));
        financeAccountRechargeValidator.setEndDate(DateTimeUtils.getDateFormat(new Date()));
        financeAccountRechargeValidator.setBusinessType(businessType);
        financeAccountRechargeValidator.setEnterpriseId(user.getOrganization());
        params.setParams(financeAccountRechargeValidator);

        //查询
        ResponseData<PageList<FinanceAccountRechargeValidator>> data = financeAccountService.rechargePage(params,"1");
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
        ModelAndView view = new ModelAndView("finance/finance_recharge_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //分页查询
        financeAccountRechargeValidator.setEnterpriseId(user.getOrganization());
        pageParams.setParams(financeAccountRechargeValidator);

        //日期格式
        if (!StringUtils.isEmpty(financeAccountRechargeValidator.getStartDate())) {
            String[] date = financeAccountRechargeValidator.getStartDate().split(" - ");
            financeAccountRechargeValidator.setStartDate(StringUtils.trimWhitespace(date[0]));
            financeAccountRechargeValidator.setEndDate(StringUtils.trimWhitespace(date[1]));
        }

        ResponseData<PageList<FinanceAccountRechargeValidator>> data = financeAccountService.rechargePage(pageParams,"1");
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }


        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("financeAccountRechargeValidator", financeAccountRechargeValidator);
        return view;
    }

    /**
     * 财务共享账户查看中心
     *
     * @return
     */
    @RequestMapping(value = "/account/share/view/{accountId}/{businessType}", method = RequestMethod.GET)
    public ModelAndView shareView(@PathVariable String accountId,@PathVariable String businessType,HttpServletRequest request) {
        ModelAndView view = new ModelAndView("finance/finance_account_share_view_center");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //账户信息
        ResponseData<FinanceAccountValidator> shareFinanceAccount = financeAccountService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(shareFinanceAccount.getCode())) {
            view.addObject("error", shareFinanceAccount.getCode() + ":" + shareFinanceAccount.getMessage());
            return view;
        }

        //是否属于自己企业
        if(!user.getOrganization().equals(shareFinanceAccount.getData().getEnterpriseId())){
            view.addObject("error", ResponseCode.OAUTH2_EXCEPTION_UNAUTH.getMessage());
            return view;
        }

        view.addObject("financeAccountValidator", shareFinanceAccount.getData());
        view.addObject("businessType", businessType);

        return view;
    }

    /**
     * 财务共享账户明细
     *
     * @return
     */
    @RequestMapping(value = "/account/shareDetail/{accountId}", method = RequestMethod.GET)
    public ModelAndView center(@PathVariable String accountId,HttpServletRequest request) {
        ModelAndView view = new ModelAndView("finance/finance_account_share_view_detail");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //账户信息
        ResponseData<FinanceAccountValidator> shareFinanceAccount = financeAccountService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(shareFinanceAccount.getCode())) {
            view.addObject("error", shareFinanceAccount.getCode() + ":" + shareFinanceAccount.getMessage());
            return view;
        }

        //是否属于自己企业
        if(!user.getOrganization().equals(shareFinanceAccount.getData().getEnterpriseId())){
            view.addObject("error", ResponseCode.OAUTH2_EXCEPTION_UNAUTH.getMessage());
            return view;
        }

        //查询共享账号的子账号信息
        ResponseData<List<FinanceAccountValidator>> data = financeAccountService.findSubsidiaryFinanceAccountByAccountId(shareFinanceAccount.getData().getAccountId());
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("financeAccountValidator", shareFinanceAccount.getData());
        view.addObject("financeAccounts", data.getData());

        return view;
    }

    /**
     * 财务充值列表
     * @return
     */
    @RequestMapping(value = "/account/shareRecharge/{accountId}", method = RequestMethod.GET)
    public ModelAndView shareRecharge(@PathVariable String accountId,HttpServletRequest request) {
        ModelAndView view = new ModelAndView("finance/finance_account_share_view_recharge");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //账户信息
        ResponseData<FinanceAccountValidator> shareFinanceAccount = financeAccountService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(shareFinanceAccount.getCode())) {
            view.addObject("error", shareFinanceAccount.getCode() + ":" + shareFinanceAccount.getMessage());
            return view;
        }

        //是否属于自己企业
        if(!user.getOrganization().equals(shareFinanceAccount.getData().getEnterpriseId())){
            view.addObject("error", ResponseCode.OAUTH2_EXCEPTION_UNAUTH.getMessage());
            return view;
        }

        //初始化数据
        PageParams<FinanceAccountRechargeValidator> params = new PageParams<FinanceAccountRechargeValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        FinanceAccountRechargeValidator financeAccountRechargeValidator = new FinanceAccountRechargeValidator();

        financeAccountRechargeValidator.setAccountId(accountId);
        params.setParams(financeAccountRechargeValidator);

        //查询
        ResponseData<PageList<FinanceAccountRechargeValidator>> data = financeAccountService.rechargePage(params,"1");
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
    @RequestMapping(value = "/account/shareRecharge/page", method = RequestMethod.POST)
    public ModelAndView accountPage(@ModelAttribute FinanceAccountRechargeValidator financeAccountRechargeValidator, PageParams pageParams,HttpServletRequest request) {
        ModelAndView view = new ModelAndView("finance/finance_account_share_view_recharge");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //账户信息
        ResponseData<FinanceAccountValidator> shareFinanceAccount = financeAccountService.findById(financeAccountRechargeValidator.getAccountId());
        if (!ResponseCode.SUCCESS.getCode().equals(shareFinanceAccount.getCode())) {
            view.addObject("error", shareFinanceAccount.getCode() + ":" + shareFinanceAccount.getMessage());
            return view;
        }

        //是否属于自己企业
        if(!user.getOrganization().equals(shareFinanceAccount.getData().getEnterpriseId())){
            view.addObject("error", ResponseCode.OAUTH2_EXCEPTION_UNAUTH.getMessage());
            return view;
        }

        //分页查询
        pageParams.setParams(financeAccountRechargeValidator);

        ResponseData<PageList<FinanceAccountRechargeValidator>> data = financeAccountService.rechargePage(pageParams,"1");
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
