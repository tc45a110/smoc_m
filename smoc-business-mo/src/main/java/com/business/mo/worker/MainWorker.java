/**
 * 获取上行数据主线程
 */
package com.business.mo.worker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.base.common.constant.FixedConstant;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.manager.BusinessDataManager;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.vo.ChannelMO;
import com.base.common.worker.SuperCacheWorker;

public class MainWorker extends SuperCacheWorker{
	
	private MOStoreWorker MOStoreWorker;
	private ThreadPoolExecutor threadPoolExecutor;
	private RepeatMatchWorker repeatMatchWorker;
	
	public MainWorker() {
		MOStoreWorker = new MOStoreWorker();
		MOStoreWorker.setName("MOStoreWorker");
		MOStoreWorker.start();
		
		repeatMatchWorker = new RepeatMatchWorker();
		repeatMatchWorker.setName("RepeatMatchWorker");
		repeatMatchWorker.start();
		
		threadPoolExecutor = new ThreadPoolExecutor(FixedConstant.CPU_NUMBER * 8,
				Integer.MAX_VALUE, 100000L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		threadPoolExecutor.prestartAllCoreThreads();
	}

	@Override
	public void doRun() throws Exception {
		long startTime = System.currentTimeMillis();
		int messageLoadMaxNumber = BusinessDataManager.getInstance().getMessageLoadMaxNumber();
		List<ChannelMO> channelMOList = loadRouteChannelMOInfo(messageLoadMaxNumber,true);
		long interval = 0;
		if(channelMOList != null && channelMOList.size() > 0 ){
			
			Vector<Future<BusinessRouteValue>> calls = new Vector<Future<BusinessRouteValue>>();
			
			interval = System.currentTimeMillis() - startTime;
			logger.info("本次加载数据条数{},耗时{}毫秒",channelMOList.size(),interval);
			startTime = System.currentTimeMillis();
			for(ChannelMO channelMO : channelMOList){
				logger.info(
						new StringBuilder().append("上行数据")
						.append("{}channelID={}")
						.append("{}channelSRCID={}")
						.append("{}channelMOSRCID={}")
						.append("{}phoneNumber={}")
						.append("{}messageContent={}")
						.toString(),
						FixedConstant.SPLICER,channelMO.getChannelID(),
						FixedConstant.SPLICER,channelMO.getChannelSRCID(),
						FixedConstant.SPLICER,channelMO.getChannelMOSRCID(),
						FixedConstant.SPLICER,channelMO.getPhoneNumber(),
						FixedConstant.SPLICER,channelMO.getMessageContent()
						);
				ChannelSRCIDWorker channelSRCIDWorker = new ChannelSRCIDWorker(channelMO);
				Future<BusinessRouteValue> call = threadPoolExecutor.submit(channelSRCIDWorker);
				calls.add(call);
			}
			
			for (Future<BusinessRouteValue> call : calls) {
				try {
					BusinessRouteValue businessRouteValue = call.get();
					if(businessRouteValue != null){
						MOStoreWorker.add(businessRouteValue);
					}
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
				}
			}
			interval = System.currentTimeMillis() - startTime;
			logger.info("匹配上行数据条数{},耗时{}毫秒",channelMOList.size(),interval);
		}
		
		interval = System.currentTimeMillis() - startTime;
		
		//上行数据加载每隔一定时间，获取一次
		if((BusinessDataManager.getInstance().getChannelMOLoadIntervalTime() - interval) > 0){
			Thread.sleep(BusinessDataManager.getInstance().getChannelMOLoadIntervalTime() - interval);
		}
		
	}
	
	/**
	 * 加载通道上行信息临时表
	 * @param messageLoadMaxNumber
	 * @return
	 */
	private List<ChannelMO> loadRouteChannelMOInfo(int messageLoadMaxNumber,boolean firstTimeFlag){
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		
		sql.append("SELECT ID,CHANNEL_ID,CHANNEL_SRCID,PHONE_NUMBER,MO_SRCID,MESSAGE_CONTENT,DATE_FORMAT(CREATED_TIME, '%Y-%m-%d %H:%i:%s') MO_TIME,MATCH_TIMES ");
		sql.append("FROM smoc_route.route_channel_mo_info ");
		
		if(firstTimeFlag){
			sql.append("WHERE MATCH_TIMES = 0 ");
		}else{
			sql.append("WHERE MATCH_TIMES > 0 ");
		}

		sql.append("ORDER BY ID ASC LIMIT 0,");
		sql.append(messageLoadMaxNumber);
		List<ChannelMO> channelMOList = new ArrayList<ChannelMO>();
		ChannelMO channelMO = null;
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				long ID = rs.getLong("ID");
				String channelID = rs.getString("CHANNEL_ID");
				String phoneNumber = rs.getString("PHONE_NUMBER");
				String MOTime = rs.getString("MO_TIME");
				String messageContent = rs.getString("MESSAGE_CONTENT");
				String channelSRCID = rs.getString("CHANNEL_SRCID");
				String channelMOSRCID = rs.getString("MO_SRCID");		
				int MATCH_TIMES = rs.getInt("MATCH_TIMES");
				channelMO = new ChannelMO(channelMOSRCID, phoneNumber, messageContent, channelID, channelSRCID, MOTime, ID, MATCH_TIMES);
				channelMOList.add(channelMO);
			}
			
			if(channelMOList != null && channelMOList.size() > 0) {
				long maxID = channelMOList.get(channelMOList.size() - 1).getID();
				sql.setLength(0);
				sql.append("UPDATE smoc_route.route_channel_mo_info SET MATCH_TIMES = MATCH_TIMES + 1 WHERE ID <= ? ");
				if(firstTimeFlag){
					sql.append("AND MATCH_TIMES = 0 ");
				}else{
					sql.append("AND MATCH_TIMES > 0 ");
				}
				pstmt2 = conn.prepareStatement(sql.toString());
				pstmt2.setLong(1, maxID);
				int row = pstmt2.executeUpdate();
				logger.info("修改 ROUTE_CHANNEL_MO_INFO ID <= {} 的匹配次数,修改行数={}",channelMO.getID(),row);
			}
			conn.commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			try {
				conn.rollback();
			}catch (Exception e1) {
				logger.error(e1.getMessage(),e1);
			}
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt2, null);
			LavenderDBSingleton.getInstance().closeAll(null, pstmt, conn);
		}
		
		return channelMOList;
	}
	
	
	
	@Override
	public void exit() {
		super.exit();
		repeatMatchWorker.exit();
	}



	//加载已经匹配过的数据，轮询间隔时间会比MainWorker的间隔时间长
	class RepeatMatchWorker extends SuperCacheWorker{

		@Override
		protected void doRun() throws Exception {
			long startTime = System.currentTimeMillis();
			int messageLoadMaxNumber = BusinessDataManager.getInstance().getMessageLoadMaxNumber();
			List<ChannelMO> channelMOList = loadRouteChannelMOInfo(messageLoadMaxNumber,false);
			long interval = 0;
			if(channelMOList != null && channelMOList.size() > 0 ){
				
				Vector<Future<BusinessRouteValue>> calls = new Vector<Future<BusinessRouteValue>>();
				
				interval = System.currentTimeMillis() - startTime;
				logger.info("本次加载数据条数{},耗时{}毫秒",channelMOList.size(),interval);
				startTime = System.currentTimeMillis();
				for(ChannelMO channelMO : channelMOList){
					logger.info(
							new StringBuilder().append("上行数据")
							.append("{}channelID={}")
							.append("{}channelSRCID={}")
							.append("{}channelMOSRCID={}")
							.append("{}phoneNumber={}")
							.append("{}messageContent={}")
							.toString(),
							FixedConstant.SPLICER,channelMO.getChannelID(),
							FixedConstant.SPLICER,channelMO.getChannelSRCID(),
							FixedConstant.SPLICER,channelMO.getChannelMOSRCID(),
							FixedConstant.SPLICER,channelMO.getPhoneNumber(),
							FixedConstant.SPLICER,channelMO.getMessageContent()
							);
					ChannelSRCIDWorker channelSRCIDWorker = new ChannelSRCIDWorker(channelMO);
					Future<BusinessRouteValue> call = threadPoolExecutor.submit(channelSRCIDWorker);
					calls.add(call);
				}
				
				for (Future<BusinessRouteValue> call : calls) {
					try {
						BusinessRouteValue businessRouteValue = call.get();
						if(businessRouteValue != null){
							MOStoreWorker.add(businessRouteValue);
						}
					} catch (Exception e) {
						logger.error(e.getMessage(),e);
					}
				}
				interval = System.currentTimeMillis() - startTime;
				logger.info("匹配上行数据条数{},耗时{}毫秒",channelMOList.size(),interval);
			}
			
			Thread.sleep(FixedConstant.COMMON_EFFECTIVE_TIME/2);
			
		}
		
	}
	
}


