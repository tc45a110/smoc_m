package com.smoc.cloud.configure.channel.repository;


import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelAccountInfoQo;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelInterfaceInfoQo;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticComplaintData;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import com.smoc.cloud.configure.channel.rowmapper.ChannelAccountInfoRowMapper;
import com.smoc.cloud.configure.channel.rowmapper.ChannelBasicInfoRowMapper;
import com.smoc.cloud.configure.channel.rowmapper.ChannelInterfaceInfoRowMapper;
import com.smoc.cloud.customer.rowmapper.AccountStatisticComplaintRowMapper;
import com.smoc.cloud.customer.rowmapper.AccountStatisticSendRowMapper;
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
        sqlBuffer.append(", t.CHANNEL_ACCESS_SALES ");
        sqlBuffer.append(", t.CHANNEL_RESTRICT_CONTENT ");
        sqlBuffer.append(", t.SPECIFIC_PROVDER ");
        sqlBuffer.append("  from config_channel_basic_info t left join config_channel_interface i on t.CHANNEL_ID=i.CHANNEL_ID");
        sqlBuffer.append("  left join (select t.CHANNEL_ID,t.CHANNEL_PRICE from config_channel_price t where t.PRICE_STYLE='UNIFIED_PRICE')p on t.CHANNEL_ID=p.CHANNEL_ID ");
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
        }else{
            sqlBuffer.append(" and t.CARRIER !='INTL' ");
        }

        if (!StringUtils.isEmpty(qo.getBusinessType())) {
            sqlBuffer.append(" and t.BUSINESS_TYPE = ?");
            paramsList.add(qo.getBusinessType().trim());
        }

        if (!StringUtils.isEmpty(qo.getInfoType())) {
            sqlBuffer.append(" and t.INFO_TYPE like ?");
            paramsList.add( "%"+qo.getInfoType().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getSpecificProvder())) {
            sqlBuffer.append(" and t.SPECIFIC_PROVDER =?");
            paramsList.add( qo.getSpecificProvder().trim());
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<ChannelBasicInfoQo> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new ChannelBasicInfoRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }

    /**
     * 通道按维度统计发送量
     * @param statisticSendData
     * @return
     */
    public  List<AccountStatisticSendData> statisticChannelSendNumber(AccountStatisticSendData statisticSendData) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");

        if("month".equals(statisticSendData.getDimension())){
            sqlBuffer.append("  a.MONTH_DAY");
            sqlBuffer.append(", IFNULL(b.MESSAGE_SUCCESS_NUM,0)SEND_NUMBER from ");
            sqlBuffer.append(" (SELECT @s :=@s + 1 as `INDEX`, DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL @s MONTH),'%Y-%m') AS `MONTH_DAY` ");
            sqlBuffer.append("  from mysql.help_topic, (SELECT @s := -1) temp WHERE  @s < 11 ORDER BY MONTH_DAY desc");
            sqlBuffer.append(" )a  left join ");
            sqlBuffer.append(" (SELECT DATE_FORMAT(t.MESSAGE_DATE, '%Y-%m')MESSAGE_DATE, sum(t.MESSAGE_SUCCESS_NUM) MESSAGE_SUCCESS_NUM ");
            sqlBuffer.append(" FROM message_daily_statistics t WHERE t.CHANNEL_ID = ? ");
            sqlBuffer.append(" GROUP BY t.CHANNEL_ID, DATE_FORMAT(t.MESSAGE_DATE, '%Y-%m')");
            sqlBuffer.append(" )b on a.MONTH_DAY = b.MESSAGE_DATE  order by a.MONTH_DAY asc");
        }

        if("day".equals(statisticSendData.getDimension())){
            sqlBuffer.append("  a.MONTH_DAY");
            sqlBuffer.append(", IFNULL(b.MESSAGE_SUCCESS_NUM,0)SEND_NUMBER from ");
            sqlBuffer.append(" (SELECT @s :=@s + 1 as `INDEX`, DATE(DATE_SUB(CURRENT_DATE, INTERVAL @s DAY)) AS `MONTH_DAY` ");
            sqlBuffer.append("  from mysql.help_topic, (SELECT @s := -1) temp WHERE  @s < 179 ORDER BY MONTH_DAY desc");
            sqlBuffer.append(" )a  left join ");
            sqlBuffer.append(" (SELECT DATE_FORMAT(t.MESSAGE_DATE, '%Y-%m-%d')MESSAGE_DATE, sum(t.MESSAGE_SUCCESS_NUM) MESSAGE_SUCCESS_NUM ");
            sqlBuffer.append(" FROM message_daily_statistics t WHERE t.CHANNEL_ID = ? ");
            sqlBuffer.append(" GROUP BY t.CHANNEL_ID, DATE_FORMAT(t.MESSAGE_DATE, '%Y-%m-%d')");
            sqlBuffer.append(" )b on a.MONTH_DAY = b.MESSAGE_DATE  order by a.MONTH_DAY asc");
        }

        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(statisticSendData.getAccountId());

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<AccountStatisticSendData> list = this.queryForObjectList(sqlBuffer.toString(), params, new AccountStatisticSendRowMapper());
        return list;
    }

    /**
     *  通道投诉率统计
     * @param statisticComplaintData
     * @return
     */
    public  List<AccountStatisticComplaintData> statisticComplaintMonth(AccountStatisticComplaintData statisticComplaintData) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  a.MONTH_DAY");
        sqlBuffer.append(", IFNULL(b.COMPLAINT_NUM,0)COMPLAINT_NUM from ");
        sqlBuffer.append(" (SELECT @s :=@s + 1 as `INDEX`, DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL @s MONTH),'%Y-%m') AS `MONTH_DAY` ");
        sqlBuffer.append("  from mysql.help_topic, (SELECT @s := -1) temp WHERE  @s < 11 ORDER BY MONTH_DAY desc");
        sqlBuffer.append(" )a  left join ");
        sqlBuffer.append(" (SELECT DATE_FORMAT(t.REPORT_DATE, '%Y-%m')MONTH_DAY,count(*)COMPLAINT_NUM ");
        sqlBuffer.append(" FROM message_complaint_info t left join config_channel_interface i on t.NUMBER_CODE = i.SRC_ID ");
        sqlBuffer.append(" WHERE t.COMPLAINT_SOURCE = 'day' and i.CHANNEL_ID = ? GROUP BY t.NUMBER_CODE,i.CHANNEL_ID, DATE_FORMAT(t.REPORT_DATE, '%Y-%m')");
        sqlBuffer.append(" )b on a.MONTH_DAY = b.MONTH_DAY  order by a.MONTH_DAY asc");


        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(statisticComplaintData.getAccountId());

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<AccountStatisticComplaintData> list = this.queryForObjectList(sqlBuffer.toString(), params, new AccountStatisticComplaintRowMapper());
        return list;
    }

    /**
     * 通道账号使用明细
     * @param pageParams
     * @return
     */
    public PageList<ChannelAccountInfoQo> channelAccountList(PageParams<ChannelAccountInfoQo> pageParams) {

        //查询条件
        ChannelAccountInfoQo qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  b.ENTERPRISE_NAME");
        sqlBuffer.append(", t.ACCOUNT_ID");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", a.ACCOUNT_NAME");
        sqlBuffer.append(", a.BUSINESS_TYPE");
        sqlBuffer.append(", a.INFO_TYPE");
        sqlBuffer.append(", a.ACCOUNT_STATUS");
        sqlBuffer.append("  from account_channel_info t left join account_base_info a on t.ACCOUNT_ID=a.ACCOUNT_ID ");
        sqlBuffer.append("  left join enterprise_basic_info b on a.ENTERPRISE_ID=b.ENTERPRISE_ID ");
        sqlBuffer.append("  where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getChannelId())) {
            if("ACCOUNT_CHANNEL".equals(qo.getConfigType())){
                sqlBuffer.append(" and t.CHANNEL_ID =?");
            }
            if("ACCOUNT_CHANNEL_GROUP".equals(qo.getConfigType())){
                sqlBuffer.append(" and t.CHANNEL_GROUP_ID =?");
            }
            paramsList.add(qo.getChannelId().trim());
        }

        sqlBuffer.append(" order by a.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<ChannelAccountInfoQo> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new ChannelAccountInfoRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }

    /**
     * 通道接口参数查询
     * @param pageParams
     * @return
     */
    public PageList<ChannelInterfaceInfoQo> channelInterfacePage(PageParams<ChannelInterfaceInfoQo> pageParams) {

        //查询条件
        ChannelInterfaceInfoQo qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.CHANNEL_ID");
        sqlBuffer.append(", t.CHANNEL_NAME");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.BUSINESS_TYPE");
        sqlBuffer.append(", t.INFO_TYPE");
        sqlBuffer.append(", t.CHANNEL_STATUS");
        sqlBuffer.append(", t.CHANNEL_RUN_STATUS");
        sqlBuffer.append(", i.CHANNEL_ACCESS_ACCOUNT");
        sqlBuffer.append(", i.CHANNEL_ACCESS_PASSWORD");
        sqlBuffer.append(", i.CHANNEL_SERVICE_URL");
        sqlBuffer.append(", i.SP_ID");
        sqlBuffer.append(", i.SRC_ID");
        sqlBuffer.append(", i.BUSINESS_CODE");
        sqlBuffer.append(", i.CONNECT_NUMBER");
        sqlBuffer.append(", i.MAX_SEND_SECOND");
        sqlBuffer.append(", i.HEARTBEAT_INTERVAL");
        sqlBuffer.append(", i.PROTOCOL");
        sqlBuffer.append("  from config_channel_basic_info t ");
        sqlBuffer.append("  left join config_channel_interface i on t.CHANNEL_ID=i.CHANNEL_ID ");
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

        if (!StringUtils.isEmpty(qo.getSrcId())) {
            sqlBuffer.append(" and i.SRC_ID like ?");
            paramsList.add( "%"+qo.getSrcId().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getChannelAccessAccount())) {
            sqlBuffer.append(" and i.CHANNEL_ACCESS_ACCOUNT like ?");
            paramsList.add( "%"+qo.getChannelAccessAccount().trim()+"%");
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

        PageList<ChannelInterfaceInfoQo> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new ChannelInterfaceInfoRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }
}
