package com.smoc.cloud.main;

import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import com.smoc.cloud.common.smoc.customer.qo.StatisticProfitData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.message.MessageChannelComplaintValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.BusinessAccountService;
import com.smoc.cloud.statistics.service.StatisticsService;
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
import java.util.Map;

/**
 * 公用系统用户（可自定义扩展）
 * 2019/5/14 11:37
 **/
@Slf4j
@RestController
public class IndexController {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 查看首页信息
     *
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request) {
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("index");

        Date date = DateTimeUtils.getFirstMonth(6);
        String startDate = DateTimeUtils.getDateFormat(date);
        String endDate = DateTimeUtils.getDateFormat(new Date());

        //统计(客户数、活跃数、通道数)
        ResponseData<Map<String, Object>> countMap = statisticsService.statisticsCountData(startDate,endDate);

        //短信发送总量、营收总额、充值总额、账户总余额
        int year = DateTimeUtils.getNowYear();
        String start = year+"-01"+"-01";
        String end = DateTimeUtils.getDateFormat(new Date());
        ResponseData<Map<String, Object>> accountMap = statisticsService.statisticsAccountData(start,end);

        //查询通道排行
        String month = DateTimeUtils.getDateFormat(new Date(),"yyyy-MM");
        MessageChannelComplaintValidator messageChannelComplaintValidator = new MessageChannelComplaintValidator();
        messageChannelComplaintValidator.setMonth(month);
        ResponseData<List<MessageChannelComplaintValidator>> complaintList = statisticsService.channelComplaintRanking(messageChannelComplaintValidator);

        view.addObject("countMap", countMap.getData());
        view.addObject("accountMap", accountMap.getData());
        view.addObject("endDate", endDate);
        view.addObject("year", year);
        view.addObject("month", month);
        view.addObject("complaintList", complaintList.getData());

        return view;

    }

    /**
     * 近12个月短信发送量
     *
     * @return
     */
    @RequestMapping(value = "/index/statisticSendMonth", method = RequestMethod.GET)
    public AccountStatisticSendData indexStatisticSendMonth(HttpServletRequest request) {


        AccountStatisticSendData statisticSendData = new AccountStatisticSendData();
        statisticSendData.setDimension("index");

        statisticSendData = statisticsService.statisticSendNumber(statisticSendData);

        return statisticSendData;
    }

    /**
     * 近12个月营业收入
     *
     * @return
     */
    @RequestMapping(value = "/index/statisticProfitMonth", method = RequestMethod.GET)
    public StatisticProfitData statisticProfitMonth(HttpServletRequest request) {

        StatisticProfitData statisticProfitData = new StatisticProfitData();

        statisticProfitData = statisticsService.statisticProfitMonth(statisticProfitData);

        return statisticProfitData;
    }
}
