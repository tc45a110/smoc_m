package com.base.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.base.common.log.CategoryLog;




public class AccountInfoDAO {
	
	/**
	 * 加载账号财务：余额及共享账号 数据库异常时返回null
	 * @return
	 */
	public static Map<String, Map<String, Object>> loadAccountFinance() {
		Map<String, Map<String, Object>> resultMap = new HashMap<String, Map<String, Object>>();
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		sql.append(
				"SELECT a.ACCOUNT_ID,a.ACCOUNT_USABLE_SUM,a.ACCOUNT_CREDIT_SUM,asd.SHARE_ACCOUNT_ID FROM smoc.finance_account a ");
		sql.append("LEFT JOIN smoc.finance_account_share_detail asd ON a.ACCOUNT_ID = asd.ACCOUNT_ID AND  asd.SHARE_STATUS = 1 ");

		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String accountID = rs.getString("ACCOUNT_ID");
				String shareAccountID = rs.getString("SHARE_ACCOUNT_ID");
				double accountUsableSum = rs.getDouble("ACCOUNT_USABLE_SUM");
				double accountCreditSum = rs.getDouble("ACCOUNT_CREDIT_SUM");
				Map<String, Object> accountFinanceMap = new HashMap<String, Object>();
				resultMap.put(accountID, accountFinanceMap);
				accountFinanceMap.put("ACCOUNT_ID", accountID);
				accountFinanceMap.put("SHARE_ACCOUNT_ID", shareAccountID);
				accountFinanceMap.put("ACCOUNT_USABLE_SUM", accountUsableSum);
				accountFinanceMap.put("ACCOUNT_CREDIT_SUM", accountCreditSum);
			}
		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(), e);
			return null;
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
		}
		return resultMap;
	}
	
	/**
	 * 兼容4.0.0和4.0.1两个版本的账号扩展码
	 * @param extendCode
	 * @param extendNumber
	 * @return
	 */
	private static String getExtendCode(String extendCode,String extendNumber){
		if(StringUtils.isNotEmpty(extendNumber)){
			return extendNumber;
		}
		if(StringUtils.isNotEmpty(extendCode)){
			return extendCode;
		}
		return "";
	}
	
	/**
	 * 加载账号基本信息,获取account_base_info、account_finance_info、account_interface_info
	 * 数据库异常时返回null
	 * @return
	 */
	public static Map<String, Map<String, Object>> loadAccountBaseInfo() {
		Map<String, Map<String, Object>> resultMap = new HashMap<String, Map<String, Object>>();
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		sql.append(
				"SELECT abi.ACCOUNT_ID,abi.ACCOUNT_NAME,abi.ACCOUNT_STATUS,abi.ACCOUNT_PRIORITY,abi.TRANSFER_TYPE,abi.BUSINESS_TYPE,abi.RANDOM_EXTEND_CODE_LENGTH,abi.INFO_TYPE,abi.EXTEND_CODE,abi.EXTEND_NUMBER,aii.PROTOCOL,aii.SRC_ID");
		sql.append(
				",aii.EXECUTE_CHECK,afi.PAY_TYPE,afi.CHARGE_TYPE,e.ENTERPRISE_FLAG,aii.MATCHING_CHECK,abi.INDUSTRY_TYPE,abi.CARRIER,abi.ENTERPRISE_ID FROM smoc.account_base_info abi ");
		sql.append("LEFT JOIN smoc.account_interface_info aii ON aii.ACCOUNT_ID = abi.ACCOUNT_ID ");
		sql.append("LEFT JOIN smoc.account_finance_info afi ON afi.ACCOUNT_ID = abi.ACCOUNT_ID ");
		sql.append("LEFT JOIN smoc.enterprise_basic_info e ON abi.ENTERPRISE_ID = e.ENTERPRISE_ID ");

		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String accountID = rs.getString("ACCOUNT_ID");
				String accountName = rs.getString("ACCOUNT_NAME");
				String accountStatus = rs.getString("ACCOUNT_STATUS");
				String accountPriority = rs.getString("ACCOUNT_PRIORITY");
				String enterpriseFlag = rs.getString("ENTERPRISE_FLAG");
				String transferType = rs.getString("TRANSFER_TYPE");
				String businessType = rs.getString("BUSINESS_TYPE");
				String infoType = rs.getString("INFO_TYPE");
				String extendCode = rs.getString("EXTEND_CODE");
				String extendNumber = rs.getString("EXTEND_NUMBER");
				String protocol = rs.getString("PROTOCOL");
				String srcId = rs.getString("SRC_ID");

				int randomExtentCodeLength = rs.getInt("RANDOM_EXTEND_CODE_LENGTH");

				String executeCheck = rs.getString("EXECUTE_CHECK");
				String payType = rs.getString("PAY_TYPE");
				String consumeType = rs.getString("CHARGE_TYPE");
				String matchingCheck = rs.getString("MATCHING_CHECK");
				String industryType = rs.getString("INDUSTRY_TYPE");
				String carrier = rs.getString("CARRIER");
				String enterpriseID = rs.getString("ENTERPRISE_ID");

				Map<String, Object> accountFinanceMap = new HashMap<String, Object>();
				accountFinanceMap.put("ACCOUNT_ID", accountID);
				accountFinanceMap.put("ACCOUNT_NAME", accountName);
				accountFinanceMap.put("ACCOUNT_STATUS", accountStatus);
				accountFinanceMap.put("ACCOUNT_PRIORITY", accountPriority);
				accountFinanceMap.put("ENTERPRISE_FLAG", enterpriseFlag);
				accountFinanceMap.put("TRANSFER_TYPE", transferType);
				accountFinanceMap.put("BUSINESS_TYPE", businessType);
				accountFinanceMap.put("INFO_TYPE", infoType);
				accountFinanceMap.put("EXTEND_CODE", getExtendCode(extendCode,extendNumber));
				accountFinanceMap.put("PROTOCOL", StringUtils.defaultString(protocol));

				accountFinanceMap.put("SRC_ID", StringUtils.defaultString(srcId));
				accountFinanceMap.put("RANDOM_EXTEND_CODE_LENGTH", randomExtentCodeLength);
				accountFinanceMap.put("EXECUTE_CHECK", StringUtils.defaultString(executeCheck,"0"));
				accountFinanceMap.put("PAY_TYPE", payType);
				accountFinanceMap.put("CHARGE_TYPE", consumeType);
				accountFinanceMap.put("MATCHING_CHECK", matchingCheck);
				accountFinanceMap.put("INDUSTRY_TYPE", industryType);
				accountFinanceMap.put("CARRIER", carrier);
				accountFinanceMap.put("ENTERPRISE_ID", enterpriseID);
				resultMap.put(accountID, accountFinanceMap);
			}

		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(), e);
			return null;
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
		}
		return resultMap;
	}

	/**
	 * 加载账号价格 数据库异常时返回null
	 * @return
	 */
	public static Map<String, Map<String, Double>> loadAccountPrice() {
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		sql.append("SELECT ACCOUNT_ID,CARRIER_TYPE,CARRIER,CARRIER_PRICE FROM smoc.account_finance_info ");
		Map<String, Map<String, Double>> resultMap = new HashMap<String, Map<String, Double>>();

		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String accountID = rs.getString("ACCOUNT_ID");
				String carrier = rs.getString("CARRIER");
				double carrierPrice = rs.getDouble("CARRIER_PRICE");
				Map<String, Double> priceMap = resultMap.get(accountID);
				if (priceMap == null) {
					priceMap = new HashMap<String, Double>();
					resultMap.put(accountID, priceMap);
				}
				priceMap.put(carrier, carrierPrice);
			}

		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(), e);
			return null;
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
		}
		return resultMap;
	}
	
	/**
	 * 通过账号和运营商获取价格   数据库异常时返回null
	 * @return
	 */
	public static String loadAccountPrice(String accountId,String carrier) {
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		sql.append("SELECT CARRIER_PRICE FROM smoc.account_finance_info where ACCOUNT_ID=? and CARRIER=?");
	
		String carrierPrice=null;
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());			
			pstmt.setString(1, accountId);
			pstmt.setString(2, carrier);
			rs = pstmt.executeQuery();		
			while (rs.next()) {				
				 carrierPrice = String.valueOf(rs.getDouble("CARRIER_PRICE"));
			}

		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(), e);
			return null;
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
		}
		return carrierPrice;
	}
	
	
}
