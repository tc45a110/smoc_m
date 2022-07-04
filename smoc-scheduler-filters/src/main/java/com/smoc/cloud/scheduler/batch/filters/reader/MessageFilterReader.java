package com.smoc.cloud.scheduler.batch.filters.reader;

import com.smoc.cloud.scheduler.batch.filters.model.BusinessRouteValue;
import com.smoc.cloud.scheduler.batch.filters.model.BusinessRouteValueRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;

@Slf4j
@Component
public class MessageFilterReader {

    @Autowired
    public DataSource dataSource;

    private Integer fetchSize = 1000;

    private Integer pageSize = 1000;

    /**
     * 查询 route_message_mt_info 表中，等待发送的短信数据
     *
     * @return
     */
    @Bean("messageFilterReader0")
    @StepScope
    public JdbcPagingItemReader<BusinessRouteValue> messageFilterReader0() {
        return reader("0");
    }

    /**
     * 查询 route_message_mt_info 表中，等待发送的短信数据
     *
     * @return
     */
    @Bean("messageFilterReader1")
    @StepScope
    public JdbcPagingItemReader<BusinessRouteValue> messageFilterReader1() {
        return reader("1");
    }

    /**
     * 查询 route_message_mt_info 表中，等待发送的短信数据
     *
     * @return
     */
    @Bean("messageFilterReader2")
    @StepScope
    public JdbcPagingItemReader<BusinessRouteValue> messageFilterReader2() {
        return reader("2");
    }

    /**
     * 查询 route_message_mt_info 表中，等待发送的短信数据
     *
     * @return
     */
    @Bean("messageFilterReader3")
    @StepScope
    public JdbcPagingItemReader<BusinessRouteValue> messageFilterReader3() {
        return reader("3");
    }

    /**
     * 查询 route_message_mt_info 表中，等待发送的短信数据
     *
     * @return
     */
    @Bean("messageFilterReader4")
    @StepScope
    public JdbcPagingItemReader<BusinessRouteValue> messageFilterReader4() {
        return reader("4");
    }

    /**
     * 查询 route_message_mt_info 表中，等待发送的短信数据
     *
     * @return
     */
    @Bean("messageFilterReader5")
    @StepScope
    public JdbcPagingItemReader<BusinessRouteValue> messageFilterReader5() {
        return reader("5");
    }

    /**
     * 查询 route_message_mt_info 表中，等待发送的短信数据
     *
     * @return
     */
    @Bean("messageFilterReader6")
    @StepScope
    public JdbcPagingItemReader<BusinessRouteValue> messageFilterReader6() {
        return reader("6");
    }

    /**
     * 查询 route_message_mt_info 表中，等待发送的短信数据
     *
     * @return
     */
    @Bean("messageFilterReader7")
    @StepScope
    public JdbcPagingItemReader<BusinessRouteValue> messageFilterReader7() {
        return reader("7");
    }

    /**
     * 查询 route_message_mt_info 表中，等待发送的短信数据
     *
     * @return
     */
    @Bean("messageFilterReader8")
    @StepScope
    public JdbcPagingItemReader<BusinessRouteValue> messageFilterReader8() {
        return reader("8");
    }

    /**
     * 查询 route_message_mt_info 表中，等待发送的短信数据
     *
     * @return
     */
    @Bean("messageFilterReader9")
    @StepScope
    public JdbcPagingItemReader<BusinessRouteValue> messageFilterReader9() {
        return reader("9");
    }

    public JdbcPagingItemReader<BusinessRouteValue> reader(String index) {
        JdbcPagingItemReader<BusinessRouteValue> reader = new JdbcPagingItemReader<>();
        // 设置数据源
        reader.setDataSource(dataSource);
        // 设置一次最大读取条数
        reader.setFetchSize(fetchSize);
        reader.setPageSize(pageSize);
        // 把数据库中的每条数据映射到BusinessRouteValueRowMapper对像中
        reader.setRowMapper(new BusinessRouteValueRowMapper());
        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause(" ID,ACCOUNT_ID,PHONE_NUMBER,SUBMIT_TIME,MESSAGE_CONTENT,MESSAGE_FORMAT,MESSAGE_ID,TEMPLATE_ID,PROTOCOL,ACCOUNT_SRC_ID,ACCOUNT_BUSINESS_CODE,PHONE_NUMBER_NUMBER,MESSAGE_CONTENT_NUMBER,REPORT_FLAG,OPTION_PARAM,DATE_FORMAT(CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME "); // 设置查询的列
        queryProvider.setFromClause(" from smoc_route.route_message_mt_info1 "); // 设置要查询的表
        queryProvider.setWhereClause(" where right(phone_number,1) = ? ");
        //设置排序
        queryProvider.setSortKeys(new HashMap<String, Order>() {{
            put("ID", Order.ASCENDING);
        }});
        reader.setParameterValues(new HashMap<String, Object>() {{
            put("index", index);
        }});
        reader.setQueryProvider(queryProvider);
        return reader;
    }
}
