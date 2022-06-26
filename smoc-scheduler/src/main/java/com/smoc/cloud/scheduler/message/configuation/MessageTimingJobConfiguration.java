package com.smoc.cloud.scheduler.message.configuation;

import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.scheduler.message.listener.MessageTimingListener;
import com.smoc.cloud.scheduler.message.processor.MessageTimingTaskProcessor;
import com.smoc.cloud.scheduler.message.service.model.MessageTimingModel;
import com.smoc.cloud.scheduler.message.service.rowmapper.MessageTimingRowMapper;
import com.smoc.cloud.scheduler.message.writer.MessageTimingWriter;
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
 * 定时发送短信任务
 */
@Slf4j
@Configuration
public class MessageTimingJobConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Autowired
    public MessageTimingListener messageTimingListener;

    @Autowired
    private MessageTimingTaskProcessor messageTimingTaskProcessor;

    @Autowired
    private MessageTimingWriter messageTimingWriter;

    @Bean("mssageTimingJob")
    public Job mssageTimingJob() {
        return jobBuilderFactory.get("mssageTimingJob").incrementer(new RunIdIncrementer()).listener(messageTimingListener).start(messageTimingJobConfigurationStep()).build();
    }

    /**
     * @return
     */
    @Bean
    public Step messageTimingJobConfigurationStep() {
        return stepBuilderFactory.get("messageTimingJobConfigurationStep")
                .<MessageTimingModel, MessageTimingModel>chunk(10)
                .reader(messageTimingReader())
                .processor(messageTimingProcessor())
                .writer(messageTimingWriter)
                .taskExecutor(messageTimingExecutor())
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
    public JdbcPagingItemReader<MessageTimingModel> messageTimingReader() {
        JdbcPagingItemReader<MessageTimingModel> reader = new JdbcPagingItemReader<>();
        // 设置数据源
        reader.setDataSource(dataSource);
        // 设置一次最大读取条数
        reader.setFetchSize(10);
        // 把数据库中的每条数据映射到ChannelPriceValidator对像中
        reader.setRowMapper(new MessageTimingRowMapper());
        //获取当前日期
        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause(" ID,UP_TYPE,INPUT_NUMBER,NUMBER_FILES,GROUP_ID,TEMPLATE_ID,BUSINESS_ACCOUNT,MESSAGE_CONTENT,MESSAGE_TYPE,BUSINESS_TYPE "); // 设置查询的列
        queryProvider.setFromClause(" from smoc.message_web_task_info "); // 设置要查询的表
        queryProvider.setWhereClause(" where SEND_TYPE =2  and SEND_STATUS='05' and DATE_FORMAT(TIMING_TIME,'%Y-%m-%d %H:%i') < '"+ DateTimeUtils.getDateTimeFormat(new Date()) +"' ");
        queryProvider.setSortKeys(new HashMap<String, Order>() {{
            put("ID", Order.ASCENDING);
        }});

        reader.setQueryProvider(queryProvider);// 设置排序列
        return reader;
    }


    @Bean
    @StepScope
    public CompositeItemProcessor<MessageTimingModel, MessageTimingModel> messageTimingProcessor() {
        CompositeItemProcessor<MessageTimingModel, MessageTimingModel> processor = new CompositeItemProcessor<>();
        List<ItemProcessor<MessageTimingModel, MessageTimingModel>> listProcessor = new ArrayList<>();
        listProcessor.add(messageTimingTaskProcessor);
        processor.setDelegates(listProcessor);
        return processor;
    }

    @Bean
    public TaskExecutor messageTimingExecutor() {
        return new SimpleAsyncTaskExecutor("messageTimingExecutorTask-");
    }

}
