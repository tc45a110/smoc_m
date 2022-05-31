package com.smoc.cloud.configure.channel.group.repository;


import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.qo.ConfigChannelGroupQo;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelGroupInfoValidator;
import com.smoc.cloud.configure.channel.group.rowmapper.CenterConfigChannelInfoRowMapper;
import com.smoc.cloud.configure.channel.group.rowmapper.ChannelGroupBaseInfoRowMapper;
import com.smoc.cloud.configure.channel.group.rowmapper.ConfigChannelGroupRowMapper;
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
            paramsList.add("%" + qo.getChannelGroupName().trim() + "%");
        }

        if (!StringUtils.isEmpty(qo.getCarrier())) {
            sqlBuffer.append(" and t.CARRIER like ?");
            paramsList.add("%" + qo.getCarrier().trim() + "%");
        } else {
            sqlBuffer.append(" and t.CARRIER !='INTL' ");
        }

        if (!StringUtils.isEmpty(qo.getBusinessType())) {
            sqlBuffer.append(" and t.BUSINESS_TYPE = ?");
            paramsList.add(qo.getBusinessType().trim());
        }

        if (!StringUtils.isEmpty(qo.getInfoType())) {
            sqlBuffer.append(" and t.INFO_TYPE like ?");
            paramsList.add("%" + qo.getInfoType().trim() + "%");
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<ChannelGroupInfoValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new ChannelGroupBaseInfoRowMapper());
        pageList.getPageParams().setParams(qo);

        List<ChannelGroupInfoValidator> list = pageList.getList();
        for (ChannelGroupInfoValidator info : list) {
            List<ConfigChannelGroupQo> channelList = queryChannelByGroupId(info.getChannelGroupId());
            if(!StringUtils.isEmpty(channelList) && channelList.size()>0){
                info.setChannelList(channelList);
                info.setRowspan(""+channelList.size());
            }else{
                info.setRowspan("1");
            }
        }

        return pageList;
    }

    public List<ChannelBasicInfoQo> centerConfigChannelList(ChannelGroupInfoValidator qo) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.CHANNEL_ID");
        sqlBuffer.append(", t.CHANNEL_NAME");
        sqlBuffer.append(", t.CHANNEL_PROVDER");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.BUSINESS_TYPE");
        sqlBuffer.append(", t.MAX_COMPLAINT_RATE");
        sqlBuffer.append(", i.SRC_ID");
        sqlBuffer.append(", i.PROTOCOL");
        sqlBuffer.append(", i.CHANNEL_ACCESS_ACCOUNT");
        sqlBuffer.append(", t.PRICE_STYLE");
        sqlBuffer.append(", p.CHANNEL_PRICE");
        sqlBuffer.append(", t.BUSINESS_AREA_TYPE");
        sqlBuffer.append(", t.MASK_PROVINCE");
        sqlBuffer.append(", t.SUPPORT_AREA_CODES");
        sqlBuffer.append(", t.CHANNEL_RUN_STATUS");
        sqlBuffer.append(", t.CHANNEL_STATUS");
        sqlBuffer.append(", i.MAX_SEND_SECOND");
        sqlBuffer.append(", t.CHANNEL_INTRODUCE ");
        sqlBuffer.append(", t.CHANNEL_ACCESS_SALES ");
        sqlBuffer.append(", t.CHANNEL_RESTRICT_CONTENT ");
        sqlBuffer.append(", a.CHANNEL_PRIORITY");
        sqlBuffer.append(", a.CHANNEL_WEIGHT");
        sqlBuffer.append("  from config_channel_group a left join config_channel_basic_info t on a.CHANNEL_ID=t.CHANNEL_ID ");
        sqlBuffer.append("  left join config_channel_interface i on a.CHANNEL_ID=i.CHANNEL_ID ");
        sqlBuffer.append("  left join (select t.CHANNEL_ID,t.CHANNEL_PRICE from config_channel_price t where t.PRICE_STYLE='UNIFIED_PRICE')p on a.CHANNEL_ID=p.CHANNEL_ID ");
        sqlBuffer.append("  where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getChannelGroupId())) {
            sqlBuffer.append(" and a.CHANNEL_GROUP_ID =?");
            paramsList.add(qo.getChannelGroupId().trim());
        }

        sqlBuffer.append(" order by a.CHANNEL_PRIORITY,a.CHANNEL_WEIGHT desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<ChannelBasicInfoQo> list = this.queryForObjectList(sqlBuffer.toString(), params, new CenterConfigChannelInfoRowMapper());
        return list;
    }

    public List<ConfigChannelGroupQo> queryChannelByGroupId(String channelGroupId) {
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", b.CHANNEL_ID");
        sqlBuffer.append(", b.CHANNEL_NAME");
        sqlBuffer.append(", b.INFO_TYPE");
        sqlBuffer.append(", b.CARRIER");
        sqlBuffer.append(", ''PROTOCOL");
        sqlBuffer.append(", t.CHANNEL_PRIORITY");
        sqlBuffer.append(", t.CHANNEL_WEIGHT");
        sqlBuffer.append(", ''CHANNEL_INTRODUCE ");
        sqlBuffer.append("  from config_channel_group t left join config_channel_basic_info b on t.CHANNEL_ID=b.CHANNEL_ID");
        sqlBuffer.append("  where t.CHANNEL_GROUP_ID = ? ");

        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(channelGroupId);

        sqlBuffer.append(" order by t.CHANNEL_PRIORITY,t.CHANNEL_WEIGHT desc ");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<ConfigChannelGroupQo> list = this.queryForObjectList(sqlBuffer.toString(), params, new ConfigChannelGroupRowMapper());
        return list;
    }
}
