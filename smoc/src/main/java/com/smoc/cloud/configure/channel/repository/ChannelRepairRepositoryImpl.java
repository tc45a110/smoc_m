package com.smoc.cloud.configure.channel.repository;


import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.configuate.validator.*;
import com.smoc.cloud.common.smoc.customer.qo.AccountChannelRepairQo;
import com.smoc.cloud.configure.channel.rowmapper.ChannelRepairRowMapper;
import com.smoc.cloud.configure.channel.rowmapper.ChannelRepairRuleRowMapper;
import com.smoc.cloud.customer.rowmapper.AccountChannelRapairRowMapper;
import org.springframework.util.StringUtils;

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
        sqlBuffer.append(", a.REPAIR_CODE");
        sqlBuffer.append(", a.REPAIR_DATE");
        sqlBuffer.append("  from config_channel_basic_info t left join config_repair_rule a on t.CHANNEL_ID = a.BUSINESS_ID ");
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

    public List<ConfigChannelRepairValidator> findSpareChannel(ConfigChannelRepairValidator qo) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.CHANNEL_ID");
        sqlBuffer.append(", t.CHANNEL_NAME");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.BUSINESS_TYPE");
        sqlBuffer.append(", ''REPAIR_CODE");
        sqlBuffer.append(", ''REPAIR_DATE");
        sqlBuffer.append("  from config_channel_basic_info t ");
        if("CHANNEL".equals(qo.getFlag())){
            sqlBuffer.append("  where t.CHANNEL_ID!='"+qo.getChannelId()+"' and t.CHANNEL_STATUS='001' ");
        }else{
            sqlBuffer.append("  where t.CHANNEL_STATUS='001' and t.CHANNEL_ID not in(select a.CHANNEL_ID from account_channel_info a where a.ACCOUNT_ID = '"+qo.getChannelId()+"') ");
        }

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getCarrier())) {
            sqlBuffer.append(" and t.CARRIER like ?");
            paramsList.add( "%"+qo.getCarrier().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getBusinessType())) {
            sqlBuffer.append(" and t.BUSINESS_TYPE = ?");
            paramsList.add(qo.getBusinessType().trim());
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
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.CHANNEL_ID");
        sqlBuffer.append(", t.BUSINESS_ID");
        sqlBuffer.append(", t.BUSINESS_TYPE");
        sqlBuffer.append(", t.CHANNEL_REPAIR_ID");
        sqlBuffer.append(", b.CHANNEL_NAME");
        sqlBuffer.append(", t.REPAIR_CODE");
        sqlBuffer.append(", t.REPAIR_STATUS");
        sqlBuffer.append(", t.SORT");
        sqlBuffer.append(", t.CREATED_BY");
        sqlBuffer.append(", DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from config_channel_repair_rule t left join config_channel_basic_info b on t.CHANNEL_REPAIR_ID = b.CHANNEL_ID ");
        if("CHANNEL".equals(businessType)){
            sqlBuffer.append("  where t.CHANNEL_ID =? and t.BUSINESS_TYPE =? and t.REPAIR_STATUS=1 ");
        }else{
            sqlBuffer.append("  where t.BUSINESS_ID =? and t.BUSINESS_TYPE =? and t.REPAIR_STATUS=1 ");
        }

        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add( channelId.trim());
        paramsList.add( businessType.trim());

        sqlBuffer.append(" order by t.SORT ,t.CARRIER,t.id");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<ConfigChannelRepairRuleValidator> list = this.queryForObjectList(sqlBuffer.toString(), params, new ChannelRepairRuleRowMapper());
        return list;
    }

    /**
     * 查询账号失败补发列表
     * @param pageParams
     * @return
     */
    public PageList<AccountChannelRepairQo> accountChannelRepairPage(PageParams<AccountChannelRepairQo> pageParams) {

        //查询条件
        AccountChannelRepairQo qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ACCOUNT_ID");
        sqlBuffer.append(", t.ACCOUNT_NAME");
        sqlBuffer.append(", t.BUSINESS_TYPE");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.INFO_TYPE");
        sqlBuffer.append(", a.REPAIR_CODE");
        sqlBuffer.append(", a.REPAIR_DATE");
        sqlBuffer.append(", e.ENTERPRISE_NAME");
        sqlBuffer.append("  from account_base_info t left join enterprise_basic_info e on t.ENTERPRISE_ID = e.ENTERPRISE_ID");
        sqlBuffer.append("  left join config_repair_rule a on t.ACCOUNT_ID = a.BUSINESS_ID ");
        sqlBuffer.append("  where t.REPAIR_STATUS=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getEnterpriseName())) {
            sqlBuffer.append(" and e.ENTERPRISE_NAME like ?");
            paramsList.add( "%"+qo.getEnterpriseName().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getAccountName())) {
            sqlBuffer.append(" and t.ACCOUNT_NAME like ?");
            paramsList.add( "%"+qo.getAccountName().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getAccountId())) {
            sqlBuffer.append(" and t.ACCOUNT_ID like ?");
            paramsList.add( "%"+qo.getAccountId().trim()+"%");
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

        PageList<AccountChannelRepairQo> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new AccountChannelRapairRowMapper());
        pageList.getPageParams().setParams(qo);

        List<AccountChannelRepairQo> list = pageList.getList();
        for (AccountChannelRepairQo info : list) {
            List<ConfigChannelRepairRuleValidator> channelList = findByChannelIdAndBusinessType(info.getAccountId(),"ACCOUNT");
            if(!StringUtils.isEmpty(channelList) && channelList.size()>0){
                info.setRepairList(channelList);
                info.setRowspan(""+channelList.size());
            }else{
                info.setRowspan("1");
            }
        }

        return pageList;
    }
}
