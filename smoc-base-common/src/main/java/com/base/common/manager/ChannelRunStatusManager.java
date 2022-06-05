package com.base.common.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.manager.ChannelRunStatusManager.ChannelRunStatusValue;
import com.base.common.worker.SuperQueueWorker;

/**
 * 通道运行状态维护
 */
public class ChannelRunStatusManager extends SuperQueueWorker<ChannelRunStatusValue> {
	
	private static ChannelRunStatusManager manager = new ChannelRunStatusManager();
	
	private ChannelRunStatusManager(){
		this.start();
	}
	
	public static ChannelRunStatusManager getInstance() {
		return manager;
	}
	
	/**
	 * 维护通道运行状态
	 * @param channelID
	 * @param runStatus
	 */
	public void process(String channelID,String runStatus) {
		ChannelRunStatusValue channelRunStatusValue = new ChannelRunStatusValue(channelID, runStatus);
		super.add(channelRunStatusValue);
	}
	
	@Override
	protected void doRun() throws Exception {
		if(superQueue.size() > 0){
			long startTime = System.currentTimeMillis();
			//临时数据
			Set<ChannelRunStatusValue> channelRunStatusValueSet;
			synchronized (lock) {
				channelRunStatusValueSet = new HashSet<ChannelRunStatusValue>(superQueue);
				superQueue.clear();
			}
			saveChannelRunStatusInfo(channelRunStatusValueSet);
			long interval = System.currentTimeMillis() - startTime;
			logger.info("维护通道运行状态信息{},耗时{}毫秒",channelRunStatusValueSet,interval);
		}else{
			//当没有数据时，需要暂停一会
			long interval = BusinessDataManager.getInstance().getMessageSaveIntervalTime();
			Thread.sleep(interval);
		}
	}
	
	/**
	 * 批量维护通道运行状态
	 * @param channelRunStatusValueSet
	 */
	private void saveChannelRunStatusInfo(Set<ChannelRunStatusValue> channelRunStatusValueSet) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE smoc.config_channel_basic_info");
		sql.append(" SET CHANNEL_RUN_STATUS = ?,UPDATED_TIME = NOW() WHERE CHANNEL_ID = ?");
		
		// 在一个事务中更新数据
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());

			for (ChannelRunStatusValue channelRunStatusValue : channelRunStatusValueSet) {
				pstmt.setString(1, channelRunStatusValue.getRunStatus());
				pstmt.setString(2, channelRunStatusValue.getChannelID());
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
	
	class ChannelRunStatusValue {
		
		private String channelID;
		private String runStatus;
		
		
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((channelID == null) ? 0 : channelID.hashCode());
			result = prime * result
					+ ((runStatus == null) ? 0 : runStatus.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ChannelRunStatusValue other = (ChannelRunStatusValue) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (channelID == null) {
				if (other.channelID != null)
					return false;
			} else if (!channelID.equals(other.channelID))
				return false;
			if (runStatus == null) {
				if (other.runStatus != null)
					return false;
			} else if (!runStatus.equals(other.runStatus))
				return false;
			return true;
		}

		public ChannelRunStatusValue(String channelID, String runStatus) {
			super();
			this.channelID = channelID;
			this.runStatus = runStatus;
		}
		
		public String getChannelID() {
			return channelID;
		}
		public String getRunStatus() {
			return runStatus;
		}

		private ChannelRunStatusManager getOuterType() {
			return ChannelRunStatusManager.this;
		}

		@Override
		public String toString() {
			JSONObject JSONObject = (JSONObject)com.alibaba.fastjson.JSONObject.toJSON(this);
			return JSONObject.toJSONString();
		}
		
	} 
	
	
}
