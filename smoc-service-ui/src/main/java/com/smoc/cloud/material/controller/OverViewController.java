package com.smoc.cloud.material.controller;


import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.message.MessageAccountValidator;
import com.smoc.cloud.common.smoc.message.model.StatisticMessageSend;
import com.smoc.cloud.common.smoc.template.AccountTemplateInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.material.service.BusinessAccountService;
import com.smoc.cloud.message.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 各种类型的概览
 */
@Slf4j
@RestController
public class OverViewController {

    @Autowired
    private BusinessAccountService businessAccountService;

    @Autowired
    private MessageService messageService;

    /**
     * 普通短信概览
     *
     * @return
     */
    @RequestMapping(value = "/{businessType}/overview", method = RequestMethod.GET)
    public ModelAndView textsms(@PathVariable String businessType, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("index");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //查询账号余额
        MessageAccountValidator messageAccountValidator = new MessageAccountValidator();
        messageAccountValidator.setEnterpriseId(user.getOrganization());
        messageAccountValidator.setBusinessType(businessType);
        ResponseData<List<MessageAccountValidator>> data = businessAccountService.messageAccountList(messageAccountValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //查询发送量
        messageAccountValidator.setStartDate(DateTimeUtils.getDateFormat(new Date()));
        messageAccountValidator.setEndDate(DateTimeUtils.getDateFormat(new Date()));
        ResponseData<StatisticMessageSend> statisticDate = messageService.statisticMessageSendCount(messageAccountValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("list", data.getData());
        view.addObject("statisticDate", statisticDate.getData());
        view.addObject("businessType", businessType);
        return view;
    }

    /**
     * 发送账号列表
     * @param businessType
     * @param request
     * @return
     */
    @RequestMapping(value = "message/account/{businessType}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String businessType, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("message/message_account_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //初始化数据
        PageParams<MessageAccountValidator> params = new PageParams<MessageAccountValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        MessageAccountValidator messageAccountValidator = new MessageAccountValidator();
        messageAccountValidator.setEnterpriseId(user.getOrganization());
        messageAccountValidator.setBusinessType(businessType);
        Date startDate = DateTimeUtils.getFirstMonth(12);
        messageAccountValidator.setStartDate(DateTimeUtils.getDateFormat(startDate));
        messageAccountValidator.setEndDate(DateTimeUtils.getDateFormat(new Date()));
        params.setParams(messageAccountValidator);

        //查询
        ResponseData<PageList<MessageAccountValidator>> data = businessAccountService.messageAccountInfoList(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("messageAccountValidator", messageAccountValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("businessType", businessType);
        return view;
    }

    /**
     * 发送账号列表分页
     * @param messageAccountValidator
     * @param pageParams
     * @param request
     * @return
     */
    @RequestMapping(value = "message/account/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute MessageAccountValidator messageAccountValidator, PageParams pageParams, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("message/message_account_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //分页查询
        messageAccountValidator.setEnterpriseId(user.getOrganization());
        if (!StringUtils.isEmpty(messageAccountValidator.getStartDate())) {
            String[] date = messageAccountValidator.getStartDate().split(" - ");
            messageAccountValidator.setStartDate(StringUtils.trimWhitespace(date[0]));
            messageAccountValidator.setEndDate(StringUtils.trimWhitespace(date[1]));
        }
        pageParams.setParams(messageAccountValidator);

        //查询
        ResponseData<PageList<MessageAccountValidator>> data = businessAccountService.messageAccountInfoList(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("messageAccountValidator", messageAccountValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("businessType", messageAccountValidator.getBusinessType());
        return view;
    }

}
