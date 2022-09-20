/**
 * @desc
 * @author ma
 * @date 2017年10月10日
 * 
 */
package com.business.statistics.util;

import com.base.common.dao.LavenderDBSingleton;
import com.base.common.vo.AlarmMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * 告警信息处理类
 */
public class AlarmUtil  {
	private static final Logger logger = LoggerFactory.getLogger(AlarmUtil.class);

	public static void process(AlarmMessage alarmMessage) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO smoc_route.alarm_message_info");
		sql.append(" (ALARM_KEY,ALARM_VALUE,CREATED_TIME) ");
		sql.append("values(?,?,NOW())");

		// 在一个事务中更新数据
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());

			pstmt.setString(1, alarmMessage.getAlarmKey().name());
			pstmt.setString(2, alarmMessage.getAlarmValue());

			pstmt.execute();

			conn.commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if(conn != null){
				try {
					conn.rollback();
				} catch (Exception e1) {
					logger.error(e.getMessage(), e1);
				}
			}

		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
		}
		
	}

	public static void saveAlarmValues(List<AlarmMessage> alarmMessageList) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO smoc_route.alarm_message_info");
		sql.append(" (ALARM_KEY,ALARM_VALUE,CREATED_TIME) ");
		sql.append("values(?,?,NOW())");

		// 在一个事务中更新数据
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());

			for (AlarmMessage alarmMessage : alarmMessageList) {
				pstmt.setString(1, alarmMessage.getAlarmKey().name());
				pstmt.setString(2, alarmMessage.getAlarmValue());
				pstmt.addBatch();
			}

			pstmt.executeBatch();
			conn.commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if(conn != null){
				try {
					conn.rollback();
				} catch (Exception e1) {
					logger.error(e.getMessage(), e1);
				}
			}
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
		}

	}
	
}
