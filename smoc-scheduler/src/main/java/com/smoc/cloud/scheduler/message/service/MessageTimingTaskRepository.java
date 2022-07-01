package com.smoc.cloud.scheduler.message.service;

import com.smoc.cloud.common.smoc.message.model.MessageFormat;
import com.smoc.cloud.scheduler.common.repository.BatchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Service
public class MessageTimingTaskRepository {

    @Autowired
    private BatchRepository batchRepository;

    @Resource
    public JdbcTemplate jdbcTemplate;

    public void updataTaskStatus(String id, String status) {
        try {
            String updateSql = "update smoc.message_web_task_info set SEND_STATUS = '"+status+"' where id ='"+id+"' ";
            batchRepository.update(updateSql);
            log.info("[短信定时任务 更新任务状态]:{}",id);
        } catch (Exception e) {
            log.error("[短信定时任务 更新任务状态]:{}", e.getMessage());
        }
    }

    public List<String> findByGroupIdAndStatus(String groupId, String status) {
        //查询sql
        StringBuffer sqlBuffer = new StringBuffer("select t.MOBILE " +
                " from smoc.enterprise_book_info t where t.GROUP_ID='"+groupId+"' and t.status='"+status+"'" );

        sqlBuffer.append(" order by t.CREATED_TIME desc,t.ID ");

        List<String> list = batchRepository.queryForObjectList(sqlBuffer.toString(), null, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString(1);
            }
        });
        return list;
    }

    public void saveMessageBatch(List<MessageFormat> messages, Integer messageCount, Integer phoneCount, String taskId) {
        final String sql = "insert into smoc_route.route_message_mt_info(ACCOUNT_ID,PHONE_NUMBER,SUBMIT_TIME,MESSAGE_CONTENT,MESSAGE_FORMAT,MESSAGE_ID,TEMPLATE_ID,PROTOCOL,ACCOUNT_SRC_ID,ACCOUNT_BUSINESS_CODE,PHONE_NUMBER_NUMBER,MESSAGE_CONTENT_NUMBER,REPORT_FLAG,OPTION_PARAM) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        log.info("[定时短信群发-异步添加开始]数据：{}-{}- 共{}条", taskId,System.currentTimeMillis(), messages.size());

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return messages.size();
            }

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                MessageFormat message = messages.get(i);
                ps.setString(1, message.getAccountId());
                ps.setString(2, message.getPhoneNumber());
                ps.setString(3, message.getSubmitTime());
                ps.setString(4, message.getMessageContent());
                ps.setString(5, message.getMessageFormat());
                ps.setString(6, message.getMessageId());
                ps.setString(7, message.getTemplateId());
                ps.setString(8, message.getProtocol());
                ps.setString(9, message.getAccountSrcId());
                ps.setString(10, message.getAccountBusinessCode());
                ps.setInt(11, phoneCount);
                ps.setInt(12, messageCount);
                ps.setInt(13, message.getReportFlag());
                ps.setString(14, message.getOptionParam());

            }

        });
        log.info("[定时短信群发-异步添加结束]数据：{}-{}", taskId,System.currentTimeMillis());
    }
}
