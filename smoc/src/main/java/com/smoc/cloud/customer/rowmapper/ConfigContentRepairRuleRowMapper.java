package com.smoc.cloud.customer.rowmapper;

import com.smoc.cloud.common.smoc.customer.validator.ConfigContentRepairRuleValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class ConfigContentRepairRuleRowMapper implements RowMapper<ConfigContentRepairRuleValidator> {

    @Override
    public ConfigContentRepairRuleValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        ConfigContentRepairRuleValidator qo = new ConfigContentRepairRuleValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setAccountId(resultSet.getString("ACCOUNT_ID"));
        qo.setAccountName(resultSet.getString("ACCOUNT_NAME"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setAreaCodes(resultSet.getString("AREA_CODES"));
        qo.setRepairContent(resultSet.getString("REPAIR_CONTENT"));
        qo.setChannelRepairId(resultSet.getString("CHANNEL_REPAIR_ID"));
        qo.setChannelName(resultSet.getString("CHANNEL_NAME"));
        qo.setBusinessType(resultSet.getString("BUSINESS_TYPE"));
        qo.setMobileNum(resultSet.getString("MOBILE_NUM"));
        qo.setMinContent(resultSet.getInt("MIN_CONTENT"));
        qo.setMaxContent(resultSet.getInt("MAX_CONTENT"));
        qo.setRepairStatus(resultSet.getString("REPAIR_STATUS"));

        return qo;
    }
}
