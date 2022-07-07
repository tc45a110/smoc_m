package com.smoc.cloud.scheduler.batch.filters.repository;

import com.smoc.cloud.scheduler.batch.filters.model.BusinessRouteValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Slf4j
@Service
public class RouteMessageRepository {

    @Autowired
    public DataSource dataSource;

    /**
     * 批量处理业务 业务1、保存带下发短信到短信通道表 2、对短信生成计费或冻结
     *
     * @param businessRouteValueList
     */
    @Async("threadPoolTaskExecutor")
    public void handlerBusinessBatch(List<BusinessRouteValue> businessRouteValueList) {
        Connection conn = null;
        Statement stmt = null;
        try {
//            conn = dataSource.getConnection();
//            conn.setAutoCommit(false);
//            stmt = conn.createStatement();
//            for (BusinessRouteValue businessRouteValue : businessRouteValueList) {
//                stmt.addBatch(this.buildRouteChannelMessageSql(businessRouteValue));
//                stmt.addBatch(this.buildUpdateFinanceSql(businessRouteValue));
//            }
//            stmt.executeBatch();
//            conn.commit();
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
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

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
//            conn = dataSource.getConnection();
//            conn.setAutoCommit(false);
//            StringBuffer sql = new StringBuffer();
//            sql.append("insert into smoc_route.route_audit_message_mt_info1 ");
//            sql.append("(ACCOUNT_ID,INFO_TYPE,PHONE_NUMBER,ACCOUNT_SUBMIT_TIME,MESSAGE_CONTENT,CHANNEL_ID,REASON,MESSAGE_MD5,MESSAGE_JSON,CREATED_TIME) ");
//            sql.append("values(?,?,?,?,?,?,?,?,?,now())");
//            stmt = conn.prepareStatement(sql.toString());
//            for (BusinessRouteValue businessRouteValue : list) {
//                stmt.setString(1, businessRouteValue.getAccountId());
//                stmt.setString(2, businessRouteValue.getInfoType());
//                stmt.setString(3, businessRouteValue.getPhoneNumber());
//                stmt.setString(4, businessRouteValue.getAccountSubmitTime());
//                stmt.setString(5, businessRouteValue.getMessageContent());
//                stmt.setString(6, businessRouteValue.getChannelId());
//                stmt.setString(7, businessRouteValue.getStatusMessage());
//                stmt.setString(8, DigestUtils.md5DigestAsHex(businessRouteValue.getMessageContent().getBytes()));
//                stmt.setString(9, new Gson().toJson(businessRouteValue));
//                stmt.addBatch();
//            }
//            stmt.executeBatch();
//            conn.commit();
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
    public void generateErrorMessageResponse(List<BusinessRouteValue> list) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            StringBuffer sql = new StringBuffer();
            sql.append("insert into smoc_route.route_message_mr_info1");
            sql.append(" (ACCOUNT_ID,PHONE_NUMBER,REPORT_TIME,SUBMIT_TIME,STATUS_CODE,STATUS_MESSAGE,MESSAGE_ID,TEMPLATE_ID,ACCOUNT_SRC_ID,ACCOUNT_BUSINESS_CODE,");
            sql.append("MESSAGE_TOTAL,MESSAGE_INDEX,OPTION_PARAM,CREATED_TIME) ");
            sql.append("values(?,?,now(),?,?,?,?,?,?,?,?,?,?,now())");
            stmt = conn.prepareStatement(sql.toString());
            for (BusinessRouteValue businessRouteValue : list) {
                stmt.setString(1, businessRouteValue.getAccountId());
                stmt.setString(2, businessRouteValue.getPhoneNumber());
//                stmt.setString(3, businessRouteValue.getChannelReportTime());
                stmt.setString(3, businessRouteValue.getAccountSubmitTime());
                stmt.setString(4, businessRouteValue.getStatusCode());
                stmt.setString(5, businessRouteValue.getStatusMessage());
                stmt.setString(6, businessRouteValue.getAccountMessageIds());
                stmt.setString(7, businessRouteValue.getAccountTemplateId());
                stmt.setString(8, businessRouteValue.getAccountSubmitSrcId());
                stmt.setString(9, businessRouteValue.getAccountBusinessCode());
                stmt.setInt(10, businessRouteValue.getMessageTotal());
                stmt.setInt(11,0);
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
        //log.info("[删除数据]：{}",list.size());
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement("delete from smoc_route.route_message_mt_info1 where ID=?");
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

    /**
     * 组建插入待发送通道表sql语句
     *
     * @param businessRouteValue
     * @return
     */
    public String buildRouteChannelMessageSql(BusinessRouteValue businessRouteValue) {
        StringBuffer insertRouteChannelMessageSql = new StringBuffer();
//        insertRouteChannelMessageSql.append("insert into smoc_route.route_channel_message_mt_info_");
//        insertRouteChannelMessageSql.append(businessRouteValue.getEnterpriseFlag().toLowerCase());
//        insertRouteChannelMessageSql.append(" (ACCOUNT_ID,ACCOUNT_PRIORITY,INFO_TYPE,PHONE_NUMBER,ACCOUNT_SUBMIT_TIME,MESSAGE_CONTENT,MESSAGE_JSON,CREATED_TIME)");
//        insertRouteChannelMessageSql.append(" values('" + businessRouteValue.getAccountId() + "',");
//        insertRouteChannelMessageSql.append("'" + businessRouteValue.getAccountPriority() + "',");
//        insertRouteChannelMessageSql.append("'" + businessRouteValue.getInfoType() + "',");
//        insertRouteChannelMessageSql.append("'" + businessRouteValue.getPhoneNumber() + "',");
//        insertRouteChannelMessageSql.append("'" + businessRouteValue.getAccountSubmitTime() + "',");
//        insertRouteChannelMessageSql.append("'" + businessRouteValue.getMessageContent() + "',");
//        insertRouteChannelMessageSql.append("'" + new Gson().toJson(businessRouteValue) + "',");
//        insertRouteChannelMessageSql.append(" now())");
        return insertRouteChannelMessageSql.toString();
    }

    /**
     * 冻结及扣费sql
     *
     * @param businessRouteValue
     * @return
     */
    public String buildUpdateFinanceSql(BusinessRouteValue businessRouteValue) {

        StringBuffer updateFinanceSql = new StringBuffer();
//        updateFinanceSql.append(" update smoc.finance_account set");
//        updateFinanceSql.append(" ACCOUNT_USABLE_SUM = ACCOUNT_USABLE_SUM - " + businessRouteValue.getMessagePrice() + ",");
//
//        // 1 下发时计费：扣费金额即冻结金额，暂不产生消费金额，一定周期内返还余额、产生消费金额
//        // 2 回执成功计费:扣费金额即消费金额，不产生冻结金额;
//        if ("1".equals(businessRouteValue.getConsumeType())) { //下发计费
//            updateFinanceSql.append(" ACCOUNT_CONSUME_SUM = ACCOUNT_CONSUME_SUM + " + businessRouteValue.getMessagePrice() + ",");
//        } else {
//            updateFinanceSql.append(" ACCOUNT_FROZEN_SUM = ACCOUNT_FROZEN_SUM + " + businessRouteValue.getMessagePrice() + ",");
//        }
//        updateFinanceSql.append(" UPDATED_TIME=now() ");
//        updateFinanceSql.append(" WHERE ACCOUNT_ID = '" + businessRouteValue.getAccountId() + "' ");
        return updateFinanceSql.toString();
    }


}
