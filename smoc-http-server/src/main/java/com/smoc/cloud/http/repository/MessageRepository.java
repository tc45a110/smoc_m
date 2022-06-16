package com.smoc.cloud.http.repository;

import com.google.gson.Gson;
import com.smoc.cloud.common.http.server.message.request.ReportBatchParams;
import com.smoc.cloud.common.http.server.message.request.ReportStatusRequestParams;
import com.smoc.cloud.common.http.server.message.response.MobileOriginalResponseParams;
import com.smoc.cloud.common.http.server.message.response.ReportResponseParams;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.http.entity.MessageFormat;
import com.smoc.cloud.http.entity.MessageHttpsTaskInfo;
import com.smoc.cloud.http.rowmapper.MobileOriginalResponseParamsRowMapper;
import com.smoc.cloud.http.rowmapper.ReportResponseParamsRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MessageRepository extends BasePageRepository {


    /**
     * 根据业务账号查询上行短信  每次做多返回1000条
     *
     * @param account
     * @return
     */
    public List<MobileOriginalResponseParams> getMobileOriginalByAccount(String account) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID,");
        sqlBuffer.append("  t.ACCOUNT_ID,");
        sqlBuffer.append("  t.MESSAGE_ID,");
        sqlBuffer.append("  t.PHONE_NUMBER,");
        sqlBuffer.append("  t.MO_TIME,");
        sqlBuffer.append("  t.MESSAGE_CONTENT,");
        sqlBuffer.append("  t.ACCOUNT_SRC_ID,");
        sqlBuffer.append("  t.ACCOUNT_BUSINESS_CODE,");
        sqlBuffer.append("  t.MESSAGE_FORMAT,");
        sqlBuffer.append("  t.OPTION_PARAM");
        sqlBuffer.append("  from smoc_route.route_message_mo_info t ");
        sqlBuffer.append("  where  t.ACCOUNT_ID=? order by t.MO_TIME desc");

        Object[] params = new Object[1];
        params[0] = account;

        //log.info("[获取上行短信sql]:{}", sqlBuffer);

        PageList<MobileOriginalResponseParams> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, 0, 1000, new MobileOriginalResponseParamsRowMapper());
        return pageList.getList();
    }

    /**
     * 批量删除上行短信
     *
     * @param params
     */
    public void batchDelete(final List<MobileOriginalResponseParams> params) {
        final String sql = "delete from smoc_route.route_message_mo_info where id=? ";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return params.size();
            }

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                MobileOriginalResponseParams responseParams = params.get(i);
                ps.setString(1, responseParams.getId());

            }

        });
    }

    /**
     * 根据业务账号查询状态报告  每次做多返回1000条
     *
     * @param reportStatusRequestParams
     * @return
     */
    public List<ReportResponseParams> getReport(ReportStatusRequestParams reportStatusRequestParams) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID,");
        sqlBuffer.append("  t.ACCOUNT_ID,");
        sqlBuffer.append("  t.PHONE_NUMBER,");
        sqlBuffer.append("  t.REPORT_TIME,");
        sqlBuffer.append("  t.SUBMIT_TIME,");
        sqlBuffer.append("  t.STATUS_CODE,");
        sqlBuffer.append("  t.MESSAGE_ID,");
        sqlBuffer.append("  t.TEMPLATE_ID,");
        sqlBuffer.append("  t.ACCOUNT_SRC_ID,");
        sqlBuffer.append("  t.ACCOUNT_BUSINESS_CODE,");
        sqlBuffer.append("  t.MESSAGE_TOTAL,");
        sqlBuffer.append("  t.MESSAGE_INDEX,");
        sqlBuffer.append("  t.OPTION_PARAM ");
        sqlBuffer.append("  from smoc_route.route_message_mr_info t ");
        sqlBuffer.append("  where  (1=1) ");

        List<Object> paramsList = new ArrayList<Object>();

        //业务账号
        if (!StringUtils.isEmpty(reportStatusRequestParams.getAccount())) {
            sqlBuffer.append(" and t.ACCOUNT_ID=? ");
            paramsList.add(reportStatusRequestParams.getAccount().trim());
        }

        //订单号
        if (!StringUtils.isEmpty(reportStatusRequestParams.getMsgId())) {
            sqlBuffer.append(" and t.MESSAGE_ID=? ");
            paramsList.add(reportStatusRequestParams.getMsgId().trim());
        }

        //手机号
        if (!StringUtils.isEmpty(reportStatusRequestParams.getMobile())) {
            sqlBuffer.append(" and t.PHONE_NUMBER=? ");
            paramsList.add(reportStatusRequestParams.getMobile().trim());
        }

        sqlBuffer.append(" order by t.SUBMIT_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        //log.info("[获取状态报告sql]:{}", sqlBuffer);

        PageList<ReportResponseParams> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, 0, 1000, new ReportResponseParamsRowMapper());
        return pageList.getList();
    }

    /**
     * 根据账号、起始日期 获取状态报告 每次最多返回1000条
     *
     * @param reportBatchParams
     * @return
     */
    public List<ReportResponseParams> getReportBatch(ReportBatchParams reportBatchParams) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID,");
        sqlBuffer.append("  t.ACCOUNT_ID,");
        sqlBuffer.append("  t.PHONE_NUMBER,");
        sqlBuffer.append("  t.REPORT_TIME,");
        sqlBuffer.append("  t.SUBMIT_TIME,");
        sqlBuffer.append("  t.STATUS_CODE,");
        sqlBuffer.append("  t.MESSAGE_ID,");
        sqlBuffer.append("  t.TEMPLATE_ID,");
        sqlBuffer.append("  t.ACCOUNT_SRC_ID,");
        sqlBuffer.append("  t.ACCOUNT_BUSINESS_CODE,");
        sqlBuffer.append("  t.MESSAGE_TOTAL,");
        sqlBuffer.append("  t.MESSAGE_INDEX,");
        sqlBuffer.append("  t.OPTION_PARAM ");
        sqlBuffer.append("  from smoc_route.route_message_mr_info t ");
        sqlBuffer.append("  where  t.ACCOUNT_ID=? and DATE_FORMAT(t.CREATED_TIME,'%Y-%m-%d') >=? and DATE_FORMAT(t.CREATED_TIME,'%Y-%m-%d') <=? order by t.SUBMIT_TIME desc");

        Object[] params = new Object[3];
        params[0] = reportBatchParams.getAccount();
        params[1] = reportBatchParams.getStartDate();
        params[2] = reportBatchParams.getEndDate();

        //log.info("[批量获取状态报告sql]:{}", sqlBuffer);

        PageList<ReportResponseParams> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, 0, 1000, new ReportResponseParamsRowMapper());
        log.info("[批量获取状态报告sql]:{}", new Gson().toJson(pageList.getList()));
        return pageList.getList();
    }

    /**
     * 批量删除状态报告
     *
     * @param reports
     */
    public void batchDeleteReports(final List<ReportResponseParams> reports) {
        final String sql = "delete from smoc_route.route_message_mr_info where id=? ";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return reports.size();
            }

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ReportResponseParams responseParams = reports.get(i);
                ps.setString(1, responseParams.getId());

            }

        });
    }

    /**
     * 批量保存 待发短信
     *
     * @param messages     短消息
     * @param messageCount 发送短信数量
     * @param phoneCount   发送手机号数量
     */
    public void saveMessageBatch(final List<MessageFormat> messages, Integer messageCount, Integer phoneCount) {
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

    /**
     * 批量保存 待发短信
     */
    public void saveBatchTask(MessageHttpsTaskInfo task) {

        StringBuffer sql = new StringBuffer("insert into message_https_task_info(ID,TEMPLATE_ID,BUSINESS_ACCOUNT,ENTERPRISE_ID,BUSINESS_TYPE,INFO_TYPE,MESSAGE_TYPE,SEND_TYPE,EXPAND_NUMBER,SPLIT_NUMBER,SUBMIT_NUMBER,MESSAGE_CONTENT,CREATED_BY,CREATED_TIME)");
        sql.append(" select '" + task.getId() + "','" + task.getTemplateId() + "','" + task.getBusinessAccount() + "',a.ENTERPRISE_ID,a.BUSINESS_TYPE,a.INFO_TYPE,a.BUSINESS_TYPE,'1','" + task.getExpandNumber() + "'," + task.getSplitNumber() + "," + task.getSubmitNumber() + ",'" + task.getMessageContent() + "','" + task.getCreatedBy() + "',now() from  account_base_info a  where a.ACCOUNT_ID='" + task.getBusinessAccount() + "' ");
        //log.info("[insert select]:{}", sql);
        jdbcTemplate.update(sql.toString());

    }


}
