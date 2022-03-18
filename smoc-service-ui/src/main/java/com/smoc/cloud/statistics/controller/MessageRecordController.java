package com.smoc.cloud.statistics.controller;

import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.statistics.service.StatisticsMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 短信记录
 */
@Controller
@RequestMapping("/statistics/record")
public class MessageRecordController {

    @Autowired
    private StatisticsMessageService statisticsMessageService;

    /**
     * 短信记录列表
     * @param request
     * @return
     */
    @RequestMapping(value = "list/{businessType}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String businessType, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("statistics/message_record_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //初始化数据
        PageParams<MessageDetailInfoValidator> params = new PageParams<>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        MessageDetailInfoValidator messageDetailInfoValidator = new MessageDetailInfoValidator();
        messageDetailInfoValidator.setEnterpriseId(user.getOrganization());
        messageDetailInfoValidator.setBusinessType(businessType);
        Date startDate = DateTimeUtils.getFirstMonth(1);
        messageDetailInfoValidator.setStartDate(DateTimeUtils.getDateFormat(startDate));
        messageDetailInfoValidator.setEndDate(DateTimeUtils.getDateFormat(new Date()));
        params.setParams(messageDetailInfoValidator);

        //查询
        ResponseData<PageList<MessageDetailInfoValidator>> data = statisticsMessageService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("messageDetailInfoValidator", messageDetailInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams",params);
        view.addObject("businessType", businessType);
        return view;
    }

    /**
     * 短信记录分页
     * @param request
     * @return
     */
    @RequestMapping(value = "page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute MessageDetailInfoValidator messageDetailInfoValidator, PageParams params, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("statistics/message_record_list");
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //分页查询
        messageDetailInfoValidator.setEnterpriseId(user.getOrganization());
        messageDetailInfoValidator.setBusinessType(messageDetailInfoValidator.getBusinessType());
        if (!StringUtils.isEmpty(messageDetailInfoValidator.getStartDate())) {
            String[] date = messageDetailInfoValidator.getStartDate().split(" - ");
            messageDetailInfoValidator.setStartDate(StringUtils.trimWhitespace(date[0]));
            messageDetailInfoValidator.setEndDate(StringUtils.trimWhitespace(date[1]));
        }
        params.setParams(messageDetailInfoValidator);

        //查询
        ResponseData<PageList<MessageDetailInfoValidator>> data = statisticsMessageService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("messageDetailInfoValidator", messageDetailInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams",params);
        view.addObject("businessType", messageDetailInfoValidator.getBusinessType());
        return view;
    }
}
