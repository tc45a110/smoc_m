/**
 * @desc
 * @author ma
 * @date 2017年10月10日
 * 
 */
package com.base.common.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.base.common.constant.FixedConstant;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.log.MOBusinessLogManager;
import com.base.common.util.DateUtil;
import com.base.common.vo.ChannelMO;
import com.base.common.worker.SuperQueueWorker;

/**
 * 上行信息处理类
 */
public class ChannelMOManager extends SuperQueueWorker<ChannelMO> {
	
	private static ChannelMOManager manager = new ChannelMOManager();
	
	private ChannelMOManager(){
		this.start();
	}
	
	public static ChannelMOManager getInstance() {
		return manager;
	}
	
	public void add(ChannelMO channelMO) {
		//记录日志
		StringBuilder sb = new StringBuilder()
		.append(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_MILLI))
		.append(FixedConstant.LOG_SEPARATOR)
		.append(channelMO.getChannelID())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(channelMO.getPhoneNumber())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(channelMO.getChannelMOSRCID())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(channelMO.getMessageContent());
		MOBusinessLogManager.getInstance().add(sb.toString());
		super.add(channelMO);
	}
	
	@Override
	protected void doRun() throws Exception {
		if(superQueue.size() > 0){
			long startTime = System.currentTimeMillis();
			//临时数据
			List<ChannelMO> channelMOList;
			synchronized (lock) {
				channelMOList = new ArrayList<ChannelMO>(superQueue);
				superQueue.clear();
			}
			saveRouteChannelMOInfo(channelMOList);
			long interval = System.currentTimeMillis() - startTime;
			logger.info("保存上行信息条数{},耗时{}毫秒",channelMOList.size(),interval);
		}else{
			//当没有数据时，需要暂停一会
			long interval = BusinessDataManager.getInstance().getMessageSaveIntervalTime();
			Thread.sleep(interval);
		}
	}

	private void saveRouteChannelMOInfo(List<ChannelMO> channelMOList) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO smoc_route.route_channel_mo_info");
		sql.append(" (CHANNEL_ID,CHANNEL_SRCID,PHONE_NUMBER,MO_SRCID,MESSAGE_CONTENT,CREATED_TIME) ");
		sql.append("values(");
		sql.append("?,?,?,?,?,NOW())");

		// 在一个事务中更新数据
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());

			for (ChannelMO channelMO : channelMOList) {
				pstmt.setString(1, channelMO.getChannelID());
				pstmt.setString(2, channelMO.getChannelSRCID());
				pstmt.setString(3, channelMO.getPhoneNumber());
				pstmt.setString(4, channelMO.getChannelMOSRCID());
				pstmt.setString(5, channelMO.getMessageContent());
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
