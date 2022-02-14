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
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountValidator;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationAccountInfoValidator;
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
 * 充值流水
 */
@Slf4j
@Controller
@RequestMapping("/finance/account")
public class FinanceRechargeController {

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private FinanceAccountService financeAccountService;

    @Autowired
    private BusinessAccountService businessAccountService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    @Autowired
    private FinanceAccountRechargeService financeAccountRechargeService;

    /**
     * 财务充值列表
     * @return
     */
    @RequestMapping(value = "/recharge/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("finance/finance_recharge_list");
        //初始化数据
        PageParams<FinanceAccountRechargeValidator> params = new PageParams<FinanceAccountRechargeValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        FinanceAccountRechargeValidator financeAccountRechargeValidator = new FinanceAccountRechargeValidator();

        Date startDate = DateTimeUtils.getFirstMonth(12);
        financeAccountRechargeValidator.setStartDate(DateTimeUtils.getDateFormat(startDate));
        financeAccountRechargeValidator.setEndDate(DateTimeUtils.getDateFormat(new Date()));
        params.setParams(financeAccountRechargeValidator);

        //查询
        ResponseData<PageList<FinanceAccountRechargeValidator>> data = financeAccountRechargeService.page(params,"1");
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        ResponseData<Map<String, Object>> countData = financeAccountRechargeService.countRechargeSum(financeAccountRechargeValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(countData.getCode())) {
            view.addObject("error", countData.getCode() + ":" + countData.getMessage());
            return view;
        }

        //log.info("[count]:{}",new Gson().toJson(countData.getData()));
        view.addObject("count", countData.getData() == null?new HashMap<>():countData.getData());
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
    public ModelAndView page(@ModelAttribute FinanceAccountRechargeValidator financeAccountRechargeValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("finance/finance_recharge_list");
        //分页查询
        pageParams.setParams(financeAccountRechargeValidator);

        //日期格式
        if (!StringUtils.isEmpty(financeAccountRechargeValidator.getStartDate())) {
            String[] date = financeAccountRechargeValidator.getStartDate().split(" - ");
            financeAccountRechargeValidator.setStartDate(StringUtils.trimWhitespace(date[0]));
            financeAccountRechargeValidator.setEndDate(StringUtils.trimWhitespace(date[1]));
        }

        ResponseData<PageList<FinanceAccountRechargeValidator>> data = financeAccountRechargeService.page(pageParams,"1");
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        ResponseData<Map<String, Object>> countData = financeAccountRechargeService.countRechargeSum(financeAccountRechargeValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(countData.getCode())) {
            view.addObject("error", countData.getCode() + ":" + countData.getMessage());
            return view;
        }

       // log.info("[count]:{}",new Gson().toJson(countData.getData()));

        view.addObject("count", countData.getData() == null?new HashMap<>():countData.getData());
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("financeAccountRechargeValidator", financeAccountRechargeValidator);
        return view;
    }

    /**
     * 财务账户充值
     *
     * @return
     */
    @RequestMapping(value = "/recharge/{accountId}/{enterpriseId}", method = RequestMethod.GET)
    public ModelAndView recharge(@PathVariable String accountId,@PathVariable String enterpriseId, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("finance/finance_recharge_edit");
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

        FinanceAccountRechargeValidator financeAccountRechargeValidator = new FinanceAccountRechargeValidator();
        financeAccountRechargeValidator.setId(UUID.uuid32());
        financeAccountRechargeValidator.setAccountId(accountId);
        financeAccountRechargeValidator.setRechargeFlowNo(StringRandom.getStringRandom(24).toUpperCase());
        financeAccountRechargeValidator.setRechargeSource("BUSINESS_ACCOUNT");
        financeAccountRechargeValidator.setCreatedBy(user.getRealName());
        financeAccountRechargeValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
        financeAccountRechargeValidator.setEnterpriseId(enterpriseId);

        view.addObject("enterprise", enterpriseData.getData());
        view.addObject("financeAccountValidator", finance.getData());
        view.addObject("accountBasicInfoValidator", businessAccountData.getData());
        view.addObject("financeAccountRechargeValidator", financeAccountRechargeValidator);

        return view;

    }

    /**
     * 充值操作
     *
     * @return
     */
    @RequestMapping(value = "/recharge/save", method = RequestMethod.POST)
    public ModelAndView rechargeSave(@ModelAttribute @Validated FinanceAccountRechargeValidator financeAccountRechargeValidator, BindingResult result, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("finance/finance_recharge_edit");

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
            systemUserLogService.logsAsync("BUSINESS_ACCOUNT", financeAccountRechargeValidator.getAccountId(), user.getUserName(), "recharge",  "账户充值", JSON.toJSONString(financeAccountRechargeValidator));
        }

        //记录日志
        log.info("[财务账户][账户充值][充值][{}]数据:{}", user.getUserName(), JSON.toJSONString(financeAccountRechargeValidator));


        view.setView(new RedirectView("/finance/account/list", true, false));

        return view;

    }

    /**
     * 充值明细
     * @return
     */
    @RequestMapping(value = "/recharge/view", method = RequestMethod.GET)
    public ModelAndView view() {
        ModelAndView view = new ModelAndView("finance/finance_recharge_view");

        return view;
    }

    /**
     * 财务充值列表
     * @return
     */
    @RequestMapping(value = "/recharge/account/list/{accountId}", method = RequestMethod.GET)
    public ModelAndView accountList(@PathVariable String accountId) {
        ModelAndView view = new ModelAndView("finance/finance_account_recharge_list");
        //初始化数据
        PageParams<FinanceAccountRechargeValidator> params = new PageParams<FinanceAccountRechargeValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        FinanceAccountRechargeValidator financeAccountRechargeValidator = new FinanceAccountRechargeValidator();

//        Date startDate = DateTimeUtils.getFirstMonth(12);
//        financeAccountRechargeValidator.setStartDate(DateTimeUtils.getDateFormat(startDate));
//        financeAccountRechargeValidator.setEndDate(DateTimeUtils.getDateFormat(new Date()));
        financeAccountRechargeValidator.setAccountId(accountId);
        params.setParams(financeAccountRechargeValidator);

        //查询
        ResponseData<PageList<FinanceAccountRechargeValidator>> data = financeAccountRechargeService.page(params,"1");
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        ResponseData<Map<String, Object>> countData = financeAccountRechargeService.countRechargeSum(financeAccountRechargeValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(countData.getCode())) {
            view.addObject("error", countData.getCode() + ":" + countData.getMessage());
            return view;
        }

        log.info("[count]:{}",new Gson().toJson(countData.getData()));
        view.addObject("count", countData.getData());
        view.addObject("financeAccountRechargeValidator", financeAccountRechargeValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;
    }

    /**
     * 财务充值列表分页
     * @return
     */
    @RequestMapping(value = "/recharge/account/list/page", method = RequestMethod.POST)
    public ModelAndView accountPage(@ModelAttribute FinanceAccountRechargeValidator financeAccountRechargeValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("finance/finance_account_recharge_list");
        //分页查询
        pageParams.setParams(financeAccountRechargeValidator);

//        //日期格式
//        if (!StringUtils.isEmpty(financeAccountRechargeValidator.getStartDate())) {
//            String[] date = financeAccountRechargeValidator.getStartDate().split(" - ");
//            financeAccountRechargeValidator.setStartDate(StringUtils.trimWhitespace(date[0]));
//            financeAccountRechargeValidator.setEndDate(StringUtils.trimWhitespace(date[1]));
//        }

        ResponseData<PageList<FinanceAccountRechargeValidator>> data = financeAccountRechargeService.page(pageParams,"1");
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        ResponseData<Map<String, Object>> countData = financeAccountRechargeService.countRechargeSum(financeAccountRechargeValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(countData.getCode())) {
            view.addObject("error", countData.getCode() + ":" + countData.getMessage());
            return view;
        }

        log.info("[count]:{}",new Gson().toJson(countData.getData()));

        view.addObject("count", countData.getData());
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("financeAccountRechargeValidator", financeAccountRechargeValidator);
        return view;
    }


}
