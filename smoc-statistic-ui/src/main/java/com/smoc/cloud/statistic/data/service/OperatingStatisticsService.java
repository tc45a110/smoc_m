package com.smoc.cloud.statistic.data.service;


import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.spss.model.StatisticRatioModel;
import com.smoc.cloud.common.smoc.spss.qo.ManagerStatisticQo;
import com.smoc.cloud.common.smoc.spss.qo.StatisticIncomeQo;
import com.smoc.cloud.statistic.data.remote.OperatingStatisticsFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * 运营数据统计分析
 */
@Slf4j
@Service
public class OperatingStatisticsService {

    @Autowired
    private OperatingStatisticsFeignClient operatingStatisticsFeignClient;

    /**
     * 运营净毛利统计
     * @param statisticIncomeQo
     * @return
     */
    public StatisticIncomeQo incomeMonthStatistic(StatisticIncomeQo statisticIncomeQo) {
        ResponseData<List<StatisticIncomeQo>> responseData = this.operatingStatisticsFeignClient.incomeMonthStatistic(statisticIncomeQo);
        List<StatisticIncomeQo> list = responseData.getData();

        Map<String, BigDecimal> monthMap = buildMonthStatistics(statisticIncomeQo.getYear());

        /**
         *  收入：封装map
         */
        if(!StringUtils.isEmpty(list) && list.size()>0){
            for (int i = 0; i < list.size(); i++) {
                StatisticIncomeQo info = list.get(i);
                monthMap.put(info.getMonth(), info.getIncome());
            }
        }

        //月份
        String[] month = new String[monthMap.size()];
        //收入
        BigDecimal[] income = new BigDecimal[monthMap.size()];
        //总收入
        double totalIncome = 0;
        //封装占比数据
        List<StatisticRatioModel> ratio = new ArrayList<>();

        int i = 0;
        for (String key : monthMap.keySet()) {
            month[i] = key;
            BigDecimal profit = monthMap.get(key);
            income[i] = profit;
            totalIncome += profit.doubleValue();
            i++;

            StatisticRatioModel model = new StatisticRatioModel();
            model.setName(key);
            model.setValue(profit);
            ratio.add(model);
        }

        //组建每月收入的 起点
        BigDecimal[] startImcome = new BigDecimal[monthMap.size()];
        for (int j = 0; j < startImcome.length; j++) {
            startImcome[j] = getStartData(j+1, monthMap, statisticIncomeQo.getYear());
        }

        statisticIncomeQo.setMonthArray(month);
        statisticIncomeQo.setIncomeArray(income);
        statisticIncomeQo.setStartImcomeArray(startImcome);
        statisticIncomeQo.setTotalImcome(new BigDecimal(totalIncome).setScale(2, BigDecimal.ROUND_HALF_UP));
        statisticIncomeQo.setRatios(ratio);

        return statisticIncomeQo;
    }

    /**
     * 根据给定的年份 组建一个 包含12个月为key的map，默认值为0
     *
     * @param year 年份
     * @return
     */
    private Map<String, BigDecimal> buildMonthStatistics(int year) {
        Map<String, BigDecimal> map = new TreeMap<String, BigDecimal>();
        for (int i = 1; i < 13; i++) {
            if (i >= 10) {
                map.put(year + "-" + i, new BigDecimal(0));
            } else {
                map.put(year + "-0" + i, new BigDecimal(0));
            }
        }
        return map;
    }

    /**
     * 每月收入的 起点
     * @param month
     * @param monthIncome
     * @param year
     * @return
     */
    public BigDecimal getStartData(int month, Map<String, BigDecimal> monthIncome, int year) {
        if (1 == month) {
            return new BigDecimal("0");
        }

        Double result = new Double("0");

        for (int t = 1; t < month; t++) {
            String key = ""+year;
            if (t >= 10) {
                key += "-" + t;
            } else {
                key += "-0" + t;
            }
            if (null != monthIncome.get(key)) {
                result += monthIncome.get(key).doubleValue();
            }
        }

        return new BigDecimal(result);

    }

    /**
     * 运营数据月查询:统计每月发送数据
     * @param managerStatisticQo
     * @return
     */
    public ResponseData<List<ManagerStatisticQo>> operatingStatisticSendMessageMonth(ManagerStatisticQo managerStatisticQo) {
        try {
            ResponseData<List<ManagerStatisticQo>> list = operatingStatisticsFeignClient.operatingStatisticSendMessageMonth(managerStatisticQo);
            return list;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
