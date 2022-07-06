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

import com.base.common.constant.FixedConstant;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.log.CategoryLog;
import com.base.common.worker.SuperMapWorker;

public class KeyWordParameterManager extends SuperMapWorker<String,Set<String>>{
	
	private static KeyWordParameterManager manager = new KeyWordParameterManager();
	
	public Set<String> getKeyWordSet(String keyWordsBusinessType,String businessID,String keyWordsType){
		String key = new StringBuffer().append(keyWordsBusinessType).append(FixedConstant.SPLICER)
										.append(businessID).append(FixedConstant.SPLICER)
										.append(keyWordsType).toString();
		return get(key);
	}
	
	private KeyWordParameterManager(){
		loadData();
		this.start();
	}
	
	public static KeyWordParameterManager getInstance(){
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
		Map<String,Set<String>> resultMap = loadKeyWord();
		
		if(resultMap != null){
			//将新获取的数据集赋值
			superMap = resultMap;
		}
		
		long endTime = System.currentTimeMillis();
		CategoryLog.commonLogger.info("size={},耗时={}",size(),(endTime-startTime));
	}
	
	/**
	 * 获取关键词配置数据
	 * 数据库异常时返回null
	 * @return
	 */
	private Map<String,Set<String>> loadKeyWord() {
		Map<String,Set<String>> resultMap = new HashMap<String, Set<String>>();
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		sql.append("SELECT KEY_WORDS_BUSINESS_TYPE,BUSINESS_ID,KEY_WORDS_TYPE,KEY_WORDS FROM smoc.filter_key_words_info");
		
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String key = new StringBuffer().append(rs.getString("KEY_WORDS_BUSINESS_TYPE")).append(FixedConstant.SPLICER)
											    .append(rs.getString("BUSINESS_ID")).append(FixedConstant.SPLICER)
												.append(rs.getString("KEY_WORDS_TYPE")).toString();
				Set<String> keyWordSet = resultMap.get(key);
				if(keyWordSet == null) {
					keyWordSet = new HashSet<String>();
					resultMap.put(key, keyWordSet);
				}
				keyWordSet.add(rs.getString("KEY_WORDS"));
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


