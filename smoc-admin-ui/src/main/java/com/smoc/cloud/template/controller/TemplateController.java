package com.smoc.cloud.template.controller;


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
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountValidator;
import com.smoc.cloud.common.smoc.template.AccountTemplateInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.customer.service.BusinessAccountService;
import com.smoc.cloud.customer.service.EnterpriseService;
import com.smoc.cloud.finance.service.FinanceAccountService;
import com.smoc.cloud.sequence.service.SequenceService;
import com.smoc.cloud.template.service.AccountTemplateInfoService;
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
import java.util.Map;

/**
 * 模板管理
 */
@Slf4j
@Controller
@RequestMapping("/template")
public class TemplateController {

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private BusinessAccountService businessAccountService;

    @Autowired
    private AccountTemplateInfoService accountTemplateInfoService;

    /**
     * 模板管理列表
     *
     * @return
     */
    @RequestMapping(value = "/list/{protocol}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String protocol) {
        ModelAndView view = new ModelAndView("templates/template_list");

        //初始化数据
        PageParams<AccountTemplateInfoValidator> params = new PageParams<AccountTemplateInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        AccountTemplateInfoValidator accountTemplateInfoValidator = new AccountTemplateInfoValidator();
        accountTemplateInfoValidator.setTemplateAgreementType(protocol);
        //accountTemplateInfoValidator.setTemplateStatus("1");
        params.setParams(accountTemplateInfoValidator);

        //查询
        ResponseData<PageList<AccountTemplateInfoValidator>> data = accountTemplateInfoService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //log.info("[page]:{}", new Gson().toJson(data));

        view.addObject("accountTemplateInfoValidator", accountTemplateInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }

    /**
     * 模板管理分页
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute AccountTemplateInfoValidator accountTemplateInfoValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("templates/template_list");

        //accountTemplateInfoValidator.setTemplateStatus("1");
        //分页查询
        pageParams.setParams(accountTemplateInfoValidator);

        ResponseData<PageList<FinanceAccountValidator>> data = accountTemplateInfoService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("accountTemplateInfoValidator", accountTemplateInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }

    /**
     * 添加模板
     *
     * @return
     */
    @RequestMapping(value = "/add/{businessAccount}/{protocol}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String businessAccount, @PathVariable String protocol) {
        ModelAndView view = new ModelAndView("templates/template_edit");

        //查询业务账号信息
        ResponseData<AccountBasicInfoValidator> accountBasicInfoValidatorResponseData = businessAccountService.findById(businessAccount);
        if (!ResponseCode.SUCCESS.getCode().equals(accountBasicInfoValidatorResponseData.getCode())) {
            view.addObject("error", accountBasicInfoValidatorResponseData.getCode() + ":" + accountBasicInfoValidatorResponseData.getMessage());
            return view;
        }

        //查询企业信息
        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(accountBasicInfoValidatorResponseData.getData().getEnterpriseId());
        if (!ResponseCode.SUCCESS.getCode().equals(enterpriseData.getCode())) {
            view.addObject("error", enterpriseData.getCode() + ":" + enterpriseData.getMessage());
            return view;
        }

        //初始化模板信息
        AccountTemplateInfoValidator accountTemplateInfoValidator = new AccountTemplateInfoValidator();
        accountTemplateInfoValidator.setTemplateId("TEMP" + sequenceService.findSequence("BUSINESS_ACCOUNT"));
        accountTemplateInfoValidator.setBusinessAccount(businessAccount);
        accountTemplateInfoValidator.setTemplateAgreementType(protocol);
        accountTemplateInfoValidator.setTemplateType(accountBasicInfoValidatorResponseData.getData().getBusinessType());
        accountTemplateInfoValidator.setTemplateStatus("1");

        view.addObject("op", "add");
        view.addObject("accountBasicInfoValidator", accountBasicInfoValidatorResponseData.getData());
        view.addObject("enterpriseBasicInfoValidator", enterpriseData.getData());
        view.addObject("accountTemplateInfoValidator", accountTemplateInfoValidator);
        return view;
    }

    /**
     * 同业务账号新建模板
     *
     * @return
     */
    @RequestMapping(value = "/copy/{business}", method = RequestMethod.GET)
    public ModelAndView copy(@PathVariable String businessAccountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("templates/template_edit");

        return view;
    }

    /**
     * 修改模板
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("templates/template_edit");

        //查询模板信息
        ResponseData<AccountTemplateInfoValidator> data = accountTemplateInfoService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //查询业务账号信息
        ResponseData<AccountBasicInfoValidator> accountBasicInfoValidatorResponseData = businessAccountService.findById(data.getData().getBusinessAccount());
        if (!ResponseCode.SUCCESS.getCode().equals(accountBasicInfoValidatorResponseData.getCode())) {
            view.addObject("error", accountBasicInfoValidatorResponseData.getCode() + ":" + accountBasicInfoValidatorResponseData.getMessage());
            return view;
        }

        //查询企业信息
        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(accountBasicInfoValidatorResponseData.getData().getEnterpriseId());
        if (!ResponseCode.SUCCESS.getCode().equals(enterpriseData.getCode())) {
            view.addObject("error", enterpriseData.getCode() + ":" + enterpriseData.getMessage());
            return view;
        }

        view.addObject("op", "edit");
        view.addObject("accountBasicInfoValidator", accountBasicInfoValidatorResponseData.getData());
        view.addObject("enterpriseBasicInfoValidator", enterpriseData.getData());
        view.addObject("accountTemplateInfoValidator", data.getData());

        return view;
    }

    /**
     * 模板详细中心
     *
     * @return
     */
    @RequestMapping(value = "/view/center/{id}/{protocol}", method = RequestMethod.GET)
    public ModelAndView center(@PathVariable String id, @PathVariable String protocol) {
        ModelAndView view = new ModelAndView("templates/template_view_center");
        view.addObject("templateId", id);
        view.addObject("protocol", protocol);
        return view;
    }

    /**
     * 模板详细
     *
     * @return
     */
    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("templates/template_view");
        //查询模板信息
        ResponseData<AccountTemplateInfoValidator> data = accountTemplateInfoService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //查询业务账号信息
        ResponseData<AccountBasicInfoValidator> accountBasicInfoValidatorResponseData = businessAccountService.findById(data.getData().getBusinessAccount());
        if (!ResponseCode.SUCCESS.getCode().equals(accountBasicInfoValidatorResponseData.getCode())) {
            view.addObject("error", accountBasicInfoValidatorResponseData.getCode() + ":" + accountBasicInfoValidatorResponseData.getMessage());
            return view;
        }

        //查询企业信息
        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(accountBasicInfoValidatorResponseData.getData().getEnterpriseId());
        if (!ResponseCode.SUCCESS.getCode().equals(enterpriseData.getCode())) {
            view.addObject("error", enterpriseData.getCode() + ":" + enterpriseData.getMessage());
            return view;
        }

        view.addObject("accountBasicInfoValidator", accountBasicInfoValidatorResponseData.getData());
        view.addObject("enterpriseBasicInfoValidator", enterpriseData.getData());
        view.addObject("accountTemplateInfoValidator", data.getData());
        return view;
    }

    /**
     * 保存企业开户
     *
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated AccountTemplateInfoValidator accountTemplateInfoValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("templates/template_edit");
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("accountTemplateInfoValidator", accountTemplateInfoValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            accountTemplateInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            accountTemplateInfoValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            accountTemplateInfoValidator.setUpdatedTime(new Date());
            accountTemplateInfoValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //保存数据
        ResponseData data = accountTemplateInfoService.save(accountTemplateInfoValidator, op,user.getId());
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("TEMPLATE_INFO", accountTemplateInfoValidator.getTemplateId(), "add".equals(op) ? accountTemplateInfoValidator.getCreatedBy() : accountTemplateInfoValidator.getUpdatedBy(), op, "add".equals(op) ? "添加模板" : "修改模板", JSON.toJSONString(accountTemplateInfoValidator));
        }

        //记录日志
        log.info("[模板管理][模板信息][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(accountTemplateInfoValidator));

        view.setView(new RedirectView("/template/list/" + accountTemplateInfoValidator.getTemplateAgreementType(), true, false));
        return view;

    }

    /**
     * 注销模板
     *
     * @return
     */
    @RequestMapping(value = "/cancel/{templateId}/{status}", method = RequestMethod.GET)
    public ModelAndView cancelTemplate(@PathVariable String templateId,@PathVariable String status, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("templates/template_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //查询模板信息
        ResponseData<AccountTemplateInfoValidator> data = accountTemplateInfoService.findById(templateId);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        if("0".equals(status)){
            status = "1";
        }else{
            status = "0";
        }
        ResponseData deleteResponseData = accountTemplateInfoService.cancelTemplate(templateId,status);
        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(deleteResponseData.getCode())) {
            systemUserLogService.logsAsync("TEMPLATE_INFO", templateId, user.getRealName(),"delete","0".equals(status) ?"注销模板":"启用模板", JSON.toJSONString(data.getData()));
        }
        //记录日志
        log.info("[模板管理][注销模板][{}][{}]数据:{}", "delete", user.getUserName(), JSON.toJSONString(data.getCode()));

        view.setView(new RedirectView("/template/list/" + data.getData().getTemplateAgreementType(), true, false));
        return view;
    }
}
