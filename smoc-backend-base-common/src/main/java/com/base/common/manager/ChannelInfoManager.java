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
import org.apache.commons.lang3.StringUtils;
import com.base.common.cache.CacheBaseService;
import com.base.common.constant.FixedConstant;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.log.CategoryLog;
import com.base.common.manager.ChannelInfoManager.ChannelInfo;
import com.base.common.worker.SuperMapWorker;

public class ChannelInfoManager extends SuperMapWorker<String, ChannelInfo> {

	private static ChannelInfoManager manager = new ChannelInfoManager();

	private ChannelInfoManager() {
		loadData();
		this.start();
	}

	public static ChannelInfoManager getInstance() {
		return manager;
	}

	/**
	 * 加载数据
	 */
	private void loadData() {
		long startTime = System.currentTimeMillis();
		Map<String, ChannelInfo> resultMap = loadChannelInfo();
		if (resultMap != null) {
			superMap = resultMap;
		}
		long endTime = System.currentTimeMillis();
		CategoryLog.commonLogger.info("size={},耗时={}", size(), (endTime - startTime));
	}

	@Override
	public void doRun() throws Exception {
		Thread.sleep(INTERVAL);
		loadData();
	}

	/**
	 * 获取通道接口参数
	 * 
	 * @param channelID
	 * @return
	 */
	public Map<String, String> getChannelInterfaceInfo(String channelID) {
		ChannelInfo channelInfo = get(channelID);
		if (channelInfo != null) {
			return channelInfo.getInterfaceInfo();
		}
		return null;
	}
	
	/**
	 * 加载所有有效通道ID
	 * @return
	 */
	public Set<String> getAvailableChannelIDs() {
		Set<String> resultSet = new HashSet<String>();
		Set<String> channelIDSet = superMap.keySet();

		for (String channelID : channelIDSet) {
			// 当通道处于正常状态
			if (FixedConstant.ChannelStatus.NORMAL.name().equals(getChannelStatus(channelID))) {
				resultSet.add(channelID);
			}
		}

		return resultSet;
	}

	/**
	 * 获取需要加载的通道ID
	 * 
	 * @param protocol
	 * @param channelIDs
	 * @return
	 */
	public Set<String> getAvailableChannelIDs(String protocol, String channelIDs) {
		Set<String> resultSet = new HashSet<String>();
		Set<String> channelIDSet = superMap.keySet();

		for (String channelID : channelIDSet) {
			// 当通道处于正常状态
			if (FixedConstant.ChannelStatus.NORMAL.name().equals(getChannelStatus(channelID))) {
				// 获取通道接口信息
				Map<String, String> interfaceInfoMap = getChannelInterfaceInfo(channelID);
				// 协议匹配
				if (protocol.equals(interfaceInfoMap.get("PROTOCOL"))) {
					// channelIDs不为空则代表只加载指定的通道ID
					if (StringUtils.isNotEmpty(channelIDs)) {
						if (channelIDs.contains(channelID))
							resultSet.add(channelID);
					} else {
						resultSet.add(channelID);
					}
				}
			}
		}

		return resultSet;
	}

	/**
	 * 判断是否是cmpp20的版本
	 * 
	 * @param channelID
	 * @return
	 */
	public boolean isCMPP20Version(String channelID) {
		ChannelInfo channelInfo = get(channelID);
		if (channelInfo != null) {
			String version = channelInfo.getInterfaceInfo().get("VERSION");
			if (StringUtils.isNotEmpty(version) && version.contains("2")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取通道状态
	 * 
	 * @param channelID
	 * @return
	 */
	public String getChannelStatus(String channelID) {
		ChannelInfo channelInfo = get(channelID);
		if (channelInfo != null) {
			switch (channelInfo.getChannelStatus()) {
			case "001":
				return FixedConstant.ChannelStatus.NORMAL.name();
			case "002":
				return FixedConstant.ChannelStatus.EDIT.name();
			case "005":
				return FixedConstant.ChannelStatus.SUSPEND.name();
			default:
				break;
			}
		}
		return FixedConstant.ChannelStatus.SUSPEND.name();
	}

	/**
	 * 获取通道运行状态
	 * 
	 * @param channelID
	 * @return
	 */
	public String getChannelRunStatus(String channelID) {
		ChannelInfo channelInfo = get(channelID);
		if (channelInfo != null) {
			switch (channelInfo.getChannelStatus()) {
			case "1":
				return FixedConstant.ChannelRunStatus.NORMAL.name();
			case "2":
				return FixedConstant.ChannelRunStatus.ABNORMAL.name();
			default:
				break;
			}
		}
		return FixedConstant.ChannelRunStatus.ABNORMAL.name();
	}

	/**
	 * 根据通道速率及reids队列剩余数据计算每次加载的最大条数,该处忽略
	 * 
	 * @param channelID
	 * @return
	 */
	public int getMessageLoadMaxNumber(String channelID) {
		// 获取通道速率
		int maxSendSecond = getMaxSendSecond(channelID);
		if (!FixedConstant.ChannelStatus.NORMAL.name().equals(getChannelStatus(channelID))) {
			return maxSendSecond;
		}
		// 获取缓存队列数据条数
		int size = CacheBaseService.getChannelQueueSizeFromMiddlewareCache(channelID);
		return 2*maxSendSecond - size;
	}

	/**
	 * 获取通道速率
	 * 
	 * @param channelID
	 * @return
	 */
	public int getMaxSendSecond(String channelID) {
		ChannelInfo channelInfo = get(channelID);
		if (channelInfo == null) {
			return 0;
		}
		return channelInfo.getMaxSendSecond();
	}

	/**
	 * 获取通道连接数
	 * 
	 * @param channelID
	 * @return
	 */
	public int getConnectNumber(String channelID) {
		ChannelInfo channelInfo = get(channelID);
		if (channelInfo == null) {
			return 0;
		}
		return channelInfo.getConnectNumber();
	}

	/**
	 * 获取提交的间隔时间
	 * 
	 * @return
	 */
	public long getSubmitInterval(String channelID) {
		ChannelInfo channelInfo = get(channelID);
		if (channelInfo == null) {
			return 10;
		}
		return channelInfo.getSubmitInterval();
	}

	/**
	 * 获取提交响应的超时时间
	 * 
	 * @param channelID
	 * @return
	 */
	public long getResponseTimeout(String channelID) {
		String extendInterfaceParam = BusinessDataManager.getInstance().getExtendInterfaceParam(channelID);
		if (StringUtils.isNotEmpty(extendInterfaceParam)) {
			String[] array = extendInterfaceParam.split(FixedConstant.DATABASE_SEPARATOR);
			for (String param : array) {
				String[] paramArray = param.split("=");
				if (paramArray.length == 2) {
					if (paramArray[0].equals("responseTimeout")) {
						return Long.parseLong(paramArray[1]);
					}
				}
			}
		}
		// 默认设置为30秒
		return 30000;
	}

	public long getGlideWindowSize(String channelID){
		String extendInterfaceParam = BusinessDataManager.getInstance().getExtendInterfaceParam(channelID);
		if(StringUtils.isNotEmpty(extendInterfaceParam) ){
			String[] array = extendInterfaceParam.split(FixedConstant.DATABASE_SEPARATOR);
			for(String param:array){
				String[] paramArray = param.split("=");
				if(paramArray.length == 2){
					if(paramArray[0].equals("glideWindow")){
						return Long.parseLong(paramArray[1]);
					}
				}
			}
		}
		//默认设置为32
		return 32;
	}
	
	/**
	 * 获取通道接入码
	 * 
	 * @param channelID
	 * @return
	 */
	public String getChannelSRCID(String channelID) {
		ChannelInfo channelInfo = get(channelID);
		if (channelInfo != null) {
			return channelInfo.getChannelSRCID();
		}
		return "";
	}
	
	/**
	 * 获取通道名称
	 * 
	 * @param channelID
	 * @return
	 */
	public String getChannelName(String channelID) {
		ChannelInfo channelInfo = get(channelID);
		if (channelInfo != null) {
			return channelInfo.getChannelName();
		}
		return "";
	}

	/**
	 * 获取通道业务区域范围
	 * 
	 * @param channelID
	 * @return
	 */
	public String getBusinessAreaType(String channelID) {
		ChannelInfo channelInfo = get(channelID);
		if (channelInfo != null) {
			return channelInfo.getBusinessAreaType();
		}
		return "";
	}

	/**
	 * 获取通道支持的业务区域
	 * 
	 * @param channelID
	 * @return
	 */
	public String getSupportAreaCodes(String channelID) {
		ChannelInfo channelInfo = get(channelID);
		if (channelInfo != null) {
			return channelInfo.getSupportAreaCodes();
		}
		return "";
	}
	
	/**
	 *	获取通道的计价方式
	 * @param channelID
	 * @return
	 */
	public String getPriceStyle(String channelID) {
		ChannelInfo channelInfo = get(channelID);
		if (channelInfo != null) {
			return channelInfo.getPriceStyle();
		}
		return "";
	}

	private Map<String, ChannelInfo> loadChannelInfo() {
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		sql.append("SELECT a.CHANNEL_ID,a.CHANNEL_NAME,a.CARRIER,a.BUSINESS_TYPE,a.MAX_COMPLAINT_RATE");
		sql.append(",a.ACCESS_PROVINCE,a.CHANNEL_PROVDER,a.INFO_TYPE,a.BUSINESS_AREA_TYPE,a.SUPPORT_AREA_CODES");
		sql.append(",a.CHANNEL_STATUS,a.CHANNEL_RUN_STATUS,a.PRICE_STYLE");
		sql.append(",b.CHANNEL_ACCESS_ACCOUNT,b.CHANNEL_ACCESS_PASSWORD,b.CHANNEL_SERVICE_URL,b.SP_ID,b.SRC_ID");
		sql.append(",b.BUSINESS_CODE,b.CONNECT_NUMBER,b.MAX_SEND_SECOND,b.HEARTBEAT_INTERVAL,b.PROTOCOL,b.VERSION ");

		sql.append(
				"FROM smoc.config_channel_basic_info a join smoc.config_channel_interface b on a.CHANNEL_ID=b.CHANNEL_ID");

		Map<String, ChannelInfo> resultMap = new HashMap<String, ChannelInfo>();

		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {

				String channelID = rs.getString("CHANNEL_ID");
				String channelName=rs.getString("CHANNEL_NAME");
				String businessAreaType = rs.getString("BUSINESS_AREA_TYPE");
				String supportAreaCodes = rs.getString("SUPPORT_AREA_CODES");
				String channelStatus = rs.getString("CHANNEL_STATUS");
				String channelRunStatus = rs.getString("CHANNEL_RUN_STATUS");
				String priceStyle = rs.getString("PRICE_STYLE");
				int maxSendSecond = rs.getInt("MAX_SEND_SECOND");
				int connectNumber = rs.getInt("CONNECT_NUMBER");
				String secId = rs.getString("SRC_ID");

				ChannelInfo channelInfo = new ChannelInfo();
				Map<String, String> interfaceInfo = new HashMap<String, String>();
				interfaceInfo.put("CHANNEL_ID", rs.getString("CHANNEL_ID"));
				interfaceInfo.put("CHANNEL_ACCESS_ACCOUNT", rs.getString("CHANNEL_ACCESS_ACCOUNT"));
				interfaceInfo.put("CHANNEL_ACCESS_PASSWORD", rs.getString("CHANNEL_ACCESS_PASSWORD"));
				interfaceInfo.put("CHANNEL_SERVICE_URL", rs.getString("CHANNEL_SERVICE_URL"));
				interfaceInfo.put("SP_ID", rs.getString("SP_ID"));
				interfaceInfo.put("SRC_ID", rs.getString("SRC_ID"));
				interfaceInfo.put("BUSINESS_CODE", rs.getString("BUSINESS_CODE"));
				interfaceInfo.put("CONNECT_NUMBER", String.valueOf(rs.getInt("CONNECT_NUMBER")));
				interfaceInfo.put("MAX_SEND_SECOND", String.valueOf(rs.getInt("MAX_SEND_SECOND")));
				interfaceInfo.put("HEARTBEAT_INTERVAL", String.valueOf(rs.getInt("HEARTBEAT_INTERVAL")));
				interfaceInfo.put("PROTOCOL", rs.getString("PROTOCOL"));
				interfaceInfo.put("VERSION", rs.getString("VERSION"));

				channelInfo.setChannelID(channelID);
				channelInfo.setChannelName(channelName);
				channelInfo.setBusinessAreaType(businessAreaType);
				channelInfo.setSupportAreaCodes(supportAreaCodes);
				channelInfo.setChannelStatus(channelStatus);
				channelInfo.setChannelRunStatus(channelRunStatus);
				channelInfo.setPriceStyle(priceStyle);
				channelInfo.setMaxSendSecond(maxSendSecond);
				channelInfo.setConnectNumber(connectNumber);
				channelInfo.setChannelSRCID(secId);
				channelInfo.setInterfaceInfo(interfaceInfo);

				resultMap.put(channelID, channelInfo);
			}

		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(), e);
			return null;
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
		}
		return resultMap;
	}

	class ChannelInfo {
		/**
		 * 通道id
		 */
		private String channelID;
		
		/**
		 * 通道名称
		 */
		private String channelName;

		/**
		 * 通道状态
		 */
		private String channelStatus;

		/**
		 * 通道运行状态
		 */
		private String channelRunStatus;

		/**
		 * 通道速率
		 */
		private int maxSendSecond;

		/**
		 * 通道连接数
		 */
		private int connectNumber;

		/**
		 * 通道接入码
		 */
		private String channelSRCID;

		/**
		 * 通道区域范围:PROVINCE、INTL、COUNTRY
		 */
		private String businessAreaType;

		/**
		 * 计价方式:AREA_PRICE、UNIFIED_PRICE
		 */
		private String priceStyle;

		/**
		 * 支持的区域范围
		 */
		private String supportAreaCodes;

		/**
		 * 通道接口参数
		 */
		private Map<String, String> interfaceInfo = new HashMap<String, String>();

		public String getChannelSRCID() {
			return channelSRCID;
		}

		public void setChannelSRCID(String channelSRCID) {
			this.channelSRCID = channelSRCID;
		}

		public String getChannelID() {
			return channelID;
		}

		public void setChannelID(String channelID) {
			this.channelID = channelID;
		}
		
		public String getChannelName() {
			return channelName;
		}

		public void setChannelName(String channelName) {
			this.channelName = channelName;
		}

		public String getChannelStatus() {
			return channelStatus;
		}

		public void setChannelStatus(String channelStatus) {
			this.channelStatus = channelStatus;
		}

		public String getChannelRunStatus() {
			return channelRunStatus;
		}

		public void setChannelRunStatus(String channelRunStatus) {
			this.channelRunStatus = channelRunStatus;
		}

		public int getMaxSendSecond() {
			return maxSendSecond;
		}

		public void setMaxSendSecond(int maxSendSecond) {
			this.maxSendSecond = maxSendSecond;
		}

		public String getBusinessAreaType() {
			return businessAreaType;
		}

		public void setBusinessAreaType(String businessAreaType) {
			this.businessAreaType = businessAreaType;
		}

		public String getPriceStyle() {
			return priceStyle;
		}

		public void setPriceStyle(String priceStyle) {
			this.priceStyle = priceStyle;
		}

		public String getSupportAreaCodes() {
			return supportAreaCodes;
		}

		public void setSupportAreaCodes(String supportAreaCodes) {
			this.supportAreaCodes = supportAreaCodes;
		}

		public Map<String, String> getInterfaceInfo() {
			return interfaceInfo;
		}

		public void setInterfaceInfo(Map<String, String> interfaceInfo) {
			this.interfaceInfo = interfaceInfo;
		}

		public int getConnectNumber() {
			return connectNumber;
		}

		public void setConnectNumber(int connectNumber) {
			this.connectNumber = connectNumber;
		}

		/**
		 * 获取提交的间隔时间
		 * 
		 * @return
		 */
		public long getSubmitInterval() {
			return (int) Math.floor((double) 1000 * connectNumber / maxSendSecond);
		}

	}

}
