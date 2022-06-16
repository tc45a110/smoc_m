package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.qo.AccountChannelInfoQo;
import com.smoc.cloud.common.smoc.customer.qo.AccountInfoQo;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticComplaintData;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.AccountChannelInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.AccountFinanceInfoValidator;
import com.smoc.cloud.common.smoc.message.MessageAccountValidator;
import com.smoc.cloud.customer.rowmapper.*;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 2020/5/28 15:44
 **/
public class BusinessAccountRepositoryImpl extends BasePageRepository {


    public PageList<AccountBasicInfoValidator> page(PageParams<AccountBasicInfoValidator> pageParams) {

        //查询条件
        AccountBasicInfoValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ACCOUNT_ID");
        sqlBuffer.append(", t.ENTERPRISE_ID");
        sqlBuffer.append(", t.ACCOUNT_NAME");
        sqlBuffer.append(", t.BUSINESS_TYPE");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.INFO_TYPE");
        sqlBuffer.append(", t.EXTEND_CODE");
        sqlBuffer.append(", t.ACCOUNT_PROCESS");
        sqlBuffer.append(", t.ACCOUNT_STATUS");
        sqlBuffer.append(", t.ACCOUNT_CHANNEL_TYPE");
        sqlBuffer.append(", t.COUNTRY_CODE");
        sqlBuffer.append(", e.ENTERPRISE_NAME");
        sqlBuffer.append("  from account_base_info t left join enterprise_basic_info e on t.ENTERPRISE_ID = e.ENTERPRISE_ID");
        sqlBuffer.append("  where 1=1 ");

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

        PageList<AccountBasicInfoValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new AccountBasicInfoRowMapper());
        pageList.getPageParams().setParams(qo);

        List<AccountBasicInfoValidator> list = pageList.getList();
        for (int i = 0; i < list.size(); i++) {
            AccountBasicInfoValidator info = list.get(i);
            String[] carrier = {};
            if("INTL".equals(info.getCarrier())){
                carrier = info.getCountryCode().split(",");
                info.setRowspan(carrier.length);
            }else{
                carrier = info.getCarrier().split(",");
                info.setRowspan(carrier.length);
            }

            Map<String, AccountFinanceInfoValidator> financeMap = new HashMap<>();
            Map<String, AccountChannelInfoValidator> channelMap = new HashMap<>();
            for (int a = 0; a < carrier.length; a++) {
                financeMap.put(carrier[a], new AccountFinanceInfoValidator());
                channelMap.put(carrier[a], new AccountChannelInfoValidator());
            }

            //查询运营商价格
            Map<String, AccountFinanceInfoValidator> financeList = queryFinanceByAccountId(info.getAccountId(),financeMap);
            info.setFinanceList(financeList);
            //查询通道信息
            Map<String, AccountChannelInfoValidator> channelList = queryChannelByAccountId(info.getAccountId(),info.getAccountChannelType(),channelMap);
            info.setChannelList(channelList);
        }

        return pageList;
    }



    public  List<AccountBasicInfoValidator> findBusinessAccountByEnterpriseId(String enterpriseId) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ACCOUNT_ID");
        sqlBuffer.append(", t.ACCOUNT_NAME");
        sqlBuffer.append(", t.BUSINESS_TYPE");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.INFO_TYPE");
        sqlBuffer.append(", t.EXTEND_CODE");
        sqlBuffer.append(", t.ACCOUNT_STATUS");
        sqlBuffer.append(", e.PROTOCOL");
        sqlBuffer.append("  from account_base_info t left join account_interface_info e on t.ACCOUNT_ID = e.ACCOUNT_ID");
        sqlBuffer.append("  where t.ENTERPRISE_ID = ? ");

        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(enterpriseId);

        sqlBuffer.append("  order by t.BUSINESS_TYPE,t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<AccountBasicInfoValidator> list = this.queryForObjectList(sqlBuffer.toString(), params, new AccountCenterInfoRowMapper());
        return list;
    }

    /**
     * 查询企业所有的业务账号
     * @param qo
     * @return
     */
    public  List<AccountBasicInfoValidator> findBusinessAccount(AccountBasicInfoValidator qo) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ACCOUNT_ID");
        sqlBuffer.append(", t.ACCOUNT_NAME");
        sqlBuffer.append(", t.BUSINESS_TYPE");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.INFO_TYPE");
        sqlBuffer.append(", t.EXTEND_CODE");
        sqlBuffer.append(", t.ACCOUNT_STATUS");
        sqlBuffer.append(", ''PROTOCOL");
        sqlBuffer.append("  from account_base_info t LEFT JOIN account_interface_info a on t.ACCOUNT_ID=a.ACCOUNT_ID ");
        sqlBuffer.append("  where a.PROTOCOL='WEB' ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getEnterpriseId())) {
            sqlBuffer.append(" and t.ENTERPRISE_ID = ?");
            paramsList.add(qo.getEnterpriseId().trim());
        }

        if (!StringUtils.isEmpty(qo.getBusinessType())) {
            sqlBuffer.append(" and t.BUSINESS_TYPE = ?");
            paramsList.add(qo.getBusinessType().trim());
        }

        if (!StringUtils.isEmpty(qo.getInfoType())) {
            sqlBuffer.append(" and t.INFO_TYPE like ?");
            paramsList.add( "%"+qo.getInfoType().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getAccountStatus())) {
            sqlBuffer.append(" and t.ACCOUNT_STATUS = ?");
            paramsList.add(qo.getAccountStatus().trim());
        }

        sqlBuffer.append("  order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<AccountBasicInfoValidator> list = this.queryForObjectList(sqlBuffer.toString(), params, new AccountCenterInfoRowMapper());
        return list;
    }

    /**
     * 查询企业下的账户和余额
     * @param qo
     * @return
     */
    public  List<MessageAccountValidator> messageAccountList(MessageAccountValidator qo) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ACCOUNT_ID");
        sqlBuffer.append(", t.ENTERPRISE_ID");
        sqlBuffer.append(", t.BUSINESS_TYPE");
        sqlBuffer.append(", a.ACCOUNT_USABLE_SUM");
        sqlBuffer.append("  from account_base_info t left join finance_account a on t.ACCOUNT_ID=a.ACCOUNT_ID ");
        sqlBuffer.append("  where 1 = 1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getEnterpriseId())) {
            sqlBuffer.append(" and t.ENTERPRISE_ID = ?");
            paramsList.add(qo.getEnterpriseId().trim());
        }

        if (!StringUtils.isEmpty(qo.getBusinessType())) {
            sqlBuffer.append(" and t.BUSINESS_TYPE = ?");
            paramsList.add(qo.getBusinessType().trim());
        }

        sqlBuffer.append("  order by t.CREATED_TIME ");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<MessageAccountValidator> list = this.queryForObjectList(sqlBuffer.toString(), params, new MessageAccountRowMapper());
        return list;
    }

    /**
     * 查询自服务平台发送账号列表
     * @param pageParams
     * @return
     */
    public PageList<MessageAccountValidator> messageAccountInfoList(PageParams<MessageAccountValidator> pageParams) {

        //查询条件
        MessageAccountValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ACCOUNT_ID");
        sqlBuffer.append(", t.ENTERPRISE_ID");
        sqlBuffer.append(", t.BUSINESS_TYPE");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.INFO_TYPE");
        sqlBuffer.append(", t.ACCOUNT_STATUS");
        sqlBuffer.append(", a.PAY_TYPE");
        sqlBuffer.append(", i.PROTOCOL");
        sqlBuffer.append(", DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append(", f.PARAM_VALUE AS SEND_LIMIT");
        sqlBuffer.append(", f1.PARAM_VALUE AS DAY_LIMIT");
        sqlBuffer.append(", f2.PARAM_VALUE AS MASK_AREA");
        sqlBuffer.append("  from account_base_info t left join (select PAY_TYPE,ACCOUNT_ID from account_finance_info group by ACCOUNT_ID)a on t.ACCOUNT_ID=a.ACCOUNT_ID ");
        sqlBuffer.append("  left join account_interface_info i on t.ACCOUNT_ID=i.ACCOUNT_ID");
        sqlBuffer.append("  left join parameter_extend_filters_value f on t.ACCOUNT_ID=f.BUSINESS_ID and f.BUSINESS_TYPE='BUSINESS_ACCOUNT_FILTER' and f.PARAM_KEY='SEND_RATE_LIMIT_SECOND'");
        sqlBuffer.append("  left join parameter_extend_filters_value f1 on t.ACCOUNT_ID=f1.BUSINESS_ID and f1.BUSINESS_TYPE='BUSINESS_ACCOUNT_FILTER' and f1.PARAM_KEY='SEND_LIMIT_NUMBER_DAILY'");
        sqlBuffer.append("  left join parameter_extend_filters_value f2 on t.ACCOUNT_ID=f2.BUSINESS_ID and f2.BUSINESS_TYPE='BUSINESS_ACCOUNT_FILTER' and f2.PARAM_KEY='MASK_PROVINCE'");
        sqlBuffer.append("  where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getEnterpriseId())) {
            sqlBuffer.append(" and t.ENTERPRISE_ID = ?");
            paramsList.add(qo.getEnterpriseId().trim());
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

        if (!StringUtils.isEmpty(qo.getProtocol())) {
            sqlBuffer.append(" and i.PROTOCOL = ?");
            paramsList.add(qo.getProtocol().trim());
        }

        if (!StringUtils.isEmpty(qo.getInfoType())) {
            sqlBuffer.append(" and t.INFO_TYPE like ?");
            paramsList.add( "%"+qo.getInfoType().trim()+"%");
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<MessageAccountValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new MessageAccountInfoRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }

    /**
     * 账号按维度统计发送量
     * @param statisticSendData
     * @return
     */
    public  List<AccountStatisticSendData> statisticAccountSendNumber(AccountStatisticSendData statisticSendData) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");

        //首页：近12个月短信发送量
        if("index".equals(statisticSendData.getDimension())){
            sqlBuffer.append("  a.MONTH_DAY");
            sqlBuffer.append(", IFNULL(b.MESSAGE_SUCCESS_NUM,0)SEND_NUMBER from ");
            sqlBuffer.append(" (SELECT @s :=@s + 1 as `INDEX`, DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL @s MONTH),'%Y-%m') AS `MONTH_DAY` ");
            sqlBuffer.append("  from mysql.help_topic, (SELECT @s := -1) temp WHERE  @s < 11 ORDER BY MONTH_DAY desc");
            sqlBuffer.append(" )a  left join ");
            sqlBuffer.append(" (SELECT DATE_FORMAT(t.MESSAGE_DATE, '%Y-%m')MESSAGE_DATE, ROUND(sum(t.MESSAGE_SUCCESS_NUM)/10000,2) MESSAGE_SUCCESS_NUM ");
            sqlBuffer.append(" FROM message_daily_statistics t ");
            sqlBuffer.append(" GROUP BY DATE_FORMAT(t.MESSAGE_DATE, '%Y-%m')");
            sqlBuffer.append(" )b on a.MONTH_DAY = b.MESSAGE_DATE  order by a.MONTH_DAY asc");

            List<AccountStatisticSendData> list = this.queryForObjectList(sqlBuffer.toString(), null, new AccountStatisticSendRowMapper());
            return list;
        }

        //账户发送量：按月
        if("month".equals(statisticSendData.getDimension())){
            sqlBuffer.append("  a.MONTH_DAY");
            sqlBuffer.append(", IFNULL(b.MESSAGE_SUCCESS_NUM,0)SEND_NUMBER from ");
            sqlBuffer.append(" (SELECT @s :=@s + 1 as `INDEX`, DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL @s MONTH),'%Y-%m') AS `MONTH_DAY` ");
            sqlBuffer.append("  from mysql.help_topic, (SELECT @s := -1) temp WHERE  @s < 11 ORDER BY MONTH_DAY desc");
            sqlBuffer.append(" )a  left join ");
            sqlBuffer.append(" (SELECT DATE_FORMAT(t.MESSAGE_DATE, '%Y-%m')MESSAGE_DATE, ROUND(sum(t.MESSAGE_SUCCESS_NUM)/10000,2) MESSAGE_SUCCESS_NUM ");
            sqlBuffer.append(" FROM message_daily_statistics t WHERE t.BUSINESS_ACCOUNT = ? ");
            sqlBuffer.append(" GROUP BY t.BUSINESS_ACCOUNT, DATE_FORMAT(t.MESSAGE_DATE, '%Y-%m')");
            sqlBuffer.append(" )b on a.MONTH_DAY = b.MESSAGE_DATE  order by a.MONTH_DAY asc");
        }

        //账户发送量：按天
        if("day".equals(statisticSendData.getDimension())){
            sqlBuffer.append("  a.MONTH_DAY");
            sqlBuffer.append(", IFNULL(b.MESSAGE_SUCCESS_NUM,0)SEND_NUMBER from ");
            sqlBuffer.append(" (SELECT @s :=@s + 1 as `INDEX`, DATE(DATE_SUB(CURRENT_DATE, INTERVAL @s DAY)) AS `MONTH_DAY` ");
            sqlBuffer.append("  from mysql.help_topic, (SELECT @s := -1) temp WHERE  @s < 179 ORDER BY MONTH_DAY desc");
            sqlBuffer.append(" )a  left join ");
            sqlBuffer.append(" (SELECT DATE_FORMAT(t.MESSAGE_DATE, '%Y-%m-%d')MESSAGE_DATE, ROUND(sum(t.MESSAGE_SUCCESS_NUM)/10000,2) MESSAGE_SUCCESS_NUM ");
            sqlBuffer.append(" FROM message_daily_statistics t WHERE t.BUSINESS_ACCOUNT = ? ");
            sqlBuffer.append(" GROUP BY t.BUSINESS_ACCOUNT, DATE_FORMAT(t.MESSAGE_DATE, '%Y-%m-%d')");
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
     * 账号投诉率统计
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
        sqlBuffer.append(" FROM message_complaint_info t WHERE t.BUSINESS_ACCOUNT = ? ");
        sqlBuffer.append(" GROUP BY t.BUSINESS_ACCOUNT, DATE_FORMAT(t.REPORT_DATE, '%Y-%m')");
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
     * 业务账号综合查询
     * @param pageParams
     * @return
     */
    public PageList<AccountInfoQo> accountAll(PageParams<AccountInfoQo> pageParams) {

        //查询条件
        AccountInfoQo qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ACCOUNT_ID");
        sqlBuffer.append(", t.ENTERPRISE_ID");
        sqlBuffer.append(", t.ACCOUNT_NAME");
        sqlBuffer.append(", e.ENTERPRISE_NAME");
        sqlBuffer.append(", t.BUSINESS_TYPE");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.INFO_TYPE");
        sqlBuffer.append(", t.EXTEND_CODE");
        sqlBuffer.append(", t.RANDOM_EXTEND_CODE_LENGTH");
        sqlBuffer.append(", t.ACCOUNT_STATUS");
        sqlBuffer.append(", f.PAY_TYPE");
        sqlBuffer.append(", f.CHARGE_TYPE");
        sqlBuffer.append(", p.PROTOCOL");
        sqlBuffer.append("  from account_base_info t left join enterprise_basic_info e on t.ENTERPRISE_ID = e.ENTERPRISE_ID");
        sqlBuffer.append("  left join (select ACCOUNT_ID,PAY_TYPE,CHARGE_TYPE from account_finance_info  group by ACCOUNT_ID)f on t.ACCOUNT_ID = f.ACCOUNT_ID");
        sqlBuffer.append("  left join account_interface_info p on t.ACCOUNT_ID = p.ACCOUNT_ID");
        sqlBuffer.append("  where 1=1 ");

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

        if (!StringUtils.isEmpty(qo.getPayType())) {
            sqlBuffer.append(" and f.PAY_TYPE = ?");
            paramsList.add(qo.getPayType().trim());
        }

        if (!StringUtils.isEmpty(qo.getChargeType())) {
            sqlBuffer.append(" and f.CHARGE_TYPE = ?");
            paramsList.add(qo.getChargeType().trim());
        }

        if (!StringUtils.isEmpty(qo.getProtocol())) {
            sqlBuffer.append(" and p.PROTOCOL = ?");
            paramsList.add(qo.getProtocol().trim());
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<AccountInfoQo> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new AccountInfoQoRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }

    //查询运营商价格
    private Map<String, AccountFinanceInfoValidator> queryFinanceByAccountId(String accountId,Map<String, AccountFinanceInfoValidator> map) {
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", t.ACCOUNT_ID");
        sqlBuffer.append(", t.PAY_TYPE");
        sqlBuffer.append(", t.CHARGE_TYPE");
        sqlBuffer.append(", t.FROZEN_RETURN_DATE");
        sqlBuffer.append(", t.ACCOUNT_CREDIT_SUM");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.CARRIER_PRICE");
        sqlBuffer.append(", t.CARRIER_TYPE");
        sqlBuffer.append("  from account_finance_info t where t.ACCOUNT_ID =? ");

        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(accountId.trim());

        sqlBuffer.append(" order by t.CARRIER ");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<AccountFinanceInfoValidator> list = this.queryForObjectList(sqlBuffer.toString(), params,  new AccountFinanceInfoRowMapper());

        if (!StringUtils.isEmpty(list)) {
            for (AccountFinanceInfoValidator info : list) {
                map.put(info.getCarrier(), info);
            }
        }
        return map;
    }

    //查询通道信息
    private Map<String, AccountChannelInfoValidator> queryChannelByAccountId(String accountId,String accountChannelType,Map<String, AccountChannelInfoValidator> map) {
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", t.ACCOUNT_ID");
        sqlBuffer.append(", t.CONFIG_TYPE");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.CHANNEL_ID");
        sqlBuffer.append(", t.CHANNEL_PRIORITY");
        sqlBuffer.append(", t.CHANNEL_WEIGHT");
        sqlBuffer.append(", t.CHANNEL_SOURCE");
        sqlBuffer.append(", t.CHANGE_SOURCE");
        sqlBuffer.append(", t.CHANNEL_STATUS");
        sqlBuffer.append(", t.CREATED_BY");
        sqlBuffer.append(", DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        if("ACCOUNT_CHANNEL_GROUP".equals(accountChannelType)){
            sqlBuffer.append(", b.CHANNEL_GROUP_NAME as CHANNEL_NAME  from account_channel_info t left join config_channel_group_info b on t.CHANNEL_GROUP_ID = b.CHANNEL_GROUP_ID ");
            sqlBuffer.append(" where t.ACCOUNT_ID = ? group by t.CHANNEL_GROUP_ID,t.CARRIER ");
        }else{
            sqlBuffer.append(", b.CHANNEL_NAME from account_channel_info t left join config_channel_basic_info b on t.CHANNEL_ID = b.CHANNEL_ID where t.ACCOUNT_ID = ? ");
        }


        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(accountId.trim());

        sqlBuffer.append(" order by t.CARRIER ");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<AccountChannelInfoValidator> list = this.queryForObjectList(sqlBuffer.toString(), params,  new AccountChannelDetailRowMapper());

        if (!StringUtils.isEmpty(list)) {
            for (AccountChannelInfoValidator info : list) {
                map.put(info.getCarrier(), info);
            }
        }

        return map;
    }
}
