package com.smoc.cloud.main;

import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.statistics.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

        view.addObject("countMap", countMap.getData());
        view.addObject("accountMap", accountMap.getData());
        view.addObject("endDate", endDate);
        view.addObject("year", year);

        return view;

    }

}
