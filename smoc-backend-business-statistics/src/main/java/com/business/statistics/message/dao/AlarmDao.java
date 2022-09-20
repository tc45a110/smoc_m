package com.business.statistics.message.dao;

import com.base.common.cache.CacheBaseService;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.log.CategoryLog;
import com.base.common.util.DateUtil;
import com.base.common.vo.AccountDelayRateAlarmConfiguration;
import com.base.common.vo.AccountSuccessRateAlarmConfiguration;
import com.business.statistics.message.alarm.AccountBusinessAlarm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AlarmDao {
    private static final Logger logger = LoggerFactory.getLogger(AlarmDao.class);

    public static Map<String, AccountSuccessRateAlarmConfiguration> loadAccountSuccessRateAlarmConfiguration(Date nowDate) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Map<String, AccountSuccessRateAlarmConfiguration> resultMap = new HashMap<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ACCOUNT_ID,STATISTICS_SEND_NUMBER_THRESHOLD,SUCCESS_RATE_THRESHOLD,EVALUATE_NUMBER,EVALUATE_INTERVAL_TIME ");
        sql.append("FROM smoc_route.account_success_rate_alarm");

        // 在一个事务中更新数据
        try {
            conn = LavenderDBSingleton.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String accountID = rs.getString("ACCOUNT_ID");
                AccountSuccessRateAlarmConfiguration configuration = CacheBaseService.getAccountSuccessRateAlarmConfigurationToMiddlewareCache(accountID);
                if (configuration == null) {
                    configuration = new AccountSuccessRateAlarmConfiguration();
                    configuration.setAccountID(accountID);
                }
                configuration.setStatisticsSendNumberThreshold(rs.getInt("STATISTICS_SEND_NUMBER_THRESHOLD"));
                configuration.setSuccessRateThreshold(rs.getDouble("SUCCESS_RATE_THRESHOLD"));
                configuration.setEvaluateNumber(rs.getInt("EVALUATE_NUMBER"));
                configuration.setEvaluateIntervalTime(rs.getInt("EVALUATE_INTERVAL_TIME"));

                if (configuration.getLastEvaluateDate() != null) {
                    int intervalTime = DateUtil.getMinuteIntervalTime(configuration.getLastEvaluateDate(), nowDate);
                    logger.info("SuccessRate:account={},intervalTime={}",accountID,intervalTime);
                    if (intervalTime % configuration.getEvaluateIntervalTime() != 0) {
                        continue;
                    }
                }
                resultMap.put(accountID, configuration);
            }
            return resultMap;
        } catch (Exception e) {
            CategoryLog.alarmLogger.error(e.getMessage(), e);
            try {
                if(conn != null){
                    conn.rollback();
                }
            } catch (Exception e1) {
                CategoryLog.alarmLogger.error(e.getMessage(), e1);
            }
        } finally {
            LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
        }
        return null;
    }

    public static Map<String, AccountDelayRateAlarmConfiguration> loadAccountDelayRateAlarmConfiguration(Date nowDate) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Map<String, AccountDelayRateAlarmConfiguration> resultMap = new HashMap<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ACCOUNT_ID,DELAY_TIME_THRESHOLD,DELAY_RATE_THRESHOLD,EVALUATE_NUMBER,EVALUATE_INTERVAL_TIME,STATISTICS_NUMBER_THRESHOLD");
        sql.append(" FROM smoc_route.account_delay_rate_alarm ");

        // 在一个事务中更新数据
        try {
            conn = LavenderDBSingleton.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String accountID = rs.getString("ACCOUNT_ID");
                //AccountDelayRateAlarmConfiguration configuration = new AccountDelayRateAlarmConfiguration();
                AccountDelayRateAlarmConfiguration configuration = CacheBaseService.getAccountDelayRateAlarmConfigurationToMiddlewareCache(accountID);
                if (configuration == null) {
                    configuration = new AccountDelayRateAlarmConfiguration();
                    configuration.setAccountID(accountID);
                }
                configuration.setDelayTimeThreshold(rs.getInt("DELAY_TIME_THRESHOLD"));
                configuration.setDelayRateThreshold(rs.getDouble("DELAY_RATE_THRESHOLD"));
                configuration.setEvaluateNumber(rs.getInt("EVALUATE_NUMBER"));
                configuration.setEvaluateIntervalTime(rs.getInt("EVALUATE_INTERVAL_TIME"));
                configuration.setStatisticsNumber(rs.getInt("STATISTICS_NUMBER_THRESHOLD"));

                if (configuration.getLastEvaluateDate() != null) {
                    int intervalTime = DateUtil.getMinuteIntervalTime(configuration.getLastEvaluateDate(), nowDate);
                    logger.info("DelayRate:account={},intervalTime={}",accountID,intervalTime);
                    if (intervalTime % configuration.getEvaluateIntervalTime() != 0) {
                        continue;
                    }
                }
                resultMap.put(accountID, configuration);
            }
            return resultMap;
        } catch (Exception e) {
            CategoryLog.alarmLogger.error(e.getMessage(), e);
            try {
                if(conn != null){
                    conn.rollback();
                }
            } catch (Exception e1) {
                CategoryLog.alarmLogger.error(e.getMessage(), e1);
            }
        } finally {
            LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
        }
        return null;
    }
}
