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

import com.base.common.constant.FixedConstant;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.manager.BusinessDataManager;
import com.base.common.util.TableNameGeneratorUtil;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperConcurrentMapWorker;

public class ChannelRepairWorkerManager{
	
	private static ChannelRepairWorkerManager manager = new ChannelRepairWorkerManager();
	
	private Map<String, Worker> workerMap = new HashMap<String, Worker>();
	
	private ChannelRepairWorkerManager(){
	}
	
	public static ChannelRepairWorkerManager getInstance(){
		return manager;
	}
	
	public void saveBusinessRouteValue(String channelID, BusinessRouteValue businessRouteValue) {
		Worker worker = workerMap.get(channelID);
		if(worker == null) {
			worker = new Worker(channelID);
			worker.start();
			workerMap.put(channelID, worker);
		}
		worker.put(businessRouteValue.getBusinessMessageID(), businessRouteValue);
	}
	
	class Worker extends SuperConcurrentMapWorker<String, BusinessRouteValue>{
		
		private String channelID;
		
		private String tableName;
		
		private Worker(String channelID) {
			this.channelID = channelID;
			this.tableName = TableNameGeneratorUtil.generateRouteChannelMessageMTInfoTableName(channelID);
		}
		
		private void put(String businessMessageID,BusinessRouteValue businessRouteValue) {
			add(businessMessageID, businessRouteValue);
		}
		
		@Override
		protected void doRun() throws Exception {
			if(superMap.size() > 0) {
				long startTime = System.currentTimeMillis();
				//临时数据
				List<BusinessRouteValue> businessRouteValueList =  new ArrayList<BusinessRouteValue>(superMap.values());
				
				//将已经取走的数据在原始缓存中进行删除
				for(BusinessRouteValue businessRouteValue : businessRouteValueList){
					superMap.remove(businessRouteValue.getBusinessMessageID());
					logger.info(
							new StringBuilder().append("从补发队列删除")
							.append("{}accountID={}")
							.append("{}phoneNumber={}")
							.append("{}channelID={}")
							.append("{}businessMessageID={}")
							.toString(),
							FixedConstant.SPLICER,businessRouteValue.getAccountID(),
							FixedConstant.SPLICER,businessRouteValue.getPhoneNumber(),
							FixedConstant.SPLICER,businessRouteValue.getChannelID(),
							FixedConstant.SPLICER,businessRouteValue.getBusinessMessageID()
							);
				}
				saveRouteChannelMessageMtInfo(businessRouteValueList, tableName);
				logger.info("失败补发通道{}保存数据条数{},耗时{}毫秒",channelID,businessRouteValueList.size(),(System.currentTimeMillis() - startTime));
			}else {
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
}


