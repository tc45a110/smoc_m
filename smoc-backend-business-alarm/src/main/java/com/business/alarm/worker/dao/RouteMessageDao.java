package com.business.alarm.worker.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.base.common.constant.TableNameConstant;
import com.base.common.dao.LavenderDBSingleton;


public class RouteMessageDao {
	private static final Logger logger = LoggerFactory.getLogger(RouteMessageDao.class);

	// 获取route_message_mt_info下行数据临时表中的数据条数
	public static int routeMessageMtInfoNumber() {
		int number = 0;
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		sql.append("SELECT COUNT(*) FROM smoc_route.`route_message_mt_info`");
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				number = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			LavenderDBSingleton.getInstance().closeAll(null, pstmt, conn);
		}
		return number;
	}

	// 获取smoc_route中所有的通道级临时表名
	public static List<String> getTable() {
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<String> list = new ArrayList<String>();

		sql.append("SELECT TABLE_NAME FROM information_schema.TABLES WHERE table_schema = 'smoc_route'");
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);

			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (rs.getString("TABLE_NAME")
						.startsWith(TableNameConstant.CHANNEL_MESSAGE_MT_INFO_TABLE_NAME_PREFIX)) {
					list.add(rs.getString("TABLE_NAME"));
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			LavenderDBSingleton.getInstance().closeAll(null, pstmt, conn);
		}
		return list;

	}

	// 获取route_channel_message_mt_info_通道级下行数据临时表中的数据条数
	public static Map<String, Long> routeChannelMessageMtInfoNumber(String tableName) {

		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, Long> map = new HashMap<String, Long>();

		sql.append("SELECT COUNT(*) FROM smoc_route.").append(tableName);
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				map.put(tableName, rs.getLong(1));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			LavenderDBSingleton.getInstance().closeAll(null, pstmt, conn);
		}
		return map;
	}

	/**
	 * 获取告警信息表alarm_message_info中的信息,获取后删除
	 * 
	 * @return
	 */
	public static Map<String, String> alarmMessage() {
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;		
		Map<String, String> map = new HashMap<String, String>();
		ArrayList<Long> idList = new ArrayList<Long>();
		sql.append("SELECT ID, ALARM_KEY,ALARM_VALUE FROM smoc_route.alarm_message_info");
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				idList.add(rs.getLong("ID"));
				map.put(rs.getString("ALARM_KEY"), rs.getString("ALARM_VALUE"));				
			}
			if (idList.size() > 0) {
				sql.setLength(0);
				sql.append("DELETE FROM smoc_route.alarm_message_info ");
				sql.append(" WHERE ");
				sql.append("  ID in ( ");
				for (int i = 0; i < idList.size(); i++) {
					sql.append(idList.get(i));
					if (i != (idList.size() - 1)) {
						sql.append(",");
					}
				}
				sql.append(")");
				pstmt2 = conn.prepareStatement(sql.toString());
				pstmt2.execute();
			}
			conn.commit();
			if (idList.size() > 0) {
				logger.info("删除smoc_route.alarm_message_info共{}条", idList.size());

			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt, null);
			LavenderDBSingleton.getInstance().closeAll(null, pstmt2, conn);
		}
		return map;
	}

	// 获取route_message_mr_info状态报告临时表中的数据条数
	public static int routeMessageMrInfoNumber() {
		int number = 0;
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		sql.append("SELECT COUNT(*) FROM smoc_route.`route_message_mr_info`");
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				number = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			LavenderDBSingleton.getInstance().closeAll(null, pstmt, conn);
		}
		return number;
	}

	// 获取config_channel_basic_info时表中通道的运行状态
	public static List<String> channelRunStatus() {

		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<String> list = new ArrayList<String>();

		sql.append(
				"SELECT CHANNEL_ID FROM smoc.config_channel_basic_info where CHANNEL_STATUS='001' and CHANNEL_RUN_STATUS='2'");
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(rs.getString("CHANNEL_ID"));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			LavenderDBSingleton.getInstance().closeAll(null, pstmt, conn);
		}
		return list;
	}
}
