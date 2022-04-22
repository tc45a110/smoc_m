package com.smoc.cloud.statistics.repository;

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
public class IndexStatisticsRepository {

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
}
