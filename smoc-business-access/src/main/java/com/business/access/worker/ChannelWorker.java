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
import com.base.common.util.TableNameGeneratorUtil;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperQueueWorker;
import com.business.access.manager.FinanceWorkerManager;

public class ChannelWorker extends SuperQueueWorker<BusinessRouteValue>{
	
	private String channelID;
	
	private String tableName;

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
			FinanceWorkerManager.getInstance().process(businessRouteValueList);
			saveRouteChannelMessageMtInfo(businessRouteValueList,tableName);
			long interval = System.currentTimeMillis() - startTime;
			logger.info("通道{}保存数据条数{},耗时{}毫秒",channelID,businessRouteValueList.size(),interval);
		}else{
			//当没有数据时，需要暂停一会
			long interval = BusinessDataManager.getInstance().getMessageSaveIntervalTime();
			Thread.sleep(interval);
		}
	
	}
	
	public ChannelWorker(String channelID) {
		this.channelID = channelID;
		this.tableName = TableNameGeneratorUtil.generateRouteChannelMessageMTInfoTableName(channelID);
	}
	
	/**
	 * 保存发下数据
	 * @param businessRouteValueList
	 * @param tableName
	 */
	private void saveRouteChannelMessageMtInfo(
			List<BusinessRouteValue> businessRouteValueList,String tableName) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO smoc_route.");
		sql.append(tableName);
		sql.append(" (ACCOUNT_ID,ACCOUNT_PRIORITY,INFO_TYPE,PHONE_NUMBER,ACCOUNT_SUBMIT_TIME,MESSAGE_CONTENT,MESSAGE_JSON,CREATED_TIME) ");
		sql.append("values(");
		sql.append("?,?,?,?,?,?,?,NOW())");

		// 在一个事务中更新数据
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());

			for (BusinessRouteValue businessRouteValue : businessRouteValueList) {
				pstmt.setString(1, businessRouteValue.getAccountID());
				pstmt.setString(2, businessRouteValue.getAccountPriority());
				pstmt.setString(3, businessRouteValue.getInfoType());
				pstmt.setString(4, businessRouteValue.getPhoneNumber());
				pstmt.setString(5, businessRouteValue.getAccountSubmitTime());
				pstmt.setString(6, businessRouteValue.getMessageContent());
				pstmt.setString(7, businessRouteValue.toJSONString());

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


