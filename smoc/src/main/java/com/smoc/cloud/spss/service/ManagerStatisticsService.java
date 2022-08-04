package com.smoc.cloud.spss.service;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.spss.qo.ManagerCarrierStatisticQo;
import com.smoc.cloud.common.smoc.spss.qo.ManagerStatisticQo;
import com.smoc.cloud.spss.repository.ManagerStatisticsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 运营数据统计分析
 */
@Slf4j
@Service
public class ManagerStatisticsService {

    @Resource
    private ManagerStatisticsRepository managerStatisticsRepository;

    /**
     * 运营管理综合日统计
     * @param managerStatisticQo
     * @return
     */
    public ResponseData<List<ManagerStatisticQo>> managerDailyStatistic(ManagerStatisticQo managerStatisticQo) {

        List<ManagerStatisticQo> list = managerStatisticsRepository.managerDailyStatistic(managerStatisticQo);

        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 运营管理综合月统计
     * @param managerStatisticQo
     * @return
     */
    public ResponseData<List<ManagerStatisticQo>> managerMonthStatistic(ManagerStatisticQo managerStatisticQo) {
        List<ManagerStatisticQo> list = managerStatisticsRepository.managerMonthStatistic(managerStatisticQo);

        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 运营管理运营商按月分类统计
     * @param managerCarrierStatisticQo
     * @return
     */
    public ResponseData<List<ManagerCarrierStatisticQo>> managerCarrierMonthStatistic(ManagerCarrierStatisticQo managerCarrierStatisticQo) {
        List<ManagerCarrierStatisticQo> list = managerStatisticsRepository.managerCarrierMonthStatistic(managerCarrierStatisticQo);

        return ResponseDataUtil.buildSuccess(list);
    }
}
