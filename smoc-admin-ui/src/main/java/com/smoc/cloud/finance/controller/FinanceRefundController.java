package com.smoc.cloud.finance.controller;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountRechargeValidator;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountRefundValidator;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.StringRandom;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.customer.service.BusinessAccountService;
import com.smoc.cloud.customer.service.EnterpriseService;
import com.smoc.cloud.finance.service.FinanceAccountRechargeService;
import com.smoc.cloud.finance.service.FinanceAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
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
import java.util.HashMap;
import java.util.Map;

/**
 * 扣款流水
 */
@Slf4j
@Controller
@RequestMapping("/finance/account")
public class FinanceRefundController {

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private FinanceAccountService financeAccountService;

    @Autowired
    private BusinessAccountService businessAccountService;

    @Autowired
    private SystemUserLogService systemUserLogService;


    /**
     * 财务账户充值
     *
     * @return
     */
    @RequestMapping(value = "/refund/{accountId}/{enterpriseId}", method = RequestMethod.GET)
    public ModelAndView recharge(@PathVariable String accountId,@PathVariable String enterpriseId, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("finance/finance_refund_edit");
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //查询财务账户数据
        ResponseData<FinanceAccountValidator> finance = financeAccountService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(finance.getCode())) {
            view.addObject("error", finance.getCode() + ":" + finance.getMessage());
        }

        //查询企业信息
        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(enterpriseId);
        if (!ResponseCode.SUCCESS.getCode().equals(enterpriseData.getCode())) {
            view.addObject("error", enterpriseData.getCode() + ":" + enterpriseData.getMessage());
            return view;
        }

        //查询业务账号
        ResponseData<AccountBasicInfoValidator> businessAccountData = businessAccountService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(businessAccountData.getCode())) {
            view.addObject("error", businessAccountData.getCode() + ":" + businessAccountData.getMessage());
            return view;
        }

        FinanceAccountRefundValidator financeAccountRefundValidator = new FinanceAccountRefundValidator();
        financeAccountRefundValidator.setId(UUID.uuid32());
        financeAccountRefundValidator.setAccountId(accountId);
        financeAccountRefundValidator.setRefundFlowNo(StringRandom.getStringRandom(24).toUpperCase());
        financeAccountRefundValidator.setRefundSource("BUSINESS_ACCOUNT");
        financeAccountRefundValidator.setCreatedBy(user.getRealName());
        financeAccountRefundValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
        financeAccountRefundValidator.setEnterpriseId(enterpriseId);

        view.addObject("enterprise", enterpriseData.getData());
        view.addObject("financeAccountValidator", finance.getData());
        view.addObject("accountBasicInfoValidator", businessAccountData.getData());
        view.addObject("financeAccountRefundValidator", financeAccountRefundValidator);

        return view;

    }

    /**
     * 充值操作
     *
     * @return
     */
    @RequestMapping(value = "/refund/save", method = RequestMethod.POST)
    public ModelAndView rechargeSave(@ModelAttribute @Validated FinanceAccountRechargeValidator financeAccountRechargeValidator, BindingResult result, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("finance/finance_refund_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        if (result.hasErrors()) {

            //查询财务账户数据
            ResponseData<FinanceAccountValidator> finance = financeAccountService.findById(financeAccountRechargeValidator.getAccountId());
            if (!ResponseCode.SUCCESS.getCode().equals(finance.getCode())) {
                view.addObject("error", finance.getCode() + ":" + finance.getMessage());
            }

            //查询企业信息
            ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(financeAccountRechargeValidator.getEnterpriseId());
            if (!ResponseCode.SUCCESS.getCode().equals(enterpriseData.getCode())) {
                view.addObject("error", enterpriseData.getCode() + ":" + enterpriseData.getMessage());
                return view;
            }

            //查询业务账号
            ResponseData<AccountBasicInfoValidator> businessAccountData = businessAccountService.findById(financeAccountRechargeValidator.getAccountId());
            if (!ResponseCode.SUCCESS.getCode().equals(businessAccountData.getCode())) {
                view.addObject("error", businessAccountData.getCode() + ":" + businessAccountData.getMessage());
                return view;
            }
            view.addObject("enterprise", enterpriseData.getData());
            view.addObject("financeAccountValidator", finance.getData());
            view.addObject("accountBasicInfoValidator", businessAccountData.getData());
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
            systemUserLogService.logsAsync("ACCOUNT_FINANCE", financeAccountRechargeValidator.getAccountId(), user.getRealName(), "recharge",  "账户充值", JSON.toJSONString(financeAccountRechargeValidator));
        }

        //记录日志
        log.info("[财务账户][账户充值][充值][{}]数据:{}", user.getUserName(), JSON.toJSONString(financeAccountRechargeValidator));


        view.setView(new RedirectView("/finance/account/list", true, false));

        return view;

    }

}
