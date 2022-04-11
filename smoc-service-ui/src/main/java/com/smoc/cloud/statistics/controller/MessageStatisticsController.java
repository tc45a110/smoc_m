package com.smoc.cloud.statistics.controller;

import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import com.smoc.cloud.common.smoc.message.MessageWebTaskInfoValidator;
import com.smoc.cloud.common.smoc.message.model.StatisticMessageSendData;
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
 * 发送统计
 */
@Controller
@RequestMapping("/statistics/message")
public class MessageStatisticsController {

    @Autowired
    private StatisticsMessageService statisticsMessageService;

    /**
     * 统计发送量
     * @param request
     * @return
     */
    @RequestMapping(value = "/number/list/{businessType}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String businessType, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("statistics/message_send_number_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //初始化数据
        PageParams<StatisticMessageSendData> params = new PageParams<>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        StatisticMessageSendData statisticMessageSendData = new StatisticMessageSendData();
        statisticMessageSendData.setEnterpriseId(user.getOrganization());
        statisticMessageSendData.setBusinessType(businessType);
        Date startDate = DateTimeUtils.getFirstMonth(1);
        statisticMessageSendData.setStartDate(DateTimeUtils.getDateFormat(startDate));
        statisticMessageSendData.setEndDate(DateTimeUtils.getDateFormat(new Date()));
        params.setParams(statisticMessageSendData);

        //查询
        ResponseData<PageList<StatisticMessageSendData>> data = statisticsMessageService.messageSendNumberList(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("statisticMessageSendData", statisticMessageSendData);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams",params);
        view.addObject("businessType", businessType);
        return view;
    }

    /**
     * 短信提交记录分页
     * @param request
     * @return
     */
    @RequestMapping(value = "/number/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute StatisticMessageSendData statisticMessageSendData, PageParams params, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("statistics/message_send_number_list");

        if("1".equals(statisticMessageSendData.getFlag())){
            view = new ModelAndView("statistics/message_send_number_list_account");
        }else if("2".equals(statisticMessageSendData.getFlag())){
            view = new ModelAndView("statistics/message_send_number_list_sign");
        }else if("3".equals(statisticMessageSendData.getFlag())){
            view = new ModelAndView("statistics/message_send_number_list_carrier");
        }

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //分页查询
        statisticMessageSendData.setEnterpriseId(user.getOrganization());
        if (!StringUtils.isEmpty(statisticMessageSendData.getStartDate())) {
            String[] date = statisticMessageSendData.getStartDate().split(" - ");
            statisticMessageSendData.setStartDate(StringUtils.trimWhitespace(date[0]));
            statisticMessageSendData.setEndDate(StringUtils.trimWhitespace(date[1]));
        }
        params.setParams(statisticMessageSendData);

        //查询
        ResponseData<PageList<StatisticMessageSendData>> data = statisticsMessageService.messageSendNumberList(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("statisticMessageSendData", statisticMessageSendData);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams",params);
        view.addObject("businessType", statisticMessageSendData.getBusinessType());
        return view;
    }
}
