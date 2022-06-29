package com.smoc.cloud.scheduler.batch.filters.job;

import com.smoc.cloud.scheduler.batch.filters.listener.MessageFilterListener;
import com.smoc.cloud.scheduler.batch.filters.model.BusinessRouteValue;
import com.smoc.cloud.scheduler.batch.filters.model.BusinessRouteValueRowMapper;
import com.smoc.cloud.scheduler.batch.filters.processor.MessageFilterProcessor;
import com.smoc.cloud.scheduler.batch.filters.writer.MessageFilterWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 短信过滤JOB配置
 */
@Slf4j
@Configuration
public class MessageFilterJobConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Autowired
    public MessageFilterListener messageFilterListener;

    @Autowired
    public MessageFilterProcessor messageFilterProcessor;

    @Autowired
    public MessageFilterWriter messageFilterWriter;

    @Bean("messageFilterJob")
    public Job messageFilterJob() {
        return jobBuilderFactory.get("messageFilterJob").incrementer(new RunIdIncrementer()).listener(messageFilterListener).start(messageFilterJobConfigurationStep()).build();
    }

    /**
     * 分批次多线程处理 待发送的数据
     *
     * @return
     */
    @Bean
    public Step messageFilterJobConfigurationStep() {
        return stepBuilderFactory.get("messageFilterJobConfigurationStep")
                .<BusinessRouteValue, BusinessRouteValue>chunk(1000)
                .reader(messageFilterReader())
                .processor(messageFilterProcessor)
                .writer(deleteMessageFilterWriter())
                .taskExecutor(messageFilterExecutor())
                .throttleLimit(10)
                .build();
    }

    /**
     * 查询 route_message_mt_info 表中，等待发送的短信数据
     *
     * @return
     */
    @Bean
    @StepScope
    public JdbcPagingItemReader<BusinessRouteValue> messageFilterReader() {
        JdbcPagingItemReader<BusinessRouteValue> reader = new JdbcPagingItemReader<>();
        // 设置数据源
        reader.setDataSource(dataSource);
        // 设置一次最大读取条数
        reader.setFetchSize(10000);
        reader.setPageSize(10000);
        // 把数据库中的每条数据映射到BusinessRouteValueRowMapper对像中
        reader.setRowMapper(new BusinessRouteValueRowMapper());
        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause(" ID,ACCOUNT_ID,PHONE_NUMBER,SUBMIT_TIME,MESSAGE_CONTENT,MESSAGE_FORMAT,MESSAGE_ID,TEMPLATE_ID,PROTOCOL,ACCOUNT_SRC_ID,ACCOUNT_BUSINESS_CODE,PHONE_NUMBER_NUMBER,MESSAGE_CONTENT_NUMBER,REPORT_FLAG,OPTION_PARAM,DATE_FORMAT(CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME "); // 设置查询的列
        queryProvider.setFromClause(" from smoc_route.route_message_mt_info1 "); // 设置要查询的表
//        queryProvider.setWhereClause(" where 1=1 ");
        //设置排序
        queryProvider.setSortKeys(new HashMap<String, Order>() {{
            put("ID", Order.ASCENDING);
        }});
//        reader.setParameterValues(new HashMap<String, Object>() {{
//        }});
        reader.setQueryProvider(queryProvider);
        return reader;
    }

    @Bean
    @StepScope
    public CompositeItemProcessor<BusinessRouteValue, BusinessRouteValue> compositeMessageFilterProcessor() {
        CompositeItemProcessor<BusinessRouteValue, BusinessRouteValue> processor = new CompositeItemProcessor<>();
        List<ItemProcessor<BusinessRouteValue, BusinessRouteValue>> listProcessor = new ArrayList<>();
        listProcessor.add(messageFilterProcessor);
        processor.setDelegates(listProcessor);
        return processor;
    }

    @Bean
    public TaskExecutor messageFilterExecutor() {
        return new SimpleAsyncTaskExecutor("filterExecutor-");
    }

    /**
     * 删除 route_message_mt_info数据
     *
     * @return
     */
    @Bean("deleteMessageFilterWriter")
    public JdbcBatchItemWriter<BusinessRouteValue> deleteMessageFilterWriter() {
        JdbcBatchItemWriter<BusinessRouteValue> writer = new JdbcBatchItemWriter<>();
        //设置数据源
        writer.setDataSource(dataSource);
        //设置sql
        writer.setSql("delete from smoc_route.route_message_mt_info1 where id=:id");
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        return writer;
    }
}
