/**
 * @desc
 * 
 */
package com.base.common.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import com.base.common.dao.LavenderDBSingleton;
import com.base.common.log.CategoryLog;
import com.base.common.worker.SuperSetWorker;

public class WhitelistManager extends SuperSetWorker<String>{
	
	private static WhitelistManager manager = new WhitelistManager();
	
	public boolean getDictionariesParameterValue(String phoneNumber){
		return contains(phoneNumber);
	}
	
	private WhitelistManager(){
		loadData();
		this.start();
	}
	
	public static WhitelistManager getInstance(){
		return manager;
	}
	
	@Override
	public void doRun() throws Exception {
			Thread.sleep(INTERVAL);
			loadData();
	}
	
	/**
	 * 加载数据
	 */
	private void loadData(){
		long startTime = System.currentTimeMillis();
		Set<String> resultSet = loadBlacklistMobile();
		
		if(resultSet != null){
			//将新获取的数据集赋值
			superSet = resultSet;
		}
		
		long endTime = System.currentTimeMillis();
		CategoryLog.commonLogger.info("size={},耗时={}",size(),(endTime-startTime));
	}
	
	
	
	
	/**
	 * 获取parameter_extend_business_param_value、parameter_extend_filters_value、parameter_extend_system_param_value的配置数据
	 * 数据库异常时返回null
	 * @return
	 */
	private Set<String> loadBlacklistMobile() {
		Set<String> resultSet = new HashSet<String>();
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		sql.append("SELECT MOBILE FROM smoc.filter_black_list WHERE `STATUS` = 1");
		
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				resultSet.add(rs.getString("MOBILE"));
			}

		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(),e);
			return null;
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
		}
		return resultSet;
	}
}


