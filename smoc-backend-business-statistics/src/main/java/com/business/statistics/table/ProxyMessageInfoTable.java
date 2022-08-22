/**
 * @desc
 * 
 */
package com.business.statistics.table;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import com.base.common.constant.TableNameConstant;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.log.CategoryLog;
import com.base.common.manager.AlarmManager;
import com.base.common.manager.ResourceManager;
import com.base.common.util.DateUtil;
import com.base.common.vo.AlarmMessage;

public class ProxyMessageInfoTable {
	
	//代理业务层按条记录表前缀
	private static String TABLENAME_PREFIX = "proxy_message_info_";
	//告警信息
	private static String ALARM_VALUE = "ProxyMessageInfoTable";
	//获取60分钟之后的时间
	private static int afterMinute = 60;
	
	public static void main(String[] args) {
		try {
			//按照一小时后日期创建表
			String tableNameSuffix = DateUtil.getAfterMinuteDateTime(afterMinute, DateUtil.DATE_FORMAT_COMPACT_DAY);
			StringBuilder tableName = new StringBuilder();
			tableName.append(TABLENAME_PREFIX).append(tableNameSuffix);
			createTable(tableName.toString());
			
			tableName.setLength(0);
			
			//proxy_message_info数据保留时间
			int days = ResourceManager.getInstance().getIntValue("proxy.message.info.remain.days");
			//默认30天
			if(days == 0){
				days = 30 ;
			}
			tableNameSuffix = DateUtil.getAfterDayDateTime(-1 * days, DateUtil.DATE_FORMAT_COMPACT_DAY);
			tableName.append(TABLENAME_PREFIX).append(tableNameSuffix);
			dropTable(tableName.toString());
		} catch (Exception e) {
			CategoryLog.proxyLogger.error(e.getMessage(),e);
			AlarmManager.getInstance().process(new AlarmMessage(AlarmMessage.AlarmKey.BusinessStatistics, 
					new StringBuilder(ALARM_VALUE).append(":").append(e.getMessage()).toString()));
		}
		CategoryLog.proxyLogger.info("程序正常退出");
		System.exit(0);
		
	}
	
	/**
	 * 创建表
	 * @param tableName
	 */
	private static void createTable(String tableName) throws Exception{
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		CallableStatement pstmt = null;
		ResultSet rs = null;
		sql.append("{call smoc_route.");
		sql.append(TableNameConstant.PROCEDURE_CREATE_PROXY_MESSAGE_INFO);
		sql.append("(");
		sql.append("?");
		sql.append(")} ");
	
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareCall(sql.toString());
			pstmt.setString(1, tableName);
			pstmt.execute();
			CategoryLog.proxyLogger.info("初始化表{}",tableName);
		} catch (Exception e) {
			CategoryLog.proxyLogger.error(e.getMessage(),e);
			throw e;
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
		}
	}
	
	/**
	 * 删除表
	 * @param tableName
	 */
	private static void dropTable(String tableName) throws Exception{
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		CallableStatement pstmt = null;
		ResultSet rs = null;
		sql.append("{call smoc_route.");
		sql.append(TableNameConstant.PROCEDURE_DROP_PROXY_MESSAGE_INFO);
		sql.append("(");
		sql.append("?");
		sql.append(")} ");
	
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareCall(sql.toString());
			pstmt.setString(1, tableName);
			pstmt.execute();
			CategoryLog.proxyLogger.info("删除表{}",tableName);
		} catch (Exception e) {
			CategoryLog.proxyLogger.error(e.getMessage(),e);
			throw e;
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
		}
	}

}


