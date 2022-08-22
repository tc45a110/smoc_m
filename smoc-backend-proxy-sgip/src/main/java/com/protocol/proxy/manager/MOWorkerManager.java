/**
 * @desc
 * 
 */
package com.protocol.proxy.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.druid.util.Base64;
import com.base.common.constant.FixedConstant;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.log.CategoryLog;
import com.base.common.manager.ChannelInfoManager;
import com.base.common.manager.ChannelMOManager;
import com.base.common.manager.LongSMSMOManager;
import com.base.common.manager.ResourceManager;
import com.base.common.util.ChannelMOUtil;
import com.base.common.util.UUIDUtil;
import com.base.common.vo.ChannelMO;
import com.base.common.worker.SuperCacheWorker;
import com.base.common.worker.SuperMapWorker;
import com.base.common.worker.SuperQueueWorker;
import com.huawei.insa2.comm.sgip.message.SGIPDeliverMessage;
import com.huawei.insa2.comm.sgip.message.SGIPMessage;

public class MOWorkerManager extends SuperQueueWorker<SGIPMessage>{
	
	private ChannelSRCIDWorker channelSRCIDWorker;

	private static MOWorkerManager manager = new MOWorkerManager();
	
	public static MOWorkerManager getInstance(){
		return manager;
	}
	
	public void process(SGIPMessage message){
		add(message);
	}
	
	private MOWorkerManager(){
		channelSRCIDWorker = new ChannelSRCIDWorker();
		
		//启动cpu的数量
		for(int i=0;i<1;i++){
			MOWorker MOWorker = new MOWorker();
			MOWorker.setName(new StringBuilder("MOWorker-").append(i+1).toString());
			MOWorker.start();
		}
		this.start();
	}
	
	@Override
	protected void doRun() throws Exception {
		Thread.sleep(FixedConstant.COMMON_MONITOR_INTERVAL_TIME);
		logger.info("上行缓存队列数量{}",size());
	}
	
	
	/**
	 * 上行短信：在SP和SMG的通信中，SMG用Deliver命向SP发送一条MO短消息
	 */
	class MOWorker extends SuperCacheWorker{

		@Override
		protected void doRun() throws Exception {
			SGIPDeliverMessage message = (SGIPDeliverMessage)superQueue.take();
			CategoryLog.messageLogger.info(message.toString());
			//短信是否包含长短信头的标识
			int tpUdhi = message.getTpUdhi();
			int messageFormat = message.getMsgFmt();
			String SPNumber = message.getSPNumber();
			String phoneNumber= message.getUserNumber();
			if (phoneNumber.length() > 11) {
				phoneNumber = phoneNumber.substring(phoneNumber
						.length() - 11);
			}
			byte[] messageContent = message.getMsgContent();
			String sequenceID= String.valueOf(message.getSequenceId());
			
			String channelID = channelSRCIDWorker.getChannelID(SPNumber);
			
			processChannelMO(tpUdhi, messageFormat, phoneNumber, SPNumber, messageContent,sequenceID,channelID);
			
		}
		
		/**
		 * 处理上行信息
		 * @param tpUdhi
		 * @param messageFormat
		 * @param phoneNumber
		 * @param channelMOSRCID
		 * @param messageContent
		 */
		private void processChannelMO(int tpUdhi,int messageFormat,String phoneNumber,String channelMOSRCID,byte[] messageContent,String sequenceID,String channelID){
			logger.info(
					new StringBuilder().append("上行信息")
					.append("{}channelID={}")
					.append("{}phoneNumber={}")
					.append("{}channelMOSRCID={}")
					.append("{}messageContentBase64={}")
					.append("{}tpUdhi={}")
					.append("{}messageFormat={}")
					.append("{}sequenceID={}")
					.toString(),
					FixedConstant.SPLICER,channelID,
					FixedConstant.SPLICER,phoneNumber,
					FixedConstant.SPLICER,channelMOSRCID,
					FixedConstant.SPLICER,Base64.byteArrayToBase64(messageContent),
					FixedConstant.SPLICER,tpUdhi,
					FixedConstant.SPLICER,messageFormat,
					FixedConstant.SPLICER,sequenceID
					);
			
			String channelSRCID = ChannelInfoManager.getInstance().getChannelSRCID(channelID);
			ChannelMO channelMO = ChannelMOUtil.getMO(tpUdhi, messageFormat, phoneNumber, channelMOSRCID, messageContent,channelID,channelSRCID);
			channelMO.setBusinessMessageID(UUIDUtil.get32UUID());
			//长短信需要进行合成处理
			if(channelMO.getMessageTotal() > 1){
				LongSMSMOManager.getInstance().add(channelMO);
			}else{
				ChannelMOManager.getInstance().put(channelMO.getBusinessMessageID(),channelMO);
			}
		
		}
		
		
		public void exit(){
			//停止线程
			super.exit();
		}
		
	}
	
	class ChannelSRCIDWorker extends SuperMapWorker<String, String>{
		
		public ChannelSRCIDWorker(){
			loadData();
		}

		@Override
		protected void doRun() throws Exception {
			Thread.sleep(INTERVAL);
			loadData();
		}
		
		/**
		 * 通过码号获取通道ID
		 * @param channelSRCID
		 * @return
		 */
		public String getChannelID(String channelSRCID){
			Map<String,String> resultMap = new HashMap<String, String>(superMap);
			for(Map.Entry<String, String> entry : resultMap.entrySet()){
				if(channelSRCID.startsWith(entry.getKey())){
					return entry.getValue();
				}
			}
			return "CH00000";
		}
		
		/**
		 * 加载数据
		 */
		private void loadData() {
			long startTime = System.currentTimeMillis();
			String protocol = ResourceManager.getInstance().getValue("load.channel.protocol");
			Map<String, String> resultMap = loadChannelSimpleInfo(protocol);
			if (resultMap != null) {
				superMap = resultMap;
			}
			long endTime = System.currentTimeMillis();
			CategoryLog.commonLogger.info("size={},耗时={}", size(), (endTime - startTime));
		}
		
		private Map<String, String> loadChannelSimpleInfo(String protocol) {
			StringBuffer sql = new StringBuffer();
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			sql.append("SELECT CHANNEL_ID,SRC_ID ");
			sql.append("FROM smoc.config_channel_interface ");
			sql.append(" WHERE PROTOCOL = ?");

			Map<String, String> resultMap = new HashMap<String, String>();

			try {
				conn = LavenderDBSingleton.getInstance().getConnection();
				pstmt = conn.prepareStatement(sql.toString());
				pstmt.setString(1, protocol);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					String channelID = rs.getString("CHANNEL_ID");
					String channelSRCID=rs.getString("SRC_ID");
					resultMap.put(channelSRCID,channelID);
				}

			} catch (Exception e) {
				CategoryLog.commonLogger.error(e.getMessage(), e);
				return null;
			} finally {
				LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
			}
			return resultMap;
		}
	}

}


