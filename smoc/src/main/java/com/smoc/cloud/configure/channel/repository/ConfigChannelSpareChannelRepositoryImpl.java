package com.smoc.cloud.configure.channel.repository;


import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelBasicInfoValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelSpareChannelValidator;
import com.smoc.cloud.configure.channel.rowmapper.SpareChannelInfoRowMapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class ConfigChannelSpareChannelRepositoryImpl extends BasePageRepository {


    public List<ConfigChannelSpareChannelValidator> findSpareChannel(ChannelBasicInfoValidator qo) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.CHANNEL_ID");
        sqlBuffer.append(", t.CHANNEL_NAME");
        sqlBuffer.append("  from config_channel_basic_info t left join config_channel_interface i on t.CHANNEL_ID=i.CHANNEL_ID");
        sqlBuffer.append("  where t.CHANNEL_ID!='"+qo.getChannelId()+"' and t.CHANNEL_STATUS='001' ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getProtocol())) {
            sqlBuffer.append(" and i.PROTOCOL = ?");
            paramsList.add(qo.getProtocol().trim());
        }

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

        List<ConfigChannelSpareChannelValidator> list = this.queryForObjectList(sqlBuffer.toString(), params, new SpareChannelInfoRowMapper());
        return list;
    }

}
