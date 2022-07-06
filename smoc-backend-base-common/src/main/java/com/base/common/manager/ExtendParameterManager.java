/**
 * @desc
 * 
 */
package com.base.common.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.base.common.constant.FixedConstant;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.log.CategoryLog;
import com.base.common.worker.SuperMapWorker;

public class ExtendParameterManager extends SuperMapWorker<String, String>{
	
	private static ExtendParameterManager manager = new ExtendParameterManager();
	
	String getParameterValue(String businessType,String businessID,String parameterKey){
		String key = new StringBuilder()
		.append(businessType)
		.append(FixedConstant.SPLICER)
		.append(businessID)
		.append(FixedConstant.SPLICER)
		.append(parameterKey).toString();
		CategoryLog.commonLogger.debug("{}",key);
		return get(key);
	}
	
	private ExtendParameterManager(){
		loadData();
		this.start();
		//new Worker().start();
	}
	
	public static ExtendParameterManager getInstance(){
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
		Map<String, String> resultMap = loadParameterExtendParamValue();
		
		if(resultMap != null){
			//将新获取的数据集赋值
			superMap = resultMap;
		}
		
		long endTime = System.currentTimeMillis();
		CategoryLog.commonLogger.debug("数据={},耗时={}",resultMap,(endTime-startTime));
		CategoryLog.commonLogger.info("size={},耗时={}",size(),(endTime-startTime));
	}
	
	/**
	 * 获取parameter_extend_business_param_value、parameter_extend_filters_value、parameter_extend_system_param_value的配置数据
	 * 数据库异常时返回null
	 * @return
	 */
	private Map<String, String> loadParameterExtendParamValue() {
		Map<String, String> resultMap = new HashMap<String,String>();
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		sql.append("SELECT  BUSINESS_TYPE,BUSINESS_ID,PARAM_KEY,PARAM_VALUE FROM  smoc.parameter_extend_filters_value ");
		sql.append("UNION ");
		sql.append("SELECT  BUSINESS_TYPE,BUSINESS_ID,PARAM_KEY,PARAM_VALUE FROM  smoc.parameter_extend_system_param_value ");
		sql.append("UNION ");
		sql.append("SELECT  BUSINESS_TYPE,BUSINESS_ID,PARAM_KEY,PARAM_VALUE FROM  smoc.parameter_extend_business_param_value ");
		
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String key = new StringBuilder()
				.append(rs.getString("BUSINESS_TYPE"))
				.append(FixedConstant.SPLICER)
				.append(rs.getString("BUSINESS_ID"))
				.append(FixedConstant.SPLICER)
				.append(rs.getString("PARAM_KEY")).toString();

				resultMap.put(key.toString(), rs.getString("PARAM_VALUE"));
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


