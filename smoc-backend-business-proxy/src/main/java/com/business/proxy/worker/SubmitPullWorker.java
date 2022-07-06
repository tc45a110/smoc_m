 /**
 * @desc
 * 从通道表中按照优先级及时间先后获取数据，每次按照通道的速率进行获取，存入到队列中
 */
package com.business.proxy.worker;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.base.common.cache.CacheBaseService;
import com.base.common.constant.FixedConstant;
import com.base.common.constant.InsideStatusCodeConstant;
import com.base.common.constant.TableNameConstant;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.manager.ChannelInfoManager;
import com.base.common.manager.MessageSubmitFailManager;
import com.base.common.util.DateUtil;
import com.base.common.util.TableNameGeneratorUtil;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperCacheWorker;
import com.base.common.worker.SuperQueueWorker;
import com.business.proxy.manager.SubmitJsonToBeanWorkerManager;
import com.business.proxy.vo.SubmitJson;

public class SubmitPullWorker extends SuperQueueWorker<BusinessRouteValue>{
	private String channelID;
	private String tableName;
	private ChannelSubmitPullWorker channelSubmitPullWorker;
	
	public SubmitPullWorker(String channelID) {
		super();
		channelSubmitPullWorker = new ChannelSubmitPullWorker();
		channelSubmitPullWorker.setName(channelID);
		channelSubmitPullWorker.start();
		this.channelID = channelID;
		this.tableName = TableNameGeneratorUtil.generateRouteChannelMessageMTInfoTableName(channelID);
		init();
		this.setName(channelID);
		//先判断表是否存在，初始化时会建表
		this.start();
	}
	
	/**
	 * 初始化:检查通道表
	 */
	private void init(){
		createRouteChannelMessageMtInfoTable(tableName);
	}

	/**
	 * 创建对应的通道下发表
	 * @param tablename
	 * @return
	 */
	private void createRouteChannelMessageMtInfoTable(String tablename) {
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		CallableStatement pstmt = null;
		ResultSet rs = null;
		sql.append("{call smoc_route.");
		sql.append(TableNameConstant.PROCEDURE_CREATE_ROUTE_CHANNEL_MESSAGE_MT_INFO);
		sql.append("(");
		sql.append("?");
		sql.append(")} ");
	
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareCall(sql.toString());
			pstmt.setString(1, tablename);
			pstmt.execute();
			logger.info("初始化表{}",tablename);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
		}
	}


	@Override
	protected void doRun() throws Exception {
		long startTime = System.currentTimeMillis();
		String channelStatus = ChannelInfoManager.getInstance().getChannelStatus(channelID);
		logger.debug("通道{}状态{}",channelID,channelStatus);
		
		int messageLoadMaxNumber = ChannelInfoManager.getInstance().getMessageLoadMaxNumber(channelID);
		logger.debug("可加载最大数据条数{}",messageLoadMaxNumber);
		
		if(messageLoadMaxNumber > 0){
			List<SubmitJson>  submitJsonList= loadRouteChannelMessageMtInfo(tableName,messageLoadMaxNumber);
			
			if(CollectionUtils.isNotEmpty(submitJsonList)){
				
				long interval = System.currentTimeMillis() - startTime;
				logger.info("通道状态{},本次加载数据条数{},耗时{}毫秒",channelStatus,submitJsonList.size(),interval);
				
				startTime = System.currentTimeMillis();
				if(FixedConstant.ChannelStatus.NORMAL.name().equals(channelStatus)){
					//当通道状态为正常时
					processChannelNormal(submitJsonList);
				}else{
					processChannelAbnormal(submitJsonList);
				}
				logger.info("通道状态{},本次分发数据条数{},耗时{}毫秒",channelStatus,submitJsonList.size(),interval);
				Thread.sleep(FixedConstant.COMMON_INTERVAL_TIME/20);
			}else{
				Thread.sleep(FixedConstant.COMMON_INTERVAL_TIME/10);
			}
		}else{
			//通道队列中元素数量已经满了
			Thread.sleep(FixedConstant.COMMON_INTERVAL_TIME/10);
		}

	}
	
	/**
	 * 通道正常处理
	 * @param businessRouteValueList
	 */
	private void processChannelNormal(List<SubmitJson>  submitJsonList){
		for(final SubmitJson submitJson : submitJsonList){
			SubmitJsonToBeanWorkerManager.getInstance().process(submitJson);
		}
	}
	
	/**
	 * 通道异常处理
	 * @param businessRouteValueList
	 */
	private void processChannelAbnormal(List<SubmitJson>  submitJsonList){
		//当通道异常时
		for(final SubmitJson submitJson : submitJsonList){
			
			BusinessRouteValue businessRouteValue = BusinessRouteValue.toObject(submitJson.getMessageJson());
			businessRouteValue.setMessageContent(submitJson.getMessageContent());
			businessRouteValue.setTableSubmitTime(submitJson.getTableSubmitTime());
			
			logger.info(
					new StringBuilder().append("通道状态异常")
					.append("{}accountID={}")
					.append("{}phoneNumber={}")
					.append("{}messageContent={}")
					.toString(),
					FixedConstant.SPLICER,businessRouteValue.getAccountID(),
					FixedConstant.SPLICER,businessRouteValue.getPhoneNumber(),
					FixedConstant.SPLICER,businessRouteValue.getMessageContent()
					);

			
			businessRouteValue.setStatusCodeSource(FixedConstant.StatusReportSource.PROXY.name());
			businessRouteValue.setStatusCode(InsideStatusCodeConstant.StatusCode.GWPAUSE.name());
			businessRouteValue.setSubStatusCode("");
			MessageSubmitFailManager.getInstance().process(businessRouteValue);
			
		}
	}
	
	/**
	 * 获取route_channel_message_mt_info_{channelID}的数据
	 * @param tableName
	 * @param messageLoadMaxNumber
	 * @return
	 */
	private List<SubmitJson> loadRouteChannelMessageMtInfo(
			String tableName, int messageLoadMaxNumber) {

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ID,MESSAGE_CONTENT,MESSAGE_JSON,CREATED_TIME FROM ");
		sql.append("smoc_route.").append(tableName);
		//sql.append(" WHERE ACCOUNT_PRIORITY in (1,2,3)");
		sql.append(" ORDER BY ACCOUNT_PRIORITY DESC,ID ASC LIMIT 0, ?");
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		ArrayList<Long> idList = null;
		ArrayList<SubmitJson> submitJsonList = null;
		long id = 0L;

		try {
			idList = new ArrayList<Long>();
			submitJsonList = new ArrayList<SubmitJson>();
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			long start = System.currentTimeMillis();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, messageLoadMaxNumber);
			rs = pstmt.executeQuery();
			logger.debug("表{}本次读取数据sql执行,耗时：{}",tableName,(System.currentTimeMillis() - start));
			while (rs.next()) {
				long startTime = System.currentTimeMillis();
				id = rs.getLong("ID");
				idList.add(id);
				
				String  messageJson = rs.getString("MESSAGE_JSON");
				String  messageContent = rs.getString("MESSAGE_CONTENT");
				String tableSubmitTime = DateUtil.format(rs.getTimestamp("CREATED_TIME"), DateUtil.DATE_FORMAT_COMPACT_STANDARD_MILLI);
				submitJsonList.add(new SubmitJson(messageJson, messageContent, tableSubmitTime));
				logger.debug("获取记录{},耗时{}",id,(System.currentTimeMillis() - startTime));
			}
			
			if(submitJsonList.size() > 0) {
				logger.info("表{}本次获取数据条数{},耗时：{}",tableName,submitJsonList.size(),(System.currentTimeMillis() - start));
			}
			
			if(idList.size() > 0) {
				start = System.currentTimeMillis();
				sql.setLength(0);
				//sql.append("DELETE FROM smoc_route.").append(tableName).append(" WHERE ID <= ? AND ID in (");
				sql.append("DELETE FROM smoc_route.").append(tableName).append(" WHERE ID in (");
				
				for(int i = 0;i < idList.size();i++) {
					sql.append(idList.get(i));
					if(i != (idList.size() - 1)) {
						sql.append(",");
					}
				}
				sql.append(")");
				pstmt2 = conn.prepareStatement(sql.toString());
				//pstmt2.setLong(1, maxID);
				pstmt2.execute();
			}
			conn.commit();
			if(idList.size() > 0) {
				logger.info("{}共删除{}条,耗时：{}",tableName,idList.size(),(System.currentTimeMillis() - start));
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
		return submitJsonList;
	}
	
	/**
	 * 当通道状态为异常时，需要从缓存队列中将积压数据拉取出来
	 */
	class ChannelSubmitPullWorker extends SuperCacheWorker{

		@Override
		protected void doRun() throws Exception {
			String channelStatus = ChannelInfoManager.getInstance().getChannelStatus(channelID);
			
			if(!FixedConstant.ChannelStatus.NORMAL.name().equals(channelStatus)){
				BusinessRouteValue businessRouteValue = CacheBaseService.getSubmitFromMiddlewareCache(channelID);
				if(businessRouteValue != null){
					
					logger.info(
							new StringBuilder().append("通道状态异常")
							.append("{}accountID={}")
							.append("{}phoneNumber={}")
							.append("{}messageContent={}")
							.toString(),
							FixedConstant.SPLICER,businessRouteValue.getAccountID(),
							FixedConstant.SPLICER,businessRouteValue.getPhoneNumber(),
							FixedConstant.SPLICER,businessRouteValue.getMessageContent()
							);
					
					businessRouteValue.setStatusCodeSource(FixedConstant.StatusReportSource.PROXY.name());
					businessRouteValue.setStatusCode(InsideStatusCodeConstant.StatusCode.GWPAUSE.name());
					businessRouteValue.setSubStatusCode("");
					MessageSubmitFailManager.getInstance().process(businessRouteValue);
					
				}else{
					Thread.sleep(FixedConstant.COMMON_INTERVAL_TIME);
				}

			}else{
				Thread.sleep(FixedConstant.COMMON_EFFECTIVE_TIME);
			}
			
			
		}
		
	}
	
}


