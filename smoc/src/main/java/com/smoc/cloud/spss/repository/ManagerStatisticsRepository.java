package com.smoc.cloud.spss.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.smoc.spss.model.StatisticModel;
import com.smoc.cloud.common.smoc.spss.qo.ManagerStatisticQo;
import com.smoc.cloud.customer.rowmapper.AccountStatisticSendRowMapper;
import com.smoc.cloud.spss.rowmapper.ManagerStatisticModelRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 运营数据统计分析
 */
@Service
public class ManagerStatisticsRepository extends BasePageRepository {

    @Resource
    public JdbcTemplate jdbcTemplate;

    /**
     * 运营管理综合日统计
     * @param managerStatisticQo
     * @return
     */
    public List<ManagerStatisticQo> managerDailyStatistic(ManagerStatisticQo managerStatisticQo) {
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" a.MESSAGE_DATE,");
        sqlBuffer.append(" IFNULL(b.MESSAGE_SUCCESS_NUM,0)MESSAGE_SUCCESS_NUM,");
        sqlBuffer.append(" IFNULL(ROUND(b.ACCOUNT_PRICE_SUM,2),0)ACCOUNT_PRICE_SUM,");
        sqlBuffer.append(" IFNULL(ROUND(b.CHANNEL_PRICE_SUM,2),0)CHANNEL_PRICE_SUM ");
        sqlBuffer.append(" from ( SELECT date_add( date_sub('"+managerStatisticQo.getStartDate()+"',interval 1 day), INTERVAL ( cast( help_topic_id AS signed INTEGER ) + 1 ) day ) MESSAGE_DATE FROM ");
        sqlBuffer.append("  mysql.help_topic WHERE help_topic_id < DATEDIFF('"+managerStatisticQo.getEndDate()+"',date_sub('"+managerStatisticQo.getStartDate()+"',interval 1 day)) ORDER BY help_topic_id  ");
        sqlBuffer.append(" )a left join ( ");
        sqlBuffer.append(" select t.MESSAGE_DATE,sum(t.MESSAGE_SUCCESS_NUM)MESSAGE_SUCCESS_NUM,sum(t.MESSAGE_SUCCESS_NUM*t.ACCOUNT_PRICE)ACCOUNT_PRICE_SUM,sum(t.MESSAGE_SUCCESS_NUM*t.CHANNEL_PRICE)CHANNEL_PRICE_SUM ");
        sqlBuffer.append(" from message_daily_statistics t where t.MESSAGE_DATE>=? and t.MESSAGE_DATE<=? group by t.MESSAGE_DATE order by t.MESSAGE_DATE  ");
        sqlBuffer.append(" )b on a.MESSAGE_DATE = b.MESSAGE_DATE order by a.MESSAGE_DATE  ");

        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(managerStatisticQo.getStartDate());
        paramsList.add(managerStatisticQo.getEndDate());

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<ManagerStatisticQo> list = this.queryForObjectList(sqlBuffer.toString(), params, new ManagerStatisticModelRowMapper());
        return list;
    }

    /**
     * 运营管理综合月统计
     * @param managerStatisticQo
     * @return
     */
    public List<ManagerStatisticQo> managerMonthStatistic(ManagerStatisticQo managerStatisticQo) {
        /**
         * 生成24个月
         * select
         * select date_format(date_add(date_add('2022-07-01', interval -23 month), interval row month),'%Y-%m') yearMonth from
         *  (
         *     select @row := @row + 1 as row from
         *     (select 0 union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) t1,
         *     (select 0 union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) t2,
         *     (select @row:=-1) t3
         *  ) t4
         *  where date_format(date_add(date_add('2022-07-01', interval -23 month), interval row month),'%Y-%m') <= date_format('2022-07-01','%Y-%m')
         */
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" DATE_FORMAT(t.MESSAGE_DATE, '%Y-%m')MESSAGE_DATE,");
        sqlBuffer.append(" sum(t.MESSAGE_SUCCESS_NUM)MESSAGE_SUCCESS_NUM,");
        sqlBuffer.append(" sum(t.MESSAGE_SUCCESS_NUM*t.ACCOUNT_PRICE)ACCOUNT_PRICE_SUM,");
        sqlBuffer.append(" sum(t.MESSAGE_SUCCESS_NUM*t.CHANNEL_PRICE)CHANNEL_PRICE_SUM ");
        sqlBuffer.append(" from message_daily_statistics t where DATE_FORMAT(t.MESSAGE_DATE, '%Y-%m')>=? and DATE_FORMAT(t.MESSAGE_DATE, '%Y-%m')<=? ");
        sqlBuffer.append(" group by DATE_FORMAT(t.MESSAGE_DATE, '%Y-%m') order by DATE_FORMAT(t.MESSAGE_DATE, '%Y-%m') ");

        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(managerStatisticQo.getStartDate());
        paramsList.add(managerStatisticQo.getEndDate());

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<ManagerStatisticQo> list = this.queryForObjectList(sqlBuffer.toString(), params, new ManagerStatisticModelRowMapper());
        return list;
    }
}
