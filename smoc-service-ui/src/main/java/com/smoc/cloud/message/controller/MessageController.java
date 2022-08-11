package com.smoc.cloud.message.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.book.service.BookService;
import com.smoc.cloud.book.service.GroupService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBookInfoValidator;
import com.smoc.cloud.common.smoc.filter.FilterGroupListValidator;
import com.smoc.cloud.common.smoc.message.model.MessageTaskDetail;
import com.smoc.cloud.common.smoc.template.AccountTemplateInfoValidator;
import com.smoc.cloud.common.smoc.message.MessageWebTaskInfoValidator;
import com.smoc.cloud.common.smoc.utils.MessageUtil;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.material.service.BusinessAccountService;
import com.smoc.cloud.material.service.MessageTemplateService;
import com.smoc.cloud.material.service.SequenceService;
import com.smoc.cloud.message.service.EnterpriseService;
import com.smoc.cloud.message.service.MessageService;
import com.smoc.cloud.message.utils.SendMessage;
import com.smoc.cloud.properties.MessageProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * 短信发送
 */
@Slf4j
@RestController
@RequestMapping("/message")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class MessageController {

    @Autowired
    private BusinessAccountService businessAccountService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageTemplateService messageTemplateService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    @Autowired
    private MessageProperties smocProperties;

    @Autowired
    private GroupService groupService;

    @Autowired
    private BookService bookService;

    @Autowired
    private EnterpriseService enterpriseService;

    /**
     * 短信发送列表
     * @param request
     * @return
     */
    @RequestMapping(value = "list/{businessType}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String businessType, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("message/message_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //初始化数据
        PageParams<MessageWebTaskInfoValidator> params = new PageParams<MessageWebTaskInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        MessageWebTaskInfoValidator messageWebTaskInfoValidator = new MessageWebTaskInfoValidator();
        messageWebTaskInfoValidator.setEnterpriseId(user.getOrganization());
        messageWebTaskInfoValidator.setBusinessType(businessType);
        messageWebTaskInfoValidator.setMessageType("1");
        messageWebTaskInfoValidator.setProtocolType("WEB");
        Date startDate = DateTimeUtils.getFirstMonth(6);
        messageWebTaskInfoValidator.setStartDate(DateTimeUtils.getDateFormat(startDate));
        messageWebTaskInfoValidator.setEndDate(DateTimeUtils.getDateFormat(new Date()));
        params.setParams(messageWebTaskInfoValidator);

        //查询
        ResponseData<PageList<MessageWebTaskInfoValidator>> data = messageService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("messageWebTaskInfoValidator", messageWebTaskInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("businessType", businessType);
        return view;
    }

    /**
     * 短信发送分页
     * @param request
     * @return
     */
    @RequestMapping(value = "page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute MessageWebTaskInfoValidator messageWebTaskInfoValidator,PageParams pageParams,HttpServletRequest request) {
        ModelAndView view = new ModelAndView("message/message_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //分页查询
        messageWebTaskInfoValidator.setProtocolType("WEB");
        messageWebTaskInfoValidator.setMessageType("1");
        messageWebTaskInfoValidator.setEnterpriseId(user.getOrganization());
        if (!StringUtils.isEmpty(messageWebTaskInfoValidator.getStartDate())) {
            String[] date = messageWebTaskInfoValidator.getStartDate().split(" - ");
            messageWebTaskInfoValidator.setStartDate(StringUtils.trimWhitespace(date[0]));
            messageWebTaskInfoValidator.setEndDate(StringUtils.trimWhitespace(date[1]));
        }
        pageParams.setParams(messageWebTaskInfoValidator);

        //查询
        ResponseData<PageList<AccountTemplateInfoValidator>> data = messageService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("messageWebTaskInfoValidator", messageWebTaskInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("signType", messageWebTaskInfoValidator.getInfoType());
        view.addObject("businessType", messageWebTaskInfoValidator.getBusinessType());
        return view;
    }

    /**
     * 添加
     * @return
     */
    @RequestMapping(value = "/add/{businessType}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String businessType,HttpServletRequest request) {
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("message/message_edit");

        //初始化参数
        MessageWebTaskInfoValidator messageWebTaskInfoValidator = new MessageWebTaskInfoValidator();
        //messageWebTaskInfoValidator.setId("TASK"+ sequenceService.findSequence("TASK"));
        messageWebTaskInfoValidator.setEnterpriseId(user.getOrganization());
        messageWebTaskInfoValidator.setBusinessType(businessType);
        messageWebTaskInfoValidator.setSendType("1");
        messageWebTaskInfoValidator.setMessageType("1");
        messageWebTaskInfoValidator.setSendStatus("02");
        messageWebTaskInfoValidator.setUpType("1");

        //查询企业下得所有WEB业务账号
        AccountBasicInfoValidator accountBasicInfoValidator = new AccountBasicInfoValidator();
        accountBasicInfoValidator.setBusinessType(businessType);
        accountBasicInfoValidator.setEnterpriseId(user.getOrganization());
        accountBasicInfoValidator.setAccountStatus("1");//正常
        ResponseData<List<AccountBasicInfoValidator>> info = businessAccountService.findBusinessAccount(accountBasicInfoValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(info.getCode())) {
            view.addObject("error", info.getCode() + ":" + info.getMessage());
            return view;
        }

        //根据父id查询群组
        ResponseData<List<FilterGroupListValidator>> data = groupService.findByParentId(user.getOrganization(),user.getOrganization());
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("groupList", data.getData());
        }

        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "add");
        view.addObject("messageWebTaskInfoValidator", messageWebTaskInfoValidator);
        view.addObject("list", info.getData());
        view.addObject("businessType", businessType);


        return view;

    }

    /**
     * 编辑
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("message/message_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //修改
        ResponseData<MessageWebTaskInfoValidator> data = messageService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }

        //查询企业下得所有WEB业务账号
        AccountBasicInfoValidator accountBasicInfoValidator = new AccountBasicInfoValidator();
        accountBasicInfoValidator.setBusinessType(data.getData().getBusinessType());
        accountBasicInfoValidator.setEnterpriseId(user.getOrganization());
        accountBasicInfoValidator.setAccountStatus("1");//正常
        ResponseData<List<AccountBasicInfoValidator>> info = businessAccountService.findBusinessAccount(accountBasicInfoValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(info.getCode())) {
            view.addObject("error", info.getCode() + ":" + info.getMessage());
            return view;
        }

        //根据父id查询群组
        ResponseData<List<FilterGroupListValidator>> groupData = groupService.findByParentId(user.getOrganization(),user.getOrganization());
        if (ResponseCode.SUCCESS.getCode().equals(groupData.getCode())) {
            view.addObject("groupList", groupData.getData());
        }

        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "edit");
        view.addObject("messageWebTaskInfoValidator", data.getData());
        view.addObject("list", info.getData());
        view.addObject("businessType", data.getData().getBusinessType());

        return view;

    }

    /**
     * 短信发送
     * @param messageWebTaskInfoValidator
     * @param result
     * @param op
     * @param request
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated MessageWebTaskInfoValidator messageWebTaskInfoValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("message/message_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        if("2".equals(messageWebTaskInfoValidator.getSendType()) && StringUtils.isEmpty(messageWebTaskInfoValidator.getTimingTime())){
            // 提交前台错误提示
            FieldError err = new FieldError("发送方式", "sendType", "定时时间不能为空");
            result.addError(err);
        }

        //变量类型
        if("2".equals(messageWebTaskInfoValidator.getMessageType())){
            messageWebTaskInfoValidator.setUpType("2");
        }

        if("1".equals(messageWebTaskInfoValidator.getUpType()) && StringUtils.isEmpty(messageWebTaskInfoValidator.getInputNumber())){
            // 提交前台错误提示
            FieldError err = new FieldError("手机号", "upType", "群发号码不能为空");
            result.addError(err);
        }
        if("2".equals(messageWebTaskInfoValidator.getUpType()) && StringUtils.isEmpty(messageWebTaskInfoValidator.getNumberFiles())){
            // 提交前台错误提示
            FieldError err = new FieldError("手机号", "upType", "群发号码不能为空");
            result.addError(err);
        }
        if("3".equals(messageWebTaskInfoValidator.getUpType()) && StringUtils.isEmpty(messageWebTaskInfoValidator.getGroupId())){
            // 提交前台错误提示
            FieldError err = new FieldError("手机号", "upType", "群发号码不能为空");
            result.addError(err);
        }

        //查询通讯录是否有联系人
        if("3".equals(messageWebTaskInfoValidator.getUpType()) && !StringUtils.isEmpty(messageWebTaskInfoValidator.getGroupId())){
            //初始化数据
            PageParams<EnterpriseBookInfoValidator> params = new PageParams<EnterpriseBookInfoValidator>();
            params.setPageSize(10);
            params.setCurrentPage(1);
            EnterpriseBookInfoValidator enterpriseBookInfoValidator = new EnterpriseBookInfoValidator();
            enterpriseBookInfoValidator.setGroupId(messageWebTaskInfoValidator.getGroupId());
            enterpriseBookInfoValidator.setEnterpriseId(user.getOrganization());
            params.setParams(enterpriseBookInfoValidator);
            ResponseData<PageList<EnterpriseBookInfoValidator>> data = bookService.page(params);
            if (StringUtils.isEmpty(data.getData())|| data.getData().getList().size()<=0) {
                // 提交前台错误提示
                FieldError err = new FieldError("手机号", "upType", "通讯录暂无联系人");
                result.addError(err);
            }
        }

        //查询是否是自己企业任务
       if(!user.getOrganization().equals(messageWebTaskInfoValidator.getEnterpriseId())){
           view.addObject("error", "无法进行操作");
           return view;
       }

        //完成参数规则验证
        if (result.hasErrors()) {
            //查询企业下得所有WEB业务账号
            AccountBasicInfoValidator accountBasicInfoValidator = new AccountBasicInfoValidator();
            accountBasicInfoValidator.setBusinessType(messageWebTaskInfoValidator.getBusinessType());
            accountBasicInfoValidator.setEnterpriseId(user.getOrganization());
            accountBasicInfoValidator.setAccountStatus("1");//正常
            ResponseData<List<AccountBasicInfoValidator>> info = businessAccountService.findBusinessAccount(accountBasicInfoValidator);
            if (!ResponseCode.SUCCESS.getCode().equals(info.getCode())) {
                view.addObject("error", info.getCode() + ":" + info.getMessage());
                return view;
            }

            //根据父id查询群组
            ResponseData<List<FilterGroupListValidator>> data = groupService.findByParentId(user.getOrganization(),user.getOrganization());
            if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
                view.addObject("groupList", data.getData());
            }

            view.addObject("messageWebTaskInfoValidator", messageWebTaskInfoValidator);
            view.addObject("list", info.getData());
            view.addObject("signType", messageWebTaskInfoValidator.getInfoType());
            view.addObject("businessType", messageWebTaskInfoValidator.getBusinessType());
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            messageWebTaskInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            messageWebTaskInfoValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            messageWebTaskInfoValidator.setUpdatedTime(new Date());
            messageWebTaskInfoValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //手动输入
        if("1".equals(messageWebTaskInfoValidator.getUpType())){
            messageWebTaskInfoValidator.setGroupId("");
            messageWebTaskInfoValidator.setNumberFiles("");
            messageWebTaskInfoValidator.setInputNumber(messageWebTaskInfoValidator.getInputNumber().replace("\r\n",""));
        }
        //上传附件
        if("2".equals(messageWebTaskInfoValidator.getUpType())){
            messageWebTaskInfoValidator.setInputNumber("");
            messageWebTaskInfoValidator.setGroupId("");
        }
        //通讯录
        if("3".equals(messageWebTaskInfoValidator.getUpType())){
            messageWebTaskInfoValidator.setInputNumber("");
            messageWebTaskInfoValidator.setNumberFiles("");
        }

        //重新查询模板内容
        ResponseData<AccountTemplateInfoValidator> templateInfo = messageTemplateService.findById(messageWebTaskInfoValidator.getTemplateId());
        messageWebTaskInfoValidator.setMessageContent(templateInfo.getData().getTemplateContent());

        //保存操作
        ResponseData data = messageService.save(messageWebTaskInfoValidator,op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("MESSAGE_SEND", messageWebTaskInfoValidator.getId(),"add".equals(op) ? messageWebTaskInfoValidator.getCreatedBy() : messageWebTaskInfoValidator.getUpdatedBy(), op, "add".equals(op) ? "添加群发短信" : "修改群发短信",JSON.toJSONString(messageWebTaskInfoValidator));
        }

        //记录日志
        log.info("[短信群发][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(messageWebTaskInfoValidator));

        if("1".equals(messageWebTaskInfoValidator.getMessageType())){
            view.setView(new RedirectView("/message/list/"+messageWebTaskInfoValidator.getBusinessType(), true, false));
        }else{
            view.setView(new RedirectView("/message/variable/list/"+messageWebTaskInfoValidator.getBusinessType(), true, false));
        }

        return view;

    }

    /**
     * 删除信息
     *
     * @return
     */
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public ModelAndView deleteById(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("message/message_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询信息
        ResponseData<MessageWebTaskInfoValidator> infoData = messageService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(infoData.getCode())) {
            view.addObject("error", infoData.getCode() + ":" + infoData.getMessage());
            return view;
        }

        //查看是否是自己企业
        if(!user.getOrganization().equals(infoData.getData().getEnterpriseId())){
            view.addObject("error", "不能进行删除操作！");
            return view;
        }

        if(!"05".equals(infoData.getData().getSendStatus())){
            view.addObject("error", "不能进行删除操作！");
            return view;
        }

        //删除操作
        ResponseData data = messageService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("MESSAGE_SEND", infoData.getData().getId(),user.getRealName(), "delete", "删除群发短信",JSON.toJSONString(infoData.getData()));
        }

        //记录日志
        log.info("[短信群发][{}][{}]数据:{}", "delete", user.getUserName(), JSON.toJSONString(infoData.getData()));

        if("1".equals(infoData.getData().getMessageType())){
            view.setView(new RedirectView("/message/list/"+infoData.getData().getBusinessType(), true, false));
        }else{
            view.setView(new RedirectView("/message/variable/list/"+infoData.getData().getBusinessType(), true, false));
        }

        return view;
    }

    /**
     * 发送
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/sendMessage/{id}", method = RequestMethod.GET)
    public ModelAndView sendMessage(@PathVariable String id, HttpServletRequest request) {
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("message/message_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询信息
        ResponseData<MessageWebTaskInfoValidator> infoData = messageService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(infoData.getCode())) {
            view.addObject("error", infoData.getCode() + ":" + infoData.getMessage());
        }

        //查看是否是自己企业
        if(!user.getOrganization().equals(infoData.getData().getEnterpriseId())){
            view.addObject("error", "不能进行操作！");
            return view;
        }

        //如果已经发送完成，不能点终止
        if(MessageUtil.MessageTaskStatus_finish.equals(infoData.getData().getSendStatus())){
            view.addObject("error", "不能进行操作！");
            return view;
        }

        //发送操作
        ResponseData data = messageService.sendMessageById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("MESSAGE_SEND", infoData.getData().getId(),user.getRealName(), "send", "发送群发短信",JSON.toJSONString(infoData.getData()));
        }

        //记录日志
        log.info("[短信群发][{}][{}]数据:{}", "send", user.getUserName(), JSON.toJSONString(infoData.getData()));

        if("1".equals(infoData.getData().getMessageType())){
            view.setView(new RedirectView("/message/list/"+infoData.getData().getBusinessType(), true, false));
        }else{
            view.setView(new RedirectView("/message/variable/list/"+infoData.getData().getBusinessType(), true, false));
        }
        return view;

    }

    /**
     * 查看明细
     *
     * @return
     */
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public ModelAndView detail(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("message/message_detail_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询信息
        ResponseData<MessageWebTaskInfoValidator> infoData = messageService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(infoData.getCode())) {
            view.addObject("error", infoData.getCode() + ":" + infoData.getMessage());
            return view;
        }

        //查看是否是自己企业
        if(!user.getOrganization().equals(infoData.getData().getEnterpriseId())){
            view.addObject("error", "无法查看！");
            return view;
        }

        //查询企业，获取标识
        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(user.getOrganization());
        if (!ResponseCode.SUCCESS.getCode().equals(enterpriseData.getCode())) {
            view.addObject("error", enterpriseData.getCode() + ":" + enterpriseData.getMessage());
            return view;
        }

        //初始化数据
        PageParams<MessageTaskDetail> params = new PageParams<MessageTaskDetail>();
        params.setPageSize(20);
        params.setCurrentPage(1);
        MessageTaskDetail messageTaskDetail = new MessageTaskDetail();
        messageTaskDetail.setTaskId(id);
        messageTaskDetail.setEnterpriseFlag(enterpriseData.getData().getEnterpriseFlag().toLowerCase());
        params.setParams(messageTaskDetail);

        //查询
        ResponseData<PageList<MessageTaskDetail>> data = messageService.webTaskDetailList(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("messageTaskDetail", messageTaskDetail);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("messageWebTaskInfoValidator", infoData.getData());

        return view;
    }

    /**
     * 查看明细分页
     * @param messageTaskDetail
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/detail/page", method = RequestMethod.POST)
    public ModelAndView detailPage(@ModelAttribute MessageTaskDetail messageTaskDetail,PageParams params,HttpServletRequest request) {
        ModelAndView view = new ModelAndView("message/message_detail_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //查询信息
        ResponseData<MessageWebTaskInfoValidator> infoData = messageService.findById(messageTaskDetail.getTaskId());
        if (!ResponseCode.SUCCESS.getCode().equals(infoData.getCode())) {
            view.addObject("error", infoData.getCode() + ":" + infoData.getMessage());
            return view;
        }

        //查看是否是自己企业
        if(!user.getOrganization().equals(infoData.getData().getEnterpriseId())){
            view.addObject("error", "无法查看！");
            return view;
        }

        //查询企业，获取标识
        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(user.getOrganization());
        if (!ResponseCode.SUCCESS.getCode().equals(enterpriseData.getCode())) {
            view.addObject("error", enterpriseData.getCode() + ":" + enterpriseData.getMessage());
            return view;
        }

        //查询
        messageTaskDetail.setEnterpriseFlag(enterpriseData.getData().getEnterpriseFlag().toLowerCase());
        params.setParams(messageTaskDetail);
        ResponseData<PageList<MessageTaskDetail>> data = messageService.webTaskDetailList(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("messageTaskDetail", messageTaskDetail);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("messageWebTaskInfoValidator", infoData.getData());
        return view;
    }

    /**
     * 导出明细
     *
     * @return
     */
    @RequestMapping(value = "/expMessage", method = RequestMethod.POST)
    public void expMessage(String taskId, HttpServletRequest request, HttpServletResponse response) {

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(taskId);
        if (!MpmValidatorUtil.validate(validator)) {
            return ;
        }

        //查询信息
        ResponseData<MessageWebTaskInfoValidator> infoData = messageService.findById(taskId);
        if (!ResponseCode.SUCCESS.getCode().equals(infoData.getCode())) {
            return ;
        }

        //查看是否是自己企业
        if(!user.getOrganization().equals(infoData.getData().getEnterpriseId())){
            return ;
        }

        //查询企业，获取标识
        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(user.getOrganization());
        if (!ResponseCode.SUCCESS.getCode().equals(enterpriseData.getCode())) {
            return ;
        }

        //初始化数据
        PageParams<MessageTaskDetail> params = new PageParams<MessageTaskDetail>();
        params.setPageSize(1000000);
        params.setCurrentPage(1);
        MessageTaskDetail messageTaskDetail = new MessageTaskDetail();
        messageTaskDetail.setTaskId(taskId);
        messageTaskDetail.setEnterpriseFlag(enterpriseData.getData().getEnterpriseFlag().toLowerCase());
        params.setParams(messageTaskDetail);

        //查询
        ResponseData<PageList<MessageTaskDetail>> data = messageService.webTaskDetailList(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return ;
        }

        ServletOutputStream outputStream = null;
        try {
            outputStream = getOutputStream(infoData.getData().getId(), response);
            ExcelWriterBuilder writeBook = EasyExcel.write(outputStream,MessageTaskDetail.class);
            ExcelWriterSheetBuilder sheet = writeBook.sheet(infoData.getData().getId());
            sheet.doWrite(data.getData().getList());
        } catch (Exception e) {
            log.error("导出excel表格失败:", e);
        }

    }

    private ServletOutputStream getOutputStream(String fileName, HttpServletResponse response) throws Exception {
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
            //设置响应的类型
            response.setContentType(MediaType.MULTIPART_FORM_DATA_VALUE);
            //设置响应的编码格式
            response.setCharacterEncoding("utf8");
            //设置响应头
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "no-store");
            response.addHeader("Cache-Control", "max-age=0");
            return response.getOutputStream();
        } catch (IOException e) {
            log.error("导出excel表格失败:", e);
            throw new Exception("导出excel表格失败!", e);
        }
    }


    /**
     * 异步上传号码
     * @param request
     * @return
     */
    @RequestMapping(value = "/uploadFile/{messageType}", method = RequestMethod.POST)
    public JSONObject uploadFile(@PathVariable String messageType, HttpServletRequest request) {

        JSONObject result = new JSONObject();
        String code = "1";
        String filePath = "";
        String sendFilePath = "";
        String errorFilePath = "";
        int sendNumber = 0;
        Long originalAttachmentSize = 0l;

        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");

        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = mRequest.getFile("file");
        if(file!=null&&file.getSize()>0){
            try {

                String nowDay = DateTimeUtils.currentDate(new Date());
                String uuid = UUID.uuid32();

                //文件格式非txt，直接返回-1，前端获取后提示用户
                if(file.getOriginalFilename().endsWith(".txt")){
                    code = "1";
                    filePath = "/" + nowDay + "/"+ user.getOrganization() +"/" + uuid + "_source.txt";
                }else if(file.getOriginalFilename().endsWith(".xls") || file.getOriginalFilename().endsWith(".xlsx")){
                    code = "1";
                    filePath = "/" + nowDay + "/"+ user.getOrganization() +"/" + uuid + "_source.xlsx";
                }else{
                    code = "-1";
                    result.put("code", code);
                    return result;
                }

                /*File desFile = new File(smocProperties.getMobileFileRootPath() + "/" + nowDay + "/"+ user.getOrganization());
                if(!desFile.getParentFile().exists()){
                    desFile.mkdirs();
                }*/
                File originalFile = new File(smocProperties.getMobileFileRootPath() + filePath);
                if(!originalFile.getParentFile().exists()){
                    //originalFile.mkdirs();
                    originalFile.getParentFile().mkdirs();
                }
                originalAttachmentSize = file.getSize();
                file.transferTo(originalFile);
/*
                //处理手机号，形成文件
                MessageWebTaskInfoValidator messageValidator = new MessageWebTaskInfoValidator();
                messageValidator.setNumberFiles(filePath);

                //普通短信
                if("1".equals(messageType)){
                    messageValidator = SendMessage.handleFileSMS(messageValidator,smocProperties,user.getOrganization());
                }

                //变量短信
                if("2".equals(messageType)){
                    messageValidator = SendMessage.handleFileVariableSMS(messageValidator,smocProperties,user.getOrganization());
                }

                errorFilePath = messageValidator.getExceptionNumberAttachment();
                sendFilePath = messageValidator.getSendNumberAttachment();
                sendNumber = messageValidator.getSubmitNumber();
                if(!StringUtils.isEmpty(errorFilePath)){
                    request.getSession().setAttribute("errorFilePath", errorFilePath);
                }*/
            } catch (Exception e) {
                e.printStackTrace();
                log.error("[短信群发][upload][{}]数据::{}", user.getUserName(), e.getMessage());
                //文件上传异常，直接返回-2，前端获取后提示用户
                code = "-2";
                result.put("code", code);
                return result;
            }
        }

        result.put("code", code);
        result.put("filePath", filePath);
        result.put("sendFilePath", sendFilePath);
        result.put("errorFilePath", errorFilePath);
        result.put("sendNumber", sendNumber);
        result.put("originalAttachmentSize", originalAttachmentSize);
        return result;
    }

    /**
     * 根据文件类型下载各类号码文件
     */
    @RequestMapping(value = "/download/{fileType}/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileType, @PathVariable String id, HttpServletRequest request) {
        ResponseEntity<byte[]> entity = null;

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        ResponseData<MessageWebTaskInfoValidator> data = messageService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return entity;
        }

        //查看任务是否属于改企业
        if(!user.getOrganization().equals(data.getData().getEnterpriseId())){
            return entity;
        }

        MessageWebTaskInfoValidator messageValidator = data.getData();

        File downloadFile = null;
        if("numberFiles".equals(fileType)){
            downloadFile = new File(smocProperties.getMobileFileRootPath() + messageValidator.getNumberFiles());
        }else if("sendNumber".equals(fileType)){
            downloadFile = new File(smocProperties.getMobileFileRootPath() + messageValidator.getSendNumberAttachment());
        }else if("exceptionFile".equals(fileType)){
            downloadFile = new File(smocProperties.getMobileFileRootPath() + messageValidator.getExceptionNumberAttachment());
        }else if("reportFile".equals(fileType)){
            downloadFile = new File(smocProperties.getMobileFileRootPath() + messageValidator.getReportAttachment());
        }else{
            return entity;
        }

        //文件不存在
        if(!downloadFile.exists()){
            log.info("[短信群发][download][{}]数据::{}", user.getUserName(), "文件不存在:"+downloadFile.getAbsolutePath());
            return entity;
        }

        //读取文件内容输入流中
        InputStream in = null;
        try {
            in = new FileInputStream(downloadFile);
            byte[] body = new byte[in.available()];
            in.read(body);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attchement;filename=" + downloadFile.getName());
            HttpStatus statusCode = HttpStatus.OK;
            entity = new ResponseEntity<byte[]>(body, headers, statusCode);
        } catch (Exception e) {
            log.error("[短信群发][download][{}]数据::{}", user.getUserName(), e.getMessage());
            e.printStackTrace();
        } finally {
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return entity;
    }

    /**
     * 异常内容文件下载
     */
    @RequestMapping(value = "/downloadErrorFile/{fileUrl}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadErrorFile(@PathVariable String fileUrl, HttpServletRequest request) {
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");

        ResponseEntity<byte[]> entity = null;

        //读取文件内容输入流中
        InputStream in = null;
        try {
            fileUrl = request.getSession().getAttribute(fileUrl).toString();

            File downloadFile = new File(smocProperties.getMobileFileRootPath() + fileUrl);

            //文件不存在
            if(!downloadFile.exists()){
                log.info("[短信群发][download][{}]数据::{}", user.getUserName(), "文件不存在:"+downloadFile.getAbsolutePath());
                return entity;
            }

            in = new FileInputStream(downloadFile);
            byte[] body = new byte[in.available()];
            in.read(body);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attchement;filename=" + downloadFile.getName());
            HttpStatus statusCode = HttpStatus.OK;
            entity = new ResponseEntity<byte[]>(body, headers, statusCode);
        } catch (Exception e) {
            log.error("[短信群发][download][{}]数据::{}", user.getUserName(), e.getMessage());
            e.printStackTrace();
        } finally {
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return entity;
    }

    /**
     * 下载txt模板
     */
    @RequestMapping(value = "/downFileTemp/{type}", method = RequestMethod.GET)
    public void downFileTemp(@PathVariable String type,HttpServletRequest request, HttpServletResponse response) {

        String fileName = "example.txt";

        if("1".equals(type)){
            fileName = "example.txt";
        }

        if("2".equals(type)){
            fileName = "example_variable.txt";
        }

        if("3".equals(type)){
            fileName = "example-variable.xlsx";
        }

        if("4".equals(type)){
            fileName = "example.xlsx";
        }


        //设置文件路径
        ClassPathResource classPathResource = new ClassPathResource("static/files/" + fileName);
        try {
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 实现文件下载
        byte[] buffer = new byte[1024];
        InputStream fis = null;
        BufferedInputStream bis = null;
        try {
            fis = classPathResource.getInputStream();;
            bis = new BufferedInputStream(fis);
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
            return ;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
