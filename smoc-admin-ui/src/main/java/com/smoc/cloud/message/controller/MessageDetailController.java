package com.smoc.cloud.message.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.message.service.MessageDetailInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 消息明细
 **/
@Controller
@RequestMapping("/message/detail")
public class MessageDetailController {

    @Autowired
    private MessageDetailInfoService messageDetailInfoService;

    /**
     * 消息明细查询
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("message/message_detail_list");

        //初始化数据
        PageParams<MessageDetailInfoValidator> params = new PageParams<>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        MessageDetailInfoValidator messageDetailInfoValidator = new MessageDetailInfoValidator();
        Date startDate = DateTimeUtils.getFirstMonth(1);
        messageDetailInfoValidator.setStartDate(DateTimeUtils.getDateFormat(startDate));
        messageDetailInfoValidator.setEndDate(DateTimeUtils.getDateFormat(new Date()));
        params.setParams(messageDetailInfoValidator);

        //查询
        ResponseData<PageList<MessageDetailInfoValidator>> data = messageDetailInfoService.tableStorePage(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("messageDetailInfoValidator", messageDetailInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * 消息明细分页查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute MessageDetailInfoValidator messageDetailInfoValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("message/message_detail_list");

        //日期格式
        if (!StringUtils.isEmpty(messageDetailInfoValidator.getStartDate())) {
            String[] date = messageDetailInfoValidator.getStartDate().split(" - ");
            messageDetailInfoValidator.setStartDate(StringUtils.trimWhitespace(date[0]));
            messageDetailInfoValidator.setEndDate(StringUtils.trimWhitespace(date[1]));
        }

        //分页查询
        pageParams.setParams(messageDetailInfoValidator);

        ResponseData<PageList<MessageDetailInfoValidator>> data = messageDetailInfoService.tableStorePage(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("messageDetailInfoValidator", messageDetailInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;

    }
}
