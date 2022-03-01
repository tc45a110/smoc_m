package com.smoc.cloud.complaint.repository;


import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.filter.ExcelModel;
import com.smoc.cloud.common.smoc.filter.FilterBlackListValidator;
import com.smoc.cloud.common.smoc.message.MessageComplaintInfoValidator;
import com.smoc.cloud.common.smoc.message.model.ComplaintExcelModel;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.complaint.rowmapper.MessageComplaintInfoRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 2020/5/28 15:44
 **/
@Slf4j
public class ComplaintRepositoryImpl extends BasePageRepository {


    public PageList<MessageComplaintInfoValidator> page(PageParams<MessageComplaintInfoValidator> pageParams) {

        //查询条件
        MessageComplaintInfoValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", t.BUSINESS_ACCOUNT");
        sqlBuffer.append(", a.BUSINESS_TYPE");
        sqlBuffer.append(", t.REPORT_NUMBER");
        sqlBuffer.append(", t.NUMBER_CODE");
        sqlBuffer.append(", t.CHANNEL_ID");
        sqlBuffer.append(", t.CARRIER_SOURCE");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.SEND_DATE");
        sqlBuffer.append(", t.SEND_RATE");
        sqlBuffer.append(", t.REPORT_CONTENT");
        sqlBuffer.append(", t.CONTENT_TYPE");
        sqlBuffer.append(", t.IS_12321");
        sqlBuffer.append(", t.REPORT_SOURCE");
        sqlBuffer.append(", t.REPORT_DATE");
        sqlBuffer.append(", t.REPORTED_NUMBER");
        sqlBuffer.append(", t.CREATED_BY");
        sqlBuffer.append(", DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from message_complaint_info t left join account_base_info a on t.BUSINESS_ACCOUNT = a.ACCOUNT_ID");
        sqlBuffer.append("  where 1=1  ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getCarrierSource())) {
            sqlBuffer.append(" and t.CARRIER_SOURCE like ? ");
            paramsList.add("%"+qo.getCarrierSource().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getBusinessAccount())) {
            sqlBuffer.append(" and t.BUSINESS_ACCOUNT like ? ");
            paramsList.add("%"+qo.getBusinessAccount().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getBusinessType())) {
            sqlBuffer.append(" and a.BUSINESS_TYPE like ? ");
            paramsList.add("%"+qo.getBusinessType().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getReportNumber())) {
            sqlBuffer.append(" and t.REPORT_NUMBER like ? ");
            paramsList.add("%"+qo.getReportNumber().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getReportContent())) {
            sqlBuffer.append(" and t.REPORT_NUMBER like ? ");
            paramsList.add("%"+qo.getReportContent().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getStartDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.CREATED_TIME,'%Y-%m-%d') >= ? ");
            paramsList.add(qo.getStartDate().trim());
        }

        if (!StringUtils.isEmpty(qo.getEndDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.CREATED_TIME,'%Y-%m-%d') <= ? ");
            paramsList.add(qo.getEndDate().trim());
        }


        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<MessageComplaintInfoValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new MessageComplaintInfoRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }

    public void batchSave(MessageComplaintInfoValidator messageComplaintInfoValidator) {

        Connection connection = null;
        PreparedStatement statement = null;
        List<ComplaintExcelModel> list= messageComplaintInfoValidator.getComplaintList();
        final String sql = "insert into message_complaint_info(ID,BUSINESS_ACCOUNT,REPORT_NUMBER,CHANNEL_ID,CARRIER_SOURCE,CARRIER,SEND_DATE,SEND_RATE,REPORT_CONTENT,CONTENT_TYPE,IS_12321,REPORT_SOURCE,REPORT_DATE" +
                ",REPORTED_NUMBER,REPORTED_PROVINCE,REPORTED_CITY,REPORT_PROVINCE,REPORT_CITY,REPORT_UNIT,REPORT_CHANN,HANDLE_CARRIER_ID,SEND_TYPE,HANDLE_STATUS,HANDLE_RESULT,HANDLE_REMARK,CREATED_BY,CREATED_TIME) " +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now()) ";
        log.info(sql);
        log.info("[投诉导入开始]数据：{}- 共{}条", System.currentTimeMillis(),list.size());
        try {
            connection = jdbcTemplate.getDataSource().getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql);
            for (ComplaintExcelModel entry : list) {
                statement.setString(1, UUID.uuid32());
                statement.setString(2, entry.getBusinessAccount());
                statement.setString(3, entry.getReportNumber());
                statement.setString(4, entry.getChannelId());
                statement.setString(5, messageComplaintInfoValidator.getCarrier());
                statement.setString(6, entry.getCarrier());
                statement.setString(7, entry.getSendDate());
                statement.setString(8, entry.getSendRate());
                statement.setString(9, entry.getReportContent());
                statement.setString(10, entry.getContentType());
                statement.setString(11, entry.getIs12321());
                statement.setString(12, entry.getReportSource());
                statement.setString(13, entry.getReportDate());
                statement.setString(14, entry.getReportedNumber());
                statement.setString(15, entry.getReportedProvince());
                statement.setString(16, entry.getReportedCity());
                statement.setString(17, entry.getReportProvince());
                statement.setString(18, entry.getReportCity());
                statement.setString(19, entry.getReportUnit());
                statement.setString(20, entry.getReportChann());
                statement.setString(21, entry.getHandleCarrierId());
                statement.setString(22, entry.getSendType());
                statement.setString(23, entry.getHandleStatus());
                statement.setString(24, entry.getHandleResult());
                statement.setString(25, entry.getHandleRemark());
                statement.setString(26, messageComplaintInfoValidator.getCreatedBy());

                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != statement) {
                    statement.close();
                }
                if (null != connection) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        log.info("[投诉导入结束]数据：{}", System.currentTimeMillis());

    }
}
