package com.smoc.cloud.statistics.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import com.smoc.cloud.common.smoc.customer.qo.StatisticProfitData;
import com.smoc.cloud.common.smoc.index.CheckRemindModel;
import com.smoc.cloud.customer.rowmapper.AccountStatisticSendRowMapper;
import com.smoc.cloud.statistics.rowmapper.StatisticCheckRemindRowMapper;
import com.smoc.cloud.statistics.rowmapper.StatisticProfitRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 首页统计数据查询
 */
@Service
public class IndexStatisticsRepository  extends BasePageRepository {

    @Resource
    public JdbcTemplate jdbcTemplate;

    /**
     * 根据起始日期 统计平台短信成功发送量
     *
     * @param startDate 开始日期 yyyy-MM-dd
     * @param endDate   结束日期 yyyy-MM-dd
     * @return
     */
    public Long getMessageSendTotal(String startDate, String endDate) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  sum(m.MESSAGE_SUCCESS_NUM) messageTotal ");
        sqlBuffer.append("  from message_daily_statistics m ");
        sqlBuffer.append("  where m.MESSAGE_DATE>=? and m.MESSAGE_DATE<=?");

        Object[] params = new Object[2];
        params[0] = startDate;
        params[1] = endDate;
        Long messageTotal = jdbcTemplate.queryForObject(sqlBuffer.toString(), params, Long.class);

        return messageTotal;

    }

    /**
     * 根据起始日期 统计业务账号活跃数
     *
     * @param startDate 开始日期 yyyy-MM-dd
     * @param endDate   结束日期 yyyy-MM-dd
     * @return
     */
    public Long getActiveAccount(String startDate, String endDate) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  count(distinct BUSINESS_ACCOUNT) activeAccount ");
        sqlBuffer.append("  from message_daily_statistics m ");
        sqlBuffer.append("  where m.MESSAGE_DATE>=? and m.MESSAGE_DATE<=?");

        Object[] params = new Object[2];
        params[0] = startDate;
        params[1] = endDate;
        Long activeAccount = jdbcTemplate.queryForObject(sqlBuffer.toString(), params, Long.class);

        return activeAccount;

    }

    /**
     * 根据起始日期 统计通道活跃数
     *
     * @param startDate 开始日期 yyyy-MM-dd
     * @param endDate   结束日期 yyyy-MM-dd
     * @return
     */
    public Long getActiveChannel(String startDate, String endDate) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  count(distinct CHANNEL_ID) activeChannel ");
        sqlBuffer.append("  from message_daily_statistics m ");
        sqlBuffer.append("  where m.MESSAGE_DATE>=? and m.MESSAGE_DATE<=?");

        Object[] params = new Object[2];
        params[0] = startDate;
        params[1] = endDate;
        Long activeChannel = jdbcTemplate.queryForObject(sqlBuffer.toString(), params, Long.class);

        return activeChannel;

    }

    /**
     *  统计所有业务账号
     * @return
     */
    public Long getAccountCount() {
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  count(*) totalAccount ");
        sqlBuffer.append("  from account_base_info m ");

        Long activeAccount = jdbcTemplate.queryForObject(sqlBuffer.toString(), Long.class);

        return activeAccount;
    }

    /**
     * 账户总余额
     * @return
     */
    public Map<String, Object> getCountUsableAccount() {
        StringBuffer sql = new StringBuffer("select");
        sql.append("  sum(t.ACCOUNT_USABLE_SUM) ACCOUNT_USABLE_SUM ");
        sql.append("  from finance_account t ");

        Map<String, Object> map = jdbcTemplate.queryForMap(sql.toString());
        return map;
    }

    /**
     * 营收总额
     * @param startDate
     * @param endDate
     * @return
     */
    public Map<String, Object> getProfitSum(String startDate, String endDate) {
        StringBuffer sql = new StringBuffer("select");
        sql.append("  IFNULL(ROUND(sum(t.ACCOUNT_PRICE)-sum(t.CHANNEL_PRICE)),0) PROFIT_SUM ");
        sql.append("  from message_daily_statistics t ");
        sql.append("  where t.MESSAGE_DATE>=? and t.MESSAGE_DATE<=?");

        Object[] params = new Object[2];
        params[0] = startDate;
        params[1] = endDate;

        Map<String, Object> map = jdbcTemplate.queryForMap(sql.toString(),params);
        return map;
    }

    /**
     * 近12个月营业收入
     * @param statisticProfitData
     * @return
     */
    public List<StatisticProfitData> statisticProfitMonth(StatisticProfitData statisticProfitData) {
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  a.MONTH_DAY");
        sqlBuffer.append(", IFNULL(b.PROFIT_NUM,0)PROFIT_NUM from ");
        sqlBuffer.append(" (SELECT @s :=@s + 1 as `INDEX`, DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL @s MONTH),'%Y-%m') AS `MONTH_DAY` ");
        sqlBuffer.append("  from mysql.help_topic, (SELECT @s := -1) temp WHERE  @s < 11 ORDER BY MONTH_DAY desc");
        sqlBuffer.append(" )a  left join ");
        sqlBuffer.append(" (SELECT DATE_FORMAT(t.MESSAGE_DATE, '%Y-%m')MESSAGE_DATE, ROUND((sum(t.ACCOUNT_PRICE)-sum(t.CHANNEL_PRICE))/10000,2) PROFIT_NUM ");
        sqlBuffer.append(" FROM message_daily_statistics t ");
        sqlBuffer.append(" GROUP BY DATE_FORMAT(t.MESSAGE_DATE, '%Y-%m')");
        sqlBuffer.append(" )b on a.MONTH_DAY = b.MESSAGE_DATE  order by a.MONTH_DAY asc");

        List<StatisticProfitData> list = this.queryForObjectList(sqlBuffer.toString(), null, new StatisticProfitRowMapper());
        return list;
    }

    /**
     * 首页:签名资质、web模板、待下发短信提醒
     * @param checkRemindModel
     * @return
     */
    public CheckRemindModel remindCheck(CheckRemindModel checkRemindModel) {
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder();
        sqlBuffer.append(" select count(*)TOTAL_NUM from enterprise_document_info t where t.DOC_STATUS=3 ");
        sqlBuffer.append(" UNION ALL ");
        sqlBuffer.append(" select count(*)TOTAL_NUM from account_template_info t where t.TEMPLATE_STATUS=3 and TEMPLATE_AGREEMENT_TYPE!='CMPP' ");
        sqlBuffer.append(" UNION ALL ");
        sqlBuffer.append(" select count(*)TOTAL_NUM from smoc_route.route_audit_message_mt_info t where t.AUDIT_FLAG=0 ");

        List<CheckRemindModel> list = this.queryForObjectList(sqlBuffer.toString(), null, new StatisticCheckRemindRowMapper());

        CheckRemindModel checkRemind = new CheckRemindModel();
        checkRemind.setSignNum(list.get(0).getTotalNum());
        checkRemind.setTemplateNum(list.get(1).getTotalNum());
        checkRemind.setMessageNum(list.get(2).getTotalNum());
        checkRemind.setTotalNum(list.get(0).getTotalNum()+list.get(1).getTotalNum()+list.get(2).getTotalNum());

        return checkRemind;
    }
}
