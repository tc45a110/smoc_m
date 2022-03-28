package com.smoc.cloud.scheduler.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 业务账号价格历史 批处理
 */
@Slf4j
@Component
public class InitWriter implements ItemWriter<InitModel> {

    @Autowired
    private  InitService initService;

    @Override
    public void write(List<? extends InitModel> list) throws Exception {
        initService.initSql(list);
    }
}
