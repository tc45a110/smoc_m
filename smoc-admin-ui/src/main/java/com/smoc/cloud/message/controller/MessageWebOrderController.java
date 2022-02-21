package com.smoc.cloud.message.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.template.MessageWebTaskInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.message.service.MessageWebTaskInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;


/**
 * web短消息任务单
 **/
@Slf4j
@Controller
@RequestMapping("/message/order/web")
public class MessageWebOrderController {

    @Autowired
    private MessageWebTaskInfoService messageWebTaskInfoService;

    /**
     * web短消息任务单查询
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("message/message_order_web_list");

        //初始化数据
        PageParams<MessageWebTaskInfoValidator> params = new PageParams<MessageWebTaskInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        MessageWebTaskInfoValidator messageWebTaskInfoValidator = new MessageWebTaskInfoValidator();

        Date startDate = DateTimeUtils.getFirstMonth(12);
        messageWebTaskInfoValidator.setStartDate(DateTimeUtils.getDateFormat(startDate));
        messageWebTaskInfoValidator.setEndDate(DateTimeUtils.getDateFormat(new Date()));
        params.setParams(messageWebTaskInfoValidator);

        //分页查询
        ResponseData<PageList<MessageWebTaskInfoValidator>> data = messageWebTaskInfoService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //数量统计
        ResponseData<Map<String, Object>> count = messageWebTaskInfoService.count(messageWebTaskInfoValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(count.getCode())) {
            view.addObject("error", count.getCode() + ":" + count.getMessage());
            return view;
        }

        Map<String,Object>  countMap = count.getData();
        //log.info("[map]:{}",new Gson().toJson(countMap));
        view.addObject("countMap", countMap);
        view.addObject("messageWebTaskInfoValidator", messageWebTaskInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * web短消息任务单分页查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute MessageWebTaskInfoValidator messageWebTaskInfoValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("message/message_order_web_list");


        //日期格式
        if (!StringUtils.isEmpty(messageWebTaskInfoValidator.getStartDate())) {
            String[] date = messageWebTaskInfoValidator.getStartDate().split(" - ");
            messageWebTaskInfoValidator.setStartDate(StringUtils.trimWhitespace(date[0]));
            messageWebTaskInfoValidator.setEndDate(StringUtils.trimWhitespace(date[1]));
        }

        pageParams.setParams(messageWebTaskInfoValidator);

        //log.info("[messageDailyStatisticValidator]:{}",new Gson().toJson(messageDailyStatisticValidator));
        //分页查询
        ResponseData<PageList<MessageWebTaskInfoValidator>> data = messageWebTaskInfoService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //数量统计
        ResponseData<Map<String, Object>> count = messageWebTaskInfoService.count(messageWebTaskInfoValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(count.getCode())) {
            view.addObject("error", count.getCode() + ":" + count.getMessage());
            return view;
        }

        Map<String,Object>  countMap = count.getData();
        //log.info("[map]:{}",new Gson().toJson(countMap));
        view.addObject("countMap", countMap);
        view.addObject("messageWebTaskInfoValidator", messageWebTaskInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;

    }
}
