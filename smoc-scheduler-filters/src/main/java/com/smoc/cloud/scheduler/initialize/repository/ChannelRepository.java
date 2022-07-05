package com.smoc.cloud.scheduler.initialize.repository;

import com.smoc.cloud.scheduler.initialize.entity.AccountBaseInfo;
import com.smoc.cloud.scheduler.initialize.entity.AccountChannelInfo;
import com.smoc.cloud.scheduler.initialize.rowmapper.AccountBaseInfoRowMapper;
import com.smoc.cloud.scheduler.initialize.rowmapper.AccountChannelRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ChannelRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 加载业务账号基本信息
     *
     * @return
     */
    public Map<String, AccountBaseInfo> getChannelInfoInfo() {
        Map<String, AccountBaseInfo> resultMap = new HashMap<>();
        StringBuffer sql = new StringBuffer("SELECT ");
        sql.append(" a.CHANNEL_ID,a.CHANNEL_NAME,a.CARRIER,a.BUSINESS_TYPE,a.MAX_COMPLAINT_RATE");
        sql.append(",a.ACCESS_PROVINCE,a.CHANNEL_PROVDER,a.INFO_TYPE,a.BUSINESS_AREA_TYPE,a.SUPPORT_AREA_CODES");
        sql.append(",a.CHANNEL_STATUS,a.CHANNEL_RUN_STATUS,a.PRICE_STYLE");
        sql.append(",b.CHANNEL_ACCESS_ACCOUNT,b.CHANNEL_ACCESS_PASSWORD,b.CHANNEL_SERVICE_URL,b.SP_ID,b.SRC_ID");
        sql.append(",b.BUSINESS_CODE,b.CONNECT_NUMBER,b.MAX_SEND_SECOND,b.HEARTBEAT_INTERVAL,b.PROTOCOL,b.VERSION ");
        sql.append(
                "FROM smoc.config_channel_basic_info a join smoc.config_channel_interface b on a.CHANNEL_ID=b.CHANNEL_ID");

        List<AccountBaseInfo> result = this.jdbcTemplate.query(sql.toString(), new AccountBaseInfoRowMapper());
        if (null == result || result.size() < 1) {
            return resultMap;
        }
        for (AccountBaseInfo baseInfo : result) {
            resultMap.put(baseInfo.getAccountId(), baseInfo);
        }
        return resultMap;
    }

    /**
     * 加载业务账号对应通道信息
     *
     * @return
     */
    public Map<String, AccountChannelInfo> getAccountChannel() {
        Map<String, AccountChannelInfo> resultMap = new HashMap<>();
        StringBuffer sql = new StringBuffer("SELECT ");
        sql.append(" base.ACCOUNT_ID,");
        sql.append(" base.CONFIG_TYPE,");
        sql.append(" base.CARRIER,");
        sql.append(" channel.BUSINESS_AREA_TYPE,");
        sql.append(" channel.SUPPORT_AREA_CODES,");
        sql.append(" base.CHANNEL_GROUP_ID,");
        sql.append(" base.CHANNEL_ID,");
        sql.append(" base.CHANNEL_PRIORITY,");
        sql.append(" base.CHANNEL_WEIGHT,");
        sql.append(" base.CHANNEL_STATUS");
        sql.append(" from smoc.account_channel_info base,smoc.config_channel_basic_info channel ");
        sql.append(" where base.CHANNEL_ID = channel.CHANNEL_ID and channel.CHANNEL_STATUS = '001' ");
        List<AccountChannelInfo> result = this.jdbcTemplate.query(sql.toString(), new AccountChannelRowMapper());

        return resultMap;
    }
}
