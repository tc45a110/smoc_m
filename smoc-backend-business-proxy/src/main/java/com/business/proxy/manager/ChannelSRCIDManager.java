/**
 * @desc
 * 
 */
package com.business.proxy.manager;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import com.base.common.constant.TableNameConstant;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.util.TableNameGeneratorUtil;
import com.base.common.worker.SuperQueueWorker;

/**
 * 维护临时表 route_channel_srcid_message_mt_info_ChannelSRCID
 */
public class ChannelSRCIDManager extends SuperQueueWorker<String>{
	
	private static ChannelSRCIDManager manager = new ChannelSRCIDManager();
	
	private Set<String> channelSRCIDSet = new HashSet<String>();
		
	private ChannelSRCIDManager(){
		this.start();
	}
	
	public static ChannelSRCIDManager getInstance(){
		return manager;
	}
	
	
	public void doRun() throws Exception {
		String channelSRCID = take();
		//当不包含channelSRCID则需新增表
		if(!channelSRCIDSet.contains(channelSRCID)){
			createRouteChannelSRCIDMessageMtInfoTable(TableNameGeneratorUtil.generateRouteChannelSRCIDMessageMTInfoTableName(channelSRCID));
			channelSRCIDSet.add(channelSRCID);
		}
	}
	
	/**
	 * 创建通道码号级下发信息表
	 * @param tablename
	 * @return
	 */
	private void createRouteChannelSRCIDMessageMtInfoTable(String tablename) {
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		CallableStatement pstmt = null;
		ResultSet rs = null;
		sql.append("{call smoc_route.");
		sql.append(TableNameConstant.PROCEDURE_CREATE_ROUTE_CHANNEL_SRCID_MESSAGE_MT_INFO);
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
	
}


