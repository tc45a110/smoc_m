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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.base.common.cache.CacheBaseService;
import com.base.common.constant.FixedConstant;
import com.base.common.constant.InsideStatusCodeConstant;
import com.base.common.constant.TableNameConstant;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.manager.ChannelInfoManager;
import com.base.common.manager.ChannelPriceManager;
import com.base.common.manager.MessageSubmitFailManager;
import com.base.common.util.Commons;
import com.base.common.util.DateUtil;
import com.base.common.util.TableNameGeneratorUtil;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperCacheWorker;
import com.base.common.worker.SuperQueueWorker;

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
			List<BusinessRouteValue> businessRouteValueList = loadRouteChannelMessageMtInfo(tableName,messageLoadMaxNumber);
			
			if(businessRouteValueList != null && businessRouteValueList.size() > 0 ){
				
				long interval = System.currentTimeMillis() - startTime;
				logger.info("通道状态{},本次加载数据条数{},耗时{}毫秒",channelStatus,businessRouteValueList.size(),interval);
				
				startTime = System.currentTimeMillis();
				if(FixedConstant.ChannelStatus.NORMAL.name().equals(channelStatus)){
					//当通道状态为正常时
					processChannelNormal(businessRouteValueList);
				}else{
					processChannelAbnormal(businessRouteValueList);
				}
				logger.info("通道状态{},分发数据条数{},耗时{}毫秒",channelStatus,businessRouteValueList.size(),interval);
			}else{
				Thread.sleep(FixedConstant.COMMON_INTERVAL_TIME);
			}
		}else{
			//通道队列中元素数量已经满了
			Thread.sleep(FixedConstant.COMMON_INTERVAL_TIME);
		}

	}
	
	/**
	 * 通道正常处理
	 * @param businessRouteValueList
	 */
	private void processChannelNormal(List<BusinessRouteValue> businessRouteValueList){
		//当通道异常时
		for(BusinessRouteValue businessRouteValue : businessRouteValueList){
			logger.info(
					new StringBuilder().append("通道状态正常")
					.append("{}accountID={}")
					.append("{}phoneNumber={}")
					.append("{}messageContent={}")
					.append("{}channelID={}")
					.toString(),
					FixedConstant.SPLICER,businessRouteValue.getAccountID(),
					FixedConstant.SPLICER,businessRouteValue.getPhoneNumber(),
					FixedConstant.SPLICER,businessRouteValue.getMessageContent(),
					FixedConstant.SPLICER,channelID
					);
			//保存在下发队列中
			businessRouteValue.setQueueSubmitTime(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_MILLI));
			CacheBaseService.saveSubmitToMiddlewareCache(channelID, businessRouteValue);
		}
	}
	
	/**
	 * 通道异常处理
	 * @param businessRouteValueList
	 */
	private void processChannelAbnormal(List<BusinessRouteValue> businessRouteValueList){
		//当通道异常时
		for(BusinessRouteValue businessRouteValue : businessRouteValueList){
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
			businessRouteValue.setSuccessCode(InsideStatusCodeConstant.FAIL_CODE);
			MessageSubmitFailManager.getInstance().process(businessRouteValue);
			
		}
	}
	
	/**
	 * 获取route_channel_message_mt_info_{channelID}的数据
	 * @param tableName
	 * @param messageLoadMaxNumber
	 * @return
	 */
	private List<BusinessRouteValue> loadRouteChannelMessageMtInfo(
			String tableName, int messageLoadMaxNumber) {
		List<BusinessRouteValue> list = new ArrayList<BusinessRouteValue>();
		Map<Integer,Long> map = new HashMap<Integer,Long>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ID,ACCOUNT_PRIORITY,MESSAGE_CONTENT,MESSAGE_JSON,CREATED_TIME FROM ");
		sql.append("smoc_route.").append(tableName);
		sql.append(" ORDER BY ACCOUNT_PRIORITY DESC,ID ASC LIMIT 0,?");
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, messageLoadMaxNumber);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				map.put(rs.getInt("ACCOUNT_PRIORITY"), rs.getLong("ID"));
				BusinessRouteValue businessRouteValue = BusinessRouteValue.toObject(rs.getString("MESSAGE_JSON"));
				businessRouteValue.setMessageContent(rs.getString("MESSAGE_CONTENT"));
				String tableSubmitTime = DateUtil.format(rs.getTimestamp("CREATED_TIME"), DateUtil.DATE_FORMAT_COMPACT_STANDARD_MILLI);
				businessRouteValue.setTableSubmitTime(tableSubmitTime);
				
				businessRouteValue.setChannelPrice(ChannelPriceManager.getInstance().getPrice(channelID, businessRouteValue.getAreaCode()));
				if(FixedConstant.PriceStyle.AREA_PRICE.name().equals(ChannelInfoManager.getInstance().getPriceStyle(channelID))) {
					businessRouteValue.setPriceAreaCode(businessRouteValue.getAreaCode());
					businessRouteValue.setChannelPrice(ChannelPriceManager.getInstance().getPrice(channelID, businessRouteValue.getAreaCode()));
				}else {
					//省份编码/国家编码,当通道不区分省份计价时，该值为ALL
					businessRouteValue.setPriceAreaCode(Commons.UNIFIED_PRICING_CODE);
					businessRouteValue.setChannelPrice(ChannelPriceManager.getInstance().getPrice(channelID, Commons.UNIFIED_PRICING_CODE));
				}
				
				list.add(businessRouteValue);
			}
			if(map.size() > 0) {
				sql.setLength(0);
				sql.append("DELETE FROM smoc_route.").append(tableName).append(" WHERE ID <= ? AND ACCOUNT_PRIORITY = ?");
				pstmt2 = conn.prepareStatement(sql.toString());
				for(Integer priority : map.keySet()) {
					long maxID = map.get(priority);
					pstmt2.setLong(1, maxID);
					pstmt2.setInt(2, priority);
					int count = pstmt2.executeUpdate();
					logger.info("删除{} ID<={} ACCOUNT_PRIORITY={}，共{}条",tableName,maxID,priority,count);
				}
				conn.commit();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			try {
				conn.rollback();
			} catch (Exception e1) {
				logger.error(e1.getMessage(),e1);
			}
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt2, null);
			LavenderDBSingleton.getInstance().closeAll(null, pstmt, conn);
		}
		return list;
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
					businessRouteValue.setSuccessCode(InsideStatusCodeConstant.FAIL_CODE);
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


