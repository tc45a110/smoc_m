package com.smoc.cloud.system.controller;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountRechargeValidator;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountValidator;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationAccountInfoValidator;
import com.smoc.cloud.common.smoc.system.SystemAccountInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.StringRandom;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.customer.service.EnterpriseService;
import com.smoc.cloud.finance.service.FinanceAccountService;
import com.smoc.cloud.identification.service.IdentificationAccountInfoService;
import com.smoc.cloud.system.service.SystemAccountInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * 公共账号财务账号
 */
@Slf4j
//@Controller
//@RequestMapping("/system/finance")
public class SystemFinanceController {

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private FinanceAccountService financeAccountService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    @Autowired
    private SystemAccountInfoService systemAccountInfoService;

    //通过这个字段，区分 变更账号类型;此处为智能短信账号
    private String businessType ="INTELLECT_ACCOUNT";

    /**
     * 账号财务账号列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("system/finance/finance_list");

        //初始化数据
        PageParams<FinanceAccountValidator> params = new PageParams<FinanceAccountValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        FinanceAccountValidator financeAccountValidator = new FinanceAccountValidator();
        financeAccountValidator.setAccountType(businessType);
        params.setParams(financeAccountValidator);

        //查询
        ResponseData<PageList<FinanceAccountValidator>> data = financeAccountService.page(params,"4");
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        ResponseData<Map<String,Object>> count = financeAccountService.count(financeAccountValidator,"4");

        view.addObject("financeAccountValidator", financeAccountValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("counter", count.getData());
        return view;

    }

    /**
     * 账号财务账号分页
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute FinanceAccountValidator financeAccountValidator, PageParams pageParams) {

        ModelAndView view = new ModelAndView("system/finance/finance_list");
        //分页查询
        financeAccountValidator.setAccountType(businessType);
        pageParams.setParams(financeAccountValidator);

        ResponseData<PageList<FinanceAccountValidator>> data = financeAccountService.page(pageParams,"4");
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        ResponseData<Map<String,Object>> count = financeAccountService.count(financeAccountValidator,"4");

        view.addObject("financeAccountValidator", financeAccountValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("counter", count.getData());

        return view;

    }

    /**
     * 账号财务充值
     *
     * @return
     */
    @RequestMapping(value = "/recharge/{accountId}", method = RequestMethod.GET)
    public ModelAndView recharge(@PathVariable String accountId, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("system/finance/finance_recharge");
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //查询账户数据
        ResponseData<SystemAccountInfoValidator> data = systemAccountInfoService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }

        //查询财务账户数据
        ResponseData<FinanceAccountValidator> finance = financeAccountService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(finance.getCode())) {
            view.addObject("error", finance.getCode() + ":" + finance.getMessage());
        }

        ResponseData<EnterpriseBasicInfoValidator> enterprise = enterpriseService.findById(data.getData().getEnterpriseId());

        FinanceAccountRechargeValidator financeAccountRechargeValidator = new FinanceAccountRechargeValidator();
        financeAccountRechargeValidator.setId(UUID.uuid32());
        financeAccountRechargeValidator.setAccountId(accountId);
        financeAccountRechargeValidator.setRechargeFlowNo(StringRandom.getStringRandom(24).toUpperCase());
        financeAccountRechargeValidator.setRechargeSource(businessType);
        financeAccountRechargeValidator.setCreatedBy(user.getRealName());
        financeAccountRechargeValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));

        view.addObject("enterprise", enterprise.getData());
        view.addObject("financeAccountValidator", finance.getData());
        view.addObject("systemAccountInfoValidator", data.getData());
        view.addObject("financeAccountRechargeValidator", financeAccountRechargeValidator);

        return view;

    }

    /**
     * 充值操作
     *
     * @return
     */
    @RequestMapping(value = "/recharge/save", method = RequestMethod.POST)
    public ModelAndView parechargeSave(@ModelAttribute @Validated FinanceAccountRechargeValidator financeAccountRechargeValidator, BindingResult result, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("system/finance/finance_recharge");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        if (result.hasErrors()) {
            //查询账户数据
            ResponseData<SystemAccountInfoValidator> data = systemAccountInfoService.findById(financeAccountRechargeValidator.getAccountId());
            if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
                view.addObject("error", data.getCode() + ":" + data.getMessage());
                return view;
            }
            //查询财务账户数据
            ResponseData<FinanceAccountValidator> finance = financeAccountService.findById(financeAccountRechargeValidator.getAccountId());
            if (!ResponseCode.SUCCESS.getCode().equals(finance.getCode())) {
                view.addObject("error", finance.getCode() + ":" + finance.getMessage());
            }
            ResponseData<EnterpriseBasicInfoValidator> enterprise = enterpriseService.findById(data.getData().getEnterpriseId());
            view.addObject("enterprise", enterprise.getData());
            view.addObject("financeAccountValidator", finance.getData());
            view.addObject("systemAccountInfoValidator", data.getData());
            view.addObject("financeAccountRechargeValidator", financeAccountRechargeValidator);
            return view;
        }

        ResponseData data  = financeAccountService.recharge(financeAccountRechargeValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync(businessType, financeAccountRechargeValidator.getAccountId(), user.getRealName(), "recharge",  "账户充值", JSON.toJSONString(financeAccountRechargeValidator));
        }

        //记录日志
        log.info("[公共账户][账户充值][充值][{}]数据:{}", user.getUserName(), JSON.toJSONString(financeAccountRechargeValidator));


        view.setView(new RedirectView("/system/finance/list", true, false));

        return view;

    }
}
