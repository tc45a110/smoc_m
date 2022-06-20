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
 * 通道失败补发
 * @author siyan
 *
 */
public class ChannelRepairManager extends SuperMapWorker<String, ArrayList<String>>{
	
	private static ChannelRepairManager manager = new ChannelRepairManager();
	
	private Map<String,HashMap<String,Object>> repairRuleMap = new HashMap<String, HashMap<String,Object>>();
	
	public static ChannelRepairManager getInstance() {
		return manager;
	}
	
	
	public String getChannelRepairID(String channelID,String channelRepairID,String statusCode) {
		if(StringUtils.isEmpty(channelRepairID)) {
			//初补发
			HashMap<String,Object> map = repairRuleMap.get(channelID);
			if(map != null) {
				String repairCodeRegex = (String)map.get("repairCode");
				//状态码匹配成功 返回补发通道
				if(match(repairCodeRegex, statusCode)) {
					return get(channelID).get(0);
				}		
			}
		}else {
			//补发通道不为空  不用匹配状态码
			ArrayList<String> list = get(channelID);
			int index = list.indexOf(channelRepairID);
			if(index != -1) {
				return list.get(index + 1);
			}//已进行补发  不管关注状态码
		}
		return "";
	}
	
	public int getChannelRepairTime(String channelID) {
		HashMap<String,Object> map = repairRuleMap.get(channelID);
		if(map != null) {
			return (int)map.get("repairDate") * 60;
		}
		return 0;
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
		sql.append("SELECT BUSINESS_ID,CHANNEL_REPAIR_ID FROM smoc.config_channel_repair_rule  ");
		sql.append("WHERE BUSINESS_TYPE = 'CHANNEL' AND REPAIR_STATUS = '1' ORDER BY BUSINESS_ID,SORT ASC");
		Map<String,ArrayList<String>> channelRepairMap = new HashMap<String, ArrayList<String>>();
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String accountID = rs.getString("BUSINESS_ID");
				String channelRepairID = rs.getString("CHANNEL_REPAIR_ID");
				
				ArrayList<String> channelRepairIDs = channelRepairMap.get(accountID);
				if(channelRepairIDs == null) {
					channelRepairIDs = new ArrayList<String>();
					channelRepairMap.put(accountID, channelRepairIDs);
				}
				channelRepairIDs.add(channelRepairID);
			}

			return channelRepairMap;
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
		sql.append("SELECT BUSINESS_ID,REPAIR_CODE,REPAIR_DATE FROM smoc.config_repair_rule WHERE BUSINESS_TYPE = 'CHANNEL' AND REPAIR_STATUS = '1'");
		Map<String,HashMap<String,Object>> accountRepairRuleMap = new HashMap<String,HashMap<String,Object>>();
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				HashMap<String, Object> repairRuleMap = new HashMap<String, Object>();
				String channelID = rs.getString("BUSINESS_ID");
				String repairCode = rs.getString("REPAIR_CODE");
				int repairDate = rs.getInt("REPAIR_DATE");
				
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
				
				repairRuleMap.put("channelID", channelID);
				repairRuleMap.put("repairCode", repairCode);
				repairRuleMap.put("repairDate", repairDate);
				
				accountRepairRuleMap.put(channelID, repairRuleMap);
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
