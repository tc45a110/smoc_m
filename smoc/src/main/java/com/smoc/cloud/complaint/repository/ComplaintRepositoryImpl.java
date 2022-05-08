package com.smoc.cloud.complaint.repository;


import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.message.MessageChannelComplaintValidator;
import com.smoc.cloud.common.smoc.message.MessageComplaintInfoValidator;
import com.smoc.cloud.common.smoc.message.model.ComplaintExcelModel;
import com.smoc.cloud.common.smoc.utils.ChannelUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.complaint.rowmapper.MessageChannelComplaintRowMapper;
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
        sqlBuffer.append(", a.ACCOUNT_NAME");
        sqlBuffer.append(", IFNULL(t.BUSINESS_TYPE,a.BUSINESS_TYPE)BUSINESS_TYPE");
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
        sqlBuffer.append(", t.HANDLE_CARRIER_ID");
        sqlBuffer.append(", t.SEND_TYPE");
        sqlBuffer.append(", t.REPORTED_PROVINCE");
        sqlBuffer.append(", t.REPORTED_CITY");
        sqlBuffer.append(", t.REPORT_PROVINCE");
        sqlBuffer.append(", t.REPORT_CITY");
        sqlBuffer.append(", t.REPORT_CHANN");
        sqlBuffer.append(", t.CREATED_BY");
        sqlBuffer.append(", DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from message_complaint_info t left join account_base_info a on t.BUSINESS_ACCOUNT = a.ACCOUNT_ID");
        sqlBuffer.append("  where 1=1  ");

        List<Object> paramsList = new ArrayList<Object>();

        if(!StringUtils.isEmpty(qo.getEnterpriseId())){
            sqlBuffer.append(" and a.ENTERPRISE_ID = ? ");
            paramsList.add(qo.getEnterpriseId().trim());
        }

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
            sqlBuffer.append(" and t.REPORT_CONTENT like ? ");
            paramsList.add("%"+qo.getReportContent().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getReportedNumber())) {
            sqlBuffer.append(" and t.REPORTED_NUMBER like ? ");
            paramsList.add("%"+qo.getReportedNumber().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getCarrier())) {
            sqlBuffer.append(" and t.CARRIER like ? ");
            paramsList.add("%"+qo.getCarrier().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getStartDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.REPORT_DATE,'%Y-%m-%d') >= ? ");
            paramsList.add(qo.getStartDate().trim());
        }

        if (!StringUtils.isEmpty(qo.getEndDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.REPORT_DATE,'%Y-%m-%d') <= ? ");
            paramsList.add(qo.getEndDate().trim());
        }

        if(!StringUtils.isEmpty(qo.getComplaintSource())){
            sqlBuffer.append(" and t.COMPLAINT_SOURCE = ? ");
            paramsList.add(qo.getComplaintSource().trim());
        }

        if (!StringUtils.isEmpty(qo.getReportedProvince())) {
            sqlBuffer.append(" and t.REPORTED_PROVINCE like ? ");
            paramsList.add("%"+qo.getReportedProvince().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getReportedCity())) {
            sqlBuffer.append(" and t.REPORTED_CITY like ? ");
            paramsList.add("%"+qo.getReportedCity().trim()+"%");
        }


        sqlBuffer.append(" order by t.REPORT_DATE desc");

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
                ",REPORTED_NUMBER,REPORTED_PROVINCE,REPORTED_CITY,REPORT_PROVINCE,REPORT_CITY,REPORT_UNIT,REPORT_CHANN,HANDLE_CARRIER_ID,SEND_TYPE,HANDLE_STATUS,HANDLE_RESULT,HANDLE_REMARK,CREATED_BY,CREATED_TIME,COMPLAINT_SOURCE,NUMBER_CODE) " +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?) ";
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
                statement.setString(27, messageComplaintInfoValidator.getComplaintSource());
                if(StringUtils.isEmpty(entry.getNumberCode())){
                    statement.setString(28, ChannelUtils.getNumbeCode(entry.getReportedNumber()));
                }else{
                    statement.setString(28, entry.getNumberCode());
                }


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

    public List<MessageChannelComplaintValidator> channelComplaintRanking(MessageChannelComplaintValidator qo) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  a.CARRIER");
        sqlBuffer.append(", a.CHANNEL_ID");
        sqlBuffer.append(", b.CHANNEL_NAME");
        sqlBuffer.append(", a.COMPLAINT_NUM");
        sqlBuffer.append(", c.MESSAGE_SUCCESS_NUM");
        sqlBuffer.append(", truncate(a.COMPLAINT_NUM/c.MESSAGE_SUCCESS_NUM*1000000,2)COMPLAINT_RATE");
        sqlBuffer.append(", b.MAX_COMPLAINT_RATE");
        sqlBuffer.append("  from (select t.CHANNEL_ID,count(t.id)COMPLAINT_NUM,t.CARRIER from message_complaint_info t where t.COMPLAINT_SOURCE='day' and DATE_FORMAT(t.REPORT_DATE,'%Y-%m')=? and ifnull(t.CHANNEL_ID,'') <> '' group by t.CHANNEL_ID,t.CARRIER,DATE_FORMAT(t.REPORT_DATE,'%Y-%m'))a");
        sqlBuffer.append("  left join (select t.CHANNEL_ID,t.CHANNEL_NAME,t.MAX_COMPLAINT_RATE from config_channel_basic_info t where t.CHANNEL_STATUS='001')b on a.CHANNEL_ID = b.CHANNEL_ID ");
        sqlBuffer.append(" left join (select t.CHANNEL_ID,sum(t.MESSAGE_SUCCESS_NUM)MESSAGE_SUCCESS_NUM from message_daily_statistics t group by t.CHANNEL_ID)c on b.CHANNEL_ID = c.CHANNEL_ID ");
        sqlBuffer.append("  where 1=1  ");

        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(qo.getMonth().trim());

        sqlBuffer.append(" order by (a.COMPLAINT_NUM/c.MESSAGE_SUCCESS_NUM*1000000) desc ");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<MessageChannelComplaintValidator> list = this.queryForObjectList(sqlBuffer.toString(), params, new MessageChannelComplaintRowMapper());
        return list;
    }
}
