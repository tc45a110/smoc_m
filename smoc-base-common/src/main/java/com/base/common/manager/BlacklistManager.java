/**
 * @desc
 * 
 */
package com.base.common.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.base.common.dao.LavenderDBSingleton;
import com.base.common.log.CategoryLog;
import com.base.common.worker.SuperMapWorker;

public class BlacklistManager extends SuperMapWorker<String,Set<String>>{
	
	private static BlacklistManager manager = new BlacklistManager();
	
	public boolean isWhite(String accountID ,String mobile){
		Set<String> whitelistMobileSet = get(accountID);
		if(whitelistMobileSet == null) {
			return false;
		}
		return whitelistMobileSet.contains(mobile);
	}
	
	private BlacklistManager(){
		loadData();
		this.start();
	}
	
	public static BlacklistManager getInstance(){
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
		Map<String,Set<String>> resultMap = loadAccountWhitelistMobile();
		
		if(resultMap != null){
			//将新获取的数据集赋值
			superMap = resultMap;
		}
		
		long endTime = System.currentTimeMillis();
		CategoryLog.commonLogger.info("size={},耗时={}",size(),(endTime-startTime));
	}
	
	
	
	
	/**
	 * 获取parameter_extend_business_param_value、parameter_extend_filters_value、parameter_extend_system_param_value的配置数据
	 * 数据库异常时返回null
	 * @return
	 */
	private Map<String,Set<String>> loadAccountWhitelistMobile() {
		Map<String,Set<String>> resultMap = new HashMap<String, Set<String>>();
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
				String accountID = rs.getString("ACCOUNT_ID");
				Set<String> whitelistSet = resultMap.get(accountID);
				if(whitelistSet == null) {
					whitelistSet = new HashSet<String>();
					resultMap.put(accountID, whitelistSet);
				}
				whitelistSet.add(rs.getString("MOBILE"));
			}

		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(),e);
			return null;
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
		}
		return resultMap;
	}
}


