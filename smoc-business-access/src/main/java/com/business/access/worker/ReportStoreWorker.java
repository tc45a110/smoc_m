/**
 * @desc
 * 
 */
package com.business.access.worker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.base.common.dao.LavenderDBSingleton;
import com.base.common.manager.BusinessDataManager;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperQueueWorker;

/**
 * 保存状态报告到数据库
 */
public class ReportStoreWorker extends SuperQueueWorker<BusinessRouteValue>{

	@Override
	public void doRun() throws Exception {

		if(superQueue.size() > 0){
			long startTime = System.currentTimeMillis();
			//临时数据
			List<BusinessRouteValue> businessRouteValueList;
			synchronized (lock) {
				businessRouteValueList = new ArrayList<BusinessRouteValue>(superQueue);
				superQueue.clear();
			}
			saveRouteMessageMrInfo(businessRouteValueList);
			long interval = System.currentTimeMillis() - startTime;
			logger.info("状态报告保存数据条数{},耗时{}毫秒",businessRouteValueList.size(),interval);
		}else{
			//当没有数据时，需要暂停一会
			long interval = BusinessDataManager.getInstance().getMessageSaveIntervalTime();
			Thread.sleep(interval);
		}
	
	}
	
	/**
	 * 保存发下数据
	 * @param businessRouteValueList
	 * @param tableName
	 */
	private void saveRouteMessageMrInfo(
			List<BusinessRouteValue> businessRouteValueList) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO smoc_route.route_message_mr_info");
		sql.append(" (ACCOUNT_ID,PHONE_NUMBER,REPORT_TIME,SUBMIT_TIME,STATUS_CODE,MESSAGE_ID,TEMPLATE_ID,ACCOUNT_SRC_ID,ACCOUNT_BUSINESS_CODE,");
		sql.append("MESSAGE_TOTAL,MESSAGE_INDEX,OPTION_PARAM,CREATED_TIME) ");
		sql.append("values(");
		sql.append("?,?,?,?,?,?,?,?,?,?,?,?,NOW())");

		// 在一个事务中更新数据
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());

			for (BusinessRouteValue businessRouteValue : businessRouteValueList) {
				pstmt.setString(1, businessRouteValue.getAccountID());
				pstmt.setString(2, businessRouteValue.getPhoneNumber());
				pstmt.setString(3, businessRouteValue.getChannelReportTime());
				pstmt.setString(4, businessRouteValue.getAccountSubmitTime());
				pstmt.setString(5, businessRouteValue.getStatusCode());
				pstmt.setString(6, businessRouteValue.getAccountMessageIDs());
				pstmt.setString(7, businessRouteValue.getAccountTemplateID());
				pstmt.setString(8, businessRouteValue.getAccountSubmitSRCID());
				pstmt.setString(9, businessRouteValue.getAccountBusinessCode());
				pstmt.setInt(10, businessRouteValue.getMessageTotal());
				pstmt.setInt(11, businessRouteValue.getMessageIndex());
				pstmt.setString(12, businessRouteValue.getOptionParam());

				pstmt.addBatch();
			}
			pstmt.executeBatch();
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
		}
	}
	
}


