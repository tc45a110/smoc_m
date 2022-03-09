package com.smoc.cloud.scheduler.channel.price.configuation;

import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.scheduler.channel.price.listener.NewDataChannelPriceHistoryListener;
import com.smoc.cloud.scheduler.channel.price.processor.NewDataChannelPriceHistoryProcessor;
import com.smoc.cloud.scheduler.channel.price.service.model.ChannelPriceModel;
import com.smoc.cloud.scheduler.channel.price.service.rowmapper.ChannelPriceRowMapper;
import com.smoc.cloud.scheduler.channel.price.writer.NewDataChannelPriceHistoryWriter;
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
 * 通道价格历史 批处理 当天创建数据
 * 对新数据进行特殊处理
 */
@Slf4j
@Configuration
public class NewDataChannelPriceHistoryJobConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Autowired
    public NewDataChannelPriceHistoryListener newDataChannelPriceHistoryListener;

    @Autowired
    private NewDataChannelPriceHistoryProcessor newDataChannelPriceHistoryProcessor;

    @Autowired
    private NewDataChannelPriceHistoryWriter newDataChannelPriceHistoryWriter;

    @Bean("newDataChannelPriceHistoryJob")
    public Job newDataChannelPriceHistoryJob() {
        return jobBuilderFactory.get("newDataChannelPriceHistoryJob").incrementer(new RunIdIncrementer()).listener(newDataChannelPriceHistoryListener).start(newDataChannelPriceJobConfigurationStep()).build();
    }

    /**
     * @return
     */
    @Bean
    public Step newDataChannelPriceJobConfigurationStep() {
        return stepBuilderFactory.get("newDataChannelPriceJobConfigurationStep")
                .<ChannelPriceModel, ChannelPriceModel>chunk(10)
                .reader(newDataChannelPriceReader())
                .processor(compositeNewDataChannelPriceProcessor())
                .writer(newDataChannelPriceHistoryWriter)
                .taskExecutor(newDataChannelPriceHistoryExecutor())
                .throttleLimit(5)
                .build();
    }

    /**
     * 查询当天创建的数据
     * 业务逻辑分为两种，一种是当天创建数据，一种是删除后在添加进去的数据
     *
     * @return
     */
    @Bean
    @StepScope
    public JdbcPagingItemReader<ChannelPriceModel> newDataChannelPriceReader() {
        JdbcPagingItemReader<ChannelPriceModel> reader = new JdbcPagingItemReader<>();
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
        queryProvider.setSelectClause(" ID,CHANNEL_ID,AREA_CODE,BATCH_DATE,CHANNEL_PRICE,CREATED_TIME,'" + today + "' PRICE_DATE,0 DAYS "); // 设置查询的列
        queryProvider.setFromClause(" from smoc.config_channel_price "); // 设置要查询的表
        queryProvider.setWhereClause(" where DATE_FORMAT(CREATED_TIME,'%Y-%m-%d') = :today ");
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
    public CompositeItemProcessor<ChannelPriceModel, ChannelPriceModel> compositeNewDataChannelPriceProcessor() {
        CompositeItemProcessor<ChannelPriceModel, ChannelPriceModel> processor = new CompositeItemProcessor<>();
        List<ItemProcessor<ChannelPriceModel, ChannelPriceModel>> listProcessor = new ArrayList<>();
        listProcessor.add(newDataChannelPriceHistoryProcessor);
        processor.setDelegates(listProcessor);
        return processor;
    }

    @Bean
    public TaskExecutor newDataChannelPriceHistoryExecutor() {
        return new SimpleAsyncTaskExecutor("newDataChannelPriceHistoryExecutor-");
    }

}
