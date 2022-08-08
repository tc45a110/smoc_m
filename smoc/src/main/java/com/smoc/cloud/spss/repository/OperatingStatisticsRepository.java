package com.smoc.cloud.spss.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.smoc.spss.qo.StatisticIncomeQo;
import com.smoc.cloud.spss.rowmapper.StatisticIncomeRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 运营数据统计分析
 */
@Service
public class OperatingStatisticsRepository extends BasePageRepository {

    @Resource
    public JdbcTemplate jdbcTemplate;


    /**
     * 运营管理运营商按月分类统计
     * @param statisticIncomeQo
     * @return
     */
    public List<StatisticIncomeQo> incomeMonthStatistic(StatisticIncomeQo statisticIncomeQo) {
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" DATE_FORMAT(t.MESSAGE_DATE, '%Y-%m')MESSAGE_DATE,");
        sqlBuffer.append(" IFNULL(sum(t.MESSAGE_SUCCESS_NUM*t.ACCOUNT_PRICE)-sum(t.MESSAGE_SUCCESS_NUM*t.CHANNEL_PRICE),0)PROFIT_SUM ");
        sqlBuffer.append(" from message_daily_statistics t where LEFT(t.MESSAGE_DATE,4)=? ");
        sqlBuffer.append(" GROUP BY LEFT(t.MESSAGE_DATE,7) ");

        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(statisticIncomeQo.getYear());

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<StatisticIncomeQo> list = this.queryForObjectList(sqlBuffer.toString(), params, new StatisticIncomeRowMapper());
        return list;
    }
}
