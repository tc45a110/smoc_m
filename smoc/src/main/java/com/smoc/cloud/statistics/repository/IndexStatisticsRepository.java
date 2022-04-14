package com.smoc.cloud.statistics.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
        sqlBuffer.append("  where (1=1) m.MESSAGE_DATE>=? && m.MESSAGE_DATE<=?");

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
        sqlBuffer.append("  where (1=1) m.MESSAGE_DATE>=? && m.MESSAGE_DATE<=?");

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
        sqlBuffer.append("  where (1=1) m.MESSAGE_DATE>=? && m.MESSAGE_DATE<=?");

        Object[] params = new Object[2];
        params[0] = startDate;
        params[1] = endDate;
        Long activeChannel = jdbcTemplate.queryForObject(sqlBuffer.toString(), params, Long.class);

        return activeChannel;

    }


}
