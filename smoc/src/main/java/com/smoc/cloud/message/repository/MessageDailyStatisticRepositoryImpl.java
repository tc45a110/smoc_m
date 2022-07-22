package com.smoc.cloud.message.repository;

import com.google.gson.Gson;
import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountRechargeValidator;
import com.smoc.cloud.common.smoc.message.MessageDailyStatisticValidator;

import com.smoc.cloud.common.smoc.message.model.StatisticMessageSendData;
import com.smoc.cloud.common.smoc.query.model.AccountSendStatisticItemsModel;
import com.smoc.cloud.common.smoc.query.model.ChannelSendStatisticModel;
import com.smoc.cloud.message.rowmapper.ChannelAccountSendRowMapper;
import com.smoc.cloud.message.rowmapper.ChannelSendStatisticsRowMapper;
import com.smoc.cloud.message.rowmapper.MessageDailyStatisticRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class MessageDailyStatisticRepositoryImpl extends BasePageRepository {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public PageList<MessageDailyStatisticValidator> page(PageParams<MessageDailyStatisticValidator> pageParams) {

        //查询条件
        MessageDailyStatisticValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");

        sqlBuffer.append(" t.ID,");
        sqlBuffer.append(" e.ENTERPRISE_NAME,");
        sqlBuffer.append(" t.BUSINESS_ACCOUNT,");
        sqlBuffer.append(" t.CHANNEL_ID,");
        sqlBuffer.append(" t.AREA_CODE,");
        sqlBuffer.append(" t.PRICE_AREA_CODE,");
        sqlBuffer.append(" t.CARRIER,");
        sqlBuffer.append(" t.BUSINESS_TYPE,");
        sqlBuffer.append(" t.CUSTOMER_SUBMIT_NUM,");
        sqlBuffer.append(" t.SUCCESS_SUBMIT_NUM,");
        sqlBuffer.append(" t.FAILURE_SUBMIT_NUM,");
        sqlBuffer.append(" t.MESSAGE_SUCCESS_NUM,");
        sqlBuffer.append(" t.MESSAGE_FAILURE_NUM,");
        sqlBuffer.append(" t.MESSAGE_NO_REPORT_NUM,");
        sqlBuffer.append(" t.MESSAGE_SIGN,");
        sqlBuffer.append(" DATE_FORMAT(t.MESSAGE_DATE, '%Y-%m-%d')MESSAGE_DATE,");
        sqlBuffer.append(" t.CREATED_BY,");
        sqlBuffer.append(" DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME ");
        sqlBuffer.append(" from message_daily_statistics t left join account_base_info a on t.BUSINESS_ACCOUNT = a.ACCOUNT_ID " +
                " left join enterprise_basic_info e on a.ENTERPRISE_ID = e.ENTERPRISE_ID ");
        sqlBuffer.append(" where  1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        //企业名称
        if (!StringUtils.isEmpty(qo.getEnterpriseName())) {
            sqlBuffer.append(" and e.ENTERPRISE_NAME like ? ");
            paramsList.add("%" + qo.getEnterpriseName().trim() + "%");
        }

        //业务账号
        if (!StringUtils.isEmpty(qo.getBusinessAccount())) {
            sqlBuffer.append(" and t.BUSINESS_ACCOUNT =?");
            paramsList.add(qo.getBusinessAccount().trim());
        }

        //运营商
        if (!StringUtils.isEmpty(qo.getCarrier())) {
            sqlBuffer.append(" and t.CARRIER =?");
            paramsList.add(qo.getCarrier().trim());
        }

        //业务类型
        if (!StringUtils.isEmpty(qo.getBusinessType())) {
            sqlBuffer.append(" and t.BUSINESS_TYPE =?");
            paramsList.add(qo.getBusinessType().trim());
        }

        //地域
        if (!StringUtils.isEmpty(qo.getAreaCode())) {
            sqlBuffer.append(" and t.AREA_CODE =?");
            paramsList.add(qo.getAreaCode().trim());
        }

        //通道id
        if (!StringUtils.isEmpty(qo.getChannelId())) {
            sqlBuffer.append(" and t.CHANNEL_ID =?");
            paramsList.add(qo.getChannelId().trim());
        }

        //时间起
        if (!StringUtils.isEmpty(qo.getStartDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.MESSAGE_DATE,'%Y-%m-%d') >=? ");
            paramsList.add(qo.getStartDate().trim());
        }
        //时间止
        if (!StringUtils.isEmpty(qo.getEndDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.MESSAGE_DATE,'%Y-%m-%d') <=? ");
            paramsList.add(qo.getEndDate().trim());
        }

        sqlBuffer.append(" order by t.MESSAGE_DATE desc,t.CREATED_TIME desc,t.MESSAGE_SUCCESS_NUM desc");


        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<MessageDailyStatisticValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new MessageDailyStatisticRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;

    }

    /**
     * 统计发送数量
     *
     * @param qo
     * @return
     */
    public Map<String, Object> countSum(MessageDailyStatisticValidator qo) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" sum(t.SUCCESS_SUBMIT_NUM) SUCCESS_SUBMIT_NUM,");
        sqlBuffer.append(" sum(t.MESSAGE_SUCCESS_NUM) MESSAGE_SUCCESS_NUM,");
        sqlBuffer.append(" sum(t.MESSAGE_FAILURE_NUM) MESSAGE_FAILURE_NUM,");
        sqlBuffer.append(" (sum(t.SUCCESS_SUBMIT_NUM)-sum(t.MESSAGE_SUCCESS_NUM)-sum(t.MESSAGE_FAILURE_NUM)) MESSAGE_NO_REPORT_NUM");
        sqlBuffer.append(" from message_daily_statistics t left join account_base_info a on t.BUSINESS_ACCOUNT = a.ACCOUNT_ID " +
                " left join enterprise_basic_info e on a.ENTERPRISE_ID = e.ENTERPRISE_ID ");
        sqlBuffer.append(" where  1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        //企业名称
        if (!StringUtils.isEmpty(qo.getEnterpriseName())) {
            sqlBuffer.append(" and e.ENTERPRISE_NAME like ? ");
            paramsList.add("%" + qo.getEnterpriseName().trim() + "%");
        }

        //业务账号
        if (!StringUtils.isEmpty(qo.getBusinessAccount())) {
            sqlBuffer.append(" and t.BUSINESS_ACCOUNT =?");
            paramsList.add(qo.getBusinessAccount().trim());
        }

        //业务账号
        if (!StringUtils.isEmpty(qo.getAccountName())) {
            sqlBuffer.append(" and a.ACCOUNT_NAME like ?");
            paramsList.add("%" +qo.getAccountName().trim()+ "%");
        }

        //运营商
        if (!StringUtils.isEmpty(qo.getCarrier())) {
            sqlBuffer.append(" and t.CARRIER =?");
            paramsList.add(qo.getCarrier().trim());
        }

        //业务类型
        if (!StringUtils.isEmpty(qo.getBusinessType())) {
            sqlBuffer.append(" and t.BUSINESS_TYPE =?");
            paramsList.add(qo.getBusinessType().trim());
        }

        //地域
        if (!StringUtils.isEmpty(qo.getAreaCode())) {
            sqlBuffer.append(" and t.AREA_CODE =?");
            paramsList.add(qo.getAreaCode().trim());
        }

        //通道id
        if (!StringUtils.isEmpty(qo.getChannelId())) {
            sqlBuffer.append(" and t.CHANNEL_ID =?");
            paramsList.add(qo.getChannelId().trim());
        }

        //时间起
        if (!StringUtils.isEmpty(qo.getStartDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.MESSAGE_DATE,'%Y-%m-%d') >=? ");
            paramsList.add(qo.getStartDate().trim());
        }
        //时间止
        if (!StringUtils.isEmpty(qo.getEndDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.MESSAGE_DATE,'%Y-%m-%d') <=? ");
            paramsList.add(qo.getEndDate().trim());
        }

        sqlBuffer.append(" order by t.MESSAGE_DATE desc,t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        Map<String, Object> map = jdbcTemplate.queryForMap(sqlBuffer.toString(), params);
        if(null == map || map.size()<1 || null == map.get("SUCCESS_SUBMIT_NUM")){
            map = new HashMap<>();
            map.put("SUCCESS_SUBMIT_NUM",0);
            map.put("MESSAGE_SUCCESS_NUM",0);
            map.put("MESSAGE_FAILURE_NUM",0);
            map.put("MESSAGE_NO_REPORT_NUM",0);
        }

        return map;

    }

    /**
     * 查询通道发送量
     * @param pageParams
     * @return
     */
    public PageList<ChannelSendStatisticModel> queryChannelSendStatistics(PageParams<ChannelSendStatisticModel> pageParams){

        //查询条件
        ChannelSendStatisticModel qo = pageParams.getParams();
        List<Object> paramsList = new ArrayList<Object>();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" b.MESSAGE_DATE,");
        sqlBuffer.append(" b.CHANNEL_ID,");
        sqlBuffer.append(" a.CHANNEL_NAME,");
        sqlBuffer.append(" b.CUSTOMER_SUBMIT_NUM,");
        sqlBuffer.append(" b.SUCCESS_SUBMIT_NUM,");
        sqlBuffer.append(" b.MESSAGE_SUCCESS_NUM,");
        sqlBuffer.append(" b.FAILURE_SUBMIT_NUM,");
        sqlBuffer.append(" b.MESSAGE_FAILURE_NUM,");
        sqlBuffer.append(" b.MESSAGE_NO_REPORT_NUM");
        sqlBuffer.append(" from(SELECT t.MESSAGE_DATE, t.CHANNEL_ID,sum(t.CUSTOMER_SUBMIT_NUM)CUSTOMER_SUBMIT_NUM,sum(t.SUCCESS_SUBMIT_NUM) SUCCESS_SUBMIT_NUM,");
        sqlBuffer.append(" sum(t.MESSAGE_SUCCESS_NUM) MESSAGE_SUCCESS_NUM, sum(t.FAILURE_SUBMIT_NUM)FAILURE_SUBMIT_NUM,sum(t.MESSAGE_FAILURE_NUM) MESSAGE_FAILURE_NUM,sum(t.MESSAGE_NO_REPORT_NUM) MESSAGE_NO_REPORT_NUM ");
        sqlBuffer.append(" FROM message_daily_statistics t ");
        sqlBuffer.append(" where 1=1 ");
        if (!StringUtils.isEmpty(qo.getChannelId())) {
            sqlBuffer.append(" and t.CHANNEL_ID = ?");
            paramsList.add( qo.getChannelId().trim());
        }
        sqlBuffer.append(" and DATE_FORMAT(t.MESSAGE_DATE, '%Y-%m-%d') >=? AND DATE_FORMAT(t.MESSAGE_DATE, '%Y-%m-%d') <=?");
        sqlBuffer.append(" GROUP BY t.MESSAGE_DATE, t.CHANNEL_ID ORDER BY t.MESSAGE_DATE DESC, sum(t.SUCCESS_SUBMIT_NUM) DESC, t.CHANNEL_ID DESC)b");
        sqlBuffer.append(" LEFT JOIN config_channel_basic_info a ON b.CHANNEL_ID = a.CHANNEL_ID");
        sqlBuffer.append(" WHERE 1=1 ");

        paramsList.add(qo.getStartDate().trim());
        paramsList.add(qo.getEndDate().trim());

        if (!StringUtils.isEmpty(qo.getChannelName())) {
            sqlBuffer.append(" and a.CHANNEL_NAME like ?");
            paramsList.add( "%"+qo.getChannelName().trim()+"%");
        }

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<ChannelSendStatisticModel> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new ChannelSendStatisticsRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }
    /**
     * 查询通道下面账号发送量
     * @param pageParams
     * @return
     */
    public PageList<AccountSendStatisticItemsModel> accountMessageSendListByChannel(PageParams<AccountSendStatisticItemsModel> pageParams){

        //查询条件
        AccountSendStatisticItemsModel qo = pageParams.getParams();
        List<Object> paramsList = new ArrayList<Object>();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" b.MESSAGE_DATE,");
        sqlBuffer.append(" b.BUSINESS_ACCOUNT,");
        sqlBuffer.append(" b.CHANNEL_ID,");
        sqlBuffer.append(" a.ACCOUNT_NAME,");
        sqlBuffer.append(" b.CUSTOMER_SUBMIT_NUM,");
        sqlBuffer.append(" b.SUCCESS_SUBMIT_NUM,");
        sqlBuffer.append(" b.FAILURE_SUBMIT_NUM,");
        sqlBuffer.append(" b.MESSAGE_SUCCESS_NUM,");
        sqlBuffer.append(" b.MESSAGE_FAILURE_NUM,");
        sqlBuffer.append(" b.MESSAGE_NO_REPORT_NUM");
        sqlBuffer.append(" from(SELECT t.MESSAGE_DATE,t.CHANNEL_ID,t.BUSINESS_ACCOUNT,sum(t.CUSTOMER_SUBMIT_NUM)CUSTOMER_SUBMIT_NUM,sum(t.SUCCESS_SUBMIT_NUM)SUCCESS_SUBMIT_NUM,");
        sqlBuffer.append(" sum(t.MESSAGE_SUCCESS_NUM) MESSAGE_SUCCESS_NUM, sum(t.FAILURE_SUBMIT_NUM)FAILURE_SUBMIT_NUM,sum(t.MESSAGE_FAILURE_NUM) MESSAGE_FAILURE_NUM,sum(t.MESSAGE_NO_REPORT_NUM) MESSAGE_NO_REPORT_NUM ");
        sqlBuffer.append(" FROM message_daily_statistics t ");
        sqlBuffer.append(" where 1=1 ");
        if (!StringUtils.isEmpty(qo.getBusinessAccount())) {
            sqlBuffer.append(" and t.BUSINESS_ACCOUNT= ?");
            paramsList.add( qo.getBusinessAccount().trim());
        }
        sqlBuffer.append(" and t.CHANNEL_ID =? and DATE_FORMAT(t.MESSAGE_DATE, '%Y-%m-%d') >=? AND DATE_FORMAT(t.MESSAGE_DATE, '%Y-%m-%d') <=?");
        sqlBuffer.append(" GROUP BY t.MESSAGE_DATE, t.BUSINESS_ACCOUNT ORDER BY t.MESSAGE_DATE DESC, sum(t.SUCCESS_SUBMIT_NUM) DESC, t.BUSINESS_ACCOUNT DESC)b");
        sqlBuffer.append(" LEFT JOIN account_base_info a ON b.BUSINESS_ACCOUNT = a.ACCOUNT_ID");
        sqlBuffer.append(" WHERE 1=1 ");

        paramsList.add( qo.getChannelId().trim());
        paramsList.add(qo.getStartDate().trim());
        paramsList.add(qo.getEndDate().trim());

        if (!StringUtils.isEmpty(qo.getAccountName())) {
            sqlBuffer.append(" and a.ACCOUNT_NAME like ?");
            paramsList.add( "%"+qo.getAccountName().trim()+"%");
        }

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<AccountSendStatisticItemsModel> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new ChannelAccountSendRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }

    /**
     * 统计发送数量
     *
     * @param qo
     * @return
     */
    public Map<String, Object> channelSendCountSum(MessageDailyStatisticValidator qo) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" sum(t.SUCCESS_SUBMIT_NUM) SUCCESS_SUBMIT_NUM,");
        sqlBuffer.append(" sum(t.MESSAGE_SUCCESS_NUM) MESSAGE_SUCCESS_NUM,");
        sqlBuffer.append(" sum(t.MESSAGE_FAILURE_NUM) MESSAGE_FAILURE_NUM,");
        sqlBuffer.append(" (sum(t.SUCCESS_SUBMIT_NUM)-sum(t.MESSAGE_SUCCESS_NUM)-sum(t.MESSAGE_FAILURE_NUM)) MESSAGE_NO_REPORT_NUM");
        sqlBuffer.append(" from message_daily_statistics t left join config_channel_basic_info a on t.CHANNEL_ID = a.CHANNEL_ID ");
        sqlBuffer.append(" where  1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        //通道id
        if (!StringUtils.isEmpty(qo.getChannelId())) {
            sqlBuffer.append(" and t.CHANNEL_ID =?");
            paramsList.add(qo.getChannelId().trim());
        }

        //业务账号
        if (!StringUtils.isEmpty(qo.getChannelName())) {
            sqlBuffer.append(" and a.CHANNEL_NAME like ?");
            paramsList.add("%" +qo.getChannelName().trim()+ "%");
        }

        //时间起
        if (!StringUtils.isEmpty(qo.getStartDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.MESSAGE_DATE,'%Y-%m-%d') >=? ");
            paramsList.add(qo.getStartDate().trim());
        }
        //时间止
        if (!StringUtils.isEmpty(qo.getEndDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.MESSAGE_DATE,'%Y-%m-%d') <=? ");
            paramsList.add(qo.getEndDate().trim());
        }

        sqlBuffer.append(" order by t.MESSAGE_DATE desc,t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        Map<String, Object> map = jdbcTemplate.queryForMap(sqlBuffer.toString(), params);
        if(null == map || map.size()<1 || null == map.get("SUCCESS_SUBMIT_NUM")){
            map = new HashMap<>();
            map.put("SUCCESS_SUBMIT_NUM",0);
            map.put("MESSAGE_SUCCESS_NUM",0);
            map.put("MESSAGE_FAILURE_NUM",0);
            map.put("MESSAGE_NO_REPORT_NUM",0);
        }
        return map;

    }

    /**
     * 统计web端发送量
     *
     * @param qo
     * @return
     */
    public Map<String, Object> webStatisticMessageCount(StatisticMessageSendData qo) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" sum(t.SUCCESS_SUBMIT_NUM) SUCCESS_SUBMIT_NUM,");
        sqlBuffer.append(" sum(t.MESSAGE_SUCCESS_NUM) MESSAGE_SUCCESS_NUM,");
        sqlBuffer.append(" sum(t.MESSAGE_FAILURE_NUM) MESSAGE_FAILURE_NUM,");
        sqlBuffer.append(" (sum(t.SUCCESS_SUBMIT_NUM)-sum(t.MESSAGE_SUCCESS_NUM)-sum(t.MESSAGE_FAILURE_NUM)) MESSAGE_NO_REPORT_NUM");
        sqlBuffer.append(" from message_daily_statistics t ");
        sqlBuffer.append(" where  1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getEnterpriseFlag())) {
            sqlBuffer.append(" and t.ENTERPRISE_FLAG =?");
            paramsList.add(qo.getEnterpriseFlag().trim());
        }

        //业务账号
        if (!StringUtils.isEmpty(qo.getBusinessAccount())) {
            sqlBuffer.append(" and t.BUSINESS_ACCOUNT =?");
            paramsList.add(qo.getBusinessAccount().trim());
        }

        if (!StringUtils.isEmpty(qo.getSign())) {
            sqlBuffer.append(" and t.MESSAGE_SIGN like ?");
            paramsList.add("%" +qo.getSign().trim()+ "%");
        }

        //业务类型
        if (!StringUtils.isEmpty(qo.getBusinessType())) {
            sqlBuffer.append(" and t.BUSINESS_TYPE =?");
            paramsList.add(qo.getBusinessType().trim());
        }

        //时间起
        if (!StringUtils.isEmpty(qo.getStartDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.MESSAGE_DATE,'%Y-%m-%d') >=? ");
            paramsList.add(qo.getStartDate().trim());
        }
        //时间止
        if (!StringUtils.isEmpty(qo.getEndDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.MESSAGE_DATE,'%Y-%m-%d') <=? ");
            paramsList.add(qo.getEndDate().trim());
        }

        sqlBuffer.append(" order by t.MESSAGE_DATE desc,t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        Map<String, Object> map = jdbcTemplate.queryForMap(sqlBuffer.toString(), params);
        if(null == map || map.size()<1 || null == map.get("SUCCESS_SUBMIT_NUM")){
            map = new HashMap<>();
            map.put("SUCCESS_SUBMIT_NUM",0);
            map.put("MESSAGE_SUCCESS_NUM",0);
            map.put("MESSAGE_FAILURE_NUM",0);
            map.put("MESSAGE_NO_REPORT_NUM",0);
        }
        return map;
    }
}
