/**
 * @desc
 * 
 */
package com.business.access.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.DigestUtils;

import com.base.common.constant.FixedConstant;
import com.base.common.constant.InsideStatusCodeConstant;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.manager.BusinessDataManager;
import com.base.common.manager.MessageSubmitFailManager;
import com.base.common.util.DateUtil;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperQueueWorker;

public class AuditWorkerManager extends SuperQueueWorker<BusinessRouteValue>{
	
	private static AuditWorkerManager manager = new AuditWorkerManager();
	
	AuditWorker worker;
	
	
	private AuditWorkerManager(){
		worker = new AuditWorker();
		worker.start();
		this.start();
	}
	
	public static AuditWorkerManager getInstance(){
		return manager;
	}
	
	/**
	 * 内部过滤服务后进入审核判断，如果不用审核，则进入外部过滤线程
	 * @param businessRouteValue
	 */
	public void process(BusinessRouteValue businessRouteValue){
		//进入审核
		if(InsideStatusCodeConstant.StatusCode.AUDIT.name().equals(businessRouteValue.getNextNodeCode())){
			add(businessRouteValue);
		}else{
			ExternalBlacklistFilterWorkerManager.getInstance().process(businessRouteValue);
		}
		
	}
	
	@Override
	protected void doRun() throws Exception {
		if(superQueue.size() > 0){
			long start = System.currentTimeMillis();
			List<BusinessRouteValue> businessRouteValueList;
			synchronized (lock) {
				businessRouteValueList = new ArrayList<BusinessRouteValue>(superQueue);
				superQueue.clear();
			}
			doAudit(businessRouteValueList);
			long interval = System.currentTimeMillis() - start;
			logger.info("保存数据条数{},耗时{}毫秒",businessRouteValueList.size(),interval);
		}else{
			long interval = BusinessDataManager.getInstance().getMessageSaveIntervalTime();
			Thread.sleep(interval);
		}
		
	}
	
	/**
	 * 保存审核数据
	 * @param businessRouteValueList
	 */
	private void doAudit(List<BusinessRouteValue> businessRouteValueList){
		saveRouteMessageAuditInfo(businessRouteValueList);
	}
	
	/**
	 * 保存需要审核的业务数据
	 * @param businessRouteValueList
	 */
	private void saveRouteMessageAuditInfo(List<BusinessRouteValue> businessRouteValueList) {
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO smoc_route.route_audit_message_mt_info ");
		sql.append("(ACCOUNT_ID,INFO_TYPE,PHONE_NUMBER,ACCOUNT_SUBMIT_TIME,MESSAGE_CONTENT,CHANNEL_ID,REASON,MESSAGE_MD5,MESSAGE_JSON,CREATED_TIME) ");
		sql.append("VALUES(");
		sql.append("?,?,?,?,?,?,?,?,?,NOW())");

		// 在一个事务中更新数据
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());

			for (BusinessRouteValue businessRouteValue : businessRouteValueList) {
				pstmt.setString(1, businessRouteValue.getAccountID());
				pstmt.setString(2, businessRouteValue.getInfoType());
				pstmt.setString(3, businessRouteValue.getPhoneNumber());
				pstmt.setString(4, businessRouteValue.getAccountSubmitTime());
				pstmt.setString(5, businessRouteValue.getMessageContent());
				pstmt.setString(6, businessRouteValue.getChannelID());
				pstmt.setString(7, businessRouteValue.getAuditReason());
				pstmt.setString(8, DigestUtils.md5DigestAsHex(businessRouteValue.getMessageContent().getBytes()));
				pstmt.setString(9, businessRouteValue.toJSONString());
				
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
	
	
	class AuditWorker extends SuperQueueWorker<BusinessRouteValue>{
		
		public AuditWorker() {
			
		}

		@Override
		public void doRun() throws Exception {
			long startTime = System.currentTimeMillis();
			
			//1.将审核信息进行编号AUDIT_ID,便于后续通过AuditID
			updateRouteAuditMessageMTInfoAuditID();
			int messageLoadMaxNumber = BusinessDataManager.getInstance().getMessageLoadMaxNumber();
			
			//2.获取已设置编号的审核信息
			List<BusinessRouteValue> businessRouteValueList = loadRouteAuditMessageMTInfo(messageLoadMaxNumber);
			if(businessRouteValueList != null && businessRouteValueList.size() > 0 ){
				
				//3.将已审批信息添加到通道队列，将已驳回信息添加添加到模拟队列
				long interval = System.currentTimeMillis() - startTime;
				logger.info("本次加载数据条数{},耗时{}毫秒",businessRouteValueList.size(),interval);
				startTime = System.currentTimeMillis();
				for(BusinessRouteValue businessRouteValue : businessRouteValueList){
					logger.info(
							new StringBuilder().append("分发数据")
							.append("{}accountID={}")
							.append("{}phoneNumber={}")
							.append("{}messageContent={}")
							.toString(),
							FixedConstant.SPLICER,businessRouteValue.getAccountID(),
							FixedConstant.SPLICER,businessRouteValue.getPhoneNumber(),
							FixedConstant.SPLICER,businessRouteValue.getMessageContent()
							);
					
					//审批通过的信息
					if(InsideStatusCodeConstant.SUCCESS_CODE.equals(businessRouteValue.getNextNodeCode())){
						ExternalBlacklistFilterWorkerManager.getInstance().process(businessRouteValue);
					//驳回的信息
					}else if(InsideStatusCodeConstant.FAIL_CODE.equals(businessRouteValue.getNextNodeCode())){
						businessRouteValue.setStatusCode(InsideStatusCodeConstant.StatusCode.REJECTD.name());
						businessRouteValue.setStatusCodeSource(FixedConstant.StatusReportSource.ACCESS.name());
						MessageSubmitFailManager.getInstance().process(businessRouteValue);
					}
				}
				interval = System.currentTimeMillis() - startTime;
				logger.info("本次分发数据条数{},耗时{}毫秒",businessRouteValueList.size(),interval);
			}else{
				//当没有数据时，需要暂停一会
				long interval = BusinessDataManager.getInstance().getMessageLoadIntervalTime();
				Thread.sleep(interval);
			}
			
		}
		
		/**
		 * 加载已设置编号的审核信息
		 * @param messageLoadMaxNumber
		 * @return
		 */
		private List<BusinessRouteValue> loadRouteAuditMessageMTInfo(int messageLoadMaxNumber){
			StringBuffer sql = new StringBuffer();
			Connection conn = null;
			
			PreparedStatement pstmt = null;
			PreparedStatement pstmt2 = null;
			
			ResultSet rs = null;
			
			sql.append("SELECT MESSAGE_CONTENT,MESSAGE_JSON,AUDIT_ID,AUDIT_FLAG,CREATED_TIME");
			sql.append(" FROM smoc_route.route_audit_message_mt_info");
			sql.append(" where AUDIT_ID >0 ");
			sql.append(" ORDER BY AUDIT_ID ASC LIMIT 0,");
			sql.append(messageLoadMaxNumber);
			List<BusinessRouteValue> businessRouteValueList = new ArrayList<BusinessRouteValue>();
			long maxAuditID = 0L;
			try {
				conn = LavenderDBSingleton.getInstance().getConnection();
				conn.setAutoCommit(false);
				pstmt = conn.prepareStatement(sql.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					maxAuditID = rs.getLong("AUDIT_ID");
					BusinessRouteValue businessRouteValue = BusinessRouteValue.toObject(rs.getString("MESSAGE_JSON"));
					businessRouteValue.setMessageContent(rs.getString("MESSAGE_CONTENT"));		
					String tableAuditTime = DateUtil.format(rs.getDate("CREATED_TIME"), DateUtil.DATE_FORMAT_COMPACT_STANDARD_MILLI);
					businessRouteValue.setTableAuditTime(tableAuditTime);
					int auditFlag = rs.getInt("AUDIT_FLAG");
					if(auditFlag == 1){
						businessRouteValue.setNextNodeCode(InsideStatusCodeConstant.SUCCESS_CODE);
					}else if(auditFlag == -1){
						businessRouteValue.setNextNodeCode(InsideStatusCodeConstant.FAIL_CODE);
					}
					businessRouteValueList.add(businessRouteValue);
				}
				
				if(maxAuditID > 0) {
					sql.setLength(0);
					sql.append("DELETE FROM smoc_route.route_audit_message_mt_info WHERE AUDIT_ID <= ?");
					pstmt2 = conn.prepareStatement(sql.toString());
					pstmt2.setLong(1, maxAuditID);
					int count = pstmt2.executeUpdate();					
					logger.info("删除smoc_route.route_audit_message_mt_info数据条数{},AUDIT_ID<={}",count,maxAuditID);
				}
				conn.commit();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				try {
					conn.rollback();
				} catch (Exception e1) {
					logger.error(e1.getMessage(),e);
				}
			} finally {
				LavenderDBSingleton.getInstance().closeAll(rs, pstmt2, null);
				LavenderDBSingleton.getInstance().closeAll(null, pstmt, conn);
			}
			return businessRouteValueList;
		}
		
		/**
		 * 将已审核的信息按序编号
		 */
		private void updateRouteAuditMessageMTInfoAuditID(){
			StringBuffer sql = new StringBuffer();
			sql.append("UPDATE smoc_route.route_audit_message_mt_info SET AUDIT_ID = smoc.system_nextval(\"ROUTE_AUDIT\") WHERE AUDIT_FLAG != 0 AND AUDIT_ID = 0 ");
			Connection conn = null;
			PreparedStatement pstmt = null;
			try {
				conn = LavenderDBSingleton.getInstance().getConnection();
				conn.setAutoCommit(false);
				pstmt = conn.prepareStatement(sql.toString());
				int updateCount = pstmt.executeUpdate();
				conn.commit();
				if(updateCount>0){
					logger.info("设置已审核信息编号数据条数{}",updateCount);
				}

			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				try {
					conn.rollback();
				} catch (Exception e1) {
					logger.error(e1.getMessage(),e1);
				}
			}finally {
				LavenderDBSingleton.getInstance().closeAll(null, pstmt, conn);
			}
		}

	}
	
}


