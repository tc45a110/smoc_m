package com.smoc.cloud.configure.channel.repository;


import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelPriceValidator;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.configure.channel.rowmapper.ChannelPriceRowMapper;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ChannelPriceRepositoryImpl extends BasePageRepository {

    @Resource
    public JdbcTemplate jdbcTemplate;

    public List<ChannelPriceValidator> findByChannelIdAndAreaCode(ChannelPriceValidator channelPriceValidator) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.CHANNEL_ID");
        sqlBuffer.append(", t.PRICE_STYLE");
        sqlBuffer.append(", t.AREA_CODE");
        sqlBuffer.append(", t.CHANNEL_PRICE");
        sqlBuffer.append(", DATE_FORMAT(t.LASTTIME_HISTORY, '%Y-%m-%d %H:%i:%S')LASTTIME_HISTORY");
        sqlBuffer.append("  from config_channel_price t where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(channelPriceValidator.getChannelId())) {
            sqlBuffer.append(" and t.CHANNEL_ID = ?");
            paramsList.add( channelPriceValidator.getChannelId().trim());
        }
        if (!StringUtils.isEmpty(channelPriceValidator.getPriceStyle())) {
            sqlBuffer.append(" and t.PRICE_STYLE = ?");
            paramsList.add( channelPriceValidator.getPriceStyle().trim());
        }
        if (!StringUtils.isEmpty(channelPriceValidator.getAreaCode())) {
            sqlBuffer.append(" and t.AREA_CODE in ("+channelPriceValidator.getAreaCode().trim()+") ");
        }


        sqlBuffer.append(" order by t.AREA_CODE ");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<ChannelPriceValidator> list = this.queryForObjectList(sqlBuffer.toString(), params,  new ChannelPriceRowMapper());
        return list;

    }

    public void deleteByChannelIdAndAreaCode(String channelId, String areaCode) {
        String sql = "delete from config_channel_price where CHANNEL_ID = '" + channelId + "' and  AREA_CODE not in ("+areaCode+") ";
        jdbcTemplate.execute(sql);
    }

    public void batchSave(ChannelPriceValidator channelPriceValidator) {

        List<ChannelPriceValidator> list = channelPriceValidator.getPrices();

        final String sql = "insert into config_channel_price(ID,CHANNEL_ID,PRICE_STYLE,AREA_CODE,CHANNEL_PRICE,LASTTIME_HISTORY) values(?,?,?,?,?,now()) ";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return list.size();
            }

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ChannelPriceValidator channelPrice = list.get(i);
                ps.setString(1, UUID.uuid32());
                ps.setString(2, channelPriceValidator.getChannelId());
                ps.setString(3, channelPriceValidator.getPriceStyle());
                ps.setString(4, channelPrice.getAreaCode());
                ps.setBigDecimal(5, channelPrice.getChannelPrice());
            }

        });
    }
}
