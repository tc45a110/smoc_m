package com.smoc.cloud.configure.channel.group.repository;


import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelGroupInfoValidator;
import com.smoc.cloud.configure.channel.group.rowmapper.ChannelGroupBaseInfoRowMapper;
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

}
