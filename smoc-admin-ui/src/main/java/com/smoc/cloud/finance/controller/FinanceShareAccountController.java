package com.smoc.cloud.finance.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountValidator;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationAccountInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.customer.service.EnterpriseService;
import com.smoc.cloud.finance.service.FinanceAccountService;
import com.smoc.cloud.sequence.service.SequenceService;
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
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 财务共享账户
 */
@Slf4j
@Controller
@RequestMapping("/finance")
public class FinanceShareAccountController {

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private FinanceAccountService financeAccountService;

    /**
     * 财务共享账户列表
     *
     * @return
     */
    @RequestMapping(value = "/account/share/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("finance/finance_account_share_list");

        //初始化数据
        PageParams<FinanceAccountValidator> params = new PageParams<FinanceAccountValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        FinanceAccountValidator financeAccountValidator = new FinanceAccountValidator();
        params.setParams(financeAccountValidator);

        //查询
        ResponseData<PageList<FinanceAccountValidator>> data = financeAccountService.page(params, "3");
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        ResponseData<Map<String, Object>> count = financeAccountService.count("3");

        view.addObject("financeAccountValidator", financeAccountValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("counter", count.getData());
        return view;
    }

    /**
     * 财务共享账户分页
     *
     * @return
     */
    @RequestMapping(value = "/account/share/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute FinanceAccountValidator financeAccountValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("finance/finance_account_share_list");

        //分页查询
        pageParams.setParams(financeAccountValidator);

        ResponseData<PageList<FinanceAccountValidator>> data = financeAccountService.page(pageParams, "3");
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        ResponseData<Map<String, Object>> count = financeAccountService.count("3");

        view.addObject("financeAccountValidator", financeAccountValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("counter", count.getData());

        return view;
    }

    /**
     * 新建财务共享账户
     *
     * @return
     */
    @RequestMapping(value = "/account/share/add/{enterpriseId}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String enterpriseId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("finance/finance_account_share_edit");

        //查询企业信息
        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(enterpriseId);
        if (!ResponseCode.SUCCESS.getCode().equals(enterpriseData.getCode())) {
            view.addObject("error", enterpriseData.getCode() + ":" + enterpriseData.getMessage());
            return view;
        }

        //查询企业财务账户信息(包括子企业财务账户)
        ResponseData<List<FinanceAccountValidator>> data = financeAccountService.findEnterpriseAndSubsidiaryFinanceAccount(enterpriseId);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        FinanceAccountValidator financeAccountValidator = new FinanceAccountValidator();
        financeAccountValidator.setAccountId("XYSH"+sequenceService.findSequence("BUSINESS_ACCOUNT"));
        financeAccountValidator.setEnterpriseId(enterpriseId);
        financeAccountValidator.setAccountName("财务共享账号");

        financeAccountValidator.setAccountType("SHARE_ACCOUNT");
        financeAccountValidator.setAccountCreditSum(new BigDecimal("0.00"));
        financeAccountValidator.setAccountStatus("1");

        financeAccountValidator.setIsUsableSumPool("1");
        financeAccountValidator.setIsFreezeSumPool("0");

        view.addObject("op", "add");
        view.addObject("financeAccountValidator", financeAccountValidator);
        view.addObject("enterprise", enterpriseData.getData());
        view.addObject("financeAccounts", data.getData());

        return view;
    }

    /**
     * 财务共享账户编辑
     *
     * @return
     */
    @RequestMapping(value = "/account/share/edit/{enterpriseId}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String enterpriseId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("finance/finance_account_share_edit");

        return view;
    }

    /**
     * 保存认账账号
     *
     * @return
     */
    @RequestMapping(value = "/account/share/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated FinanceAccountValidator financeAccountValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("finance/finance_account_share_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        if (result.hasErrors()) {
            //查询销售人员
            view.addObject("financeAccountValidator", financeAccountValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            financeAccountValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            financeAccountValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            financeAccountValidator.setUpdatedTime(new Date());
            financeAccountValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }


        //保存数据
        ResponseData data = financeAccountService.save(financeAccountValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("SHARE_ACCOUNT", financeAccountValidator.getAccountId(), user.getUserName(), op, "add".equals(op) ? "创建财务共享账号" : "修改财务共享账号", JSON.toJSONString(financeAccountValidator));
        }

        //记录日志
        log.info("[财务共享账户][开户信息][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(financeAccountValidator));

        return view;

    }

    /**
     * 财务共享账户查看中心
     *
     * @return
     */
    @RequestMapping(value = "/account/share/view/center/*", method = RequestMethod.GET)
    public ModelAndView center() {
        ModelAndView view = new ModelAndView("finance/finance_account_share_view_center");

        return view;
    }

    /**
     * 财务共享账户基本信息明细
     *
     * @return
     */
    @RequestMapping(value = "/account/share/view/base/{id}", method = RequestMethod.GET)
    public ModelAndView view_base(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("finance/finance_account_share_view_base");

        return view;
    }

    /**
     * 财务共享账户充值列表
     *
     * @return
     */
    @RequestMapping(value = "/account/share/view/recharge/{id}", method = RequestMethod.GET)
    public ModelAndView view_recharge(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("finance/finance_account_share_view_recharge_list");
        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(3);
        params.setPageSize(10);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(22);

        view.addObject("pageParams", params);
        return view;
    }

    /**
     * 财务共享账户消费列表
     *
     * @return
     */
    @RequestMapping(value = "/account/share/view/consume/{id}", method = RequestMethod.GET)
    public ModelAndView view_consume(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("finance/finance_account_share_view_consume_list");
        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(3);
        params.setPageSize(10);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(22);

        view.addObject("pageParams", params);
        return view;
    }


}
