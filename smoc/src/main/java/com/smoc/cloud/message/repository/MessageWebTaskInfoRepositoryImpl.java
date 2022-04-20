package com.smoc.cloud.message.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.message.MessageAccountValidator;
import com.smoc.cloud.common.smoc.message.model.MessageFormat;
import com.smoc.cloud.common.smoc.message.model.StatisticMessageSend;
import com.smoc.cloud.common.smoc.message.MessageWebTaskInfoValidator;
import com.smoc.cloud.common.smoc.message.model.StatisticMessageSendData;
import com.smoc.cloud.message.rowmapper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.util.StringUtils;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class MessageWebTaskInfoRepositoryImpl extends BasePageRepository {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public PageList<MessageWebTaskInfoValidator> page(PageParams<MessageWebTaskInfoValidator> pageParams) {

        //查询条件
        MessageWebTaskInfoValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");

        sqlBuffer.append(" t.ID,");
        sqlBuffer.append(" t.SUBJECT,");
        sqlBuffer.append(" e.ENTERPRISE_NAME,");
        sqlBuffer.append(" t.TEMPLATE_ID,");
        sqlBuffer.append(" t.BUSINESS_ACCOUNT,");
        sqlBuffer.append(" t.INFO_TYPE,");
        sqlBuffer.append(" t.BUSINESS_TYPE,");
        sqlBuffer.append(" t.SEND_TYPE,");
        sqlBuffer.append(" t.TIMING_TIME,");
        sqlBuffer.append(" t.EXPAND_NUMBER,");
        sqlBuffer.append(" t.SUBMIT_NUMBER,");
        sqlBuffer.append(" t.SUCCESS_NUMBER,");
        sqlBuffer.append(" t.SUCCESS_SEND_NUMBER,");
        sqlBuffer.append(" t.FAILURE_NUMBER,");
        sqlBuffer.append(" t.NO_REPORT_NUMBER,");
        sqlBuffer.append(" t.APPLE_SEND_TIME,");
        sqlBuffer.append(" t.SEND_TIME,");
        sqlBuffer.append(" t.SEND_STATUS,");
        sqlBuffer.append(" t.INPUT_NUMBER,");
        sqlBuffer.append(" t.NUMBER_FILES,");
        sqlBuffer.append(" t.MESSAGE_CONTENT,");
        sqlBuffer.append(" t.SPLIT_NUMBER,");
        sqlBuffer.append(" t.CREATED_BY,");
        sqlBuffer.append(" DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME ");
        sqlBuffer.append(" from message_web_task_info t,account_base_info a,enterprise_basic_info e ");
        sqlBuffer.append(" where t.BUSINESS_ACCOUNT = a.ACCOUNT_ID and a.ENTERPRISE_ID = e.ENTERPRISE_ID ");

        List<Object> paramsList = new ArrayList<Object>();

        //任务Id
        if (!StringUtils.isEmpty(qo.getId())) {
            sqlBuffer.append(" and t.ID =?");
            paramsList.add(qo.getId().trim());
        }

        //企业名称
        if (!StringUtils.isEmpty(qo.getEnterpriseId())) {
            sqlBuffer.append(" and t.ENTERPRISE_ID = ? ");
            paramsList.add(qo.getEnterpriseId().trim() );
        }

        //企业名称
        if (!StringUtils.isEmpty(qo.getEnterpriseName())) {
            sqlBuffer.append(" and e.ENTERPRISE_NAME like ? ");
            paramsList.add("%" + qo.getEnterpriseName().trim() + "%");
        }

        //业务账号
        if (!StringUtils.isEmpty(qo.getBusinessAccount())) {
            sqlBuffer.append(" and t.BUSINESS_ACCOUNT like ?");
            paramsList.add("%" + qo.getBusinessAccount().trim() + "%");
        }

        //模板ID
        if (!StringUtils.isEmpty(qo.getTemplateId())) {
            sqlBuffer.append(" and t.TEMPLATE_ID =?");
            paramsList.add(qo.getTemplateId().trim());
        }

        //业务类型
        if (!StringUtils.isEmpty(qo.getBusinessType())) {
            sqlBuffer.append(" and t.BUSINESS_TYPE =?");
            paramsList.add(qo.getBusinessType().trim());
        }

        //信息分类
        if (!StringUtils.isEmpty(qo.getInfoType())) {
            sqlBuffer.append(" and t.INFO_TYPE =?");
            paramsList.add(qo.getInfoType().trim());
        }

        //短信类型
        if (!StringUtils.isEmpty(qo.getMessageType())) {
            sqlBuffer.append(" and t.MESSAGE_TYPE =?");
            paramsList.add(qo.getMessageType().trim());
        }

        //企业名称
        if (!StringUtils.isEmpty(qo.getMessageContent())) {
            sqlBuffer.append(" and t.MESSAGE_CONTENT like ? ");
            paramsList.add("%" + qo.getMessageContent().trim() + "%");
        }

        //发送状态
        if (!StringUtils.isEmpty(qo.getSendStatus())) {
            sqlBuffer.append(" and t.SEND_STATUS =?");
            paramsList.add(qo.getSendStatus().trim());
        }

        //时间起
        if (!StringUtils.isEmpty(qo.getStartDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.CREATED_TIME,'%Y-%m-%d') >=? ");
            paramsList.add(qo.getStartDate().trim());
        }
        //时间止
        if (!StringUtils.isEmpty(qo.getEndDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.CREATED_TIME,'%Y-%m-%d') <=? ");
            paramsList.add(qo.getEndDate().trim());
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");
        //log.info("[SQL]:{}",sqlBuffer);
        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);
        PageList<MessageWebTaskInfoValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new MessageWebTaskInfoRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }


    /**
     * 统计发送数量
     *
     * @param qo
     * @return
     */
    public Map<String, Object> countSum(MessageWebTaskInfoValidator qo) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" sum(t.SUBMIT_NUMBER) SUBMIT_NUMBER,");
        sqlBuffer.append(" sum(t.SUCCESS_NUMBER) SUCCESS_NUMBER,");
        sqlBuffer.append(" sum(t.SUCCESS_SEND_NUMBER) SUCCESS_SEND_NUMBER,");
        sqlBuffer.append(" sum(t.FAILURE_NUMBER) FAILURE_NUMBER");
        sqlBuffer.append(" from message_web_task_info t,account_base_info a,enterprise_basic_info e ");
        sqlBuffer.append(" where t.BUSINESS_ACCOUNT = a.ACCOUNT_ID and a.ENTERPRISE_ID = e.ENTERPRISE_ID ");

        List<Object> paramsList = new ArrayList<Object>();

        //任务Id
        if (!StringUtils.isEmpty(qo.getId())) {
            sqlBuffer.append(" and t.ID =?");
            paramsList.add(qo.getId().trim());
        }

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

        //模板ID
        if (!StringUtils.isEmpty(qo.getTemplateId())) {
            sqlBuffer.append(" and t.TEMPLATE_ID =?");
            paramsList.add(qo.getTemplateId().trim());
        }

        //业务类型
        if (!StringUtils.isEmpty(qo.getBusinessType())) {
            sqlBuffer.append(" and t.BUSINESS_TYPE =?");
            paramsList.add(qo.getBusinessType().trim());
        }

        //发送状态
        if (!StringUtils.isEmpty(qo.getSendStatus())) {
            sqlBuffer.append(" and t.SEND_STATUS =?");
            paramsList.add(qo.getSendStatus().trim());
        }

        //时间起
        if (!StringUtils.isEmpty(qo.getStartDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.CREATED_TIME,'%Y-%m-%d') >=? ");
            paramsList.add(qo.getStartDate().trim());
        }
        //时间止
        if (!StringUtils.isEmpty(qo.getEndDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.CREATED_TIME,'%Y-%m-%d') <=? ");
            paramsList.add(qo.getEndDate().trim());
        }

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        Map<String, Object> map = jdbcTemplate.queryForMap(sqlBuffer.toString(), params);
        if(null == map || map.size()<1 || null == map.get("SUBMIT_NUMBER")){
            map = new HashMap<>();
            map.put("SUBMIT_NUMBER",0);
            map.put("SUCCESS_NUMBER",0);
            map.put("SUCCESS_SEND_NUMBER",0);
            map.put("FAILURE_NUMBER",0);
        }
        return map;

    }

    /**
     * 查询企业发送量
     *
     * @param qo
     * @return
     */
    public StatisticMessageSend statisticMessageSendCount(MessageAccountValidator qo) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" sum(t.SUBMIT_NUMBER) SUBMIT_NUMBER,");
        sqlBuffer.append(" sum(t.SUCCESS_NUMBER) SUCCESS_NUMBER,");
        sqlBuffer.append(" sum(t.SUCCESS_SEND_NUMBER) SUCCESS_SEND_NUMBER,");
        sqlBuffer.append(" sum(t.FAILURE_NUMBER) FAILURE_NUMBER,");
        sqlBuffer.append(" sum(t.NO_REPORT_NUMBER) NO_REPORT_NUMBER");
        sqlBuffer.append(" from message_web_task_info t");
        sqlBuffer.append(" where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        //业务类型
        if (!StringUtils.isEmpty(qo.getBusinessType())) {
            sqlBuffer.append(" and t.BUSINESS_TYPE =?");
            paramsList.add(qo.getBusinessType().trim());
        }

        //企业
        if (!StringUtils.isEmpty(qo.getEnterpriseId())) {
            sqlBuffer.append(" and t.ENTERPRISE_ID =?");
            paramsList.add(qo.getEnterpriseId().trim());
        }

        //账号
        if (!StringUtils.isEmpty(qo.getAccountId())) {
            sqlBuffer.append(" and t.BUSINESS_ACCOUNT like ?");
            paramsList.add("%"+qo.getAccountId().trim()+"%");
        }

        //时间起
        if (!StringUtils.isEmpty(qo.getStartDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.CREATED_TIME,'%Y-%m-%d') >=? ");
            paramsList.add(qo.getStartDate().trim());
        }
        //时间止
        if (!StringUtils.isEmpty(qo.getEndDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.CREATED_TIME,'%Y-%m-%d') <=? ");
            paramsList.add(qo.getEndDate().trim());
        }

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<StatisticMessageSend> list = this.queryForObjectList(sqlBuffer.toString(), params, new StatisticMessageSendRowMapper());
        StatisticMessageSend statisticMessageSend = new StatisticMessageSend();
        if(!StringUtils.isEmpty(list) && list.size()>0){
            statisticMessageSend = list.get(0);
        }

        return statisticMessageSend;

    }

    /**
     * 统计短信提交发送量
     *
     * @param qo
     * @return
     */
    public StatisticMessageSend statisticSubmitMessageSendCount(MessageWebTaskInfoValidator qo) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" sum(t.SUBMIT_NUMBER) SUBMIT_NUMBER,");
        sqlBuffer.append(" sum(t.SUCCESS_NUMBER) SUCCESS_NUMBER,");
        sqlBuffer.append(" sum(t.SUCCESS_SEND_NUMBER) SUCCESS_SEND_NUMBER,");
        sqlBuffer.append(" sum(t.FAILURE_NUMBER) FAILURE_NUMBER,");
        sqlBuffer.append(" sum(t.NO_REPORT_NUMBER) NO_REPORT_NUMBER");
        sqlBuffer.append(" from message_web_task_info t");
        sqlBuffer.append(" where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        //业务类型
        if (!StringUtils.isEmpty(qo.getBusinessType())) {
            sqlBuffer.append(" and t.BUSINESS_TYPE =?");
            paramsList.add(qo.getBusinessType().trim());
        }

        //企业
        if (!StringUtils.isEmpty(qo.getEnterpriseId())) {
            sqlBuffer.append(" and t.ENTERPRISE_ID =?");
            paramsList.add(qo.getEnterpriseId().trim());
        }

        //账号
        if (!StringUtils.isEmpty(qo.getBusinessAccount())) {
            sqlBuffer.append(" and t.BUSINESS_ACCOUNT like ?");
            paramsList.add("%"+qo.getBusinessAccount().trim()+"%");
        }

        //任务id
        if (!StringUtils.isEmpty(qo.getId())) {
            sqlBuffer.append(" and t.ID =?");
            paramsList.add(qo.getId().trim());
        }

        //内容
        if (!StringUtils.isEmpty(qo.getMessageContent())) {
            sqlBuffer.append(" and t.MESSAGE_CONTENT like ?");
            paramsList.add("%"+qo.getMessageContent().trim()+"%");
        }

        //时间起
        if (!StringUtils.isEmpty(qo.getStartDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.CREATED_TIME,'%Y-%m-%d') >=? ");
            paramsList.add(qo.getStartDate().trim());
        }
        //时间止
        if (!StringUtils.isEmpty(qo.getEndDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.CREATED_TIME,'%Y-%m-%d') <=? ");
            paramsList.add(qo.getEndDate().trim());
        }

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<StatisticMessageSend> list = this.queryForObjectList(sqlBuffer.toString(), params, new StatisticMessageSendRowMapper());
        StatisticMessageSend statisticMessageSend = new StatisticMessageSend();
        if(!StringUtils.isEmpty(list) && list.size()>0){
            statisticMessageSend = list.get(0);
        }

        return statisticMessageSend;

    }

    /**
     * 批量保存 待发短信
     *
     * @param messages     短消息
     * @param messageCount 发送短信数量
     * @param phoneCount   发送手机号数量
     */
    public void saveMessageBatch(List<MessageFormat> messages, Integer messageCount, Integer phoneCount) {
        final String sql = "insert into smoc_route.route_message_mt_info(ID,ACCOUNT_ID,PHONE_NUMBER,SUBMIT_TIME,MESSAGE_CONTENT,MESSAGE_FORMAT,MESSAGE_ID,TEMPLATE_ID,PROTOCOL,ACCOUNT_SRC_ID,ACCOUNT_BUSINESS_CODE,PHONE_NUMBER_NUMBER,MESSAGE_CONTENT_NUMBER,REPORT_FLAG,OPTION_PARAM,CREATED_TIME) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now()) ";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return messages.size();
            }

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                MessageFormat message = messages.get(i);
                ps.setLong(1, message.getId());
                ps.setString(2, message.getAccountId());
                ps.setString(3, message.getPhoneNumber());
                ps.setString(4, message.getSubmitTime());
                ps.setString(5, message.getMessageContent());
                ps.setString(6, message.getMessageFormat());
                ps.setString(7, message.getMessageId());
                ps.setString(8, message.getTemplateId());
                ps.setString(9, message.getProtocol());
                ps.setString(10, message.getAccountSrcId());
                ps.setString(11, message.getAccountBusinessCode());
                ps.setInt(12, phoneCount);
                ps.setInt(13, messageCount);
                ps.setInt(14, message.getReportFlag());
                ps.setString(15, message.getOptionParam());

            }

        });

    }
}
