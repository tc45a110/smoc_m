package com.smoc.cloud.material.controller;


import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.message.MessageAccountValidator;
import com.smoc.cloud.common.smoc.message.model.StatisticMessageSend;
import com.smoc.cloud.common.smoc.template.MessageWebTaskInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.material.service.BusinessAccountService;
import com.smoc.cloud.message.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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
     * 多媒体短信概览
     *
     * @return
     */
    @RequestMapping(value = "/multisms/overview", method = RequestMethod.GET)
    public ModelAndView multisms( HttpServletRequest request) {
        ModelAndView view = new ModelAndView("index");
        view.addObject("businessTypeName", "多媒体短信");
        return view;
    }

    /**
     * 彩信短信概览
     *
     * @return
     */
    @RequestMapping(value = "/mms/overview", method = RequestMethod.GET)
    public ModelAndView mms(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("index");
        view.addObject("businessTypeName", "彩信");
        return view;
    }

    /**
     * 5G短信概览
     *
     * @return
     */
    @RequestMapping(value = "/sms5g/overview", method = RequestMethod.GET)
    public ModelAndView sms5g(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("index");
        view.addObject("businessTypeName", "5G短信");
        return view;
    }

    /**
     * 国际短信概览
     *
     * @return
     */
    @RequestMapping(value = "/international/overview", method = RequestMethod.GET)
    public ModelAndView international(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("index");
        view.addObject("businessTypeName", "国际短信");
        return view;
    }

    /**
     * 过黑服务概览
     *
     * @return
     */
    @RequestMapping(value = "/blackservice/overview", method = RequestMethod.GET)
    public ModelAndView blackservice(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("index");
        view.addObject("businessTypeName", "过黑服务");
        return view;
    }
}
