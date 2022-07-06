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
import com.base.common.vo.AlarmMessage;
import com.base.common.worker.SuperConcurrentMapWorker;

/**
 * 告警信息处理类
 */
public class AlarmManager extends SuperConcurrentMapWorker<String,AlarmMessage> {
	
	private static AlarmManager manager = new AlarmManager();
	
	private AlarmManager(){
		this.start();
	}
	
	public static AlarmManager getInstance() {
		return manager;
	}
	
	@Override
	protected void doRun() throws Exception {
		
		if(size() > 0){
			long startTime = System.currentTimeMillis();
			//临时数据
			List<AlarmMessage> alarmMessageList = new ArrayList<AlarmMessage>(superMap.values());
			for(AlarmMessage alarmValue : alarmMessageList) {
				remove(alarmValue.getAlarmKey().name());
			}
			saveAlarmValue(alarmMessageList);
			long interval = System.currentTimeMillis() - startTime;
			logger.info("保存告警信息条数{},耗时{}毫秒",alarmMessageList.size(),interval);
		}

		Thread.sleep(FixedConstant.COMMON_MONITOR_INTERVAL_TIME);
		
	}
	
	public void process(AlarmMessage alarmMessage){
		add(alarmMessage.getAlarmKey().name(), alarmMessage);
	}

	private void saveAlarmValue(List<AlarmMessage> alarmMessageList) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO smoc_route.alarm_message_info");
		sql.append(" (ALARM_KEY,ALARM_VALUE,CREATED_TIME) ");
		sql.append("values(?,?,NOW())");

		// 在一个事务中更新数据
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql.toString());

			for (AlarmMessage alarmMessage : alarmMessageList) {
				pstmt.setString(1, alarmMessage.getAlarmKey().name());
				pstmt.setString(2, alarmMessage.getAlarmValue());
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
