package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.customer.qo.AccountChannelInfoQo;
import com.smoc.cloud.configure.channel.group.rowmapper.ChannelInfoRowMapper;
import com.smoc.cloud.customer.rowmapper.AccountChannelConfigRowMapper;
import com.smoc.cloud.customer.rowmapper.AccountChannelInfoRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


public class AccountChannelRepositoryImpl extends BasePageRepository {

    @Resource
    public JdbcTemplate jdbcTemplate;

    public List<AccountChannelInfoQo> findAccountChannelConfig(AccountChannelInfoQo accountChannelInfoQo) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", t.ACCOUNT_ID");
        sqlBuffer.append(", t.CHANNEL_ID");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", b.CHANNEL_NAME");
        sqlBuffer.append(", i.PROTOCOL");
        sqlBuffer.append(", b.CHANNEL_INTRODUCE");
        sqlBuffer.append("  from account_channel_info t left join config_channel_basic_info b on t.CHANNEL_ID=b.CHANNEL_ID ");
        sqlBuffer.append("  left join config_channel_interface i on t.CHANNEL_ID=i.CHANNEL_ID where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(accountChannelInfoQo.getAccountId())) {
            sqlBuffer.append(" and t.ACCOUNT_ID = ?");
            paramsList.add( accountChannelInfoQo.getAccountId().trim());
        }

        sqlBuffer.append(" order by t.CARRIER ");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<AccountChannelInfoQo> list = this.queryForObjectList(sqlBuffer.toString(), params,  new AccountChannelConfigRowMapper());
        return list;

    }

    public List<ChannelBasicInfoQo> findChannelList(ChannelBasicInfoQo qo) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.CHANNEL_ID");
        sqlBuffer.append(", t.CHANNEL_NAME");
        sqlBuffer.append(", t.BUSINESS_TYPE");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.INFO_TYPE");
        sqlBuffer.append(", i.SRC_ID");
        sqlBuffer.append(", i.PROTOCOL");
        sqlBuffer.append(", t.CHANNEL_INTRODUCE ");
        sqlBuffer.append("  from config_channel_basic_info t left join config_channel_interface i on t.CHANNEL_ID=i.CHANNEL_ID");
        sqlBuffer.append("  left join (select t.id,t.CHANNEL_ID from account_channel_info t where t.ACCOUNT_ID=? and t.CARRIER=? and t.CHANNEL_ID=? )g ON t.CHANNEL_ID = g.CHANNEL_ID");
        sqlBuffer.append("  where g.ID is null ");

        List<Object> paramsList = new ArrayList<Object>();

        paramsList.add( qo.getAccountId());
        paramsList.add( qo.getCarrier());
        paramsList.add( qo.getChannelId());
        if (!StringUtils.isEmpty(qo.getChannelName())) {
            sqlBuffer.append(" and t.CHANNEL_NAME like ?");
            paramsList.add( "%"+qo.getChannelName().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getChannelId())) {
            sqlBuffer.append(" and t.CHANNEL_ID like ?");
            paramsList.add( "%"+qo.getChannelId().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getSrcId())) {
            sqlBuffer.append(" and i.SRC_ID like ?");
            paramsList.add( "%"+qo.getSrcId().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getCarrier())) {
            sqlBuffer.append(" and t.CARRIER like ?");
            paramsList.add( "%"+qo.getCarrier().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getBusinessType())) {
            sqlBuffer.append(" and t.BUSINESS_TYPE = ?");
            paramsList.add(qo.getBusinessType().trim());
        }

        if (!StringUtils.isEmpty(qo.getInfoType())) {
            sqlBuffer.append(" and t.INFO_TYPE like ?");
            paramsList.add( "%"+qo.getInfoType().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getChannelStatus())) {
            sqlBuffer.append(" and t.CHANNEL_STATUS = ?");
            paramsList.add(qo.getChannelStatus().trim());
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<ChannelBasicInfoQo> list = this.queryForObjectList(sqlBuffer.toString(), params, new AccountChannelInfoRowMapper());
        return list;

    }

}
