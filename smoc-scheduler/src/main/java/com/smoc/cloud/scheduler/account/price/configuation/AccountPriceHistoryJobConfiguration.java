package com.smoc.cloud.scheduler.account.price.configuation;

import com.smoc.cloud.common.smoc.customer.validator.AccountFinanceInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.scheduler.account.price.listener.AccountPriceHistoryListener;
import com.smoc.cloud.scheduler.account.price.processor.AccountPriceHistoryProcessor;
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
@Slf4j
@Configuration
public class AccountPriceHistoryJobConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Autowired
    public AccountPriceHistoryListener accountPriceHistoryListener;

    @Autowired
    private AccountPriceHistoryProcessor accountPriceHistoryProcessor;

    @Autowired
    private AccountPriceHistoryWriter accountPriceHistoryWriter;

    @Bean("accountPriceHistoryJob")
    public Job accountPriceHistoryJob() {
        return jobBuilderFactory.get("accountPriceHistoryJob").incrementer(new RunIdIncrementer()).listener(accountPriceHistoryListener).start(accountPriceJobConfigurationStep()).build();
    }

    /**
     * @return
     */
    @Bean
    public Step accountPriceJobConfigurationStep() {
        return stepBuilderFactory.get("accountPriceJobConfigurationStep")
                .<AccountFinanceInfoValidator, AccountFinanceInfoValidator>chunk(10)
                .reader(channelPriceReader())
                .processor(compositeAccountPriceProcessor())
                .writer(accountPriceHistoryWriter)
                .taskExecutor(accountPriceHistoryExecutor())
                .throttleLimit(5)
                .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<AccountFinanceInfoValidator> channelPriceReader() {
        JdbcPagingItemReader<AccountFinanceInfoValidator> reader = new JdbcPagingItemReader<>();
        // 设置数据源
        reader.setDataSource(dataSource);
        // 设置一次最大读取条数
        reader.setFetchSize(10);
        // 把数据库中的每条数据映射到ChannelPriceValidator对像中
        reader.setRowMapper(new AccountPriceRowMapper());
        //获取当前日期
        String today = DateTimeUtils.getDateFormat(new Date());
        //log.info("[today]:{}",today);
        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause(" ID,ACCOUNT_ID,'"+today+"' TODAY,TIMESTAMPDIFF(DAY,DATE_FORMAT(UPDATED_TIME, '%Y-%m-%d'),'"+today+"') DAYS "); // 设置查询的列
        queryProvider.setFromClause(" from smoc.account_finance_info "); // 设置要查询的表
        queryProvider.setWhereClause(" where DATE_FORMAT(UPDATED_TIME,'%Y-%m-%d') < :today ");
        queryProvider.setSortKeys(new HashMap<String, Order>() {{
            put("UPDATED_TIME", Order.ASCENDING);
        }});
        reader.setParameterValues(new HashMap<String, Object>() {{
            put("today", today);
        }});
        reader.setQueryProvider(queryProvider);// 设置排序列
        return reader;
    }



    @Bean
    @StepScope
    public CompositeItemProcessor<AccountFinanceInfoValidator, AccountFinanceInfoValidator> compositeAccountPriceProcessor() {
        CompositeItemProcessor<AccountFinanceInfoValidator, AccountFinanceInfoValidator> processor = new CompositeItemProcessor<>();
        List<ItemProcessor<AccountFinanceInfoValidator, AccountFinanceInfoValidator>> listProcessor = new ArrayList<>();
        listProcessor.add(accountPriceHistoryProcessor);
        processor.setDelegates(listProcessor);
        return processor;
    }

    @Bean
    public TaskExecutor accountPriceHistoryExecutor() {
        return new SimpleAsyncTaskExecutor("accountPriceHistoryExecutorTask-");
    }

}
