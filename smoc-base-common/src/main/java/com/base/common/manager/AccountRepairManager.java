package com.base.common.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.base.common.constant.FixedConstant;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.log.CategoryLog;
import com.base.common.worker.SuperMapWorker;

/**
 * 账号失败补发
 * @author siyan
 *
 */
public class AccountRepairManager extends SuperMapWorker<String, ArrayList<String>>{
	
	private static AccountRepairManager manager = new AccountRepairManager();
	
	private Map<String,HashMap<String,Object>> repairRuleMap = new HashMap<String, HashMap<String,Object>>();
	
	public static AccountRepairManager getInstance() {
		return manager;
	}
	
	private AccountRepairManager() {
		loadData();
		this.start();
	}
	
	/**
	 * 获取失败补发通道
	 * @param accountID
	 * @param businessCarrier
	 * @param statusCode
	 * @param channelRepairID
	 * @return
	 */
	public String getChannelRepairID(String accountID,String businessCarrier,String statusCode,String channelRepairID) {
		String key = new StringBuffer().append(accountID).append(FixedConstant.SPLICER).append(businessCarrier).toString();
		if(StringUtils.isEmpty(channelRepairID)) {
			//补发通道为空  第一次补发  状态码匹配成功 返回补发通道
			String repairCodeRegex = (String)repairRuleMap.get(key).get("repairCode");
			if(match(repairCodeRegex, statusCode)) {
				return get(key).get(0);
			}
		}else {
			//补发通道不为空  不用匹配状态码
			ArrayList<String> list = get(key);
			int index = list.indexOf(channelRepairID);
			if(index != -1) {
				return list.get(index + 1);
			}
		}
		return "";
	}
	
	/**
	 * 获取失败补发的有效期限
	 * @param accountID
	 * @param businessCarrier
	 * @return
	 */
	public int getAccountRepairTime(String accountID, String businessCarrier) {
		String key = new StringBuffer().append(accountID).append(FixedConstant.SPLICER).append(businessCarrier).toString();
		HashMap<String,Object> map = repairRuleMap.get(key);
		if(map != null) {
			return (int)map.get("repairDate") * 60;
		}
		return 0;
	}
	
	/**
	 * 获取
	 * @param accountID
	 * @param businessCarrier
	 * @return
	 */
	public String getAccountRepairStatus(String accountID, String businessCarrier) {
		if(!superMap.containsKey(accountID)) {
			return FixedConstant.RepairStatus.NO_CONFIG.name();
		}else {
			if(getAccountRepairTime(accountID, businessCarrier) == 0) {
				return FixedConstant.RepairStatus.NO_REPAIR.name();
			}else {
				return FixedConstant.RepairStatus.REPAIR.name();
			}
		}
	}
	
	/**
	 *  匹配函数
	 * @param repairCodeRegex
	 * @param statusCode
	 * @return
	 */
    private boolean match(String repairCodeRegex, String statusCode) {
        return Pattern.matches(repairCodeRegex, statusCode);
    }

	@Override
	protected void doRun() throws Exception {
		sleep(INTERVAL);
		loadData();
	}
	
	private void loadData() {
		long startTime = System.currentTimeMillis();
		Map<String,ArrayList<String>> resultAccountChannelRepairMapMap = loadConfigChannelRepairValues();
		Map<String,HashMap<String,Object>> resultAccountRepairRuleMap = loadConfigRepairRule();
		if(resultAccountChannelRepairMapMap != null){
			superMap = resultAccountChannelRepairMapMap;
		}
		
		if(resultAccountRepairRuleMap != null){
			repairRuleMap = resultAccountRepairRuleMap;
		}
		
		long endTime = System.currentTimeMillis();
		CategoryLog.commonLogger.info("size={},耗时={}",size(),(endTime-startTime));
	}
	
	private Map<String,ArrayList<String>> loadConfigChannelRepairValues() {
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		sql.append("SELECT BUSINESS_ID,CARRIER,CHANNEL_REPAIR_ID FROM smoc.config_channel_repair_rule ");
		sql.append("WHERE BUSINESS_TYPE = 'ACCOUNT' AND REPAIR_STATUS = '1' ORDER BY BUSINESS_ID,CARRIER,SORT ASC");
		Map<String,ArrayList<String>> accountChannelRepairMap = new HashMap<String, ArrayList<String>>();
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String accountID = rs.getString("BUSINESS_ID");
				String carrier = rs.getString("CARRIER");
				String channelRepairID = rs.getString("CHANNEL_REPAIR_ID");
				
				String key = new StringBuffer().append(accountID).append(FixedConstant.SPLICER).append(carrier).toString();
				ArrayList<String> channelRepairIDs = accountChannelRepairMap.get(key);
				if(channelRepairIDs == null) {
					channelRepairIDs = new ArrayList<String>();
					accountChannelRepairMap.put(key, channelRepairIDs);
				}
				channelRepairIDs.add(channelRepairID);
			}

			return accountChannelRepairMap;
		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(), e);
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
		}
		return null;
	}
	
	private Map<String,HashMap<String,Object>> loadConfigRepairRule() {
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		sql.append("SELECT BUSINESS_ID,CARRIER,REPAIR_CODE,REPAIR_DATE FROM smoc.config_repair_rule WHERE BUSINESS_TYPE = 'ACCOUNT' AND REPAIR_STATUS = '1'");
		Map<String,HashMap<String,Object>> accountRepairRuleMap = new HashMap<String,HashMap<String,Object>>();
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				HashMap<String, Object> repairRuleMap = new HashMap<String, Object>();
				String accountID = rs.getString("BUSINESS_ID");
				String repairCode = rs.getString("REPAIR_CODE");
				int repairDate = rs.getInt("REPAIR_DATE");
				String carrier = rs.getString("CARRIER");
				
				if(FixedConstant.FAIL_REPAIR_ALL_FAILED_CODE.equals(repairCode)) {
					repairCode = FixedConstant.REPAIR_ALL_FAILED_CODE_REGEXP;
				}else {
					if(StringUtils.isNotEmpty(repairCode)) {
						StringBuilder repairCodeSB = new StringBuilder();
						for(String errCode : repairCode.split(FixedConstant.DATABASE_SEPARATOR)) {
							if(repairCodeSB.length() > 0) {
								repairCodeSB.append("|");
							}else {
								repairCodeSB.append("^(");
							}
							repairCodeSB.append(errCode);
						}
						repairCodeSB.append("){1}");
						repairCode = repairCodeSB.toString();
					}
				}
				
				repairRuleMap.put("accountID", accountID);
				repairRuleMap.put("repairCode", repairCode);
				repairRuleMap.put("repairDate", repairDate);
				repairRuleMap.put("carrier", carrier);
				String key = new StringBuffer().append(accountID).append(FixedConstant.SPLICER).append(carrier).toString();
				
				accountRepairRuleMap.put(key, repairRuleMap);
			}
			return accountRepairRuleMap;
		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(), e);
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
		}
		return null;
	}
}
