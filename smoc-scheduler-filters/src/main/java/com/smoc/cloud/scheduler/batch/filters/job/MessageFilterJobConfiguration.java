package com.smoc.cloud.scheduler.batch.filters.job;

import com.smoc.cloud.scheduler.batch.filters.model.BusinessRouteValue;
import com.smoc.cloud.scheduler.batch.filters.writer.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import javax.annotation.Resource;

/**
 * 短信过滤JOB配置
 */
@Slf4j
@Configuration
public class MessageFilterJobConfiguration {

    //每批次处理数量
    private Integer chunk = 1000;

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public MessageFilterWriter messageFilterWriter;


    @Bean("messageFilterJob")
    public Job messageFilterJob() {
        return this.jobBuilderFactory.get("messageFilterJob") .incrementer(new RunIdIncrementer())
                .start(parallelFlow())
                .end()
                .build();
    }

    @Bean
    public Flow parallelFlow() {
        return new FlowBuilder<Flow>("parallelFlow").split(messageFilterExecutor()).add(flow0(),flow1(),flow2(),flow3(),flow4(),flow5(),flow6(),flow7(),flow8(),flow9()).build();
    }

    @Resource(name = "messageFilterReader0")
    public JdbcPagingItemReader<BusinessRouteValue> messageFilterReader0;

    @Resource(name = "messageFilterReader1")
    public JdbcPagingItemReader<BusinessRouteValue> messageFilterReader1;

    @Resource(name = "messageFilterReader2")
    public JdbcPagingItemReader<BusinessRouteValue> messageFilterReader2;

    @Resource(name = "messageFilterReader3")
    public JdbcPagingItemReader<BusinessRouteValue> messageFilterReader3;

    @Resource(name = "messageFilterReader4")
    public JdbcPagingItemReader<BusinessRouteValue> messageFilterReader4;

    @Resource(name = "messageFilterReader5")
    public JdbcPagingItemReader<BusinessRouteValue> messageFilterReader5;

    @Resource(name = "messageFilterReader6")
    public JdbcPagingItemReader<BusinessRouteValue> messageFilterReader6;

    @Resource(name = "messageFilterReader7")
    public JdbcPagingItemReader<BusinessRouteValue> messageFilterReader7;

    @Resource(name = "messageFilterReader8")
    public JdbcPagingItemReader<BusinessRouteValue> messageFilterReader8;

    @Resource(name = "messageFilterReader9")
    public JdbcPagingItemReader<BusinessRouteValue> messageFilterReader9;


    @Bean
    public Flow flow0() {
        return new FlowBuilder<Flow>("flow0").start(filterStep0()).build();
    }

    @Bean
    public Flow flow1() {
        return new FlowBuilder<Flow>("flow1").start(filterStep1()).build();
    }

    @Bean
    public Flow flow2() {
        return new FlowBuilder<Flow>("flow2").start(filterStep2()).build();
    }

    @Bean
    public Flow flow3() {
        return new FlowBuilder<Flow>("flow3").start(filterStep3()).build();
    }

    @Bean
    public Flow flow4() {
        return new FlowBuilder<Flow>("flow4").start(filterStep4()).build();
    }

    @Bean
    public Flow flow5() {
        return new FlowBuilder<Flow>("flow5").start(filterStep5()).build();
    }

    @Bean
    public Flow flow6() {
        return new FlowBuilder<Flow>("flow6").start(filterStep6()).build();
    }

    @Bean
    public Flow flow7() {
        return new FlowBuilder<Flow>("flow7").start(filterStep7()).build();
    }

    @Bean
    public Flow flow8() {
        return new FlowBuilder<Flow>("flow8").start(filterStep8()).build();
    }

    @Bean
    public Flow flow9() {
        return new FlowBuilder<Flow>("flow9").start(filterStep9()).build();
    }

    @Bean("filterStep0")
    public Step filterStep0() {
        return this.stepBuilderFactory.get("filterStep0").<BusinessRouteValue, BusinessRouteValue>chunk(chunk).reader(messageFilterReader0).writer(messageFilterWriter).build();
    }

    @Bean("filterStep1")
    public Step filterStep1() {
        return this.stepBuilderFactory.get("filterStep1").<BusinessRouteValue, BusinessRouteValue>chunk(chunk).reader(messageFilterReader1).writer(messageFilterWriter).build();
    }

    @Bean("filterStep2")
    public Step filterStep2() {
        return this.stepBuilderFactory.get("filterStep2").<BusinessRouteValue, BusinessRouteValue>chunk(chunk).reader(messageFilterReader2).writer(messageFilterWriter).build();
    }

    @Bean("filterStep3")
    public Step filterStep3() {
        return this.stepBuilderFactory.get("filterStep3").<BusinessRouteValue, BusinessRouteValue>chunk(chunk).reader(messageFilterReader3).writer(messageFilterWriter).build();
    }

    @Bean("filterStep4")
    public Step filterStep4() {
        return this.stepBuilderFactory.get("filterStep4").<BusinessRouteValue, BusinessRouteValue>chunk(chunk).reader(messageFilterReader4).writer(messageFilterWriter).build();
    }

    @Bean("filterStep5")
    public Step filterStep5() {
        return this.stepBuilderFactory.get("filterStep5").<BusinessRouteValue, BusinessRouteValue>chunk(chunk).reader(messageFilterReader5).writer(messageFilterWriter).build();
    }

    @Bean("filterStep6")
    public Step filterStep6() {
        return this.stepBuilderFactory.get("filterStep6").<BusinessRouteValue, BusinessRouteValue>chunk(chunk).reader(messageFilterReader6).writer(messageFilterWriter).build();
    }

    @Bean("filterStep7")
    public Step filterStep7() {
        return this.stepBuilderFactory.get("filterStep7").<BusinessRouteValue, BusinessRouteValue>chunk(chunk).reader(messageFilterReader7).writer(messageFilterWriter).build();
    }

    @Bean("filterStep8")
    public Step filterStep8() {
        return this.stepBuilderFactory.get("filterStep8").<BusinessRouteValue, BusinessRouteValue>chunk(chunk).reader(messageFilterReader8).writer(messageFilterWriter).build();
    }

    @Bean("filterStep9")
    public Step filterStep9() {
        return this.stepBuilderFactory.get("filterStep9").<BusinessRouteValue, BusinessRouteValue>chunk(chunk).reader(messageFilterReader9).writer(messageFilterWriter).build();
    }

    @Bean
    public TaskExecutor messageFilterExecutor() {
        return new SimpleAsyncTaskExecutor("filter-t-");
    }

}
