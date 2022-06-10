package com.smoc.cloud.configure.channel.repository;


import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.configuate.qo.ConfigChannelGroupQo;
import com.smoc.cloud.common.smoc.configuate.validator.*;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.configure.channel.rowmapper.ChannelRepairRowMapper;
import com.smoc.cloud.configure.channel.rowmapper.ChannelRepairRuleRowMapper;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.util.StringUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ChannelRepairRepositoryImpl extends BasePageRepository {


    public PageList<ConfigChannelRepairValidator> page(PageParams<ConfigChannelRepairValidator> pageParams) {

        //查询条件
        ConfigChannelRepairValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.CHANNEL_ID");
        sqlBuffer.append(", t.CHANNEL_NAME");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.BUSINESS_TYPE");
        sqlBuffer.append("  from config_channel_basic_info t ");
        sqlBuffer.append("  where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getChannelId())) {
            sqlBuffer.append(" and t.CHANNEL_ID like ?");
            paramsList.add( "%"+qo.getChannelId().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getChannelName())) {
            sqlBuffer.append(" and t.CHANNEL_NAME like ?");
            paramsList.add( "%"+qo.getChannelName().trim()+"%");
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

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<ConfigChannelRepairValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new ChannelRepairRowMapper());
        pageList.getPageParams().setParams(qo);

        List<ConfigChannelRepairValidator> list = pageList.getList();
        for (ConfigChannelRepairValidator info : list) {
            List<ConfigChannelRepairRuleValidator> channelList = findByChannelIdAndBusinessType(info.getChannelId(),"CHANNEL");
            if(!StringUtils.isEmpty(channelList) && channelList.size()>0){
                info.setRepairList(channelList);
                info.setRowspan(""+channelList.size());
            }else{
                info.setRowspan("1");
            }
        }

        return pageList;
    }

    public List<ConfigChannelRepairValidator> findSpareChannel(ChannelBasicInfoValidator qo) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.CHANNEL_ID");
        sqlBuffer.append(", t.CHANNEL_NAME");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.BUSINESS_TYPE");
        sqlBuffer.append("  from config_channel_basic_info t ");
        sqlBuffer.append("  where t.CHANNEL_ID!='"+qo.getChannelId()+"' and t.CHANNEL_STATUS='001' ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getCarrier())) {
            sqlBuffer.append(" and t.CARRIER = ?");
            paramsList.add( qo.getCarrier().trim());
        }

        if (!StringUtils.isEmpty(qo.getBusinessType())) {
            sqlBuffer.append(" and t.BUSINESS_TYPE = ?");
            paramsList.add(qo.getBusinessType().trim());
        }

        if (!StringUtils.isEmpty(qo.getInfoType())) {
            sqlBuffer.append(" and t.INFO_TYPE = ?");
            paramsList.add( qo.getInfoType().trim());
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<ConfigChannelRepairValidator> list = this.queryForObjectList(sqlBuffer.toString(), params, new ChannelRepairRowMapper());
        return list;
    }

    public List<ConfigChannelRepairRuleValidator> findByChannelIdAndBusinessType(String channelId, String businessType) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.CHANNEL_ID");
        sqlBuffer.append(", t.BUSINESS_ID");
        sqlBuffer.append(", t.BUSINESS_TYPE");
        sqlBuffer.append(", t.CHANNEL_REPAIR_ID");
        sqlBuffer.append(", b.CHANNEL_NAME");
        sqlBuffer.append(", t.REPAIR_CODE");
        sqlBuffer.append(", t.REPAIR_STATUS");
        sqlBuffer.append("  from config_channel_repair_rule t left join config_channel_basic_info b on t.CHANNEL_REPAIR_ID = b.CHANNEL_ID ");
        sqlBuffer.append("  where t.CHANNEL_ID =? and t.BUSINESS_TYPE =? and t.REPAIR_STATUS=1 ");

        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add( channelId.trim());
        paramsList.add( businessType.trim());

        sqlBuffer.append(" order by t.CREATED_TIME desc,t.id");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<ConfigChannelRepairRuleValidator> list = this.queryForObjectList(sqlBuffer.toString(), params, new ChannelRepairRuleRowMapper());
        return list;
    }

    public void batchSave(ConfigChannelRepairValidator configChannelRepairValidator) {

        List<ConfigChannelRepairRuleValidator> list = configChannelRepairValidator.getRepairList();

       final String sql = "insert into config_channel_repair_rule(ID,CHANNEL_ID,BUSINESS_ID,BUSINESS_TYPE,CHANNEL_REPAIR_ID,REPAIR_CODE,REPAIR_STATUS,CREATED_BY,CREATED_TIME) values(?,?,?,?,?,?,?,?,now()) ";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return list.size();
            }

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ConfigChannelRepairRuleValidator info = list.get(i);
                ps.setString(1, UUID.uuid32());
                ps.setString(2, info.getChannelId());
                ps.setString(3, info.getBusinessId());
                ps.setString(4, info.getBusinessType());
                ps.setString(5, info.getChannelRepairId());
                ps.setString(6, info.getRepairCode());
                ps.setString(7, info.getRepairStatus());
                ps.setString(8, info.getCreatedBy());
            }
        });

    }
}
