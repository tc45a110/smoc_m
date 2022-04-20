package com.smoc.cloud.http.repository;

import com.google.gson.Gson;
import com.smoc.cloud.common.http.server.message.request.MobileOriginalRequestParams;
import com.smoc.cloud.common.http.server.message.request.ReportBatchParams;
import com.smoc.cloud.common.http.server.message.request.ReportStatusRequestParams;
import com.smoc.cloud.common.http.server.message.response.MobileOriginalResponseParams;
import com.smoc.cloud.common.http.server.message.response.ReportResponseParams;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.http.rowmapper.MobileOriginalResponseParamsRowMapper;
import com.smoc.cloud.http.rowmapper.ReportResponseParamsRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Service;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Service
public class MessageRepository extends BasePageRepository {


    /**
     * 根据业务账号查询上行短信  每次做多返回1000条
     *
     * @param requestParams
     * @return
     */
    public List<MobileOriginalResponseParams> getMobileOriginalByAccount(MobileOriginalRequestParams requestParams) {

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
        params[0] = requestParams.getAccount();

        log.info("[获取上行短信sql]:{}", sqlBuffer);

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
    public List<ReportResponseParams> getReportByAccount(ReportStatusRequestParams reportStatusRequestParams) {

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
        sqlBuffer.append("  where  t.ACCOUNT_ID=?  order by t.SUBMIT_TIME desc");

        Object[] params = new Object[1];
        params[0] = reportStatusRequestParams.getAccount();

        log.info("[获取状态报告sql]:{}", sqlBuffer);

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

        log.info("[批量获取状态报告sql]:{}", sqlBuffer);

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


}
