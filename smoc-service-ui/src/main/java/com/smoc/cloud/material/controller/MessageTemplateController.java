package com.smoc.cloud.material.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smoc.cloud.admin.security.remote.service.FlowApproveService;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.validator.FlowApproveValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.AccountInterfaceInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseDocumentInfoValidator;
import com.smoc.cloud.common.smoc.template.AccountTemplateInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.material.service.BusinessAccountService;
import com.smoc.cloud.material.service.MessageSignService;
import com.smoc.cloud.material.service.MessageTemplateService;
import com.smoc.cloud.material.service.SequenceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 短信模板
 */
@Slf4j
@RestController
@RequestMapping("/template")
public class MessageTemplateController {

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private MessageTemplateService messageTemplateService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    @Autowired
    private FlowApproveService flowApproveService;

    @Autowired
    private MessageSignService messageSignService;

    @Autowired
    private BusinessAccountService businessAccountService;

    /**
     * 短信模板列表
     * @param type
     * @param request
     * @return
     */
    @RequestMapping(value = "list/{type}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String type, HttpServletRequest request) {

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        ModelAndView view = new ModelAndView("template/message_template_list");
        //初始化数据
        PageParams<AccountTemplateInfoValidator> params = new PageParams<AccountTemplateInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        AccountTemplateInfoValidator accountTemplateInfoValidator = new AccountTemplateInfoValidator();
        accountTemplateInfoValidator.setEnterpriseId(user.getOrganization());
        accountTemplateInfoValidator.setTemplateAgreementType("SERVICE_WEB");
        accountTemplateInfoValidator.setTemplateType(type);
        params.setParams(accountTemplateInfoValidator);

        //查询
        ResponseData<PageList<AccountTemplateInfoValidator>> data = messageTemplateService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("accountTemplateInfoValidator", accountTemplateInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("type", type);
        return view;
    }

    /**
     *  短信模板分页
     * @param type
     * @param request
     * @return
     */
    @RequestMapping(value = "page/{type}", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute AccountTemplateInfoValidator accountTemplateInfoValidator, @PathVariable String type,PageParams pageParams,HttpServletRequest request) {
        ModelAndView view = new ModelAndView("template/message_template_list");
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //分页查询
        accountTemplateInfoValidator.setEnterpriseId(user.getOrganization());
        accountTemplateInfoValidator.setTemplateType(type);
        accountTemplateInfoValidator.setTemplateAgreementType("SERVICE_WEB");
        pageParams.setParams(accountTemplateInfoValidator);


        ResponseData<PageList<AccountTemplateInfoValidator>> data = messageTemplateService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("accountTemplateInfoValidator", accountTemplateInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("type", type);
        return view;
    }

    /**
     * 添加
     * @return
     */
    @RequestMapping(value = "/add/{type}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String type, HttpServletRequest request) {
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("template/message_template_edit");

        //初始化参数
        AccountTemplateInfoValidator accountTemplateInfoValidator = new AccountTemplateInfoValidator();
        accountTemplateInfoValidator.setTemplateId("TEMP" + sequenceService.findSequence("TEMPLATE"));
        accountTemplateInfoValidator.setTemplateType(type);
        accountTemplateInfoValidator.setTemplateStatus("3");
        accountTemplateInfoValidator.setTemplateFlag("1");
        accountTemplateInfoValidator.setTemplateAgreementType("WEB");

        //查询签名
        EnterpriseDocumentInfoValidator enterpriseDocumentInfoValidator = new EnterpriseDocumentInfoValidator();
        enterpriseDocumentInfoValidator.setEnterpriseId(user.getOrganization());
        enterpriseDocumentInfoValidator.setBusinessType(type);
        enterpriseDocumentInfoValidator.setDocStatus("2");
        ResponseData<List<EnterpriseDocumentInfoValidator>> signList = messageSignService.findMessageSign(enterpriseDocumentInfoValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(signList.getCode())) {
            view.addObject("error", signList.getCode() + ":" + signList.getMessage());
            return view;
        }

        //查询企业下得所有WEB业务账号
        AccountBasicInfoValidator accountBasicInfoValidator = new AccountBasicInfoValidator();
        accountBasicInfoValidator.setBusinessType(type);
        accountBasicInfoValidator.setEnterpriseId(user.getOrganization());
        accountBasicInfoValidator.setAccountStatus("1");//正常
        ResponseData<List<AccountBasicInfoValidator>> info = businessAccountService.findBusinessAccount(accountBasicInfoValidator);
        if (ResponseCode.SUCCESS.getCode().equals(info.getCode()) && !StringUtils.isEmpty(info.getData()) && info.getData().size()>0) {
            view.addObject("infoTypeList", info.getData());
        }

        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "add");
        view.addObject("accountTemplateInfoValidator", accountTemplateInfoValidator);
        view.addObject("signList", signList.getData());
        view.addObject("type", type);

        return view;

    }

    /**
     * 编辑
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("template/message_template_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //修改
        ResponseData<AccountTemplateInfoValidator> data = messageTemplateService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        };

        //查询签名
        EnterpriseDocumentInfoValidator enterpriseDocumentInfoValidator = new EnterpriseDocumentInfoValidator();
        enterpriseDocumentInfoValidator.setEnterpriseId(user.getOrganization());
        enterpriseDocumentInfoValidator.setBusinessType(data.getData().getTemplateType());
        enterpriseDocumentInfoValidator.setDocStatus("2");
        ResponseData<List<EnterpriseDocumentInfoValidator>> signList = messageSignService.findMessageSign(enterpriseDocumentInfoValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(signList.getCode())) {
            view.addObject("error", signList.getCode() + ":" + signList.getMessage());
            return view;
        }

        //查询企业下得所有WEB业务账号
        AccountBasicInfoValidator accountBasicInfoValidator = new AccountBasicInfoValidator();
        accountBasicInfoValidator.setBusinessType(data.getData().getTemplateType());
        accountBasicInfoValidator.setEnterpriseId(user.getOrganization());
        accountBasicInfoValidator.setAccountStatus("1");//正常
        ResponseData<List<AccountBasicInfoValidator>> info = businessAccountService.findBusinessAccount(accountBasicInfoValidator);
        if (ResponseCode.SUCCESS.getCode().equals(info.getCode()) && !StringUtils.isEmpty(info.getData()) && info.getData().size()>0) {
            view.addObject("infoTypeList", info.getData());
        }

        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "edit");
        view.addObject("accountTemplateInfoValidator", data.getData());
        view.addObject("type", data.getData().getTemplateType());
        view.addObject("signList", signList.getData());

        return view;

    }

    /**
     * 保存
     * @param accountTemplateInfoValidator
     * @param result
     * @param op
     * @param request
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated AccountTemplateInfoValidator accountTemplateInfoValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("template/message_template_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        if(StringUtils.isEmpty(accountTemplateInfoValidator.getSignName())){
            // 提交前台错误提示
            FieldError err = new FieldError("签名", "signName", "签名不能为空");
            result.addError(err);
        }
        if(StringUtils.isEmpty(accountTemplateInfoValidator.getBusinessAccount())){
            // 提交前台错误提示
            FieldError err = new FieldError("业务账号", "businessAccount", "业务账号不能为空");
            result.addError(err);
        }

        //重新查签名，重新组内容
        //去除转义
        String content = StringEscapeUtils.unescapeHtml4(accountTemplateInfoValidator.getTemplateContent());
        content = content.replace("\n","");
        content = content.replaceFirst("\\【.*?\\】", "");
        content = "【"+accountTemplateInfoValidator.getSignName()+"】"+content;
        if(content.length()>2000){
            // 提交前台错误提示
            FieldError err = new FieldError("模板内容", "templateContent", "模板内容不能大于2000个字符");
            result.addError(err);
        }

        //完成参数规则验证
        if (result.hasErrors()) {
            //查询企业下得所有WEB业务账号
            AccountBasicInfoValidator accountBasicInfoValidator = new AccountBasicInfoValidator();
            accountBasicInfoValidator.setBusinessType(accountTemplateInfoValidator.getTemplateType());
            accountBasicInfoValidator.setEnterpriseId(user.getOrganization());
            accountBasicInfoValidator.setAccountStatus("1");//正常
            ResponseData<List<AccountBasicInfoValidator>> info = businessAccountService.findBusinessAccount(accountBasicInfoValidator);
            if (ResponseCode.SUCCESS.getCode().equals(info.getCode()) && !StringUtils.isEmpty(info.getData()) && info.getData().size()>0) {
                view.addObject("infoTypeList", info.getData());
            }

            //查询签名
            EnterpriseDocumentInfoValidator enterpriseDocumentInfoValidator = new EnterpriseDocumentInfoValidator();
            enterpriseDocumentInfoValidator.setEnterpriseId(user.getOrganization());
            enterpriseDocumentInfoValidator.setBusinessType(accountTemplateInfoValidator.getTemplateType());
            enterpriseDocumentInfoValidator.setDocStatus("2");
            ResponseData<List<EnterpriseDocumentInfoValidator>> signList = messageSignService.findMessageSign(enterpriseDocumentInfoValidator);
            view.addObject("accountTemplateInfoValidator", accountTemplateInfoValidator);
            view.addObject("signList", signList.getData());
            view.addObject("op", op);
            return view;
        }

        //查询信息
        ResponseData<AccountTemplateInfoValidator> infoDate = messageTemplateService.findById(accountTemplateInfoValidator.getTemplateId());
        if (ResponseCode.SUCCESS.getCode().equals(infoDate.getCode()) && !StringUtils.isEmpty(infoDate.getData())) {
            if("2".equals(infoDate.getData().getTemplateStatus())){
                view.addObject("error", "已审核通过，不能进行修改！");
                return view;
            }
        }

        //查询账号
        ResponseData<AccountBasicInfoValidator> account = businessAccountService.findById(accountTemplateInfoValidator.getBusinessAccount());
        if (ResponseCode.SUCCESS.getCode().equals(account.getCode())) {
            accountTemplateInfoValidator.setInfoType(account.getData().getInfoType());
        }

        //查询签名
        /*ResponseData<EnterpriseDocumentInfoValidator> signInfo = businessAccountService.findSignByEnterpriseIdAndSignNameAndBusinessType(user.getOrganization(),accountTemplateInfoValidator.getSignName(),account.getData().getBusinessType());
        if (!ResponseCode.SUCCESS.getCode().equals(signInfo.getCode())) {
            view.addObject("error", signInfo.getCode() + ":" + signInfo.getMessage());
            return view;
        }*/


        accountTemplateInfoValidator.setTemplateContent(content);

        //默认需要审核
        accountTemplateInfoValidator.setTemplateStatus("3");
        //查询账号接口信息
        ResponseData<AccountInterfaceInfoValidator> accountInterfaceInfo = businessAccountService.findAccountInterfaceByAccountId(accountTemplateInfoValidator.getBusinessAccount());
        if (ResponseCode.SUCCESS.getCode().equals(accountInterfaceInfo.getCode()) && !StringUtils.isEmpty(accountInterfaceInfo.getData())) {
            if("0".equals(accountInterfaceInfo.getData().getExecuteCheck())){
                accountTemplateInfoValidator.setTemplateStatus("2");
            }
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
        accountTemplateInfoValidator.setEnterpriseId(user.getOrganization());
        ResponseData data = messageTemplateService.save(accountTemplateInfoValidator,op,user.getId());
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("TEMPLATE_INFO", accountTemplateInfoValidator.getTemplateId(), "add".equals(op) ? accountTemplateInfoValidator.getCreatedBy() : accountTemplateInfoValidator.getUpdatedBy(), op, "add".equals(op) ? "添加模板" : "修改模板", JSON.toJSONString(accountTemplateInfoValidator));
        }

        //记录日志
        log.info("[模板管理][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(accountTemplateInfoValidator));

        view.setView(new RedirectView("/template/list/"+accountTemplateInfoValidator.getTemplateType(), true, false));
        return view;

    }

    /**
     * 删除信息
     *
     * @return
     */
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public ModelAndView deleteById(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("template/message_template_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询信息
        ResponseData<AccountTemplateInfoValidator> infoDate = messageTemplateService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(infoDate.getCode())) {
            view.addObject("error", infoDate.getCode() + ":" + infoDate.getMessage());
            return view;
        }

        //查询信息
        if (ResponseCode.SUCCESS.getCode().equals(infoDate.getCode()) && !StringUtils.isEmpty(infoDate.getData())) {
            if("2".equals(infoDate.getData().getTemplateStatus())){
                view.addObject("error", "已审核通过，不能进行删除！");
                return view;
            }
        }

        //删除操作
        ResponseData data = messageTemplateService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("TEMPLATE_INFO", infoDate.getData().getTemplateId(), user.getRealName(), "delete", "删除模板" , JSON.toJSONString(infoDate.getData()));
        }

        //记录日志
        log.info("[模板管理][{}][{}]数据:{}", "delete", user.getUserName(), JSON.toJSONString(infoDate.getData()));

        view.setView(new RedirectView("/template/list/"+infoDate.getData().getTemplateType(), true, false));
        return view;
    }

    /**
     * 显示详情
     * @return
     */
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public ModelAndView detail(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("template/message_template_detail");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询信息
        ResponseData<AccountTemplateInfoValidator> infoDate = messageTemplateService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(infoDate.getCode())) {
            view.addObject("error", infoDate.getCode() + ":" + infoDate.getMessage());
            return view;
        }

        //查询审核记录
        ResponseData<List<FlowApproveValidator>> checkRecordData = flowApproveService.checkRecord(infoDate.getData().getTemplateId());
        if (!ResponseCode.SUCCESS.getCode().equals(checkRecordData.getCode())) {
            view.addObject("error", checkRecordData.getCode() + ":" + checkRecordData.getMessage());
            return view;
        }

        view.addObject("accountTemplateInfoValidator", infoDate.getData());
        view.addObject("checkRecord", checkRecordData.getData());

        return view;

    }

    /**
     * 模版分页查询，用于群发时模版选择
     */
    @RequestMapping(value = "/templateAjax", method = RequestMethod.POST)
    public JSONObject templateAjax(@RequestBody JSONObject queryCondition, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        List<AccountTemplateInfoValidator> list = new ArrayList<AccountTemplateInfoValidator>();

        String currentPage = "1";
        Integer pageSize = 6;
        String templateId = null;
        String templateFlag = null;
        String keyword = null;
        String bussinessType = null;
        String businessAccount = null;
        if(queryCondition!=null){
            templateId = queryCondition.getString("templateId");
            templateFlag = queryCondition.getString("templateFlag");
            keyword = queryCondition.getString("keyword");
            currentPage = queryCondition.getString("currentPage");
            bussinessType = queryCondition.getString("businessType");
            businessAccount = queryCondition.getString("businessAccount");
        }

        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");

        if(!StringUtils.isEmpty(templateId)){
            ResponseData<AccountTemplateInfoValidator> data = messageTemplateService.findById(templateId);
            if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
                list.add(data.getData());
            }

            result.put("list", list);
            result.put("pages", 1);
            result.put("currentPage", 1);
            return result;
        }

        AccountTemplateInfoValidator messageTemplateValidator = new AccountTemplateInfoValidator();

        //查询账号
        ResponseData<AccountBasicInfoValidator> account = businessAccountService.findById(businessAccount);
        if (ResponseCode.SUCCESS.getCode().equals(account.getCode())) {
            messageTemplateValidator.setInfoType(account.getData().getInfoType());
        }

        PageParams<AccountTemplateInfoValidator> pageParams = new PageParams<AccountTemplateInfoValidator>();
        pageParams.setPageSize(pageSize);
        pageParams.setCurrentPage(new Integer(currentPage));

        messageTemplateValidator.setEnterpriseId(user.getOrganization());
        messageTemplateValidator.setTemplateType(bussinessType);
        messageTemplateValidator.setTemplateFlag(templateFlag);
        messageTemplateValidator.setTemplateStatus("2");
        messageTemplateValidator.setTemplateContent(keyword);
        messageTemplateValidator.setTemplateAgreementType("SERVICE_WEB");
        pageParams.setParams(messageTemplateValidator);
        ResponseData<PageList<AccountTemplateInfoValidator>> data = messageTemplateService.page(pageParams);
        list = data.getData().getList();

        //以空格代替换行，避免字数不一致
        /*for(AccountTemplateInfoValidator tmp:list){
            String content_tmp = tmp.getTemplateContent();
            if(content_tmp.indexOf("\r\n")!=-1){
                tmp.setTemplateContent(content_tmp.replaceAll("\r\n", " "));
            }
        }*/

        result.put("list", list);
        result.put("pages", data.getData().getPageParams().getPages());
        result.put("currentPage", data.getData().getPageParams().getCurrentPage());
        return result;
    }
}
