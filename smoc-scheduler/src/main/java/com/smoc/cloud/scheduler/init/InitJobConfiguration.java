package com.smoc.cloud.scheduler.init;

import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.scheduler.account.price.listener.AccountPriceHistoryListener;
import com.smoc.cloud.scheduler.account.price.processor.AccountPriceHistoryProcessor;
import com.smoc.cloud.scheduler.account.price.service.model.AccountPriceModel;
import com.smoc.cloud.scheduler.account.price.service.rowmapper.AccountPriceRowMapper;
import com.smoc.cloud.scheduler.account.price.writer.AccountPriceHistoryWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 业务账号价格历史 批处理
 */
//@Slf4j
//@Configuration
public class InitJobConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Autowired
    public InitListener initListener;

    @Autowired
    private InitProcessor initProcessor;

    @Autowired
    private InitWriter initWriter;

    @Bean("initJob")
    public Job initJob() {
        return jobBuilderFactory.get("initJob").incrementer(new RunIdIncrementer()).listener(initListener).start(InitJobConfigurationStep()).build();
    }

    /**
     * @return
     */
    @Bean
    public Step InitJobConfigurationStep() {
        return stepBuilderFactory.get("InitJobConfigurationStep").<InitModel, InitModel>chunk(10).reader(initReader()).processor(compositeInitProcessor()).writer(initWriter).taskExecutor(initExecutor()).throttleLimit(5).build();
    }

    /**
     * 查询非今天创建、批处理日期小于今天的数据
     *
     * @return
     */
    @Bean
    @StepScope
    public JdbcPagingItemReader<InitModel> initReader() {
        JdbcPagingItemReader<InitModel> reader = new JdbcPagingItemReader<>();
        // 设置数据源
        reader.setDataSource(dataSource);
        // 设置一次最大读取条数
        reader.setFetchSize(10);
        // 把数据库中的每条数据映射到ChannelPriceValidator对像中
        reader.setRowMapper(new InitRowMapper());
        //获取当前日期
        String today = DateTimeUtils.getDateFormat(new Date());
        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause(" ID,ACCOUNT_ID,CARRIER,CHANNEL_ID,CREATED_TIME "); // 设置查询的列
        queryProvider.setFromClause(" from smoc.account_channel_info "); // 设置要查询的表
        queryProvider.setWhereClause(" where DATE_FORMAT(CREATED_TIME,'%Y-%m-%d') < :today ");
        queryProvider.setSortKeys(new HashMap<String, Order>() {{
            put("CREATED_TIME", Order.DESCENDING);
        }});
        reader.setParameterValues(new HashMap<String, Object>() {{
            put("today", today);
        }});
        reader.setQueryProvider(queryProvider);// 设置排序列
        return reader;
    }


    @Bean
    @StepScope
    public CompositeItemProcessor<InitModel, InitModel> compositeInitProcessor() {
        CompositeItemProcessor<InitModel, InitModel> processor = new CompositeItemProcessor<>();
        List<ItemProcessor<InitModel, InitModel>> listProcessor = new ArrayList<>();
        listProcessor.add(initProcessor);
        processor.setDelegates(listProcessor);
        return processor;
    }

    @Bean
    public TaskExecutor initExecutor() {
        return new SimpleAsyncTaskExecutor("initExecutorTask-");
    }

}
