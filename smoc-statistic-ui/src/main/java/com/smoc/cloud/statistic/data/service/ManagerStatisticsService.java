package com.smoc.cloud.statistic.data.service;


import com.smoc.cloud.statistic.data.controller.StatisticModel;
import com.smoc.cloud.statistic.data.remote.IndexStatisticsFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 运营数据统计分析
 */
@Slf4j
@Service
public class ManagerStatisticsService {

    @Autowired
    private IndexStatisticsFeignClient indexStatisticsFeignClient;

    /**
     * 运营管理综合日统计
     * @param startDate
     * @return
     */
    public StatisticModel managerDailyStatistic(String startDate) {

        StatisticModel model = new StatisticModel();
        //model.setDate(date);
        //model.setSendAmount(sendAmount);
        return model;
    }
}
