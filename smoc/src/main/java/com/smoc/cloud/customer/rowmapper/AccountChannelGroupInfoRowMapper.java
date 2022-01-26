package com.smoc.cloud.customer.rowmapper;

import com.smoc.cloud.common.smoc.configuate.validator.ChannelGroupInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class AccountChannelGroupInfoRowMapper implements RowMapper<ChannelGroupInfoValidator> {

    @Override
    public ChannelGroupInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        ChannelGroupInfoValidator qo = new ChannelGroupInfoValidator();
        qo.setChannelGroupId(resultSet.getString("CHANNEL_GROUP_ID"));
        qo.setChannelGroupName(resultSet.getString("CHANNEL_GROUP_NAME"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setBusinessType(resultSet.getString("BUSINESS_TYPE"));
        qo.setInfoType(resultSet.getString("INFO_TYPE"));
        qo.setMaskProvince(resultSet.getString("MASK_PROVINCE"));
        qo.setChannelGroupIntroduce(resultSet.getString("CHANNEL_GROUP_INTRODUCE"));
        qo.setChannelNum(resultSet.getInt("CHANNEL_NUM"));

        return qo;
    }
}
