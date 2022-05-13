package com.smoc.cloud.saler.repository;

import com.smoc.cloud.common.BasePageRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 统计数据查询
 */
@Service
public class SalerStatisticsRepository extends BasePageRepository {

    @Resource
    public JdbcTemplate jdbcTemplate;

    /**
     * 根据起始日期 统计客户短信成功发送量
     *
     * @param startDate 开始日期 yyyy-MM-dd
     * @param endDate   结束日期 yyyy-MM-dd
     * @return
     */
    public Long getMessageSendTotal(String startDate, String endDate,String salerId) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  IFNULL(sum(m.MESSAGE_SUCCESS_NUM),0) messageTotal ");
        sqlBuffer.append("  from message_daily_statistics m left join account_base_info a on m.BUSINESS_ACCOUNT = a.ACCOUNT_ID");
        sqlBuffer.append("  left join enterprise_basic_info e on a.ENTERPRISE_ID = e.ENTERPRISE_ID");
        sqlBuffer.append("  where m.MESSAGE_DATE>=? and m.MESSAGE_DATE<=? and e.SALER = ? ");

        Object[] params = new Object[3];
        params[0] = startDate;
        params[1] = endDate;
        params[2] = salerId;
        Long messageTotal = jdbcTemplate.queryForObject(sqlBuffer.toString(), params, Long.class);

        return messageTotal;

    }

    /**
     * 客户账户总余额
     * @return
     */
    public Map<String, Object> getCustomerUsableTotal(String salerId) {
        StringBuffer sqlBuffer = new StringBuffer("select");
        sqlBuffer.append("  IFNULL(sum(t.ACCOUNT_USABLE_SUM),0) ACCOUNT_USABLE_SUM ");
        sqlBuffer.append("  from finance_account t left join enterprise_basic_info e on t.ENTERPRISE_ID = e.ENTERPRISE_ID");
        sqlBuffer.append("  where e.SALER = ? ");

        Object[] params = new Object[1];
        params[0] = salerId;

        Map<String, Object> map = jdbcTemplate.queryForMap(sqlBuffer.toString(), params);
        return map;
    }

    /**
     * 客户消费总余额
     * @return
     */
    public Map<String, Object> getCustomerConsumeTotal(String startDate, String endDate,String salerId) {
        StringBuffer sqlBuffer = new StringBuffer("select");
        sqlBuffer.append("  IFNULL(sum(m.ACCOUNT_PRICE),0) ACCOUNT_CONSUME_SUM ");
        sqlBuffer.append("  from message_daily_statistics m left join account_base_info a on m.BUSINESS_ACCOUNT = a.ACCOUNT_ID");
        sqlBuffer.append("  left join enterprise_basic_info e on a.ENTERPRISE_ID = e.ENTERPRISE_ID");
        sqlBuffer.append("  where m.MESSAGE_DATE>=? and m.MESSAGE_DATE<=? and e.SALER = ? ");

        Object[] params = new Object[3];
        params[0] = startDate;
        params[1] = endDate;
        params[2] = salerId;

        Map<String, Object> map = jdbcTemplate.queryForMap(sqlBuffer.toString(), params);
        return map;
    }

}
