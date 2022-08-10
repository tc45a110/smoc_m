package com.smoc.cloud.query.controller;

import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.saler.qo.ChannelStatisticSendData;
import com.smoc.cloud.common.smoc.spss.qo.ManagerStatisticQo;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.statistic.data.service.OperatingStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

/**
 * 运营数据查询
 */
@Slf4j
@Controller
@RequestMapping("/query/operating")
public class OperatingDataQueryController {

    @Autowired
    private OperatingStatisticsService operatingStatisticsService;

    /**
     * 运营数据月查询:统计每月发送数据
     * @return
     */
    @RequestMapping(value = "/month", method = RequestMethod.GET)
    public ModelAndView month() throws ParseException {
        ModelAndView view = new ModelAndView("query/operating/operating_statistic_month");

        //初始化数据
        ManagerStatisticQo managerStatisticQo = new ManagerStatisticQo();
        managerStatisticQo.setStartDate(""+DateTimeUtils.getNowYear());

        //查询
        ResponseData<List<ManagerStatisticQo>> data = operatingStatisticsService.operatingStatisticSendMessageMonth(managerStatisticQo);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        List<ManagerStatisticQo> list = data.getData();

        //发送量
        BigDecimal totalNumber = list.stream().map(ManagerStatisticQo::getSendAmount).reduce(BigDecimal::add).get();
        //营收
        BigDecimal incomeAmount = list.stream().map(ManagerStatisticQo::getIncomeAmount).reduce(BigDecimal::add).get();
        //成本
        BigDecimal costAmount = list.stream().map(ManagerStatisticQo::getCostAmount).reduce(BigDecimal::add).get();
        //净利润
        BigDecimal profitAmount = list.stream().map(ManagerStatisticQo::getProfitAmount).reduce(BigDecimal::add).get();

        view.addObject("list", list);
        view.addObject("totalNumber", totalNumber);
        view.addObject("incomeAmount", incomeAmount);
        view.addObject("costAmount", costAmount);
        view.addObject("profitAmount", profitAmount);
        return view;
    }
}
