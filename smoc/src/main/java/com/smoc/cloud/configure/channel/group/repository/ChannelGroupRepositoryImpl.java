package com.smoc.cloud.configure.channel.group.repository;


import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelGroupInfoValidator;
import com.smoc.cloud.configure.channel.group.rowmapper.ChannelGroupBaseInfoRowMapper;
import com.smoc.cloud.configure.channel.group.rowmapper.ChannelInfoRowMapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class ChannelGroupRepositoryImpl extends BasePageRepository {


    public PageList<ChannelGroupInfoValidator> page(PageParams<ChannelGroupInfoValidator> pageParams) {

        ChannelGroupInfoValidator qo = pageParams.getParams();
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.CHANNEL_GROUP_ID");
        sqlBuffer.append(", t.CHANNEL_GROUP_NAME");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.INFO_TYPE");
        sqlBuffer.append(", t.BUSINESS_TYPE");
        sqlBuffer.append(", t.MASK_PROVINCE");
        sqlBuffer.append(", t.CHANNEL_GROUP_INTRODUCE ");
        sqlBuffer.append(", t.CHANNEL_GROUP_STATUS ");
        sqlBuffer.append(", IFNULL(a.CHANNEL_NUM,0)CHANNEL_NUM ");
        sqlBuffer.append("  from config_channel_group_info t ");
        sqlBuffer.append("  left join (select t.CHANNEL_GROUP_ID,count(t.ID)CHANNEL_NUM from config_channel_group t group by t.CHANNEL_GROUP_ID)a ");
        sqlBuffer.append("  on t.CHANNEL_GROUP_ID=a.CHANNEL_GROUP_ID where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getChannelGroupName())) {
            sqlBuffer.append(" and t.CHANNEL_GROUP_NAME like ?");
            paramsList.add( "%"+qo.getChannelGroupName().trim()+"%");
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

        PageList<ChannelGroupInfoValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new ChannelGroupBaseInfoRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }

    public List<ChannelBasicInfoQo> findChannelList(ChannelBasicInfoQo qo) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.CHANNEL_ID");
        sqlBuffer.append(", t.CHANNEL_NAME");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.INFO_TYPE");
        sqlBuffer.append(", i.SRC_ID");
        sqlBuffer.append(", i.PROTOCOL");
        sqlBuffer.append(", t.CHANNEL_INTRODUCE ");
        sqlBuffer.append("  from config_channel_basic_info t left join config_channel_interface i on t.CHANNEL_ID=i.CHANNEL_ID");
        sqlBuffer.append("  left join config_channel_group g on t.CHANNEL_ID=g.CHANNEL_ID ");
        sqlBuffer.append("  where g.ID is null ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getChannelName())) {
            sqlBuffer.append(" and t.CHANNEL_NAME like ?");
            paramsList.add( "%"+qo.getChannelName().trim()+"%");
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

        List<ChannelBasicInfoQo> list = this.queryForObjectList(sqlBuffer.toString(), params, new ChannelInfoRowMapper());
        return list;
    }

}
