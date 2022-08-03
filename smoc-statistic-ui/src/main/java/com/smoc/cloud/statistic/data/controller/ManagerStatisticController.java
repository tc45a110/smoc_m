package com.smoc.cloud.statistic.data.controller;

import com.google.gson.Gson;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.saler.qo.CustomerAccountInfoQo;
import com.smoc.cloud.common.smoc.spss.model.StatisticModel;
import com.smoc.cloud.common.smoc.spss.qo.ManagerCarrierStatisticQo;
import com.smoc.cloud.common.smoc.spss.qo.ManagerStatisticQo;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.statistic.data.service.ManagerStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.Random;

/**
 * 运营数据统计分析
 */
@Slf4j
@RestController
@RequestMapping("/statistic/manager")
public class ManagerStatisticController {

    @Autowired
    private ManagerStatisticsService managerStatisticsService;

    /**
     * 运营管理综合日统计
     * @return
     */
    @RequestMapping(value = "/daily", method = RequestMethod.GET)
    public ModelAndView daily() throws ParseException {
        ModelAndView view = new ModelAndView("statistic/manager/manager_statistic_daily");

        ManagerStatisticQo managerStatisticQo = new ManagerStatisticQo();
        managerStatisticQo.setStartDate(DateTimeUtils.getDateFormat(new Date()));

        view.addObject("managerStatisticQo",managerStatisticQo);
        return view;

    }

    /**
     * 运营管理综合日统计-查询
     * @param managerStatisticQo
     * @param request
     * @return
     */
    @RequestMapping(value = "/daily/query", method = RequestMethod.POST)
    public ModelAndView dailyQuery(@ModelAttribute ManagerStatisticQo managerStatisticQo, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("statistic/manager/manager_statistic_daily");

        view.addObject("managerStatisticQo",managerStatisticQo);
        return view;
    }

    /**
     * 运营管理综合日统计Ajax
     * @param startDate
     * @param request
     * @return
     */
    @RequestMapping(value = "/daily/ajax/{startDate}", method = RequestMethod.GET)
    public StatisticModel managerDailyStatistic(@PathVariable String startDate, HttpServletRequest request) {

        ManagerStatisticQo managerStatisticQo = new ManagerStatisticQo();
        managerStatisticQo.setStartDate(DateTimeUtils.checkOption(startDate,-365));
        managerStatisticQo.setEndDate(startDate);

        StatisticModel statisticModel = managerStatisticsService.managerDailyStatistic(managerStatisticQo);

        return statisticModel;
    }

    /**
     * 运营管理综合月统计
     * @return
     */
    @RequestMapping(value = "/month", method = RequestMethod.GET)
    public ModelAndView month() throws ParseException {
        ModelAndView view = new ModelAndView("statistic/manager/manager_statistic_month");

        ManagerStatisticQo managerStatisticQo = new ManagerStatisticQo();
        managerStatisticQo.setStartDate(DateTimeUtils.getMonth());

        view.addObject("managerStatisticQo",managerStatisticQo);
        return view;
    }

    /**
     * 运营管理综合日统计-查询
     * @param managerStatisticQo
     * @param request
     * @return
     */
    @RequestMapping(value = "/month/query", method = RequestMethod.POST)
    public ModelAndView monthQuery(@ModelAttribute ManagerStatisticQo managerStatisticQo, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("statistic/manager/manager_statistic_month");

        view.addObject("managerStatisticQo",managerStatisticQo);
        return view;
    }

    /**
     * 运营管理综合月统计Ajax
     * @param startDate
     * @param request
     * @return
     */
    @RequestMapping(value = "/month/ajax/{startDate}", method = RequestMethod.GET)
    public StatisticModel managerMonthStatistic(@PathVariable String startDate, HttpServletRequest request) {

        ManagerStatisticQo managerStatisticQo = new ManagerStatisticQo();
        managerStatisticQo.setStartDate(DateTimeUtils.dateAddMonthsStr(startDate,-24));
        managerStatisticQo.setEndDate(startDate);

        StatisticModel statisticModel = managerStatisticsService.managerMonthStatistic(managerStatisticQo);

        return statisticModel;
    }

    /**
     * 运营管理运营商分类统计
     * @return
     */
    @RequestMapping(value = "/carrier/month", method = RequestMethod.GET)
    public ModelAndView carrierMonth() throws ParseException {
        ModelAndView view = new ModelAndView("statistic/manager/manager_statistic_carrier_month");

        ManagerCarrierStatisticQo managerCarrierStatisticQo = new ManagerCarrierStatisticQo();
        managerCarrierStatisticQo.setStartDate(DateTimeUtils.getMonth());

        view.addObject("managerCarrierStatisticQo",managerCarrierStatisticQo);
        return view;

    }

    /**
     * 运营管理运营商分类统计-查询
     * @param managerCarrierStatisticQo
     * @param request
     * @return
     */
    @RequestMapping(value = "/carrier/month/query", method = RequestMethod.POST)
    public ModelAndView carrierMonthQuery(@ModelAttribute ManagerCarrierStatisticQo managerCarrierStatisticQo, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("statistic/manager/manager_statistic_carrier_month");

        view.addObject("managerCarrierStatisticQo",managerCarrierStatisticQo);
        return view;
    }

    /**
     * 运营管理运营商按月分类统计Ajax
     * @param request
     * @return
     */
    @RequestMapping(value = "/carrier/month/ajax/{startDate}", method = RequestMethod.POST)
    public ManagerCarrierStatisticQo managerCarrierMonthStatistic(@ModelAttribute ManagerCarrierStatisticQo managerCarrierStatisticQo, HttpServletRequest request) {

        managerCarrierStatisticQo.setStartDate(DateTimeUtils.dateAddMonthsStr(managerCarrierStatisticQo.getStartDate(),-24));
        managerCarrierStatisticQo.setEndDate(managerCarrierStatisticQo.getStartDate());

        ManagerCarrierStatisticQo statisticModel = managerStatisticsService.managerCarrierMonthStatistic(managerCarrierStatisticQo);

        return statisticModel;
    }

    /**
     * 生成[x, y]之间的随机数
     * @return [x, y]之间的随机数
     */
    public static Integer getRandomNumber2(Integer min,Integer max) {

        Random random = new  Random();

        int result = random.nextInt(max) % (max-min+1) + min;
        return result;
    }



}
