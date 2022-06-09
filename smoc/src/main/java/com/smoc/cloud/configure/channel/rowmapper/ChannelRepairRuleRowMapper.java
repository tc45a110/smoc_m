package com.smoc.cloud.configure.channel.rowmapper;

import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelRepairRuleValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelRepairValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class ChannelRepairRuleRowMapper implements RowMapper<ConfigChannelRepairRuleValidator> {

    @Override
    public ConfigChannelRepairRuleValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        ConfigChannelRepairRuleValidator qo = new ConfigChannelRepairRuleValidator();
        qo.setChannelId(resultSet.getString("CHANNEL_ID"));
        qo.setBusinessId(resultSet.getString("BUSINESS_ID"));
        qo.setBusinessType(resultSet.getString("BUSINESS_TYPE"));
        qo.setChannelRepairId(resultSet.getString("CHANNEL_REPAIR_ID"));
        qo.setChannelRepairName(resultSet.getString("CHANNEL_NAME"));
        qo.setRepairCode(resultSet.getString("REPAIR_CODE"));
        qo.setRepairStatus(resultSet.getString("REPAIR_STATUS"));

        return qo;
    }
}
