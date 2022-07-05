package com.smoc.cloud.scheduler.initialize.rowmapper;

import com.smoc.cloud.scheduler.initialize.entity.AccountChannelInfo;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountChannelRowMapper implements RowMapper<AccountChannelInfo> {
    @Override
    public AccountChannelInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        AccountChannelInfo qo = new AccountChannelInfo();
//        qo.setId(rs.getString("ID"));
        qo.setAccountId(rs.getString("ACCOUNT_ID"));
        qo.setConfigType(rs.getString("CONFIG_TYPE"));
        qo.setCarrier(rs.getString("CARRIER"));
        qo.setBusinessAreaType(rs.getString("BUSINESS_AREA_TYPE"));
        qo.setSupportAreaCodes(rs.getString("SUPPORT_AREA_CODES"));
        qo.setAreaCodes(this.convertToSet(qo.getSupportAreaCodes()));
        qo.setChannelGroupId(rs.getString("CHANNEL_GROUP_ID"));
        qo.setChannelId(rs.getString("CHANNEL_ID"));
        qo.setChannelPriority(rs.getString("CHANNEL_PRIORITY"));
        qo.setChannelWeight(rs.getInt("CHANNEL_WEIGHT"));
        qo.setChannelStatus(rs.getString("CHANNEL_STATUS"));
        return qo;
    }

    /**
     * 分割的字符串转set
     *
     * @param areaCodes
     * @return
     */
    private Set<String> convertToSet(String areaCodes) {
        Set<String> areaCodeSet = new HashSet<>();
        if (StringUtils.isEmpty(areaCodes)) {
            return areaCodeSet;
        }
        areaCodeSet = Arrays.stream(areaCodes.split(",")).collect(Collectors.toSet());
        return areaCodeSet;
    }
}
