package com.base.common.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.base.common.cache.CacheBaseService;
import com.base.common.constant.FixedConstant;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.log.CategoryLog;
import com.base.common.manager.AccountChannelManager.ChannelWeight;
import com.base.common.worker.SuperMapWorker;
/**
 * 账号通道对应关系
 */
public class AccountChannelManager  extends SuperMapWorker<String,ChannelWeight>{
	
	private static AccountChannelManager manager = new AccountChannelManager();

	private AccountChannelManager() {
		loadData();
		this.start();
	}

	public static AccountChannelManager getInstance() {
		return manager;
	}
	
	@Override
	public void doRun() throws Exception {
		Thread.sleep(INTERVAL);
		loadData();
	}
	
	/**
	 * 根据账号、运营商(移动、联通、电信、国际)、省份或国家编码获取通道ID
	 * @param accountID
	 * @param carrier
	 * @param areaCode
	 * @return
	 */
	public String getChannel(String accountID, String carrier,String areaCode) {
		try {
			String accountCarrierAreaCode = new StringBuilder()
			.append(accountID).append(FixedConstant.SPLICER)	
			.append(carrier).append(FixedConstant.SPLICER)
			.append(areaCode).toString();	
			ChannelWeight channelWeight = get(accountCarrierAreaCode);
			if(channelWeight != null){
				return channelWeight.getChannelID();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
		return "";

	}

	/**
	 * 加载数据
	 */
	private void loadData() {
		long startTime = System.currentTimeMillis();

		Map<String,List<AccountChannelInfo>> accountChannelInfoListMap = loadAccountChannelInfo();
		
		if(accountChannelInfoListMap != null ){
			Map<String,List<AccountChannelInfo>> accountCarrierAreaCodeChannelInfoMap = convertToAccountCarrierAreaCodeChannelInfoMap(accountChannelInfoListMap);
			superMap = convertToAccountCarrierAreaCodeChannelWeightMap(accountCarrierAreaCodeChannelInfoMap);
		}
		long endTime = System.currentTimeMillis();
		CategoryLog.commonLogger.info("size={},耗时={}", size(), (endTime - startTime));
	}
	
	/**
	 * 将ID&carrier&areaCode-List<AccountChannelInfo>转换成ID&carrier&areaCode-ChannelWeight
	 * @param accountCarrierAreaCodeChannelInfoMap
	 * @return
	 */
	private Map<String,ChannelWeight>  convertToAccountCarrierAreaCodeChannelWeightMap(Map<String,List<AccountChannelInfo>> accountCarrierAreaCodeChannelInfoMap){
		Map<String,ChannelWeight> resultMap = new HashMap<String, ChannelWeight>();
		for(Map.Entry<String, List<AccountChannelInfo>> entry : accountCarrierAreaCodeChannelInfoMap.entrySet()){
			String accountCarrierAreaCode = entry.getKey();
			String areaCode = accountCarrierAreaCode.split(FixedConstant.SPLICER)[2];
			List<AccountChannelInfo> accountChannelInfoList = entry.getValue();
			ChannelWeight channelWeight = convertToChannelWeight(areaCode,accountChannelInfoList);
			resultMap.put(accountCarrierAreaCode, channelWeight);
		}
		return resultMap;
	}
	
	/**
	 * 将accountChannelInfoList转ChannelWeight
	 * 判断通道分省逻辑
	 * @param areaCode
	 * @param accountChannelInfoList
	 * @return
	 */
	private ChannelWeight convertToChannelWeight(String areaCode,List<AccountChannelInfo> accountChannelInfoList){

		List<AccountChannelInfo> proviceAccountChannelInfoList = null;
		//先判断是有分省
		for(AccountChannelInfo accountChannelInfo:accountChannelInfoList){
			boolean result = isProvinceChannel(areaCode,accountChannelInfo.getBusinessAreaType(),accountChannelInfo.getAreaCodeSet());
			//当存在分省时，维护分省通道，支持多个分省通道
			if(result){
				if(proviceAccountChannelInfoList == null){
					proviceAccountChannelInfoList = new ArrayList<AccountChannelInfo>();
				}
				proviceAccountChannelInfoList.add(accountChannelInfo);
			}
		}
		if(CollectionUtils.isNotEmpty(proviceAccountChannelInfoList)){
			return convertToChannelWeight(proviceAccountChannelInfoList);
		}
		
		return convertToChannelWeight(accountChannelInfoList);
	}
	
	/**
	 *  将accountChannelInfoList转ChannelWeight
	 *  判断通道日限量月限量逻辑
	 * @param accountChannelInfoList
	 * @return
	 */
	private ChannelWeight convertToChannelWeight(List<AccountChannelInfo> accountChannelInfoList){
		
		List<AccountChannelInfo> notFullAccountChannelInfoList = new ArrayList<AccountChannelInfo>();
		//先判断每个通道的量是否满了日限量或月限量
		for(AccountChannelInfo accountChannelInfo:accountChannelInfoList){
			String channelID = accountChannelInfo.getChannelID();
			//获取没有满量的通道
			boolean result = isFullSuccessNumber(channelID);
			
			if(!result){
				notFullAccountChannelInfoList.add(accountChannelInfo);
			}
		}
		
		ChannelWeight channelWeight = new ChannelWeight();
		//当存在满量的通道时
		int index = 0;
		if(notFullAccountChannelInfoList.size() > 0){
			//按照权重构造ChannelWeight
			for(AccountChannelInfo accountChannelInfo : notFullAccountChannelInfoList){
				for(int i=0;i<accountChannelInfo.getChannelWeight();i++){
					channelWeight.put(index, accountChannelInfo.getChannelID());
					index++;
				}
			}
		}else{
			for(AccountChannelInfo accountChannelInfo : accountChannelInfoList){
				channelWeight.put(index, accountChannelInfo.getChannelID());
				index++;
			}
		}
		return channelWeight;
	}
	
	/**
	 * 判断日限量或月限量是否满
	 * @param channelID
	 * @return
	 */
	private boolean isFullSuccessNumber(String channelID){
		long monthSuccessNumber = BusinessDataManager.getInstance().getMonthSuccessNumber(channelID);
		long daySuccessNumber = BusinessDataManager.getInstance().getDaySuccessNumber(channelID);
		// 获取通道的当日/月发送量
		long currentDaySuccessNumber = CacheBaseService
				.getChannelTodaySuccessNumberFromMiddlewareCache(channelID);
		long currentMonthSuccessNumber = CacheBaseService
				.getChannelMonthSuccessNumberFromMiddlewareCache(channelID);
		
		return (daySuccessNumber >0 && currentDaySuccessNumber > daySuccessNumber) 
				|| (monthSuccessNumber >0 && currentMonthSuccessNumber > monthSuccessNumber);
	}
	
	/**
	 * 判断某个通道是否为指定省的分省通道
	 * @param areaCode
	 * @param businessAreaType
	 * @param areaCodeSet
	 * @return
	 */
	private boolean isProvinceChannel(String areaCode,String businessAreaType,Set<String> areaCodeSet){
		return FixedConstant.BusinessAreaType.PROVINCE.name().equals(businessAreaType) && areaCodeSet.contains(areaCode);
	}
	
	
	/**
	 * 将账号ID-List<AccountChannelInfo>转换 成账号ID&carrier&areaCode-List<AccountChannelInfo>
	 * @param accountChannelInfoListMap
	 * @return
	 */
	private Map<String,List<AccountChannelInfo>>  convertToAccountCarrierAreaCodeChannelInfoMap(Map<String,List<AccountChannelInfo>> accountChannelInfoListMap){
		Map<String,List<AccountChannelInfo>> resultMap = new HashMap<String, List<AccountChannelInfo>>();
		
		for(Map.Entry<String,List<AccountChannelInfo>> entry : accountChannelInfoListMap.entrySet()){
			String accountID = entry.getKey();
		
			List<AccountChannelInfo> accountChannelInfoList = entry.getValue();
			
			for(AccountChannelInfo accountChannelInfo:accountChannelInfoList){

				String carrier = accountChannelInfo.getCarrier();
				Set<String> areaCodeSet = accountChannelInfo.getAreaCodeSet();
				
				for(String areaCode : areaCodeSet){
					String key = new StringBuilder().append(accountID).append(FixedConstant.SPLICER)
					.append(carrier).append(FixedConstant.SPLICER)
					.append(areaCode).toString();	
					List<AccountChannelInfo> resultList = resultMap.get(key);
					if(resultList == null){
						resultList = new ArrayList<AccountChannelInfo>();
						resultMap.put(key, resultList);
					}
					resultList.add(accountChannelInfo);
				}
				
			}
			

			
		}
		
		return resultMap;
	}
	
	/**
	 * 加载账号通道信息、包含通道区域范围和支持的业务区域  数据库异常时返回null
	 * 需注意的过滤条件：通道组状态和通道状态
	 * @return
	 */
	private Map<String,List<AccountChannelInfo>> loadAccountChannelInfo() {
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//需关联 通道的真实状态	
		sql.append("SELECT ac.ACCOUNT_ID,ac.CARRIER,ac.CHANNEL_ID,ac.CHANNEL_WEIGHT,ac.CHANNEL_GROUP_ID FROM smoc.account_channel_info ac INNER JOIN smoc.config_channel_basic_info c ON ac.CHANNEL_ID = c.CHANNEL_ID AND c.CHANNEL_STATUS = ? ");
		
		Map<String,List<AccountChannelInfo>> resultMap = new HashMap<String, List<AccountChannelInfo>>();

		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			//通道的状态 001-正常
			pstmt.setString(1, "001");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String accountID = rs.getString("ACCOUNT_ID");
				String carrier = rs.getString("CARRIER");
				String channelID = rs.getString("CHANNEL_ID");
				int channelWeight = rs.getInt("CHANNEL_WEIGHT");
				String channelGroupID = rs.getString("CHANNEL_GROUP_ID");
				if(StringUtils.isEmpty(channelGroupID)){
					channelWeight = 1;
				}
				String areaCodes = ChannelInfoManager.getInstance().getSupportAreaCodes(channelID);
				String businessAreaType = ChannelInfoManager.getInstance().getBusinessAreaType(channelID);
				
				//有支持区域时才处理数据 && FixedConstant.AccountStatus.NORMAL.name().equals(AccountInfoManager.getInstance().getAccountStatus(accountID))
				if(StringUtils.isNotEmpty(areaCodes) 
						&& FixedConstant.AccountStatus.NORMAL.name().equals(AccountInfoManager.getInstance().getAccountStatus(accountID)) 
						){
					Set<String> areaCodeSet = new HashSet<String>();
					areaCodeSet.addAll(Arrays.asList(areaCodes.split(FixedConstant.DATABASE_SEPARATOR)));	
					List<AccountChannelInfo> accountChannelInfoList = resultMap.get(accountID);
					if(accountChannelInfoList == null){
						accountChannelInfoList = new ArrayList<AccountChannelInfo>();
						resultMap.put(accountID, accountChannelInfoList);
					}
					AccountChannelInfo accountChannelInfo = new AccountChannelInfo(accountID, carrier, areaCodeSet, channelID, businessAreaType, channelWeight);
					accountChannelInfoList.add(accountChannelInfo);
				}
				
			}

		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(), e);
			return null;
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
		}
		return resultMap;
	}
	
	class AccountChannelInfo{
		/**
		 * 账号ID
		 */
		private String accountID;
		
		/**
		 * 运营商:CMCC/UNIC/TELC/INTL
		 */
		private String carrier;
		
		/**
		 * 业务区域
		 */
		private Set<String> areaCodeSet;
		
		/**
		 * 通道ID
		 */
		private String channelID;
		
		/**
		 * 通道区域范围
		 */
		private String businessAreaType;
		
		/**
		 * 权重
		 */
		private int channelWeight;

		public AccountChannelInfo(String accountID, String carrier,
				Set<String> areaCodeSet, String channelID,
				String businessAreaType, int channelWeight) {
			super();
			this.accountID = accountID;
			this.carrier = carrier;
			this.areaCodeSet = areaCodeSet;
			this.channelID = channelID;
			this.businessAreaType = businessAreaType;
			this.channelWeight = channelWeight;
		}

		public String getAccountID() {
			return accountID;
		}

		public String getCarrier() {
			return carrier;
		}

		public Set<String> getAreaCodeSet() {
			return areaCodeSet;
		}

		public String getChannelID() {
			return channelID;
		}

		public String getBusinessAreaType() {
			return businessAreaType;
		}

		public int getChannelWeight() {
			return channelWeight;
		}

		@Override
		public String toString() {
			return "AccountChannelInfo [accountID=" + accountID + ", carrier="
					+ carrier + ", areaCodeSet=" + areaCodeSet + ", channelID="
					+ channelID + ", businessAreaType=" + businessAreaType
					+ ", channelWeight=" + channelWeight + "]";
		}
		
		

	}
	
	class ChannelWeight {

		/**
		 * 通道按权重分配集合:随机数-channelID
		 */
		private Map<Integer, String> channelIDMap = new HashMap<Integer, String>();
		
		/**
		 * 根据权重获取通道ID
		 * @return
		 */
		public String getChannelID(){
			Map<Integer, String> channelIDMap = new HashMap<Integer, String>(this.channelIDMap);
			if(channelIDMap.size() > 0){
				return channelIDMap.get((int)(Math.random() * channelIDMap.size()));
			}
			return "";
		}
		
		public void put(int index,String channelID){
			channelIDMap.put(index, channelID);
		}

		@Override
		public String toString() {
			return "ChannelWeight [channelIDMap=" + channelIDMap + "]";
		}
		
		
		
	}
}
