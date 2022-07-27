package com.smoc.cloud.spss.repository;

import com.smoc.cloud.common.BasePageRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 首页统计数据查询
 */
@Service
public class IndexStatisticsDataRepository extends BasePageRepository {

    @Resource
    public JdbcTemplate jdbcTemplate;

    /**
     * 营收总额
     * @param startDate
     * @param endDate
     * @return
     */
    public Map<String, Object> getProfitSum(String startDate, String endDate) {
        StringBuffer sql = new StringBuffer("select");
        sql.append("  IFNULL(ROUND(sum(t.ACCOUNT_PRICE*t.MESSAGE_SUCCESS_NUM)),0) ACCOUNT_PROFIT_SUM, ");
        sql.append("  IFNULL(ROUND(sum(t.CHANNEL_PRICE*t.MESSAGE_SUCCESS_NUM)),0) CHANNEL_PROFIT_SUM ");
        sql.append("  from message_daily_statistics t ");
        sql.append("  where t.MESSAGE_DATE>=? and t.MESSAGE_DATE<=?");

        Object[] params = new Object[2];
        params[0] = startDate;
        params[1] = endDate;

        Map<String, Object> map = jdbcTemplate.queryForMap(sql.toString(),params);
        return map;
    }

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
     *  统计本年度新增账户
     * @return
     */
    public Long getAccountCountByYear(String year) {
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  count(*) totalAccount ");
        sqlBuffer.append("  from account_base_info t ");
        sqlBuffer.append("  where DATE_FORMAT(t.CREATED_TIME, '%Y')=? ");
        sqlBuffer.append("  group by DATE_FORMAT(t.CREATED_TIME, '%Y') ");

        Object[] params = new Object[1];
        params[0] = year;

        Long activeAccount = jdbcTemplate.queryForObject(sqlBuffer.toString(),params, Long.class);

        return activeAccount;
    }

    /**
     *  统计所有通道
     * @return
     */
    public Long getChannelCount() {
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  count(*) totalChannel ");
        sqlBuffer.append("  from config_channel_basic_info m ");

        Long activeAccount = jdbcTemplate.queryForObject(sqlBuffer.toString(), Long.class);

        return activeAccount;
    }

    /**
     *  统计本年度新增通道
     * @return
     */
    public Long getChannelCountByYear(String year) {
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  count(*) totalChannel ");
        sqlBuffer.append("  from config_channel_basic_info t ");
        sqlBuffer.append("  where DATE_FORMAT(t.CREATED_TIME, '%Y')=? ");
        sqlBuffer.append("  group by DATE_FORMAT(t.CREATED_TIME, '%Y') ");

        Object[] params = new Object[1];
        params[0] = year;

        Long activeChannel = jdbcTemplate.queryForObject(sqlBuffer.toString(),params, Long.class);

        return activeChannel;
    }


}
