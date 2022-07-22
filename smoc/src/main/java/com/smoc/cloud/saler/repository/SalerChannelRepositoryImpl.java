package com.smoc.cloud.saler.repository;


import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.saler.qo.ChannelStatisticSendData;
import com.smoc.cloud.common.smoc.saler.qo.CustomerChannelInfoQo;
import com.smoc.cloud.saler.rowmapper.ChannelStatisticSendRowMapper;
import com.smoc.cloud.saler.rowmapper.CustomerChannelInfoRowMapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 2020/5/28 15:44
 **/
public class SalerChannelRepositoryImpl extends BasePageRepository {


    /**
     * 通道接口参数查询
     * @param pageParams
     * @return
     */
    public PageList<CustomerChannelInfoQo> page(PageParams<CustomerChannelInfoQo> pageParams) {

        //查询条件
        CustomerChannelInfoQo qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.CHANNEL_ID");
        sqlBuffer.append(", t.CHANNEL_NAME");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.BUSINESS_TYPE");
        sqlBuffer.append(", t.INFO_TYPE");
        sqlBuffer.append(", t.CHANNEL_STATUS");
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
        sqlBuffer.append("  where t.CHANNEL_ACCESS_SALES = ? ");

        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(qo.getSalerId().trim());

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

        PageList<CustomerChannelInfoQo> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new CustomerChannelInfoRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }

    /**
     * 通道按维度统计发送量
     * @param statisticSendData
     * @return
     */
    public List<ChannelStatisticSendData> statisticSendNumberMonthByChannel(ChannelStatisticSendData statisticSendData) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");

        //账户发送量：按月
        if("month".equals(statisticSendData.getDimension())){
            sqlBuffer.append("  a.MONTH_DAY");
            sqlBuffer.append(", IFNULL(b.MESSAGE_SUCCESS_NUM,0)SEND_NUMBER from ");
            sqlBuffer.append(" (SELECT @s :=@s + 1 as `INDEX`, DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL @s MONTH),'%Y-%m') AS `MONTH_DAY` ");
            sqlBuffer.append("  from mysql.help_topic, (SELECT @s := -1) temp WHERE  @s < 23 ORDER BY MONTH_DAY desc");
            sqlBuffer.append(" )a  left join ");
            sqlBuffer.append(" (SELECT DATE_FORMAT(t.MESSAGE_DATE, '%Y-%m')MESSAGE_DATE, ROUND(sum(t.MESSAGE_SUCCESS_NUM)/10000,2) MESSAGE_SUCCESS_NUM ");
            sqlBuffer.append(" FROM message_daily_statistics t WHERE t.CHANNEL_ID = ? ");
            sqlBuffer.append(" GROUP BY t.CHANNEL_ID, DATE_FORMAT(t.MESSAGE_DATE, '%Y-%m')");
            sqlBuffer.append(" )b on a.MONTH_DAY = b.MESSAGE_DATE  order by a.MONTH_DAY asc");
        }

        //账户发送量：按天
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
        paramsList.add(statisticSendData.getChannelId());

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<ChannelStatisticSendData> list = this.queryForObjectList(sqlBuffer.toString(), params, new ChannelStatisticSendRowMapper());
        return list;
    }

    /**
     * 通道总发送量统计
     * @param statisticSendData
     * @return
     */
    public List<ChannelStatisticSendData> statisticTotalSendNumberByChannel(ChannelStatisticSendData statisticSendData) {

        String sql = "";
        if(!StringUtils.isEmpty(statisticSendData.getChannelId())){
            sql = " and t.CHANNEL_ID = '"+statisticSendData.getChannelId().trim()+"'";
        }

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");

        //账户发送量：按月
        if("month".equals(statisticSendData.getDimension())){
            sqlBuffer.append("  a.MONTH_DAY");
            sqlBuffer.append(", IFNULL(b.MESSAGE_SUCCESS_NUM,0)SEND_NUMBER from ");
            sqlBuffer.append(" (SELECT @s :=@s + 1 as `INDEX`, DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL @s MONTH),'%Y-%m') AS `MONTH_DAY` ");
            sqlBuffer.append("  from mysql.help_topic, (SELECT @s := -1) temp WHERE  @s < 23 ORDER BY MONTH_DAY desc");
            sqlBuffer.append(" )a  left join ");
            sqlBuffer.append(" (SELECT DATE_FORMAT(t.MESSAGE_DATE, '%Y-%m')MESSAGE_DATE, ROUND(sum(t.MESSAGE_SUCCESS_NUM)/10000,2) MESSAGE_SUCCESS_NUM ");
            sqlBuffer.append(" FROM message_daily_statistics t left join config_channel_basic_info m on t.CHANNEL_ID = m.CHANNEL_ID");
            sqlBuffer.append(" WHERE m.CHANNEL_ACCESS_SALES = ? ");
            sqlBuffer.append(sql);
            sqlBuffer.append(" GROUP BY DATE_FORMAT(t.MESSAGE_DATE, '%Y-%m')");
            sqlBuffer.append(" )b on a.MONTH_DAY = b.MESSAGE_DATE  order by a.MONTH_DAY asc");
        }

        //账户发送量：按天
        if("day".equals(statisticSendData.getDimension())){
            sqlBuffer.append("  a.MONTH_DAY");
            sqlBuffer.append(", IFNULL(b.MESSAGE_SUCCESS_NUM,0)SEND_NUMBER from ");
            sqlBuffer.append(" (SELECT @s :=@s + 1 as `INDEX`, DATE(DATE_SUB(CURRENT_DATE, INTERVAL @s DAY)) AS `MONTH_DAY` ");
            sqlBuffer.append("  from mysql.help_topic, (SELECT @s := -1) temp WHERE  @s < 179 ORDER BY MONTH_DAY desc");
            sqlBuffer.append(" )a  left join ");
            sqlBuffer.append(" (SELECT DATE_FORMAT(t.MESSAGE_DATE, '%Y-%m-%d')MESSAGE_DATE, sum(t.MESSAGE_SUCCESS_NUM) MESSAGE_SUCCESS_NUM ");
            sqlBuffer.append(" FROM message_daily_statistics t left join config_channel_basic_info m on t.CHANNEL_ID = m.CHANNEL_ID");
            sqlBuffer.append(" WHERE m.CHANNEL_ACCESS_SALES = ? ");
            sqlBuffer.append(sql);
            sqlBuffer.append(" GROUP BY DATE_FORMAT(t.MESSAGE_DATE, '%Y-%m-%d')");
            sqlBuffer.append(" )b on a.MONTH_DAY = b.MESSAGE_DATE  order by a.MONTH_DAY asc");
        }

        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(statisticSendData.getSaler());

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<ChannelStatisticSendData> list = this.queryForObjectList(sqlBuffer.toString(), params, new ChannelStatisticSendRowMapper());
        return list;
    }
}
