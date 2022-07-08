package com.smoc.cloud.customer.rowmapper;

import com.smoc.cloud.common.smoc.customer.validator.ConfigRouteContentRuleValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class ConfigContentRepairRuleRowMapper implements RowMapper<ConfigRouteContentRuleValidator> {

    @Override
    public ConfigRouteContentRuleValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        ConfigRouteContentRuleValidator qo = new ConfigRouteContentRuleValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setAccountId(resultSet.getString("ACCOUNT_ID"));
        qo.setAccountName(resultSet.getString("ACCOUNT_NAME"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setAreaCodes(resultSet.getString("AREA_CODES"));
        qo.setRouteContent(resultSet.getString("ROUTE_CONTENT"));
        qo.setRouteReverseContent(resultSet.getString("ROUTE_REVERSE_CONTENT"));
        qo.setChannelId(resultSet.getString("CHANNEL_ID"));
        qo.setChannelName(resultSet.getString("CHANNEL_NAME"));
        qo.setBusinessType(resultSet.getString("BUSINESS_TYPE"));
        qo.setMobileNum(resultSet.getString("MOBILE_NUM"));
        qo.setMinContent(resultSet.getInt("MIN_CONTENT"));
        qo.setMaxContent(resultSet.getInt("MAX_CONTENT"));
        qo.setRouteStatus(resultSet.getString("ROUTE_STATUS"));

        return qo;
    }
}
