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

    private Integer fetchSize = 2000;

    private Integer pageSize = 2000;

    /**
     * 查询 route_message_mt_info 表中，等待发送的短信数据
     *
     * @return
     */
    @Bean("messageFilterReader0")
    @StepScope
    public JdbcPagingItemReader<BusinessRouteValue> messageFilterReader0() {
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
        queryProvider.setWhereClause(" where right(phone_number,1) = '0' ");
        //设置排序
        queryProvider.setSortKeys(new HashMap<String, Order>() {{
            put("ID", Order.ASCENDING);
        }});
//        reader.setParameterValues(new HashMap<String, Object>() {{
//            put("index", index);
//        }});
        reader.setQueryProvider(queryProvider);
        return reader;
    }

    /**
     * 查询 route_message_mt_info 表中，等待发送的短信数据
     *
     * @return
     */
    @Bean("messageFilterReader1")
    @StepScope
    public JdbcPagingItemReader<BusinessRouteValue> messageFilterReader1() {
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
        queryProvider.setWhereClause(" where right(phone_number,1) = '1' ");
        //设置排序
        queryProvider.setSortKeys(new HashMap<String, Order>() {{
            put("ID", Order.ASCENDING);
        }});
//        reader.setParameterValues(new HashMap<String, Object>() {{
//            put("index", index);
//        }});
        reader.setQueryProvider(queryProvider);
        return reader;
    }

    /**
     * 查询 route_message_mt_info 表中，等待发送的短信数据
     *
     * @return
     */
    @Bean("messageFilterReader2")
    @StepScope
    public JdbcPagingItemReader<BusinessRouteValue> messageFilterReader2() {
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
        queryProvider.setWhereClause(" where right(phone_number,1) = '2' ");
        //设置排序
        queryProvider.setSortKeys(new HashMap<String, Order>() {{
            put("ID", Order.ASCENDING);
        }});
//        reader.setParameterValues(new HashMap<String, Object>() {{
//            put("index", index);
//        }});
        reader.setQueryProvider(queryProvider);
        return reader;
    }

    /**
     * 查询 route_message_mt_info 表中，等待发送的短信数据
     *
     * @return
     */
    @Bean("messageFilterReader3")
    @StepScope
    public JdbcPagingItemReader<BusinessRouteValue> messageFilterReader3() {
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
        queryProvider.setWhereClause(" where right(phone_number,1) = '3' ");
        //设置排序
        queryProvider.setSortKeys(new HashMap<String, Order>() {{
            put("ID", Order.ASCENDING);
        }});
//        reader.setParameterValues(new HashMap<String, Object>() {{
//            put("index", index);
//        }});
        reader.setQueryProvider(queryProvider);
        return reader;
    }

    /**
     * 查询 route_message_mt_info 表中，等待发送的短信数据
     *
     * @return
     */
    @Bean("messageFilterReader4")
    @StepScope
    public JdbcPagingItemReader<BusinessRouteValue> messageFilterReader4() {
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
        queryProvider.setWhereClause(" where right(phone_number,1) = '4' ");
        //设置排序
        queryProvider.setSortKeys(new HashMap<String, Order>() {{
            put("ID", Order.ASCENDING);
        }});
//        reader.setParameterValues(new HashMap<String, Object>() {{
//            put("index", index);
//        }});
        reader.setQueryProvider(queryProvider);
        return reader;
    }

    /**
     * 查询 route_message_mt_info 表中，等待发送的短信数据
     *
     * @return
     */
    @Bean("messageFilterReader5")
    @StepScope
    public JdbcPagingItemReader<BusinessRouteValue> messageFilterReader5() {
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
        queryProvider.setWhereClause(" where right(phone_number,1) = '5' ");
        //设置排序
        queryProvider.setSortKeys(new HashMap<String, Order>() {{
            put("ID", Order.ASCENDING);
        }});
//        reader.setParameterValues(new HashMap<String, Object>() {{
//            put("index", index);
//        }});
        reader.setQueryProvider(queryProvider);
        return reader;
    }

    /**
     * 查询 route_message_mt_info 表中，等待发送的短信数据
     *
     * @return
     */
    @Bean("messageFilterReader6")
    @StepScope
    public JdbcPagingItemReader<BusinessRouteValue> messageFilterReader6() {
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
        queryProvider.setWhereClause(" where right(phone_number,1) = '6' ");
        //设置排序
        queryProvider.setSortKeys(new HashMap<String, Order>() {{
            put("ID", Order.ASCENDING);
        }});
//        reader.setParameterValues(new HashMap<String, Object>() {{
//            put("index", index);
//        }});
        reader.setQueryProvider(queryProvider);
        return reader;
    }

    /**
     * 查询 route_message_mt_info 表中，等待发送的短信数据
     *
     * @return
     */
    @Bean("messageFilterReader7")
    @StepScope
    public JdbcPagingItemReader<BusinessRouteValue> messageFilterReader7() {
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
        queryProvider.setWhereClause(" where right(phone_number,1) = '7' ");
        //设置排序
        queryProvider.setSortKeys(new HashMap<String, Order>() {{
            put("ID", Order.ASCENDING);
        }});
//        reader.setParameterValues(new HashMap<String, Object>() {{
//            put("index", index);
//        }});
        reader.setQueryProvider(queryProvider);
        return reader;
    }

    /**
     * 查询 route_message_mt_info 表中，等待发送的短信数据
     *
     * @return
     */
    @Bean("messageFilterReader8")
    @StepScope
    public JdbcPagingItemReader<BusinessRouteValue> messageFilterReader8() {
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
        queryProvider.setWhereClause(" where right(phone_number,1) = '8' ");
        //设置排序
        queryProvider.setSortKeys(new HashMap<String, Order>() {{
            put("ID", Order.ASCENDING);
        }});
//        reader.setParameterValues(new HashMap<String, Object>() {{
//            put("index", index);
//        }});
        reader.setQueryProvider(queryProvider);
        return reader;
    }

    /**
     * 查询 route_message_mt_info 表中，等待发送的短信数据
     *
     * @return
     */
    @Bean("messageFilterReader9")
    @StepScope
    public JdbcPagingItemReader<BusinessRouteValue> messageFilterReader9() {
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
        queryProvider.setWhereClause(" where right(phone_number,1) = '9' ");
        //设置排序
        queryProvider.setSortKeys(new HashMap<String, Order>() {{
            put("ID", Order.ASCENDING);
        }});
//        reader.setParameterValues(new HashMap<String, Object>() {{
//            put("index", index);
//        }});
        reader.setQueryProvider(queryProvider);
        return reader;
    }
}
