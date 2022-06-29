package com.smoc.cloud.scheduler.batch.filters.repository;

import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.scheduler.batch.filters.model.BusinessRouteValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class RouteMessageMtInfoRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 批量保存 待发短信
     */
    public void delete(final List<? extends BusinessRouteValue> list) {
        final String sql = "delete from smoc_route.route_message_mt_info1 where id=?";
        log.info("[短信删除]条数：{}", list.size());
        log.info("[短信删除][开始]数据：{}", DateTimeUtils.getDateFormat(new Date(), "yyyy-MM-dd HH mm ss SSS"));
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return list.size();
            }

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                BusinessRouteValue message = list.get(i);
                ps.setLong(1, message.getId());
            }

        });
        log.info("[短信删除][结束]数据：{}", DateTimeUtils.getDateFormat(new Date(), "yyyy-MM-dd HH mm ss SSS"));

    }


}
