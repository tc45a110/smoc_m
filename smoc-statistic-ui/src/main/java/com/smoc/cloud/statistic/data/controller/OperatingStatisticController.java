package com.smoc.cloud.statistic.data.controller;


import com.smoc.cloud.common.smoc.spss.qo.StatisticIncomeQo;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.statistic.data.service.OperatingStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

/**
 * 运营数据统计
 */
@Slf4j
@RestController
@RequestMapping("/statistic/operating")
public class OperatingStatisticController {

    @Autowired
    private OperatingStatisticsService operatingStatisticsService;

    /**
     * 运营综合日统计
     * @return
     */
    @RequestMapping(value = "/daily", method = RequestMethod.GET)
    public ModelAndView manager_daily() throws ParseException {
        ModelAndView view = new ModelAndView("statistic/operating/operating_statistic_daily");

        return view;

    }

    /**
     * 运营净毛利统计
     * @return
     */
    @RequestMapping(value = "/income", method = RequestMethod.GET)
    public ModelAndView income() throws ParseException {
        ModelAndView view = new ModelAndView("statistic/operating/operating_statistic_income");

        StatisticIncomeQo statisticIncomeQo = new StatisticIncomeQo();
        statisticIncomeQo.setYear(DateTimeUtils.getNowYear());

        view.addObject("statisticIncomeQo",statisticIncomeQo);
        return view;

    }

    /**
     * 运营净毛利统计-查询
     * @param statisticIncomeQo
     * @param request
     * @return
     */
    @RequestMapping(value = "/income/query", method = RequestMethod.POST)
    public ModelAndView incomeMonthQuery(@ModelAttribute StatisticIncomeQo statisticIncomeQo, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("statistic/operating/operating_statistic_income");

        view.addObject("statisticIncomeQo",statisticIncomeQo);
        return view;
    }

    /**
     * 运营净毛利统计Ajax
     * @param request
     * @return
     */
    @RequestMapping(value = "/income/month/ajax/{year}", method = RequestMethod.GET)
    public StatisticIncomeQo incomeMonthStatistic(@PathVariable int year, HttpServletRequest request) {

        StatisticIncomeQo statisticIncomeQo = new StatisticIncomeQo();
        statisticIncomeQo.setYear(year);

        statisticIncomeQo = operatingStatisticsService.incomeMonthStatistic(statisticIncomeQo);

        return statisticIncomeQo;
    }

}
