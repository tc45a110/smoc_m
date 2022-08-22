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

import org.apache.commons.lang3.StringUtils;

import com.base.common.constant.FixedConstant;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.log.CategoryLog;
import com.base.common.worker.SuperMapWorker;

/**
 * 账号状态码转换管理
 */
public class AccountStatusCodeConversionManager extends SuperMapWorker<String, Map<String,String>>{
	
	private static AccountStatusCodeConversionManager manager = new AccountStatusCodeConversionManager();
	
	private AccountStatusCodeConversionManager(){
		loadData();
		this.start();
	}
	
	public static AccountStatusCodeConversionManager getInstance(){
		return manager;
	}
	
	/**
	 * 获取一个账号的状态码映射值
	 * @param accountID
	 * @param statusCode
	 * @return
	 */
	public String getAccountStatusCodeConversion(String accountID,String statusCode) {
		//判断账号配置映射数据
		Map<String,String> statusCodeConversionMap = get(accountID);
		if(statusCodeConversionMap == null){
			return null;
		}
		//判断映射数据包含状态码
		return statusCodeConversionMap.get(statusCode);
	}

	@Override
	protected void doRun() throws Exception {
		Thread.sleep(INTERVAL);
		loadData();
	}

	private void loadData() {
		long startTime = System.currentTimeMillis();
		Map<String,Map<String,String>> resultMap = loadAccountStatusCodeConversion();
		
		if(resultMap != null){
			//将新获取的数据集赋值
			superMap = resultMap;
		}
		
		long endTime = System.currentTimeMillis();
		CategoryLog.commonLogger.info("size={},耗时={}",size(),(endTime-startTime));
		
	}
	
	/**
	 * 加载账号级状态转换关系
	 * @return
	 */
	private Map<String,Map<String,String>> loadAccountStatusCodeConversion() {
		Map<String,Map<String,String>> resultMap = new HashMap<String, Map<String,String>>();
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		sql.append("SELECT BUSINESS_ID,PARAM_VALUE FROM smoc.parameter_extend_business_param_value WHERE BUSINESS_TYPE = ? AND PARAM_KEY = ? ");
		
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, FixedConstant.BusinessDataCategory.BUSINESS_ACCOUNT.name());
			pstmt.setString(2, FixedConstant.AccountExtendItem.STATUS_CODE_CONVERSION.name());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String accountID = rs.getString("BUSINESS_ID");
				String paramValue = rs.getString("PARAM_VALUE");
				if(StringUtils.isNotEmpty(accountID) && StringUtils.isNotEmpty(paramValue)){
					String[] statusCodeConversionArray = paramValue.split(FixedConstant.DATABASE_SEPARATOR);
					Map<String,String> statusCodeConversionMap = new HashMap<String, String>();
					for(String statusCodeConversion : statusCodeConversionArray){
						String[] statusCodeArray = statusCodeConversion.split("=");
						if(statusCodeArray.length == 2){
							statusCodeConversionMap.put(statusCodeArray[0], statusCodeArray[1]);
						}
					}
					resultMap.put(accountID, statusCodeConversionMap);
				}
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


