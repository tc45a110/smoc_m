package com.smoc.cloud.message.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.template.AccountTemplateInfoValidator;
import com.smoc.cloud.common.smoc.template.MessageWebTaskInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.material.service.BusinessAccountService;
import com.smoc.cloud.material.service.SequenceService;
import com.smoc.cloud.message.service.MessageService;
import com.smoc.cloud.message.utils.SendMessage;
import com.smoc.cloud.properties.SmocProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * 变量短信发送
 */
@Slf4j
@RestController
@RequestMapping("/message/variable")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class MessageVariableController {

    @Autowired
    private BusinessAccountService businessAccountService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    @Autowired
    private SmocProperties smocProperties;

    /**
     * 短信发送列表
     * @param signType
     * @param request
     * @return
     */
    @RequestMapping(value = "list/{businessType}/{signType}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String businessType, @PathVariable String signType, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("message/message_variable_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //初始化数据
        PageParams<MessageWebTaskInfoValidator> params = new PageParams<MessageWebTaskInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        MessageWebTaskInfoValidator messageWebTaskInfoValidator = new MessageWebTaskInfoValidator();
        messageWebTaskInfoValidator.setEnterpriseId(user.getOrganization());
        messageWebTaskInfoValidator.setBusinessType(businessType);
        messageWebTaskInfoValidator.setInfoType(signType);
        messageWebTaskInfoValidator.setMessageType("2");
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
        view.addObject("signType", signType);
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
        ModelAndView view = new ModelAndView("message/message_variable_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //分页查询
        messageWebTaskInfoValidator.setMessageType("2");
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
    @RequestMapping(value = "/add/{businessType}/{signType}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String businessType,@PathVariable String signType, HttpServletRequest request) {
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("message/message_edit");

        //初始化参数
        MessageWebTaskInfoValidator messageWebTaskInfoValidator = new MessageWebTaskInfoValidator();
        messageWebTaskInfoValidator.setId("TASK"+ sequenceService.findSequence("BUSINESS_ACCOUNT"));
        messageWebTaskInfoValidator.setEnterpriseId(user.getOrganization());
        messageWebTaskInfoValidator.setBusinessType(businessType);
        messageWebTaskInfoValidator.setInfoType(signType);
        messageWebTaskInfoValidator.setSendType("1");
        messageWebTaskInfoValidator.setMessageType("2");
        messageWebTaskInfoValidator.setSendStatus("05");

        //查询企业下得所有业务账号
        AccountBasicInfoValidator accountBasicInfoValidator = new AccountBasicInfoValidator();
        accountBasicInfoValidator.setBusinessType(businessType);
        accountBasicInfoValidator.setInfoType(signType);
        accountBasicInfoValidator.setEnterpriseId(user.getOrganization());
        accountBasicInfoValidator.setAccountStatus("1");//正常
        ResponseData<List<AccountBasicInfoValidator>> info = businessAccountService.findBusinessAccount(accountBasicInfoValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(info.getCode())) {
            view.addObject("error", info.getCode() + ":" + info.getMessage());
            return view;
        }

        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "add");
        view.addObject("messageWebTaskInfoValidator", messageWebTaskInfoValidator);
        view.addObject("list", info.getData());
        view.addObject("signType", signType);
        view.addObject("businessType", businessType);

        return view;

    }

}
