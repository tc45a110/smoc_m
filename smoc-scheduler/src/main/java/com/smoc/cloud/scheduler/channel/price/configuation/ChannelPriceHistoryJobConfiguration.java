package com.smoc.cloud.scheduler.channel.price.configuation;

import com.smoc.cloud.common.smoc.configuate.validator.ChannelPriceValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.scheduler.channel.price.listener.ChannelPriceHistoryListener;
import com.smoc.cloud.scheduler.channel.price.processor.ChannelPriceHistoryProcessor;
import com.smoc.cloud.scheduler.channel.price.service.rowmapper.ChannelPriceRowMapper;
import com.smoc.cloud.scheduler.channel.price.writer.ChannelPriceHistoryWriter;
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
 * 通道价格历史 批处理
 */
@Slf4j
@Configuration
public class ChannelPriceHistoryJobConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Autowired
    public ChannelPriceHistoryListener channelPriceHistoryListener;

    @Autowired
    private ChannelPriceHistoryProcessor channelPriceHistoryProcessor;

    @Autowired
    private ChannelPriceHistoryWriter channelPriceHistoryWriter;

    @Bean("channelPriceHistoryJob")
    public Job channelPriceHistoryJob() {
        return jobBuilderFactory.get("channelPriceHistoryJob").incrementer(new RunIdIncrementer()).listener(channelPriceHistoryListener).start(channelPriceJobConfigurationStep()).build();
    }

    /**
     * @return
     */
    @Bean
    public Step channelPriceJobConfigurationStep() {
        return stepBuilderFactory.get("channelPriceJobConfigurationStep")
                .<ChannelPriceValidator, ChannelPriceValidator>chunk(10)
                .reader(channelPriceReader())
                .processor(compositeChannelPriceProcessor())
                .writer(channelPriceHistoryWriter)
                .taskExecutor(channelPriceHistoryExecutor())
                .throttleLimit(5)
                .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<ChannelPriceValidator> channelPriceReader() {
        JdbcPagingItemReader<ChannelPriceValidator> reader = new JdbcPagingItemReader<>();
        // 设置数据源
        reader.setDataSource(dataSource);
        // 设置一次最大读取条数
        reader.setFetchSize(10);
        // 把数据库中的每条数据映射到ChannelPriceValidator对像中
        reader.setRowMapper(new ChannelPriceRowMapper());
        //获取当前日期
        String today = DateTimeUtils.getDateFormat(new Date());
        //log.info("[today]:{}",today);
        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause(" ID,CHANNEL_ID,PRICE_STYLE,AREA_CODE,CHANNEL_PRICE,LASTTIME_HISTORY,'"+today+"' TODAY,TIMESTAMPDIFF(DAY,DATE_FORMAT(LASTTIME_HISTORY, '%Y-%m-%d'),'"+today+"') DAYS "); // 设置查询的列
        queryProvider.setFromClause(" from smoc.config_channel_price "); // 设置要查询的表
        queryProvider.setWhereClause(" where DATE_FORMAT(LASTTIME_HISTORY,'%Y-%m-%d') < :today ");
        queryProvider.setSortKeys(new HashMap<String, Order>() {{
            put("LASTTIME_HISTORY", Order.ASCENDING);
        }});
        reader.setParameterValues(new HashMap<String, Object>() {{
            put("today", today);
        }});
        reader.setQueryProvider(queryProvider);// 设置排序列
        return reader;
    }



    @Bean
    @StepScope
    public CompositeItemProcessor<ChannelPriceValidator, ChannelPriceValidator> compositeChannelPriceProcessor() {
        CompositeItemProcessor<ChannelPriceValidator, ChannelPriceValidator> processor = new CompositeItemProcessor<>();
        List<ItemProcessor<ChannelPriceValidator, ChannelPriceValidator>> listProcessor = new ArrayList<>();
        listProcessor.add(channelPriceHistoryProcessor);
        processor.setDelegates(listProcessor);
        return processor;
    }

    @Bean
    public TaskExecutor channelPriceHistoryExecutor() {
        return new SimpleAsyncTaskExecutor("channelPriceHistoryExecutor-");
    }

}
