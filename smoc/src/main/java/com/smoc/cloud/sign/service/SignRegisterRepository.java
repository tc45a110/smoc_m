package com.smoc.cloud.sign.service;

import com.smoc.cloud.sign.mode.SignChannel;
import com.smoc.cloud.sign.mode.SignRegister;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SignRegisterRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据账号，查询账号配置的通道列表
     *
     * @param account
     * @return
     */
    public List<SignChannel> findChannelByAccount(String account) {

        StringBuffer sql = new StringBuffer("select ");
        sql.append("ac.ACCOUNT_ID,");
        sql.append("ac.CARRIER,");
        sql.append("ac.CHANNEL_ID,");
        sql.append("ci.SRC_ID,");
        sql.append("c.ACCESS_PROVINCE,");
        sql.append("c.CHANNEL_NAME ");
        sql.append(" from account_channel_info ac,config_channel_interface ci,config_channel_basic_info c");
        sql.append(" where ac.CHANNEL_ID = ci.CHANNEL_ID and ac.CHANNEL_ID = c.CHANNEL_ID and c.IS_REGISTER='1' and ac.ACCOUNT_ID=?");

        String[] params = new String[1];
        params[0] = account;
        List<SignChannel> channels = this.jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<SignChannel>(SignChannel.class), params);
        return channels;
    }

    /**
     * 根据账号，查询账号报备的签名列表
     *
     * @param account
     * @return
     */
    public List<SignRegister> findSignRegisterByAccount(String account) {

        StringBuffer sql = new StringBuffer("select ");
        sql.append("t.ID,");
        sql.append("t.ACCOUNT,");
        sql.append("t.SIGN,");
        sql.append("t.SIGN_EXTEND_NUMBER,");
        sql.append("t.EXTEND_DATA ");
        sql.append(" from account_sign_register t");
        sql.append(" where t.ACCOUNT=?");

        String[] params = new String[1];
        params[0] = account;
        List<SignRegister> signRegisters = this.jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<SignRegister>(SignRegister.class), params);
        return signRegisters;
    }

    /**
     * 根据账号，查询账号配置的路由通道
     *
     * @param account
     * @return
     */
    public List<SignChannel> findRouteChannelByAccount(String account) {

        StringBuffer sql = new StringBuffer("select ");
        sql.append("ac.ACCOUNT_ID,");
        sql.append("ac.CARRIER,");
        sql.append("ac.CHANNEL_ID,");
        sql.append("ci.SRC_ID,");
        sql.append("c.ACCESS_PROVINCE,");
        sql.append("c.CHANNEL_NAME ");
        sql.append(" from config_route_content_rule ac,config_channel_interface ci,config_channel_basic_info c");
        sql.append(" where ac.CHANNEL_ID = ci.CHANNEL_ID and ac.CHANNEL_ID = c.CHANNEL_ID and c.IS_REGISTER='1'  and ac.ACCOUNT_ID=?");
        String[] params = new String[1];
        params[0] = account;
        List<SignChannel> routeChannels = this.jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<SignChannel>(SignChannel.class), params);
        return routeChannels;
    }

    /**
     * 根据账号，查询账号配置的补发通道
     *
     * @param account
     * @return
     */
    public List<SignChannel> findRepairChannelByAccount(String account) {

        StringBuffer sql = new StringBuffer("select ");
        sql.append("ac.BUSINESS_ID ACCOUNT_ID,");
        sql.append("ac.CARRIER,");
        sql.append("ac.CHANNEL_REPAIR_ID CHANNEL_ID,");
        sql.append("ci.SRC_ID,");
        sql.append("c.ACCESS_PROVINCE,");
        sql.append("c.CHANNEL_NAME ");
        sql.append(" from config_channel_repair_rule ac,config_channel_interface ci,config_channel_basic_info c");
        sql.append(" where ac.CHANNEL_REPAIR_ID = ci.CHANNEL_ID and ac.CHANNEL_REPAIR_ID = c.CHANNEL_ID and c.IS_REGISTER='1'  and ac.BUSINESS_ID=?");

        String[] params = new String[1];
        params[0] = account;
        List<SignChannel> routeChannels = this.jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<SignChannel>(SignChannel.class), params);
        return routeChannels;
    }


}
