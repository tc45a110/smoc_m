package com.smoc.cloud.statistic.data.service;


import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.spss.model.StatisticRatioModel;
import com.smoc.cloud.common.smoc.spss.model.StatisticModel;
import com.smoc.cloud.common.smoc.spss.qo.ManagerCarrierStatisticQo;
import com.smoc.cloud.common.smoc.spss.qo.ManagerStatisticQo;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.statistic.data.remote.ManagerStatisticsFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 运营数据统计分析
 */
@Slf4j
@Service
public class ManagerStatisticsService {

    @Autowired
    private ManagerStatisticsFeignClient managerStatisticsFeignClient;

    /**
     * 运营管理综合日统计
     *
     * @param managerStatisticQo
     * @return
     */
    public StatisticModel managerDailyStatistic(ManagerStatisticQo managerStatisticQo) {

        ResponseData<List<ManagerStatisticQo>> responseData = this.managerStatisticsFeignClient.managerDailyStatistic(managerStatisticQo);
        List<ManagerStatisticQo> list = responseData.getData();

        //每一天日期
        String[] day = list.stream().map(ManagerStatisticQo::getMessageDate).toArray(String[]::new);
        //发送量
        BigDecimal[] sendAmount = list.stream().map(ManagerStatisticQo::getSendAmount).toArray(BigDecimal[]::new);
        //营收
        BigDecimal[] incomeAmount = list.stream().map(ManagerStatisticQo::getIncomeAmount).toArray(BigDecimal[]::new);
        //净利润
        BigDecimal[] profitAmount = list.stream().map(ManagerStatisticQo::getProfitAmount).toArray(BigDecimal[]::new);

        StatisticModel model = new StatisticModel();
        model.setDate(day);
        model.setSendAmount(sendAmount);
        model.setIncomeAmount(incomeAmount);
        model.setProfitAmount(profitAmount);

        return model;
    }

    /**
     * 运营管理综合月统计
     *
     * @param managerStatisticQo
     * @return
     */
    public StatisticModel managerMonthStatistic(ManagerStatisticQo managerStatisticQo) {
        ResponseData<List<ManagerStatisticQo>> responseData = this.managerStatisticsFeignClient.managerMonthStatistic(managerStatisticQo);
        List<ManagerStatisticQo> list = responseData.getData();

        //封装月份
        Map<String, ManagerStatisticQo> monthMap = buildMonthStatistics(managerStatisticQo.getEndDate(), 24);
        if (!StringUtils.isEmpty(list) && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                ManagerStatisticQo info = list.get(i);
                monthMap.put(info.getMessageDate(), info);
            }
        }

        //月份
        String[] month = new String[monthMap.size()];
        //发送量
        BigDecimal[] sendAmount = new BigDecimal[monthMap.size()];
        //营收
        BigDecimal[] incomeAmount = new BigDecimal[monthMap.size()];
        //净利润
        BigDecimal[] profitAmount = new BigDecimal[monthMap.size()];
        int i = 0;
        for (String key : monthMap.keySet()) {
            ManagerStatisticQo info = monthMap.get(key);
            month[i] = key;
            sendAmount[i] = info.getSendAmount();
            incomeAmount[i] = info.getIncomeAmount();
            profitAmount[i] = info.getProfitAmount();
            if (StringUtils.isEmpty(info.getSendAmount())) {
                sendAmount[i] = new BigDecimal(0);
            }
            if (StringUtils.isEmpty(info.getIncomeAmount())) {
                incomeAmount[i] = new BigDecimal(0);
            }
            if (StringUtils.isEmpty(info.getProfitAmount())) {
                profitAmount[i] = new BigDecimal(0);
            }
            i++;
        }

        StatisticModel model = new StatisticModel();
        model.setDate(month);
        model.setSendAmount(sendAmount);
        model.setIncomeAmount(incomeAmount);
        model.setProfitAmount(profitAmount);

        return model;
    }

    /**
     * 运营管理运营商按月分类统计
     *
     * @param managerCarrierStatisticQo
     * @return
     */
    public ManagerCarrierStatisticQo managerCarrierMonthStatistic(ManagerCarrierStatisticQo managerCarrierStatisticQo) {
        ResponseData<List<ManagerCarrierStatisticQo>> responseData = this.managerStatisticsFeignClient.managerCarrierMonthStatistic(managerCarrierStatisticQo);
        List<ManagerCarrierStatisticQo> list = responseData.getData();

        //封装月份
        Map<String, Map<String, BigDecimal>> monthMap = buildCarrierMonthStatistics(managerCarrierStatisticQo.getEndDate(), 12);

        //封装数据
        if(!StringUtils.isEmpty(list) && list.size()>0){
            for (String key : monthMap.keySet()) {
                Map<String, BigDecimal> map = monthMap.get(key);
                int a = 1;
                for (ManagerCarrierStatisticQo qo : list) {
                    if (key.equals(qo.getMessageDate())) {
                        map.put(qo.getCarrier(), qo.getCarrierData());
                        a++;
                    }
                    if (a == 4) {
                        break;
                    }
                }
            }
        }

        //月份
        String[] month = new String[monthMap.size()];
        //发送量
        BigDecimal[] cmccArray = new BigDecimal[monthMap.size()];
        //营收
        BigDecimal[] unicArray = new BigDecimal[monthMap.size()];
        //净利润
        BigDecimal[] telcArray = new BigDecimal[monthMap.size()];
        //净利润
        BigDecimal[] intlArray = new BigDecimal[monthMap.size()];

        int i = 0;
        Double totalCMCC = 0D;
        Double totalUNIC = 0D;
        Double totalTELC = 0D;
        Double totalINTL = 0D;
        for (String key : monthMap.keySet()) {
            Map<String, BigDecimal> info = monthMap.get(key);
            month[i] = key;

            BigDecimal cmcc = info.get("CMCC");
            BigDecimal unic = info.get("UNIC");
            BigDecimal telc = info.get("TELC");
            BigDecimal intl = info.get("INTL");

            cmccArray[i] = cmcc;
            unicArray[i] = unic;
            telcArray[i] = telc;
            intlArray[i] = intl;

            totalCMCC += cmcc.doubleValue();
            totalUNIC += unic.doubleValue();
            totalTELC += telc.doubleValue();
            totalINTL += intl.doubleValue();

            i++;
        }

        //封装占比数据
        List<StatisticRatioModel> ratio = new ArrayList<>();
        StatisticRatioModel modelCMCC = new StatisticRatioModel();
        modelCMCC.setName("移动");
        modelCMCC.setValue(new BigDecimal(totalCMCC).setScale(2, BigDecimal.ROUND_HALF_UP));
        ratio.add(modelCMCC);

        StatisticRatioModel modelUNIC = new StatisticRatioModel();
        modelUNIC.setName("联通");
        modelUNIC.setValue(new BigDecimal(totalUNIC).setScale(2, BigDecimal.ROUND_HALF_UP));
        ratio.add(modelUNIC);

        StatisticRatioModel modelTELC = new StatisticRatioModel();
        modelTELC.setName("电信");
        modelTELC.setValue(new BigDecimal(totalTELC).setScale(2, BigDecimal.ROUND_HALF_UP));
        ratio.add(modelTELC);

        StatisticRatioModel modelINTL = new StatisticRatioModel();
        modelINTL.setName("国际");
        modelINTL.setValue(new BigDecimal(totalINTL).setScale(2, BigDecimal.ROUND_HALF_UP));
        ratio.add(modelINTL);

        managerCarrierStatisticQo.setDate(month);
        managerCarrierStatisticQo.setCmccArray(cmccArray);
        managerCarrierStatisticQo.setUnicArray(unicArray);
        managerCarrierStatisticQo.setTelcArray(telcArray);
        managerCarrierStatisticQo.setIntlArray(intlArray);
        managerCarrierStatisticQo.setTotal(new BigDecimal(totalCMCC+totalUNIC+totalTELC+totalINTL).setScale(2, BigDecimal.ROUND_HALF_UP));
        managerCarrierStatisticQo.setRatio(ratio);

        return managerCarrierStatisticQo;
    }


    /**
     * 查询当前月份和向后推N个月
     *
     * @return
     */
    public Map<String, ManagerStatisticQo> buildMonthStatistics(String startDate, int a) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        // 当前月份
        Date nowDate;
        try {
            nowDate = sdf.parse(startDate);
            Map<String, ManagerStatisticQo> map = new TreeMap<String, ManagerStatisticQo>();
            map.put(startDate, new ManagerStatisticQo());
            for (int i = 1; i < a; i++) {
                Date subtract = DateTimeUtils.dateAddMonths(nowDate, -i);
                map.put(sdf.format(subtract), new ManagerStatisticQo());
            }

            return map;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, Map<String, BigDecimal>> buildCarrierMonthStatistics(String startDate, int a) {
        Map<String, Map<String, BigDecimal>> monthMap = new TreeMap<String, Map<String, BigDecimal>>();
        //当前月
        Map<String, BigDecimal> map = new TreeMap<String, BigDecimal>();
        map.put("CMCC", new BigDecimal(0));
        map.put("UNIC", new BigDecimal(0));
        map.put("TELC", new BigDecimal(0));
        map.put("INTL", new BigDecimal(0));
        monthMap.put(startDate, map);

        for (int i = 1; i < a; i++) {
            String subtract = DateTimeUtils.dateAddMonthsStr(startDate, -i);

            Map<String, BigDecimal> info = new TreeMap<String, BigDecimal>();
            info.put("CMCC", new BigDecimal(0));
            info.put("UNIC", new BigDecimal(0));
            info.put("TELC", new BigDecimal(0));
            info.put("INTL", new BigDecimal(0));

            monthMap.put(subtract, info);
        }

        return monthMap;
    }

}
