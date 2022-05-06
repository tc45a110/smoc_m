package com.smoc.cloud.message.repository;

import com.google.gson.Gson;
import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountRechargeValidator;
import com.smoc.cloud.common.smoc.message.MessageDailyStatisticValidator;

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
        sqlBuffer.append(" sum(t.MESSAGE_NO_REPORT_NUM) MESSAGE_NO_REPORT_NUM");
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
