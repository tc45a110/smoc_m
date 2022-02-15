package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelGroupInfoValidator;
import com.smoc.cloud.common.smoc.customer.qo.AccountChannelInfoQo;
import com.smoc.cloud.common.smoc.customer.validator.AccountChannelInfoValidator;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.configure.channel.group.entity.ConfigChannelGroup;
import com.smoc.cloud.customer.entity.AccountChannelInfo;
import com.smoc.cloud.customer.rowmapper.*;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
        sqlBuffer.append(", b.CARRIER AS CHANNEL_CARRIER");
        sqlBuffer.append(", b.INFO_TYPE AS CHANNEL_INFO_TYPE");
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

    public List<AccountChannelInfoQo> findAccountChannelGroupConfig(AccountChannelInfoQo accountChannelInfoQo) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ACCOUNT_ID");
        sqlBuffer.append(", t.CHANNEL_GROUP_ID");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", b.CHANNEL_GROUP_NAME");
        sqlBuffer.append(", b.CHANNEL_GROUP_INTRODUCE");
        sqlBuffer.append(", b.CARRIER as CHANNEL_GROUP_CARRIER");
        sqlBuffer.append(", b.INFO_TYPE as CHANNEL_GROUP_INFO_TYPE");
        sqlBuffer.append("  from (select a.ACCOUNT_ID,a.CHANNEL_GROUP_ID,a.CARRIER from account_channel_info a where a.ACCOUNT_ID = ? group by a.ACCOUNT_ID,a.CHANNEL_GROUP_ID,a.CARRIER)t ");
        sqlBuffer.append("  left join config_channel_group_info b on t.CHANNEL_GROUP_ID=b.CHANNEL_GROUP_ID ");
        sqlBuffer.append("  where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add( accountChannelInfoQo.getAccountId().trim());

        sqlBuffer.append(" order by t.CARRIER ");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<AccountChannelInfoQo> list = this.queryForObjectList(sqlBuffer.toString(), params,  new AccountChannelGroupConfigRowMapper());
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
        sqlBuffer.append("  left join (select t.id,t.CHANNEL_ID from account_channel_info t where t.ACCOUNT_ID=? and t.CARRIER=? )g ON t.CHANNEL_ID = g.CHANNEL_ID");
        sqlBuffer.append("  where g.ID is null ");

        List<Object> paramsList = new ArrayList<Object>();

        paramsList.add( qo.getAccountId());
        paramsList.add( qo.getCarrier());
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
        }else{
            sqlBuffer.append(" and t.CARRIER = 'flag'");
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

    public List<ChannelGroupInfoValidator> findChannelGroupList(ChannelGroupInfoValidator qo) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.CHANNEL_GROUP_ID");
        sqlBuffer.append(", t.CHANNEL_GROUP_NAME");
        sqlBuffer.append(", t.BUSINESS_TYPE");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.INFO_TYPE");
        sqlBuffer.append(", t.MASK_PROVINCE");
        sqlBuffer.append(", t.CHANNEL_GROUP_INTRODUCE ");
        sqlBuffer.append(", IFNULL(a.CHANNEL_NUM,0)CHANNEL_NUM ");
        sqlBuffer.append("  from config_channel_group_info t ");
        sqlBuffer.append("  left join (select t.CHANNEL_GROUP_ID from account_channel_info t where t.ACCOUNT_ID=? and t.CARRIER=? group by t.CHANNEL_GROUP_ID)g ON t.CHANNEL_GROUP_ID = g.CHANNEL_GROUP_ID");
        sqlBuffer.append("  left join (select t.CHANNEL_GROUP_ID,count(t.ID)CHANNEL_NUM from config_channel_group t group by t.CHANNEL_GROUP_ID)a  on t.CHANNEL_GROUP_ID=a.CHANNEL_GROUP_ID ");
        sqlBuffer.append("  where g.CHANNEL_GROUP_ID is null and IFNULL(a.CHANNEL_NUM,0)>0 ");

        List<Object> paramsList = new ArrayList<Object>();

        paramsList.add( qo.getAccountId());
        paramsList.add( qo.getCarrier());
        if (!StringUtils.isEmpty(qo.getChannelGroupName())) {
            sqlBuffer.append(" and t.CHANNEL_GROUP_NAME like ?");
            paramsList.add( "%"+qo.getChannelGroupName().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getCarrier())) {
            sqlBuffer.append(" and t.CARRIER like ?");
            paramsList.add( "%"+qo.getCarrier().trim()+"%");
        }else{
            sqlBuffer.append(" and t.CARRIER = 'flag'");
        }

        if (!StringUtils.isEmpty(qo.getBusinessType())) {
            sqlBuffer.append(" and t.BUSINESS_TYPE = ?");
            paramsList.add(qo.getBusinessType().trim());
        }

        if (!StringUtils.isEmpty(qo.getInfoType())) {
            sqlBuffer.append(" and t.INFO_TYPE like ?");
            paramsList.add( "%"+qo.getInfoType().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getChannelGroupStatus())) {
            sqlBuffer.append(" and t.CHANNEL_GROUP_STATUS = ?");
            paramsList.add(qo.getChannelGroupStatus().trim());
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<ChannelGroupInfoValidator> list = this.queryForObjectList(sqlBuffer.toString(), params, new AccountChannelGroupInfoRowMapper());
        return list;

    }

    public void batchSave(AccountChannelInfo entity, List<ConfigChannelGroup> list) {

        final String sql = "insert into account_channel_info(ID,ACCOUNT_ID,CONFIG_TYPE,CARRIER,CHANNEL_GROUP_ID,CHANNEL_ID,CHANNEL_PRIORITY,CHANNEL_WEIGHT,CHANNEL_SOURCE,CHANNEL_STATUS,CREATED_BY,CREATED_TIME) " +
                " values(?,?,?,?,?,?,?,?,?,?,?,now()) ";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return list.size();
            }

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ConfigChannelGroup qo = list.get(i);
                ps.setString(1, UUID.uuid32());
                ps.setString(2, entity.getAccountId());
                ps.setString(3, entity.getConfigType());
                ps.setString(4, entity.getCarrier());
                ps.setString(5, entity.getChannelGroupId());
                ps.setString(6, qo.getChannelId());
                ps.setString(7, entity.getChannelPriority());
                ps.setInt(8, qo.getChannelWeight());
                ps.setString(9, entity.getChannelSource());
                ps.setString(10, entity.getChannelStatus());
                ps.setString(11, entity.getCreatedBy());
            }

        });
    }

    public void deleteConfigChannelByCarrier(String accountId, String carrier) {
        String[] carriers = carrier.split(",");

        StringBuilder sql = new StringBuilder("delete from account_channel_info where ACCOUNT_ID = '"+accountId+"' ");
        for(int i=0;i<carriers.length;i++){
            sql.append("and  CARRIER !='"+carriers[i]+"' ");
        }
        jdbcTemplate.execute(sql.toString());
    }

    public List<AccountChannelInfoQo> accountChannelByAccountIdAndCarrier(String accountId, String carrier, String accountChannelType) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  ''ID");
        sqlBuffer.append(", t.ACCOUNT_ID");
        sqlBuffer.append(", ''CHANNEL_ID");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", '' CHANNEL_NAME");
        sqlBuffer.append(", ''PROTOCOL");
        sqlBuffer.append(", ''CHANNEL_INTRODUCE");
        sqlBuffer.append(", ''CHANNEL_CARRIER");
        sqlBuffer.append(", ''CHANNEL_INFO_TYPE");
        sqlBuffer.append("  from account_channel_info t  where t.ACCOUNT_ID = ? ");

        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add( accountId.trim());

        String[] carriers = carrier.split(",");
        sqlBuffer.append(" and (");
        for(int i=0;i<carriers.length;i++){
            if(i==0){
                sqlBuffer.append(" t.CARRIER ='"+carriers[i]+"' ");
            }else{
                sqlBuffer.append(" or  t.CARRIER ='"+carriers[i]+"'");
            }
        }
        sqlBuffer.append(") ");
        if("ACCOUNT_CHANNEL_GROUP".equals(accountChannelType)){
            sqlBuffer.append(" group by t.ACCOUNT_ID,t.CARRIER");
        }

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<AccountChannelInfoQo> list = this.queryForObjectList(sqlBuffer.toString(), params,  new AccountChannelConfigRowMapper());
        return list;

    }

    public List<AccountChannelInfoValidator> channelDetail(AccountChannelInfoValidator qo) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", t.CONFIG_TYPE");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.CHANNEL_ID");
        sqlBuffer.append(", t.CHANNEL_PRIORITY");
        sqlBuffer.append(", t.CHANNEL_WEIGHT");
        sqlBuffer.append(", t.CHANNEL_SOURCE");
        sqlBuffer.append(", t.CHANGE_SOURCE");
        sqlBuffer.append(", t.CHANNEL_STATUS");
        sqlBuffer.append(", t.CREATED_BY");
        sqlBuffer.append(", b.CHANNEL_NAME");
        sqlBuffer.append(", DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from account_channel_info t left join config_channel_basic_info b on t.CHANNEL_ID = b.CHANNEL_ID where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getAccountId())) {
            sqlBuffer.append(" and t.ACCOUNT_ID = ?");
            paramsList.add(qo.getAccountId().trim());
        }

        sqlBuffer.append(" order by t.CARRIER ");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<AccountChannelInfoValidator> list = this.queryForObjectList(sqlBuffer.toString(), params,  new AccountChannelDetailRowMapper());
        return list;

    }

    public List<AccountChannelInfoQo> findAccountChannelGroupByChannelGroupIdAndCarrierAndAccountId(String channelGroupId, String carrier) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ACCOUNT_ID");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.CHANNEL_GROUP_ID");
        sqlBuffer.append(", ''CHANNEL_GROUP_NAME");
        sqlBuffer.append(", ''CHANNEL_GROUP_INTRODUCE");
        sqlBuffer.append(", '' CHANNEL_GROUP_CARRIER");
        sqlBuffer.append(", ''CHANNEL_GROUP_INFO_TYPE");
        sqlBuffer.append("  from account_channel_info t  where t.CHANNEL_GROUP_ID=? and t.CARRIER=?  ");

        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(channelGroupId.trim());
        paramsList.add(carrier.trim());

        sqlBuffer.append(" group by t.CHANNEL_GROUP_ID,t.CARRIER,t.ACCOUNT_ID ");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<AccountChannelInfoQo> list = this.queryForObjectList(sqlBuffer.toString(), params,  new AccountChannelGroupConfigRowMapper());
        return list;

    }

    public void batchSaveAccountChannel(List<AccountChannelInfoQo> list, ConfigChannelGroup entity) {

        final String sql = "insert into account_channel_info(ID,ACCOUNT_ID,CONFIG_TYPE,CARRIER,CHANNEL_GROUP_ID,CHANNEL_ID,CHANNEL_PRIORITY,CHANNEL_WEIGHT,CHANNEL_SOURCE,CHANNEL_STATUS,CREATED_BY,CREATED_TIME) " +
                " values(?,?,?,?,?,?,?,?,?,?,?,now()) ";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return list.size();
            }

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                AccountChannelInfoQo qo = list.get(i);
                ps.setString(1, UUID.uuid32());
                ps.setString(2, qo.getAccountId());
                ps.setString(3, "ACCOUNT_CHANNEL_GROUP");
                ps.setString(4, qo.getCarrier());
                ps.setString(5, qo.getChannelGroupId());
                ps.setString(6, entity.getChannelId());
                ps.setString(7, "1");
                ps.setInt(8, entity.getChannelWeight());
                ps.setString(9, "CHANNEL_GROUP");
                ps.setString(10, "1");
                ps.setString(11, entity.getCreatedBy());
            }

        });
    }
}
