package com.smoc.cloud.scheduler.common.repository;

import com.smoc.cloud.scheduler.channel.price.service.model.ChannelPriceModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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

    /**
     * 查询返回实体对象集合
     *
     * @param sql    sql语句
     * @param params 填充sql问号占位符数
     * @param mapper
     * @return
     */
    public <T> List<T> queryForObjectList(String sql, Object[] params, RowMapper<T> mapper) {

        List<T> list = new ArrayList();

        try {
            list = jdbcTemplate.query(sql, params, mapper);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (list.size() <= 0) {
            return null;
        }
        return list;
    }
}
