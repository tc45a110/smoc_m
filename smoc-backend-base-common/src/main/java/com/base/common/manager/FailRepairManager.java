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
import com.base.common.constant.FixedConstant.RepairBusinessType;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.log.CategoryLog;
import com.base.common.worker.SuperMapWorker;

/**
 * 账号失败补发
 * @author siyan
 *
 */
public class FailRepairManager extends SuperMapWorker<String, ArrayList<String>>{
	
	private static FailRepairManager manager = new FailRepairManager();
	
	private Map<String,String> accountRepairStatusMap = new HashMap<String, String>();
	
	private Map<String,String> configRepairCodeMap = new HashMap<String, String>();
	
	private Map<String,Integer> configRepairDateMap = new HashMap<String, Integer>();
	
	public static FailRepairManager getInstance() {
		return manager;
	}
	
	private FailRepairManager() {
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
	public ArrayList<String> getChannelRepairID(String accountID,String businessCarrier,String statusCode,String channelID) {
		String key = new StringBuffer().append(accountID).append(FixedConstant.SPLICER).append(FixedConstant.RepairBusinessType.ACCOUNT.name())
														 .append(FixedConstant.SPLICER).append(businessCarrier).toString();
		
		String accountRepairStatus = getAccountRepairStatus(accountID, channelID, businessCarrier);
		ArrayList<String> list;
		if(FixedConstant.RepairStatus.ACCOUNT_REPAIR.name().equals(accountRepairStatus)) {
			//账号补发
			list = get(key);
			if(list != null && list.size() > 0) {
				String repairCodeRegex = configRepairCodeMap.get(key);
				if(StringUtils.isNotEmpty(repairCodeRegex) && match(repairCodeRegex, statusCode)) {
					return list;
				}
			}
		}else if(FixedConstant.RepairStatus.CHANNEL_REPAIR.name().equals(accountRepairStatus)) {
			//通道补发
			key = new StringBuffer().append(channelID).append(FixedConstant.SPLICER).append(FixedConstant.RepairBusinessType.CHANNEL.name())
					  								  .append(FixedConstant.SPLICER).append(businessCarrier).toString();
			list = get(key);
			if(list != null && list.size() > 0) {
				String repairCodeRegex = configRepairCodeMap.get(key);
				if(StringUtils.isNotEmpty(repairCodeRegex) && match(repairCodeRegex, statusCode)) {
					return list;
				}
			}
		}
		//不进行补发
		return new ArrayList<String>();
	}
	
	/**
	 * 获取失败补发的有效期限
	 * @param accountID
	 * @param businessCarrier
	 * @return
	 */
	public int getRepairTime(String accountID,String businessCarrier,String channelID) {
		String accountRepairStatus = getAccountRepairStatus(accountID,channelID,businessCarrier);
		
		if(FixedConstant.RepairStatus.ACCOUNT_REPAIR.name().equals(accountRepairStatus)) {
			//账号补发
			return configRepairDateMap.get(accountID) == null ? 0 : configRepairDateMap.get(accountID) ;
		}else if(FixedConstant.RepairStatus.CHANNEL_REPAIR.name().equals(accountRepairStatus)) {
			//通道补发
			return configRepairDateMap.get(channelID) == null ? 0 : configRepairDateMap.get(channelID) ;
		}
		return 0;
	}
	
	/**
	 * 获取
	 * @param accountID
	 * @param businessCarrier
	 * @return
	 */
	public String getAccountRepairStatus(String accountID,String channelID,String businessCarrier) {
		String key = new StringBuilder().append(accountID).append(FixedConstant.SPLICER).append(RepairBusinessType.ACCOUNT.name())
														  .append(FixedConstant.SPLICER).append(businessCarrier).toString();
		
		if(accountRepairStatusMap.containsKey(accountID)) {
			//判断账号是否进行补发
			ArrayList<String> list = get(key);
			if(list != null && list.size() > 0) {
				return FixedConstant.RepairStatus.ACCOUNT_REPAIR.name();
			}
			
			//判断通道是否进行补发
			key = new StringBuffer().append(channelID).append(FixedConstant.SPLICER).append(FixedConstant.RepairBusinessType.CHANNEL.name())
					  .append(FixedConstant.SPLICER).append(businessCarrier).toString();
			list = get(key);
			if(list != null && list.size() > 0) {
				return FixedConstant.RepairStatus.CHANNEL_REPAIR.name();
			}
		}
		return FixedConstant.RepairStatus.NO_REPAIR.name();
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
	
	@SuppressWarnings("unchecked")
	private void loadData() {
		long startTime = System.currentTimeMillis();
		Map<String,String> resultConfigRepairStatusMap = loadAccountRepairStatus();
		if(resultConfigRepairStatusMap != null) {
			accountRepairStatusMap = resultConfigRepairStatusMap;
		}
		Map<String,Integer> resultConfigRepairDateMap = loadConfigRepairDate();
		if(resultConfigRepairDateMap != null) {
			configRepairDateMap = resultConfigRepairDateMap;
		}
		Map<String,Object> resultMap = loadConfigChannelRepairValues();
		if(resultMap != null) {
			Map<String,ArrayList<String>> resultConfigRepairChannelMap = (Map<String,ArrayList<String>>)resultMap.get("configRepairChannelMap");
			if(resultConfigRepairChannelMap != null) {
				superMap = resultConfigRepairChannelMap;
			}
			Map<String,String> resultConfigRepairCodeMap = (Map<String,String>)resultMap.get("configRepairCodeMap");
			if(resultConfigRepairCodeMap != null) {
				configRepairCodeMap = resultConfigRepairCodeMap;
			}
		}
		long endTime = System.currentTimeMillis();
		CategoryLog.commonLogger.info("size={},耗时={}",size(),(endTime-startTime));	
	}
	
	private Map<String,Object> loadConfigChannelRepairValues() {
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		sql.append("SELECT BUSINESS_ID,BUSINESS_TYPE,CARRIER,CHANNEL_REPAIR_ID,REPAIR_CODE FROM smoc.config_channel_repair_rule ");
		sql.append("WHERE REPAIR_STATUS = '1' ORDER BY BUSINESS_ID,CARRIER,SORT ASC ");
		Map<String,ArrayList<String>> configRepairChannelMap = new HashMap<String, ArrayList<String>>();
		Map<String,String> configRepairCodeMap = new HashMap<String,String>();
		Map<String,Object> resultMap = new HashMap<String, Object>();
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String businessID = rs.getString("BUSINESS_ID");
				String businessType = rs.getString("BUSINESS_TYPE");
				String carrier = rs.getString("CARRIER");
				String channelRepairID = rs.getString("CHANNEL_REPAIR_ID");
				String repairCode = rs.getString("REPAIR_CODE");
				
				String key = new StringBuffer().append(businessID).append(FixedConstant.SPLICER).append(businessType)
																	.append(FixedConstant.SPLICER).append(carrier).toString();
				ArrayList<String> channelRepairIDs = configRepairChannelMap.get(key);
				if(channelRepairIDs == null) {
					channelRepairIDs = new ArrayList<String>();
					configRepairChannelMap.put(key, channelRepairIDs);
				}
				channelRepairIDs.add(channelRepairID);
				
				//错误码处理
				if(!configRepairCodeMap.containsKey(key)) {
					if(FixedConstant.FAIL_REPAIR_ALL_FAILED_CODE.equals(repairCode)) {
						repairCode = FixedConstant.REPAIR_ALL_FAILED_CODE_REGEXP;
					}else {
						if(StringUtils.isNotEmpty(repairCode)) {
							StringBuilder repairCodeSB = new StringBuilder("^(");
							repairCodeSB.append(repairCode);
							repairCodeSB.append("){1}");
							repairCode = repairCodeSB.toString();
						}
					}
					configRepairCodeMap.put(key, repairCode);
				}
			}
			
			resultMap.put("configRepairChannelMap", configRepairChannelMap);
			resultMap.put("configRepairCodeMap", configRepairCodeMap);

			return resultMap;
		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(), e);
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
		}
		return null;
	}
	
	private Map<String,Integer> loadConfigRepairDate() {
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		sql.append("SELECT BUSINESS_ID,BUSINESS_TYPE,REPAIR_DATE FROM smoc.config_repair_rule WHERE REPAIR_STATUS = '1'");
		Map<String,Integer> configRepairDateMap = new HashMap<String,Integer>();
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String businessID = rs.getString("BUSINESS_ID");
				int repairDate = rs.getInt("REPAIR_DATE");
				
				configRepairDateMap.put(businessID, repairDate);
			}
			return configRepairDateMap;
		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(), e);
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
		}
		return null;
	}
	
	private Map<String,String> loadAccountRepairStatus() {
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		sql.append("SELECT ACCOUNT_ID,REPAIR_STATUS FROM smoc.account_base_info WHERE REPAIR_STATUS = '1'");
		Map<String,String> configRepairStatusMap = new HashMap<String,String>();
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String accountID = rs.getString("ACCOUNT_ID");
				configRepairStatusMap.put(accountID, "");
			}
			return configRepairStatusMap;
		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(), e);
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
		}
		return null;
	}
	
}
