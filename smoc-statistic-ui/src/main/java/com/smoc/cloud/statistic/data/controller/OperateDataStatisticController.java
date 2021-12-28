package com.smoc.cloud.statistic.data.controller;


import com.google.gson.Gson;
import com.smoc.cloud.common.utils.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.util.Date;
import java.util.Random;

/**
 * 运营数据统计
 */
@Slf4j
@Controller
@RequestMapping("/data/statistic")
public class OperateDataStatisticController {

    /**
     * 运营数据综合日统计
     * @return
     */
    @RequestMapping(value = "/operating/daily", method = RequestMethod.GET)
    public ModelAndView daily() throws ParseException {
        ModelAndView view = new ModelAndView("statistic/data/operating_data_statistic_daily");

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
        model.setIncomeAmount(incomeAmount);
        model.setCostAmount(costAmount);
        model.setProfitAmount(profitAmount);

        String str = new Gson().toJson(model);
        log.info(str);
        return view;

    }

    /**
     * 运营数据综合月统计
     * @return
     */
    @RequestMapping(value = "/operating/month", method = RequestMethod.GET)
    public ModelAndView month() throws ParseException {
        ModelAndView view = new ModelAndView("statistic/data/operating_data_statistic_month");

        return view;

    }


    /**
     * 运营数据综合年查询
     * @return
     */
    @RequestMapping(value = "/operating/year", method = RequestMethod.GET)
    public ModelAndView year() throws ParseException {
        ModelAndView view = new ModelAndView("statistic/data/operating_data_statistic_year");

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
