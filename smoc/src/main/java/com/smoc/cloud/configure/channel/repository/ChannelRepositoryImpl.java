package com.smoc.cloud.configure.channel.repository;


import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.configure.channel.rowmapper.ChannelBasicInfoRowMapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class ChannelRepositoryImpl extends BasePageRepository {


    public PageList<ChannelBasicInfoQo> page(PageParams<ChannelBasicInfoQo> pageParams) {

        //查询条件
        ChannelBasicInfoQo qo = pageParams.getParams();

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
        sqlBuffer.append(", u.REAL_NAME AS CHANNEL_ACCESS_SALES ");
        sqlBuffer.append(", t.CHANNEL_RESTRICT_CONTENT ");
        sqlBuffer.append("  from config_channel_basic_info t left join config_channel_interface i on t.CHANNEL_ID=i.CHANNEL_ID");
        sqlBuffer.append("  left join (select t.CHANNEL_ID,t.CHANNEL_PRICE from config_channel_price t where t.PRICE_STYLE='UNIFIED_PRICE')p on t.CHANNEL_ID=p.CHANNEL_ID ");
        sqlBuffer.append("  left join smoc_oauth.base_user_extends u on t.CHANNEL_ACCESS_SALES = u.ID where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getChannelId())) {
            sqlBuffer.append(" and t.CHANNEL_ID like ?");
            paramsList.add( "%"+qo.getChannelId().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getChannelName())) {
            sqlBuffer.append(" and t.CHANNEL_NAME like ?");
            paramsList.add( "%"+qo.getChannelName().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getSrcId())) {
            sqlBuffer.append(" and i.SRC_ID like ?");
            paramsList.add( "%"+qo.getSrcId().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getChannelAccessAccount())) {
            sqlBuffer.append(" and i.CHANNEL_ACCESS_ACCOUNT like ?");
            paramsList.add( "%"+qo.getChannelAccessAccount().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getChannelAccessSales())) {
            sqlBuffer.append(" and u.REAL_NAME like ?");
            paramsList.add( "%"+qo.getChannelAccessSales().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getChannelProvder())) {
            sqlBuffer.append(" and t.CHANNEL_PROVDER = ?");
            paramsList.add(qo.getChannelProvder().trim());
        }

        if (!StringUtils.isEmpty(qo.getAccessProvince())) {
            sqlBuffer.append(" and t.ACCESS_PROVINCE = ?");
            paramsList.add(qo.getAccessProvince().trim());
        }

        if (!StringUtils.isEmpty(qo.getChannelRunStatus())) {
            sqlBuffer.append(" and t.CHANNEL_RUN_STATUS = ?");
            paramsList.add(qo.getChannelRunStatus().trim());
        }

        if (!StringUtils.isEmpty(qo.getProtocol())) {
            sqlBuffer.append(" and i.PROTOCOL = ?");
            paramsList.add(qo.getProtocol().trim());
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

        PageList<ChannelBasicInfoQo> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new ChannelBasicInfoRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }

}
