/**
 * @desc
 * 
 */
package com.base.common.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.base.common.constant.FixedConstant;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.log.CategoryLog;
import com.base.common.manager.ChannelGroupInfoManager.ChannelGroupInfo;
import com.base.common.worker.SuperMapWorker;

public class ChannelGroupInfoManager extends SuperMapWorker<String,ChannelGroupInfo>{
	
	private static ChannelGroupInfoManager manager = new ChannelGroupInfoManager();
	
	private ChannelGroupInfoManager(){
		loadData();
		this.start();
	}
	
	public static ChannelGroupInfoManager getInstance(){
		return manager;
	}
	
	/**
	 * 加载数据
	 */
	private void loadData(){
		long startTime = System.currentTimeMillis();
		Map<String, ChannelGroupInfo> resultMap = loadChannelGroupInfo();
		if(resultMap != null){
			superMap = resultMap;
		}
		long endTime = System.currentTimeMillis();
		CategoryLog.commonLogger.info("size={},耗时={}",size(),(endTime-startTime));
	}
	
	private Map<String, ChannelGroupInfo> loadChannelGroupInfo(){
		Map<String, ChannelGroupInfo> resultMap = new HashMap<String,ChannelGroupInfo>();
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		sql.append("SELECT CHANNEL_GROUP_ID,MASK_PROVINCE FROM smoc.config_channel_group_info");
		ChannelGroupInfo channelGroupInfo = null;
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				channelGroupInfo = new ChannelGroupInfo();
				channelGroupInfo.setGroupID(rs.getString("CHANNEL_GROUP_ID"));
				channelGroupInfo.setProvinces(rs.getString("MASK_PROVINCE"));
				resultMap.put(channelGroupInfo.getGroupID(), channelGroupInfo);
			}

		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(),e);
			return null;
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
		}
		return resultMap;
	}
	

	@Override
	public void doRun() throws Exception {
		Thread.sleep(INTERVAL);
		loadData();
	}
	
	class ChannelGroupInfo{
		//通道组id
		private String groupID; 
		//支持的省份按逗号分隔
		private String provinces;
		//支持的省份集合
		private Set<String> proviceSet;
		
		public String getGroupID() {
			return groupID;
		}
		public void setGroupID(String groupID) {
			this.groupID = groupID;
		}
		public String getProvinces() {
			return provinces;
		}
		
		public void setProvinces(String provinces) {
			this.provinces = provinces;
			Set<String> set = new HashSet<String>();
			if(!StringUtils.isEmpty(provinces)){
				set.addAll(Arrays.asList(provinces.split(FixedConstant.DATABASE_SEPARATOR)));
			}
			proviceSet = set;
		}
		
		public Set<String> getProviceSet() {
			return proviceSet;
		}
		
		

	}
	
}


