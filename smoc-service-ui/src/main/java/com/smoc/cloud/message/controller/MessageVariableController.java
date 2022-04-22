package com.smoc.cloud.message.controller;


import com.alibaba.fastjson.JSONObject;
import com.smoc.cloud.book.util.FileUtils;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.template.AccountTemplateInfoValidator;
import com.smoc.cloud.common.smoc.message.MessageWebTaskInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.material.service.BusinessAccountService;
import com.smoc.cloud.material.service.MessageTemplateService;
import com.smoc.cloud.material.service.SequenceService;
import com.smoc.cloud.message.service.MessageService;
import com.smoc.cloud.properties.MessageProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.util.*;

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
    private MessageProperties smocProperties;
    @Autowired
    private MessageTemplateService messageTemplateService;



    /**
     * 短信发送列表
     * @param request
     * @return
     */
    @RequestMapping(value = "list/{businessType}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String businessType,  HttpServletRequest request) {
        ModelAndView view = new ModelAndView("message/message_variable_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //初始化数据
        PageParams<MessageWebTaskInfoValidator> params = new PageParams<MessageWebTaskInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        MessageWebTaskInfoValidator messageWebTaskInfoValidator = new MessageWebTaskInfoValidator();
        messageWebTaskInfoValidator.setEnterpriseId(user.getOrganization());
        messageWebTaskInfoValidator.setBusinessType(businessType);
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
        ResponseData<PageList<MessageWebTaskInfoValidator>> data = messageService.page(pageParams);
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
        messageWebTaskInfoValidator.setId("TASK"+ sequenceService.findSequence("BUSINESS_ACCOUNT"));
        messageWebTaskInfoValidator.setEnterpriseId(user.getOrganization());
        messageWebTaskInfoValidator.setBusinessType(businessType);
        messageWebTaskInfoValidator.setSendType("1");
        messageWebTaskInfoValidator.setMessageType("2");
        messageWebTaskInfoValidator.setSendStatus("01");
        messageWebTaskInfoValidator.setUpType("2");

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

        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "add");
        view.addObject("messageWebTaskInfoValidator", messageWebTaskInfoValidator);
        view.addObject("list", info.getData());
        view.addObject("businessType", businessType);

        return view;

    }

    /**
     * 预览拼装后的前3条发送数据
     */
    @RequestMapping(value = "/preview", method = RequestMethod.POST)
    public @ResponseBody
    JSONObject preview(@RequestBody JSONObject queryCondition, HttpServletRequest request) {
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");
        JSONObject result = new JSONObject();
        List<String> previewList = new ArrayList<String>();

        String filePath = null;
        String templateId = null;
        if(queryCondition!=null){
            filePath = queryCondition.getString("filePath");
            templateId = queryCondition.getString("templateId");
        }
        if(StringUtils.isEmpty(filePath) || StringUtils.isEmpty(templateId)){
            result.put("previewList", previewList);
            return result;
        }

        //查询模板
        ResponseData<AccountTemplateInfoValidator> data = messageTemplateService.findById(templateId);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            result.put("previewList", previewList);
            return result;
        }
        AccountTemplateInfoValidator messageTemplateValidator = data.getData();

        BufferedReader br = null;
        File desFile = new File(smocProperties.getMobileFileRootPath() + filePath);

        String fileType = filePath.substring(filePath.lastIndexOf("."));
        if(".xls".equals(fileType) || ".xlsx".equals(fileType)){
            previewList = FileUtils.readMessageTemplateExcel(desFile);
        }else if (".txt".equals(fileType)){
            previewList = FileUtils.readMessageTemplateTxt(desFile);
        }

        for(int i=0;i<previewList.size();i++){
            String[] inputInfo = previewList.get(i).split("\\|");
            String content = messageTemplateValidator.getTemplateContent();
            for(int j=1;j<inputInfo.length;j++){
                String replaceKey = "\\$\\{"+j+"\\}";
                content = content.replaceAll(replaceKey, inputInfo[j]);
            }
            previewList.set(i, inputInfo[0]+","+content);
        }

        result.put("previewList", previewList);

        return result;
    }
}
