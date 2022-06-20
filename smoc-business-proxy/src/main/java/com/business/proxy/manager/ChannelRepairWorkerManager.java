/**
 * @desc
 * 
 */
package com.business.proxy.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.base.common.dao.LavenderDBSingleton;
import com.base.common.manager.BusinessDataManager;
import com.base.common.util.TableNameGeneratorUtil;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperMapWorker;

public class ChannelRepairWorkerManager extends SuperMapWorker<String,List<BusinessRouteValue>>{
	
	private static ChannelRepairWorkerManager manager = new ChannelRepairWorkerManager();
	
	private ChannelRepairWorkerManager(){
		this.start();
	}
	
	public static ChannelRepairWorkerManager getInstance(){
		return manager;
	}
	
	public void saveBusinessRouteValue(String channelID, BusinessRouteValue businessRouteValue) {
		List<BusinessRouteValue> list = superMap.get(channelID);
		if(list == null) {
			list = new ArrayList<BusinessRouteValue>();
			superMap.put(channelID, list);
		}
		list.add(businessRouteValue);
	}

	@Override
	public void doRun() throws Exception {

		if(superMap.size() > 0){
			long startTime = System.currentTimeMillis();
			//临时数据
			Map<String,List<BusinessRouteValue>> channelIDMap;
			synchronized (lock) {
				channelIDMap = new HashMap<String,List<BusinessRouteValue>>(superMap);
				superMap.clear();
			}
			for(String channelID : channelIDMap.keySet()) {
				String tableName = TableNameGeneratorUtil.generateRouteChannelMessageMTInfoTableName(channelID);
				saveRouteChannelMessageMtInfo(channelIDMap.get(channelID), tableName);
			}
			long interval = System.currentTimeMillis() - startTime;
			logger.info("保存数据条数{},耗时{}毫秒",channelIDMap.size(),interval);
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


