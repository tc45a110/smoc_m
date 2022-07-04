package com.smoc.cloud.scheduler.account.price.configuation;

import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.scheduler.account.price.listener.AccountPriceFutureListener;
import com.smoc.cloud.scheduler.account.price.listener.AccountPriceHistoryListener;
import com.smoc.cloud.scheduler.account.price.processor.AccountPriceFutureProcessor;
import com.smoc.cloud.scheduler.account.price.processor.AccountPriceHistoryProcessor;
import com.smoc.cloud.scheduler.account.price.service.model.AccountFutruePriceModel;
import com.smoc.cloud.scheduler.account.price.service.model.AccountFutruePriceModel;
import com.smoc.cloud.scheduler.account.price.service.rowmapper.AccountFutruePriceRowMapper;
import com.smoc.cloud.scheduler.account.price.service.rowmapper.AccountPriceRowMapper;
import com.smoc.cloud.scheduler.account.price.writer.AccountPriceFutureWriter;
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
 * 业务账号未来价格 批处理
 */
@Slf4j
@Configuration
public class AccountPriceFutureJobConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Autowired
    public AccountPriceFutureListener accountPriceFutureListener;

    @Autowired
    private AccountPriceFutureProcessor accountPriceFutureProcessor;

    @Autowired
    private AccountPriceFutureWriter accountPriceFutureWriter;

    @Bean("accountPriceFutureJob")
    public Job accountPriceHistoryJob() {
        return jobBuilderFactory.get("accountPriceFutureJob").incrementer(new RunIdIncrementer()).listener(accountPriceFutureListener).start(accountPriceFutureJobConfigurationStep()).build();
    }

    /**
     * @return
     */
    @Bean
    public Step accountPriceFutureJobConfigurationStep() {
        return stepBuilderFactory.get("accountPriceFutureJobConfigurationStep")
                .<AccountFutruePriceModel, AccountFutruePriceModel>chunk(10)
                .reader(accountPriceFutureReader())
                .processor(AccountPriceFutureProcessor())
                .writer(accountPriceFutureWriter)
                .taskExecutor(accountPriceFutureExecutor())
                .throttleLimit(5)
                .build();
    }

    /**
     * 查询非今天创建、批处理日期小于今天的数据
     *
     * @return
     */
    @Bean
    @StepScope
    public JdbcPagingItemReader<AccountFutruePriceModel> accountPriceFutureReader() {
        JdbcPagingItemReader<AccountFutruePriceModel> reader = new JdbcPagingItemReader<>();
        // 设置数据源
        reader.setDataSource(dataSource);
        // 设置一次最大读取条数
        reader.setFetchSize(10);
        // 把数据库中的每条数据映射到ChannelPriceValidator对像中
        reader.setRowMapper(new AccountFutruePriceRowMapper());
        //获取当前日期
        String today = DateTimeUtils.getDateFormat(new Date());
        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause(" t.ID,t.BUSINESS_ID,t.PRICE_AREA,t.CHANGE_PRICE,t.START_DATE,t.DATA_ID,a.CARRIER CARRIER_TYPE "); // 设置查询的列
        queryProvider.setFromClause(" from smoc.system_history_price_change_record t left join smoc.account_base_info a on t.BUSINESS_ID=a.ACCOUNT_ID "); // 设置要查询的表
        queryProvider.setWhereClause(" where t.TASK_TYPE=2 and t.TASK_STATUS=0 and t.CHANGE_TYPE='ACCOUNT'  and  t.START_DATE = :today ");
        queryProvider.setSortKeys(new HashMap<String, Order>() {{
            put("t.START_DATE", Order.ASCENDING);
        }});
        reader.setParameterValues(new HashMap<String, Object>() {{
            put("today", today);
        }});
        reader.setQueryProvider(queryProvider);// 设置排序列
        return reader;
    }


    @Bean
    @StepScope
    public CompositeItemProcessor<AccountFutruePriceModel, AccountFutruePriceModel> AccountPriceFutureProcessor() {
        CompositeItemProcessor<AccountFutruePriceModel, AccountFutruePriceModel> processor = new CompositeItemProcessor<>();
        List<ItemProcessor<AccountFutruePriceModel, AccountFutruePriceModel>> listProcessor = new ArrayList<>();
        listProcessor.add(accountPriceFutureProcessor);
        processor.setDelegates(listProcessor);
        return processor;
    }

    @Bean
    public TaskExecutor accountPriceFutureExecutor() {
        return new SimpleAsyncTaskExecutor("accountPriceFutureExecutorTask-");
    }

}
