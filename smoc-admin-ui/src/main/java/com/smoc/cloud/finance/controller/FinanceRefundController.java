package com.smoc.cloud.finance.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountRefundValidator;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.StringRandom;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.customer.service.BusinessAccountService;
import com.smoc.cloud.customer.service.EnterpriseService;
import com.smoc.cloud.finance.service.FinanceAccountRefundService;
import com.smoc.cloud.finance.service.FinanceAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 退款流水
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
    private FinanceAccountRefundService financeAccountRefundService;

    @Autowired
    private SystemUserLogService systemUserLogService;


    /**
     * 财务账户充值
     *
     * @return
     */
    @RequestMapping(value = "/refund/{accountId}/{enterpriseId}", method = RequestMethod.GET)
    public ModelAndView refund(@PathVariable String accountId,@PathVariable String enterpriseId, HttpServletRequest request) {

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
    public ModelAndView save(@ModelAttribute @Validated FinanceAccountRefundValidator financeAccountRefundValidator, BindingResult result, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("finance/finance_refund_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //查询财务账户数据
        ResponseData<FinanceAccountValidator> finance = financeAccountService.findById(financeAccountRefundValidator.getAccountId());
        if (!ResponseCode.SUCCESS.getCode().equals(finance.getCode())) {
            view.addObject("error", finance.getCode() + ":" + finance.getMessage());
        }

        //判断退款金额不能大于可用余额
        if(financeAccountRefundValidator.getRefundSum().compareTo(new BigDecimal(0)) == 0){
            FieldError err = new FieldError("退款金额", "refundSum", "退款金额不能等于0！");
            result.addError(err);
        }
        BigDecimal refundSum = financeAccountRefundValidator.getRefundSum().subtract(finance.getData().getAccountUsableSum());
        if( refundSum.compareTo(new BigDecimal(0))  == 1 ){
            FieldError err = new FieldError("退款金额", "refundSum", "退款金额不能大于账号可用余额！");
            result.addError(err);
        }

        //完成参数规则验证
        if (result.hasErrors()) {

            //查询企业信息
            ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(financeAccountRefundValidator.getEnterpriseId());
            if (!ResponseCode.SUCCESS.getCode().equals(enterpriseData.getCode())) {
                view.addObject("error", enterpriseData.getCode() + ":" + enterpriseData.getMessage());
                return view;
            }

            //查询业务账号
            ResponseData<AccountBasicInfoValidator> businessAccountData = businessAccountService.findById(financeAccountRefundValidator.getAccountId());
            if (!ResponseCode.SUCCESS.getCode().equals(businessAccountData.getCode())) {
                view.addObject("error", businessAccountData.getCode() + ":" + businessAccountData.getMessage());
                return view;
            }
            view.addObject("enterprise", enterpriseData.getData());
            view.addObject("financeAccountValidator", finance.getData());
            view.addObject("accountBasicInfoValidator", businessAccountData.getData());
            view.addObject("financeAccountRefundValidator", financeAccountRefundValidator);
            return view;
        }

        ResponseData data  = financeAccountService.refund(financeAccountRefundValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("ACCOUNT_FINANCE", financeAccountRefundValidator.getAccountId(), user.getRealName(), "refund",  "账户退款", JSON.toJSONString(financeAccountRefundValidator));
        }

        //记录日志
        log.info("[财务账户][账户退款][退款][{}]数据:{}", user.getUserName(), JSON.toJSONString(financeAccountRefundValidator));


        view.setView(new RedirectView("/finance/account/list", true, false));

        return view;

    }

    /**
     * 财务共享账户充值
     *
     * @return
     */
    @RequestMapping(value = "/share/refund/{accountId}/{enterpriseId}", method = RequestMethod.GET)
    public ModelAndView shareRecharge(@PathVariable String accountId, @PathVariable String enterpriseId, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("finance/finance_account_share_refund");
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

        //初始化数据
        FinanceAccountRefundValidator financeAccountRefundValidator = new FinanceAccountRefundValidator();
        financeAccountRefundValidator.setId(UUID.uuid32());
        financeAccountRefundValidator.setAccountId(accountId);
        financeAccountRefundValidator.setRefundFlowNo(StringRandom.getStringRandom(24).toUpperCase());
        financeAccountRefundValidator.setRefundSource("SHARE_ACCOUNT");
        financeAccountRefundValidator.setCreatedBy(user.getRealName());
        financeAccountRefundValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
        financeAccountRefundValidator.setEnterpriseId(enterpriseId);

        view.addObject("enterprise", enterpriseData.getData());
        view.addObject("financeAccountValidator", finance.getData());
        view.addObject("financeAccountRefundValidator", financeAccountRefundValidator);

        return view;

    }

    /**
     * 充值操作
     *
     * @return
     */
    @RequestMapping(value = "/share/refund/save", method = RequestMethod.POST)
    public ModelAndView refundSave(@ModelAttribute @Validated FinanceAccountRefundValidator financeAccountRefundValidator, BindingResult result, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("finance/finance_account_share_refund");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //查询财务账户数据
        ResponseData<FinanceAccountValidator> finance = financeAccountService.findById(financeAccountRefundValidator.getAccountId());
        if (!ResponseCode.SUCCESS.getCode().equals(finance.getCode())) {
            view.addObject("error", finance.getCode() + ":" + finance.getMessage());
        }

        //判断退款金额不能大于可用余额
        if(financeAccountRefundValidator.getRefundSum().compareTo(new BigDecimal(0)) == 0){
            FieldError err = new FieldError("退款金额", "refundSum", "退款金额不能等于0！");
            result.addError(err);
        }
        BigDecimal refundSum = financeAccountRefundValidator.getRefundSum().subtract(finance.getData().getAccountUsableSum());
        if( refundSum.compareTo(new BigDecimal(0))  == 1 ){
            FieldError err = new FieldError("退款金额", "refundSum", "退款金额不能大于账号可用余额！");
            result.addError(err);
        }

        //完成参数规则验证
        if (result.hasErrors()) {

            //查询企业信息
            ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(financeAccountRefundValidator.getEnterpriseId());
            if (!ResponseCode.SUCCESS.getCode().equals(enterpriseData.getCode())) {
                view.addObject("error", enterpriseData.getCode() + ":" + enterpriseData.getMessage());
                return view;
            }

            view.addObject("enterprise", enterpriseData.getData());
            view.addObject("financeAccountValidator", finance.getData());
            view.addObject("financeAccountRefundValidator", financeAccountRefundValidator);
            return view;
        }

        ResponseData data = financeAccountService.refund(financeAccountRefundValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("SHARE_ACCOUNT", financeAccountRefundValidator.getAccountId(), user.getRealName(), "refund", "共享账户退款", JSON.toJSONString(financeAccountRefundValidator));
        }

        //记录日志
        log.info("[财务账户][共享账户退款][退款][{}]数据:{}", user.getUserName(), JSON.toJSONString(financeAccountRefundValidator));

        view.setView(new RedirectView("/finance/account/share/list", true, false));

        return view;

    }

    /**
     * 财务帐号退款流水
     * @param accountId
     * @return
     */
    @RequestMapping(value = "/refund/account/list/{accountId}", method = RequestMethod.GET)
    public ModelAndView accountList(@PathVariable String accountId) {
        ModelAndView view = new ModelAndView("finance/finance_account_refund_list");
        //初始化数据
        PageParams<FinanceAccountRefundValidator> params = new PageParams<FinanceAccountRefundValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        FinanceAccountRefundValidator financeAccountRefundValidator = new FinanceAccountRefundValidator();
        financeAccountRefundValidator.setAccountId(accountId);
        params.setParams(financeAccountRefundValidator);

        //查询
        ResponseData<PageList<FinanceAccountRefundValidator>> data = financeAccountRefundService.page(params,"1");
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }


        view.addObject("financeAccountRefundValidator", financeAccountRefundValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;
    }

    /**
     * 财务帐号退款列表分页
     * @return
     */
    @RequestMapping(value = "/refund/account/list/page", method = RequestMethod.POST)
    public ModelAndView accountPage(@ModelAttribute FinanceAccountRefundValidator financeAccountRefundValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("finance/finance_account_refund_list");
        //分页查询
        pageParams.setParams(financeAccountRefundValidator);

        ResponseData<PageList<FinanceAccountRefundValidator>> data = financeAccountRefundService.page(pageParams,"1");
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("financeAccountRefundValidator", financeAccountRefundValidator);
        return view;
    }
}
