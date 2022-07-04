package com.smoc.cloud.scheduler.channel.price.configuation;

import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.scheduler.channel.price.listener.ChannelPriceFutureListener;
import com.smoc.cloud.scheduler.channel.price.processor.ChannelPriceFutureProcessor;
import com.smoc.cloud.scheduler.channel.price.service.model.ChannelFutruePriceModel;
import com.smoc.cloud.scheduler.channel.price.service.rowmapper.ChannelFutruePriceRowMapper;
import com.smoc.cloud.scheduler.channel.price.writer.ChannelPriceFutureWriter;
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
 * 通道未来价格 批处理
 */
@Slf4j
@Configuration
public class ChannelPriceFutureJobConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Autowired
    public ChannelPriceFutureListener channelPriceFutureListener;

    @Autowired
    private ChannelPriceFutureProcessor channelPriceFutureProcessor;

    @Autowired
    private ChannelPriceFutureWriter channelPriceFutureWriter;

    @Bean("channelPriceFutureJob")
    public Job channelPriceFutureJob() {
        return jobBuilderFactory.get("channelPriceFutureJob").incrementer(new RunIdIncrementer()).listener(channelPriceFutureListener).start(channelPriceFutureJobConfigurationStep()).build();
    }

    /**
     * @return
     */
    @Bean
    public Step channelPriceFutureJobConfigurationStep() {
        return stepBuilderFactory.get("channelPriceFutureJobConfigurationStep")
                .<ChannelFutruePriceModel, ChannelFutruePriceModel>chunk(10)
                .reader(channelPriceFutureReader())
                .processor(ChannelPriceFutureProcessor())
                .writer(channelPriceFutureWriter)
                .taskExecutor(channelPriceFutureExecutor())
                .throttleLimit(5)
                .build();
    }

    /**
     * 查询通道未来价格
     *
     * @return
     */
    @Bean
    @StepScope
    public JdbcPagingItemReader<ChannelFutruePriceModel> channelPriceFutureReader() {
        JdbcPagingItemReader<ChannelFutruePriceModel> reader = new JdbcPagingItemReader<>();
        // 设置数据源
        reader.setDataSource(dataSource);
        // 设置一次最大读取条数
        reader.setFetchSize(10);
        // 把数据库中的每条数据映射到ChannelPriceValidator对像中
        reader.setRowMapper(new ChannelFutruePriceRowMapper());
        //获取当前日期
        String today = DateTimeUtils.getDateFormat(new Date());
        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause(" t.ID,t.BUSINESS_ID,t.PRICE_AREA,t.CHANGE_PRICE,t.START_DATE,t.DATA_ID,a.PRICE_STYLE "); // 设置查询的列
        queryProvider.setFromClause(" from smoc.system_history_price_change_record t left join smoc.config_channel_basic_info a on t.BUSINESS_ID=a.CHANNEL_ID "); // 设置要查询的表
        queryProvider.setWhereClause(" where t.TASK_TYPE=2 and t.TASK_STATUS=0 and t.CHANGE_TYPE='CHANNEL'  and  t.START_DATE = :today ");
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
    public CompositeItemProcessor<ChannelFutruePriceModel, ChannelFutruePriceModel> ChannelPriceFutureProcessor() {
        CompositeItemProcessor<ChannelFutruePriceModel, ChannelFutruePriceModel> processor = new CompositeItemProcessor<>();
        List<ItemProcessor<ChannelFutruePriceModel, ChannelFutruePriceModel>> listProcessor = new ArrayList<>();
        listProcessor.add(channelPriceFutureProcessor);
        processor.setDelegates(listProcessor);
        return processor;
    }

    @Bean
    public TaskExecutor channelPriceFutureExecutor() {
        return new SimpleAsyncTaskExecutor("channelPriceFutureExecutorTask-");
    }

}
