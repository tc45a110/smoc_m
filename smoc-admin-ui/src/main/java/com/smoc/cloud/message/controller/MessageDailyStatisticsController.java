package com.smoc.cloud.message.controller;

import com.google.gson.Gson;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.message.MessageDailyStatisticValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.message.service.MessageDailyStatisticService;
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
import java.util.HashMap;
import java.util.Map;


/**
 * 消息日统计明细
 **/
@Slf4j
@Controller
@RequestMapping("/message/daily/statistics")
public class MessageDailyStatisticsController {

    @Autowired
    private MessageDailyStatisticService messageDailyStatisticService;

    /**
     * 消息日统计明细查询
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("message/message_daily_statistics_list");

        //初始化数据
        PageParams<MessageDailyStatisticValidator> params = new PageParams<MessageDailyStatisticValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        MessageDailyStatisticValidator messageDailyStatisticValidator = new MessageDailyStatisticValidator();

        Date startDate = DateTimeUtils.getFirstMonth(12);
        messageDailyStatisticValidator.setStartDate(DateTimeUtils.getDateFormat(startDate));
        messageDailyStatisticValidator.setEndDate(DateTimeUtils.getDateFormat(new Date()));
        params.setParams(messageDailyStatisticValidator);

        //查询
        ResponseData<PageList<MessageDailyStatisticValidator>> data = messageDailyStatisticService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        ResponseData<Map<String, Object>> count = messageDailyStatisticService.count(messageDailyStatisticValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(count.getCode())) {
            view.addObject("error", count.getCode() + ":" + count.getMessage());
            return view;
        }

        Map<String,Object> countMap = new HashMap<>();
        if(null != count.getData() || (count.getData().size())<1){
            countMap.put("SUCCESS_SUBMIT_NUM",0);
            countMap.put("MESSAGE_SUCCESS_NUM",0);
            countMap.put("MESSAGE_FAILURE_NUM",0);
            countMap.put("MESSAGE_NO_REPORT_NUM",0);
        }else{
            countMap = count.getData();
        }

        //log.info("countMap：{}",new Gson().toJson(countMap));

        view.addObject("countMap", countMap);
        view.addObject("messageDailyStatisticValidator", messageDailyStatisticValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * 消息日统计明细分页查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute MessageDailyStatisticValidator messageDailyStatisticValidator, PageParams pageParams) {

        ModelAndView view = new ModelAndView("message/message_daily_statistics_list");

        //日期格式
        if (!StringUtils.isEmpty(messageDailyStatisticValidator.getStartDate())) {
            String[] date = messageDailyStatisticValidator.getStartDate().split(" - ");
            messageDailyStatisticValidator.setStartDate(StringUtils.trimWhitespace(date[0]));
            messageDailyStatisticValidator.setEndDate(StringUtils.trimWhitespace(date[1]));
        }

        //分页查询
        pageParams.setParams(messageDailyStatisticValidator);

        //log.info("[messageDailyStatisticValidator]:{}",new Gson().toJson(messageDailyStatisticValidator));

        ResponseData<PageList<MessageDailyStatisticValidator>> data = messageDailyStatisticService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        ResponseData<Map<String, Object>> count = messageDailyStatisticService.count(messageDailyStatisticValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(count.getCode())) {
            view.addObject("error", count.getCode() + ":" + count.getMessage());
            return view;
        }

        Map<String,Object> countMap = new HashMap<>();
        if(null != count.getData() || (count.getData().size())<1){
            countMap.put("SUCCESS_SUBMIT_NUM",0);
            countMap.put("MESSAGE_SUCCESS_NUM",0);
            countMap.put("MESSAGE_FAILURE_NUM",0);
            countMap.put("MESSAGE_NO_REPORT_NUM",0);
        }else{
            countMap = count.getData();
        }

        //log.info("countMap：{}",new Gson().toJson(countMap));

        view.addObject("countMap", countMap);
        view.addObject("messageDailyStatisticValidator", messageDailyStatisticValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;

    }
}
