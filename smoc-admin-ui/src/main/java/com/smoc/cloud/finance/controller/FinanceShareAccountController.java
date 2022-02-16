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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 财务共享账户
 */
@Slf4j
@Controller
@RequestMapping("/finance/account")
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
    @RequestMapping(value = "/share/list", method = RequestMethod.GET)
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

        ResponseData<Map<String, Object>> count = financeAccountService.count(financeAccountValidator, "3");


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
    @RequestMapping(value = "/share/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute FinanceAccountValidator financeAccountValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("finance/finance_account_share_list");

        //分页查询
        pageParams.setParams(financeAccountValidator);

        ResponseData<PageList<FinanceAccountValidator>> data = financeAccountService.page(pageParams, "3");
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        ResponseData<Map<String, Object>> count = financeAccountService.count(financeAccountValidator, "3");

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
    @RequestMapping(value = "/share/add/{enterpriseId}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String enterpriseId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("finance/finance_account_share_add");

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
        financeAccountValidator.setAccountId("XYSH" + sequenceService.findSequence("BUSINESS_ACCOUNT"));
        financeAccountValidator.setEnterpriseId(enterpriseId);

        financeAccountValidator.setAccountType("SHARE_ACCOUNT");
        financeAccountValidator.setAccountCreditSum(new BigDecimal("0.00"));
        financeAccountValidator.setAccountStatus("1");

        financeAccountValidator.setIsUsableSumPool("0");
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
    @RequestMapping(value = "/share/edit/{accountId}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("finance/finance_account_share_edit");

        ResponseData<FinanceAccountValidator> shareFinanceAccount = financeAccountService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(shareFinanceAccount.getCode())) {
            view.addObject("error", shareFinanceAccount.getCode() + ":" + shareFinanceAccount.getMessage());
            return view;
        }
        FinanceAccountValidator financeAccountValidator = shareFinanceAccount.getData();
        financeAccountValidator.setIsFreezeSumPool("0");
        financeAccountValidator.setIsUsableSumPool("0");

        //已选中财务账户
        Map<String, Boolean> selectedMap = new HashMap<>();
        String[] selectedAccounts = shareFinanceAccount.getData().getShareId().split(",");
        for (int i = 0; i < selectedAccounts.length; i++) {
            selectedMap.put(selectedAccounts[i], true);
        }

        //查询企业信息
        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(shareFinanceAccount.getData().getEnterpriseId());
        if (!ResponseCode.SUCCESS.getCode().equals(enterpriseData.getCode())) {
            view.addObject("error", enterpriseData.getCode() + ":" + enterpriseData.getMessage());
            return view;
        }

        //查询企业财务账户信息(包括子企业财务账户)
        ResponseData<List<FinanceAccountValidator>> data = financeAccountService.findEnterpriseAndSubsidiaryFinanceAccount(shareFinanceAccount.getData().getEnterpriseId());
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("op", "edit");
        view.addObject("selectedMap", selectedMap);
        view.addObject("financeAccountValidator", financeAccountValidator);
        view.addObject("enterprise", enterpriseData.getData());
        view.addObject("financeAccounts", data.getData());

        return view;
    }

    /**
     * 保存认账账号
     *
     * @return
     */
    @RequestMapping(value = "/share/save/{op}", method = RequestMethod.POST)
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

        //保存数据之前，要处理日志问题
        ResponseData<FinanceAccountValidator> shareFinanceAccount = null;
        if (op.equals("edit")) {
            shareFinanceAccount = financeAccountService.findById(financeAccountValidator.getAccountId());
            if (!ResponseCode.SUCCESS.getCode().equals(shareFinanceAccount.getCode())) {
                view.addObject("error", shareFinanceAccount.getCode() + ":" + shareFinanceAccount.getMessage());
                return view;
            }
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            financeAccountValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            financeAccountValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            financeAccountValidator.setUpdatedTime(new Date());
            financeAccountValidator.setCreatedBy(user.getRealName());
            financeAccountValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }


        financeAccountValidator.setIsFreezeSumPool("0");
        financeAccountValidator.setIsUsableSumPool("0");
        //保存数据
        ResponseData data = financeAccountService.save(financeAccountValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("SHARE_ACCOUNT", financeAccountValidator.getAccountId(), user.getRealName(), op, "add".equals(op) ? "创建财务共享账号" : "修改财务共享账号", JSON.toJSONString(financeAccountValidator));
        }

        //集中记录日志
        if (op.equals("add")) {
            this.logs(financeAccountValidator, null, user);
        }
        if (op.equals("edit")) {
            this.logs(financeAccountValidator, shareFinanceAccount.getData(), user);
        }

        //记录日志
        log.info("[财务共享账户][开户信息][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(financeAccountValidator));

        view.setView(new RedirectView("/finance/account/share/list", true, false));
        return view;

    }

    /**
     * 财务共享账户查看中心
     *
     * @return
     */
    @RequestMapping(value = "/share/view/center/{accountId}/{enterpriseId}", method = RequestMethod.GET)
    public ModelAndView center(@PathVariable String accountId, @PathVariable String enterpriseId) {
        ModelAndView view = new ModelAndView("finance/finance_account_share_view_center");

        //保存数据之前，要处理日志问题
        ResponseData<FinanceAccountValidator> shareFinanceAccount = financeAccountService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(shareFinanceAccount.getCode())) {
            view.addObject("error", shareFinanceAccount.getCode() + ":" + shareFinanceAccount.getMessage());
            return view;
        }

        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(shareFinanceAccount.getData().getEnterpriseId());
        if (!ResponseCode.SUCCESS.getCode().equals(enterpriseData.getCode())) {
            view.addObject("error", enterpriseData.getCode() + ":" + enterpriseData.getMessage());
            return view;
        }

        view.addObject("financeAccountValidator", shareFinanceAccount.getData());
        view.addObject("enterprise", enterpriseData.getData());

        return view;
    }

    /**
     * 财务共享账户基本信息明细
     *
     * @return
     */
    @RequestMapping(value = "/share/view/base/{accountId}", method = RequestMethod.GET)
    public ModelAndView view_base(@PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("finance/finance_account_share_view_base");

        ResponseData<FinanceAccountValidator> shareFinanceAccount = financeAccountService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(shareFinanceAccount.getCode())) {
            view.addObject("error", shareFinanceAccount.getCode() + ":" + shareFinanceAccount.getMessage());
            return view;
        }
        FinanceAccountValidator financeAccountValidator = shareFinanceAccount.getData();

        //已选中财务账户
        Map<String, Boolean> selectedMap = new HashMap<>();
        String[] selectedAccounts = shareFinanceAccount.getData().getShareId().split(",");
        for (int i = 0; i < selectedAccounts.length; i++) {
            selectedMap.put(selectedAccounts[i], true);
        }

        //查询企业财务账户信息(包括子企业财务账户)
        ResponseData<List<FinanceAccountValidator>> data = financeAccountService.findEnterpriseAndSubsidiaryFinanceAccount(shareFinanceAccount.getData().getEnterpriseId());
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //查询企业信息
        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(shareFinanceAccount.getData().getEnterpriseId());
        if (!ResponseCode.SUCCESS.getCode().equals(enterpriseData.getCode())) {
            view.addObject("error", enterpriseData.getCode() + ":" + enterpriseData.getMessage());
            return view;
        }

        view.addObject("enterprise", enterpriseData.getData());
        view.addObject("selectedMap", selectedMap);
        view.addObject("financeAccounts", data.getData());

        return view;
    }

    /**
     * 财务共享账户充值
     *
     * @return
     */
    @RequestMapping(value = "/share/recharge/{accountId}/{enterpriseId}", method = RequestMethod.GET)
    public ModelAndView recharge(@PathVariable String accountId, @PathVariable String enterpriseId, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("finance/finance_account_share_recharge");
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


        FinanceAccountRechargeValidator financeAccountRechargeValidator = new FinanceAccountRechargeValidator();
        financeAccountRechargeValidator.setId(UUID.uuid32());
        financeAccountRechargeValidator.setAccountId(accountId);
        financeAccountRechargeValidator.setRechargeFlowNo(StringRandom.getStringRandom(24).toUpperCase());
        financeAccountRechargeValidator.setRechargeSource("SHARE_ACCOUNT");
        financeAccountRechargeValidator.setCreatedBy(user.getRealName());
        financeAccountRechargeValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
        financeAccountRechargeValidator.setEnterpriseId(enterpriseId);

        view.addObject("enterprise", enterpriseData.getData());
        view.addObject("financeAccountValidator", finance.getData());
        view.addObject("financeAccountRechargeValidator", financeAccountRechargeValidator);

        return view;

    }

    /**
     * 充值操作
     *
     * @return
     */
    @RequestMapping(value = "/share/recharge/save", method = RequestMethod.POST)
    public ModelAndView rechargeSave(@ModelAttribute @Validated FinanceAccountRechargeValidator financeAccountRechargeValidator, BindingResult result, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("finance/finance_account_share_recharge");

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

            view.addObject("enterprise", enterpriseData.getData());
            view.addObject("financeAccountValidator", finance.getData());
            view.addObject("financeAccountRechargeValidator", financeAccountRechargeValidator);
            return view;
        }

        ResponseData data = financeAccountService.recharge(financeAccountRechargeValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("SHARE_ACCOUNT", financeAccountRechargeValidator.getAccountId(), user.getRealName(), "recharge", "共享账户充值", JSON.toJSONString(financeAccountRechargeValidator));
        }

        //记录日志
        log.info("[财务账户][共享账户充值][充值][{}]数据:{}", user.getUserName(), JSON.toJSONString(financeAccountRechargeValidator));


        view.setView(new RedirectView("/finance/account/share/list", true, false));

        return view;

    }

    /**
     * 批量记录日志
     *
     * @param qo
     * @param financeAccountRechargeValidator
     */
    public void logs(FinanceAccountValidator qo, FinanceAccountValidator financeAccountRechargeValidator, SecurityUser user) {


        if (null == financeAccountRechargeValidator) {

            if (StringUtils.isEmpty(StringUtils.isEmpty(qo.getAccountIds()))) {
                return;
            }
            //新共享分账户
            String[] accountIds = qo.getAccountIds().split(",");
            for (int i = 0; i < accountIds.length; i++) {
                systemUserLogService.logsAsync("ACCOUNT_FINANCE", accountIds[i], user.getRealName(), "share", "账户共享", JSON.toJSONString(qo));
            }
            //systemUserLogService.logsAsync("SHARE_ACCOUNT", qo.getAccountId(), user.getRealName(), "add", "创建共享账户", JSON.toJSONString(qo));
            return;
        }

        if (StringUtils.isEmpty(qo.getAccountIds()) && StringUtils.isEmpty(financeAccountRechargeValidator.getShareId())) {
            return;
        }

        //Boolean 为true 表示，新数据中，仍然有该财务账户
        Map<String, Boolean> shareIdsMap = new HashMap<>();
        //要新创建的 分财务账号   结合shareIdsMap 就能分辨出，那些是被删除的分财务账户，那些是新加进来的分财务账户， 新的和原来的都存在的财务账户，不动
        Map<String, Boolean> newShareIdMap = new HashMap<>();

        //现有共享分账户  所有的现有分财务账户，在map 中初始化为false
        String[] shareIds = null;
        if (StringUtils.isEmpty(financeAccountRechargeValidator.getShareId())) {
            shareIds = financeAccountRechargeValidator.getShareId().split(",");
            for (int i = 0; i < shareIds.length; i++) {
                shareIdsMap.put(shareIds[i], false);
            }
        }
        //新共享分账户
        String[] accountIds = null;
        if (StringUtils.isEmpty(qo.getAccountIds())) {
            accountIds = qo.getAccountIds().split(",");
            for (int i = 0; i < accountIds.length; i++) {
                //表示该共享分账户，存在现有数据中
                if (null != shareIdsMap.get(accountIds[i])) {
                    shareIdsMap.put(accountIds[i], true);
                } else {
                    newShareIdMap.put(accountIds[i], true);
                }
            }
        }

        //删除共享
        for (String key : shareIdsMap.keySet()) {
            if (!shareIdsMap.get(key))
                systemUserLogService.logsAsync("ACCOUNT_FINANCE", key, user.getRealName(), "delete share", "删除共享", JSON.toJSONString(qo));
        }

        //添加共享
        for (String key : newShareIdMap.keySet()) {
            if (newShareIdMap.get(key))
                systemUserLogService.logsAsync("ACCOUNT_FINANCE", key, user.getRealName(), "share", "账户共享", JSON.toJSONString(qo));
        }

        // systemUserLogService.logsAsync("SHARE_ACCOUNT", qo.getAccountId(), user.getRealName(), "edit", "共享账户变更", JSON.toJSONString(financeAccountRechargeValidator));


    }


}
