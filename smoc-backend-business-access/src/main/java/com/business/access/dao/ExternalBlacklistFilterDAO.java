/**
 * @desc
 * 
 */
package com.business.access.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.common.dao.LavenderDBSingleton;
import com.base.common.vo.BusinessRouteValue;
import com.business.access.util.ThreadPoolExecutorFactory;

public class ExternalBlacklistFilterDAO {
	private static final Logger logger = LoggerFactory.getLogger(ExternalBlacklistFilterDAO.class);
	
	/**
	 * 保存黑名单过滤数据
	 * @param businessRouteValueList
	 * @param tableName
	 */
	public static void saveRouteMessageBlacklistFilterInfo(
			List<BusinessRouteValue> businessRouteValueList) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO smoc_route.route_message_blacklist_filter_info");
		sql.append(" (ACCOUNT_ID,ACCOUNT_PRIORITY,INFO_TYPE,PHONE_NUMBER,ACCOUNT_SUBMIT_TIME,MESSAGE_CONTENT,MESSAGE_JSON,CREATED_TIME) ");
		sql.append("values(");
		sql.append("?,?,?,?,?,?,?,NOW())");

		// 在一个事务中更新数据
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());
			int count = 0;
			for (BusinessRouteValue businessRouteValue : businessRouteValueList) {
				pstmt.setString(1, businessRouteValue.getAccountID());
				pstmt.setString(2, businessRouteValue.getAccountPriority());
				pstmt.setString(3, businessRouteValue.getInfoType());
				pstmt.setString(4, businessRouteValue.getPhoneNumber());
				pstmt.setString(5, businessRouteValue.getAccountSubmitTime());
				pstmt.setString(6, businessRouteValue.getMessageContent());
				pstmt.setString(7, businessRouteValue.toJSONString());
				pstmt.addBatch();
				count++;
				if( count % 10240 == 0){
					pstmt.executeBatch();
					pstmt.clearBatch();
				}
			}
			pstmt.executeBatch();
			pstmt.clearBatch();
			conn.commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			try {
				conn.rollback();
			} catch (Exception e1) {
				logger.error(e.getMessage(), e1);
			}
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
			businessRouteValueList = null;
		}
	}
	
	/**
	 * 当sql出现异常时，返回null，删除异常时，数据返回但是没有删除数据
	 * @param messageLoadMaxNumber
	 * @return
	 */
	public static List<BusinessRouteValue> loadRouteMessageBlacklistFilterInfo(int messageLoadMaxNumber) {
		List<BusinessRouteValue> list = new ArrayList<BusinessRouteValue>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ID,MESSAGE_CONTENT,MESSAGE_JSON,CREATED_TIME FROM ");
		sql.append("smoc_route.route_message_blacklist_filter_info");
		sql.append(" ORDER BY ID ASC LIMIT 0, ?");
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		ArrayList<Long> idList = null;
		long id = 0L;
		try {
			idList = new ArrayList<Long>();
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, messageLoadMaxNumber);
			rs = pstmt.executeQuery();
	
			Vector<Future<BusinessRouteValue>> futures = new Vector<Future<BusinessRouteValue>>();
			while (rs.next()) {
				
				id = rs.getLong("ID");
				idList.add(id);
				
				final String  messageJson = rs.getString("MESSAGE_JSON");
				final String  messageContent = rs.getString("MESSAGE_CONTENT");
		
				//通过并发方式提升对象封装速率
				Callable<BusinessRouteValue> callable = new Callable<BusinessRouteValue>() {				
					@Override
					public BusinessRouteValue call() throws Exception {
						BusinessRouteValue businessRouteValue = BusinessRouteValue.toObject(messageJson);
						businessRouteValue.setMessageContent(messageContent);
						return businessRouteValue;
					}
				};
				
				Future<BusinessRouteValue> future = ThreadPoolExecutorFactory.process(callable);
				futures.add(future);

			}
			
			for (Future<BusinessRouteValue> future : futures) {
				try {					
					list.add(future.get());
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			} 
			
			if(idList.size() > 0) {
				sql.setLength(0);
				sql.append("DELETE FROM smoc_route.route_message_blacklist_filter_info WHERE ID in (");
				
				for(int i = 0;i < idList.size();i++) {
					sql.append(idList.get(i));
					if(i != (idList.size() - 1)) {
						sql.append(",");
					}
				}
				sql.append(")");
				pstmt2 = conn.prepareStatement(sql.toString());
				pstmt2.execute();
			}
			conn.commit();
			if(idList.size() > 0) {
				logger.info("删除smoc_route.route_message_blacklist_filter_info共{}条",idList.size());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			try {
				conn.rollback();
			} catch (Exception e1) {
				logger.error(e1.getMessage(),e1);
			}
			return null;
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt2, null);
			LavenderDBSingleton.getInstance().closeAll(null, pstmt, conn);
		}
		return list;
	}
}


