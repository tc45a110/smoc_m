package com.smoc.cloud.scheduler.channel.price.service.repository;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ChannelPriceRepoitory {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //处理数据
    @Transactional
    public void batchSavePriceHistory(List<String> list) throws Exception {

        if (null == list || list.size() < 1) {
            return;
        }

        //组织sql
        String[] sql = new String[list.size()];
        list.toArray(sql);

        jdbcTemplate.batchUpdate(sql);

    }


}
