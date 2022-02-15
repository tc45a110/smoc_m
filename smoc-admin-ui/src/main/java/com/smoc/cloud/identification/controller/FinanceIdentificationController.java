package com.smoc.cloud.identification.controller;


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
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.StringRandom;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.customer.service.EnterpriseService;
import com.smoc.cloud.finance.service.FinanceAccountService;
import com.smoc.cloud.identification.service.IdentificationAccountInfoService;
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
 * 认账账号财务账号
 */
@Slf4j
@Controller
@RequestMapping("/identification/finance")
public class FinanceIdentificationController {

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private FinanceAccountService financeAccountService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    @Autowired
    private IdentificationAccountInfoService identificationAccountInfoService;

    /**
     * 认账账号财务账号列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("identification/finance/identification_finance_list");

        //初始化数据
        PageParams<FinanceAccountValidator> params = new PageParams<FinanceAccountValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        FinanceAccountValidator financeAccountValidator = new FinanceAccountValidator();
        params.setParams(financeAccountValidator);

        //查询
        ResponseData<PageList<FinanceAccountValidator>> data = financeAccountService.page(params,"2");
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        ResponseData<Map<String,Object>> count = financeAccountService.count(financeAccountValidator,"2");

        view.addObject("financeAccountValidator", financeAccountValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("counter", count.getData());
        return view;

    }

    /**
     * 认账账号财务账号分页
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute FinanceAccountValidator financeAccountValidator, PageParams pageParams) {

        ModelAndView view = new ModelAndView("identification/finance/identification_finance_list");
        //分页查询
        pageParams.setParams(financeAccountValidator);

        ResponseData<PageList<FinanceAccountValidator>> data = financeAccountService.page(pageParams,"2");
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        ResponseData<Map<String,Object>> count = financeAccountService.count(financeAccountValidator,"2");

        view.addObject("financeAccountValidator", financeAccountValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("counter", count.getData());

        return view;

    }

    /**
     * 认账账号财务账号分页
     *
     * @return
     */
    @RequestMapping(value = "/recharge/{accountId}", method = RequestMethod.GET)
    public ModelAndView recharge(@PathVariable String accountId, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("identification/finance/identification_finance_recharge");
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //查询认证账户数据
        ResponseData<IdentificationAccountInfoValidator> data = identificationAccountInfoService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }

        //查询认证财务账户数据
        ResponseData<FinanceAccountValidator> finance = financeAccountService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(finance.getCode())) {
            view.addObject("error", finance.getCode() + ":" + finance.getMessage());
        }

        ResponseData<EnterpriseBasicInfoValidator> enterprise = enterpriseService.findById(data.getData().getEnterpriseId());

        FinanceAccountRechargeValidator financeAccountRechargeValidator = new FinanceAccountRechargeValidator();
        financeAccountRechargeValidator.setId(UUID.uuid32());
        financeAccountRechargeValidator.setAccountId(accountId);
        financeAccountRechargeValidator.setRechargeFlowNo(StringRandom.getStringRandom(24).toUpperCase());
        financeAccountRechargeValidator.setRechargeSource("IDENTIFICATION_ACCOUNT");
        financeAccountRechargeValidator.setCreatedBy(user.getUserName());
        financeAccountRechargeValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));

        view.addObject("enterprise", enterprise.getData());
        view.addObject("financeAccountValidator", finance.getData());
        view.addObject("identificationAccountInfoValidator", data.getData());
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

        ModelAndView view = new ModelAndView("identification/finance/identification_finance_recharge");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        if (result.hasErrors()) {
            //查询认证账户数据
            ResponseData<IdentificationAccountInfoValidator> data = identificationAccountInfoService.findById(financeAccountRechargeValidator.getAccountId());
            if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
                view.addObject("error", data.getCode() + ":" + data.getMessage());
                return view;
            }
            //查询认证财务账户数据
            ResponseData<FinanceAccountValidator> finance = financeAccountService.findById(financeAccountRechargeValidator.getAccountId());
            if (!ResponseCode.SUCCESS.getCode().equals(finance.getCode())) {
                view.addObject("error", finance.getCode() + ":" + finance.getMessage());
            }
            ResponseData<EnterpriseBasicInfoValidator> enterprise = enterpriseService.findById(data.getData().getEnterpriseId());
            view.addObject("enterprise", enterprise.getData());
            view.addObject("financeAccountValidator", finance.getData());
            view.addObject("identificationAccountInfoValidator", data.getData());
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
            systemUserLogService.logsAsync("IDENTIFICATION_ACCOUNT", financeAccountRechargeValidator.getAccountId(), user.getRealName(), "recharge",  "认证账户充值", JSON.toJSONString(financeAccountRechargeValidator));
        }

        //记录日志
        log.info("[认证账户][认证账户充值][充值][{}]数据:{}", user.getUserName(), JSON.toJSONString(financeAccountRechargeValidator));


        view.setView(new RedirectView("/identification/finance/list", true, false));

        return view;

    }
}
