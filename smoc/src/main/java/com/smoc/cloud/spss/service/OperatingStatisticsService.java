package com.smoc.cloud.spss.service;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.spss.qo.StatisticIncomeQo;
import com.smoc.cloud.spss.repository.ManagerStatisticsRepository;
import com.smoc.cloud.spss.repository.OperatingStatisticsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 运营数据统计分析
 */
@Slf4j
@Service
public class OperatingStatisticsService {

    @Resource
    private OperatingStatisticsRepository operatingStatisticsRepository;

    /**
     * 运营净毛利统计
     * @param statisticIncomeQo
     * @return
     */
    public ResponseData<List<StatisticIncomeQo>> incomeMonthStatistic(StatisticIncomeQo statisticIncomeQo) {
        List<StatisticIncomeQo> list = operatingStatisticsRepository.incomeMonthStatistic(statisticIncomeQo);

        return ResponseDataUtil.buildSuccess(list);
    }
}
