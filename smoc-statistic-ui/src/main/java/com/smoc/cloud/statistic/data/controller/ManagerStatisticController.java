package com.smoc.cloud.statistic.data.controller;

import com.google.gson.Gson;
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

        //日期
        String[] date = new String[365];
        for(int i=0;i<365;i++){
            date[i] = DateTimeUtils.checkOption(""+DateTimeUtils.dateFormat(new Date(),DateTimeUtils.DATE_DEFAULT_FORMAT),-(365-i));
        }

        //发送量
        Integer[] sendAmount = new Integer[31];
        for(int i=0;i<31;i++){
            sendAmount[i] = getRandomNumber2(600,700);
        }

        //营收
        Integer[] incomeAmount = new Integer[31];
        for(int i=0;i<31;i++){
            incomeAmount[i] = getRandomNumber2(400,500);
        }
        //成本
        Integer[] costAmount = new Integer[31];
        for(int i=0;i<31;i++){
            costAmount[i] = getRandomNumber2(250,400);
        }
        //利润
        Integer[] profitAmount = new Integer[31];
        for(int i=0;i<31;i++){
            profitAmount[i] = getRandomNumber2(150,250);
        }

        StatisticModel model = new StatisticModel();
        model.setDate(date);
        model.setSendAmount(sendAmount);
        //model.setIncomeAmount(incomeAmount);
        //model.setCostAmount(costAmount);
        //model.setProfitAmount(profitAmount);

        String str = new Gson().toJson(model);
        log.info(str);
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

        StatisticModel statisticModel = managerStatisticsService.managerDailyStatistic(startDate);

        return statisticModel;
    }

    /**
     * 运营管理综合月统计
     * @return
     */
    @RequestMapping(value = "/month", method = RequestMethod.GET)
    public ModelAndView month() throws ParseException {
        ModelAndView view = new ModelAndView("statistic/manager/manager_statistic_month");

        return view;

    }

    /**
     * 运营管理运营商分类统计
     * @return
     */
    @RequestMapping(value = "/carrier/month", method = RequestMethod.GET)
    public ModelAndView manager_month() throws ParseException {
        ModelAndView view = new ModelAndView("statistic/manager/manager_statistic_carrier_month");

        return view;

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
