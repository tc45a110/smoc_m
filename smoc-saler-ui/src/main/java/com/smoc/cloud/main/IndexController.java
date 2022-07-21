package com.smoc.cloud.main;

import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.saler.service.CustomerService;
import com.smoc.cloud.saler.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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

    @Autowired
    private CustomerService customerService;

    /**
     * 查看首页信息
     *
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request) {
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("index");

        //客户发送总量、客户消费总额、账户总余额
        int year = DateTimeUtils.getNowYear();
        String start = year+"-01"+"-01";
        String endDate = DateTimeUtils.getDateFormat(new Date());
        ResponseData<Map<String, Object>> dataMap = statisticsService.statisticsSalerIndexData(start,endDate,user.getId());

        view.addObject("countMap", dataMap.getData());
        view.addObject("endDate", endDate);
        view.addObject("year", year);

        return view;

    }

    /**
     * 客户近12个月账号短信发送量统计
     * @param request
     * @return
     */
    @RequestMapping(value = "/index/indexStatisticMessageSendSum", method = RequestMethod.GET)
    public AccountStatisticSendData indexStatisticMessageSendSum(HttpServletRequest request) {
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        AccountStatisticSendData statisticSendData = new AccountStatisticSendData();
        statisticSendData.setSaler(user.getId());

        statisticSendData = statisticsService.indexStatisticMessageSendSum(statisticSendData);

        return statisticSendData;
    }
}
