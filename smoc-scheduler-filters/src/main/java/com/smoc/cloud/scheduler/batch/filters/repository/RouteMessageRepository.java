package com.smoc.cloud.scheduler.batch.filters.repository;

import com.google.gson.Gson;
import com.smoc.cloud.scheduler.batch.filters.model.BusinessRouteValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

@Slf4j
@Service
public class RouteMessageRepository {

    @Autowired
    public DataSource dataSource;

    /**
     * 为过滤出来的审核短信，入审核库
     *
     * @param list
     */
    @Async("threadPoolTaskExecutor")
    public void generateMessageAudit(List<BusinessRouteValue> list) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            StringBuffer sql = new StringBuffer();
            sql.append("INSERT INTO smoc_route.route_audit_message_mt_info ");
            sql.append("(ACCOUNT_ID,INFO_TYPE,PHONE_NUMBER,ACCOUNT_SUBMIT_TIME,MESSAGE_CONTENT,CHANNEL_ID,REASON,MESSAGE_MD5,MESSAGE_JSON,CREATED_TIME) ");
            sql.append("VALUES(?,?,?,?,?,?,?,?,?,NOW())");
            stmt = conn.prepareStatement(sql.toString());
            for (BusinessRouteValue businessRouteValue : list) {
                stmt.setString(1, businessRouteValue.getAccountId());
                stmt.setString(2, businessRouteValue.getInfoType());
                stmt.setString(3, businessRouteValue.getPhoneNumber());
                stmt.setString(4, businessRouteValue.getAccountSubmitTime());
                stmt.setString(5, businessRouteValue.getMessageContent());
                stmt.setString(6, businessRouteValue.getChannelId());
                stmt.setString(7, businessRouteValue.getAuditReason());
                stmt.setString(8, DigestUtils.md5DigestAsHex(businessRouteValue.getMessageContent().getBytes()));
                stmt.setString(9, new Gson().toJson(businessRouteValue));
                stmt.addBatch();
            }
            stmt.executeBatch();
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != conn) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
                if (null != stmt) {
                    stmt.clearBatch();
                    stmt.clearParameters();
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 为过滤出来的非法短信，生成状态报告
     *
     * @param list
     */
    @Async("threadPoolTaskExecutor")
    public void generateMessageResponse(List<BusinessRouteValue> list) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            StringBuffer sql = new StringBuffer();
            sql.append("INSERT INTO smoc_route.route_message_mr_info1");
            sql.append(" (ACCOUNT_ID,PHONE_NUMBER,REPORT_TIME,SUBMIT_TIME,STATUS_CODE,MESSAGE_ID,TEMPLATE_ID,ACCOUNT_SRC_ID,ACCOUNT_BUSINESS_CODE,");
            sql.append("MESSAGE_TOTAL,MESSAGE_INDEX,OPTION_PARAM,CREATED_TIME) ");
            sql.append("values(?,?,?,?,?,?,?,?,?,?,?,?,NOW())");
            stmt = conn.prepareStatement(sql.toString());
            for (BusinessRouteValue businessRouteValue : list) {
                stmt.setString(1, businessRouteValue.getAccountId());
                stmt.setString(2, businessRouteValue.getPhoneNumber());
                stmt.setString(3, businessRouteValue.getChannelReportTime());
                stmt.setString(4, businessRouteValue.getAccountSubmitTime());
                stmt.setString(5, businessRouteValue.getStatusCode());
                stmt.setString(6, businessRouteValue.getAccountMessageIds());
                stmt.setString(7, businessRouteValue.getAccountTemplateId());
                stmt.setString(8, businessRouteValue.getAccountSubmitSrcId());
                stmt.setString(9, businessRouteValue.getAccountBusinessCode());
                stmt.setInt(10, businessRouteValue.getMessageTotal());
                stmt.setInt(11, businessRouteValue.getMessageIndex());
                stmt.setString(12, businessRouteValue.getOptionParam());
                stmt.addBatch();
            }
            stmt.executeBatch();
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != conn) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
                if (null != stmt) {
                    stmt.clearBatch();
                    stmt.clearParameters();
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除短信临时表中，已处理的数据
     *
     * @param list
     */
    public void deleteByBatch(final List<? extends BusinessRouteValue> list) {
        Connection conn = null;
        PreparedStatement stmt = null;
//        log.info("[删除数据]：{}",list.size());
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement("delete from smoc_route.route_message_mt_info1 where id=?");
            for (BusinessRouteValue businessRouteValue : list) {
                stmt.setLong(1, businessRouteValue.getId());   // raw_tag_data// org_raw_tag_data
                stmt.addBatch();
            }
            stmt.executeBatch();
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != conn) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
                if (null != stmt) {
                    stmt.clearBatch();
                    stmt.clearParameters();
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
