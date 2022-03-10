package com.smoc.cloud.scheduler.common.repository;

import com.smoc.cloud.scheduler.channel.price.service.model.ChannelPriceModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class BatchRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 批量sql 执行
     *
     * @param list
     * @throws Exception
     */
    @Transactional
    public void batchSave(List<String> list) throws Exception {

        if (null == list || list.size() < 1) {
            return;
        }
        //组织sql
        String[] sql = new String[list.size()];
        list.toArray(sql);
        jdbcTemplate.batchUpdate(sql);
    }

    /**
     * update sql 执行
     *
     * @throws Exception
     */
    @Transactional
    public void update(String sql) throws Exception {

        jdbcTemplate.update(sql);
    }


}
