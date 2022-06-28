package com.business.access.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.worker.SuperMapWorker;
import com.business.access.manager.ContentRouteManager.ContentRoute;

public class ContentRouteManager extends SuperMapWorker<String, ContentRoute> {

	private static ContentRouteManager Manager = new ContentRouteManager();

	private ContentRouteManager() {
		loadData();
		this.start();
	}

	public static ContentRouteManager getInstance() {
		return Manager;
	}

	/**
	 * 将路由数据config_content_repair_rule表中的数据加载进内存
	 */
	private void loadData() {
		long startTime = System.currentTimeMillis();
		Map<String, ContentRoute> contentRouteMap = loadContentRoute();
		if (contentRouteMap != null) {
			superMap = contentRouteMap;

		}
		long endTime = System.currentTimeMillis();
		logger.info("size={},耗时={}", size(), (endTime - startTime));
	}

	@Override
	public void doRun() throws Exception {
		Thread.sleep(INTERVAL);
		loadData();
	}

	/**
	 * 根据业务账号，运营商,发送内容,手机号码，业务区域匹配路由通道
	 * 
	 * @param accountID   业务账号
	 * @param carrier     运营商
	 * @param content     发送内容
	 * @param phoneNumber 手机号码
	 * @param areaCode    业务区域编码
	 * @return 通道id
	 */
	public String mapping(String accountID, String carrier, String content, String phoneNumber, String areaCode) {
		if (StringUtils.isNotEmpty(accountID) && StringUtils.isNotEmpty(carrier) && StringUtils.isNotEmpty(content)
				&& StringUtils.isNotEmpty(phoneNumber) && StringUtils.isNotEmpty(areaCode)) {

			// 根据账号和运营商获取该账号的路由信息
			String key = new StringBuffer().append(accountID).append("&").append(carrier).toString();

			if (superMap != null) {
				
				// 获取路由信息
				ContentRoute contentroute = get(key);

				if (contentroute != null) {

					// 匹配路由内容
					if (!match(contentroute.getRapairContent(), content)) {

						return "";
					}
					// 匹配手机号段
					if (StringUtils.isNotEmpty(contentroute.getMobileNum())) {
						if (!match(contentroute.getMobileNum(), phoneNumber)) {

							return "";
						}
					}
					// 匹配短信字数
					if (contentroute.getMinContent() != 0 && contentroute.getMaxContent() != 0) {
						if (!(content.length() < contentroute.getMinContent()
								|| content.length() > contentroute.getMaxContent())) {

							return "";
						}
					} else if (contentroute.getMinContent() != 0 && contentroute.getMaxContent() == 0) {
						if (!(content.length() < contentroute.getMinContent())) {

							return "";
						}
					} else if (contentroute.getMinContent() == 0 && contentroute.getMaxContent() != 0) {
						if (!(content.length() > contentroute.getMaxContent())) {

							return "";
						}
					}

					// 匹配业务区域编码
					if (StringUtils.isNotEmpty(contentroute.getAreaCodes())) {
						if (!equalsAreaCodes(contentroute.getAreaCodes(), areaCode)) {

							return "";
						}
					}
					return contentroute.getChannelRepairID();

				}
				return "";
			}

			return "";
		}

		return "";
	}

	/**
	 * 匹配函数
	 * 
	 * @param regex
	 * @param inputString
	 * @return
	 */
	private boolean match(String regex, String inputString) {
		return Pattern.matches(regex, inputString);
	}

	public boolean equalsAreaCodes(String AreaCodes, String inputString) {
		if ("ALL".equals(AreaCodes)) {
			return true;
		}
		return match(AreaCodes, inputString);
	}

	/**
	 * @return加载数据
	 */
	public Map<String, ContentRoute> loadContentRoute() {
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		sql.append(
				"SELECT ACCOUNT_ID,CARRIER,AREA_CODES,REPAIR_CONTENT,MOBILE_NUM,MIN_CONTENT,MAX_CONTENT,CHANNEL_REPAIR_ID");
		sql.append(" FROM smoc.config_content_repair_rule where REPAIR_STATUS='1'");

		Map<String, ContentRoute> contentRouteMap = new HashMap<String, ContentRoute>();

		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ContentRoute contentroute = new ContentRoute();

				contentroute.setRapairContent(new StringBuffer().append(".*(").append(rs.getString("REPAIR_CONTENT"))
						.append(").*").toString());

				if (StringUtils.isNotEmpty(rs.getString("MOBILE_NUM"))) {
					contentroute.setMobileNum(new StringBuffer().append("^(").append(rs.getString("MOBILE_NUM"))
							.append(")\\d+").toString());
				}

				if (StringUtils.isNotEmpty(rs.getString("AREA_CODES"))) {
					contentroute.setAreaCodes(rs.getString("AREA_CODES").replaceAll(",", "|"));
				}

				contentroute.setMinContent(rs.getInt("MIN_CONTENT"));
				contentroute.setMaxContent(rs.getInt("MAX_CONTENT"));
				contentroute.setChannelRepairID(rs.getString("CHANNEL_REPAIR_ID"));

				if (StringUtils.isNotEmpty(rs.getString("ACCOUNT_ID"))
						&& StringUtils.isNotEmpty(rs.getString("CARRIER"))) {
					String key = new StringBuffer().append(rs.getString("ACCOUNT_ID")).append("&")
							.append(rs.getString("CARRIER")).toString();
					contentRouteMap.put(key, contentroute);
				}

			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
		}
		return contentRouteMap;

	}

	class ContentRoute {

		/**
		 * 省份
		 */
		private String areaCodes;

		/**
		 * 内容
		 */
		private String rapairContent;

		/**
		 * 手机号段
		 */
		private String mobileNum;

		/**
		 * 短信字数小于值
		 */
		private int minContent;

		/**
		 * 短信字数大于值
		 */
		private int maxContent;

		/**
		 * 路由通道ID
		 */
		private String channelRepairID;

		public String getAreaCodes() {
			return areaCodes;
		}

		public void setAreaCodes(String areaCodes) {
			this.areaCodes = areaCodes;
		}

		public String getRapairContent() {
			return rapairContent;
		}

		public void setRapairContent(String rapairContent) {
			this.rapairContent = rapairContent;
		}

		public String getMobileNum() {
			return mobileNum;
		}

		public void setMobileNum(String mobileNum) {
			this.mobileNum = mobileNum;
		}

		public int getMinContent() {
			return minContent;
		}

		public void setMinContent(int minContent) {
			this.minContent = minContent;
		}

		public int getMaxContent() {
			return maxContent;
		}

		public void setMaxContent(int maxContent) {
			this.maxContent = maxContent;
		}

		public String getChannelRepairID() {
			return channelRepairID;
		}

		public void setChannelRepairID(String channelRepairID) {
			this.channelRepairID = channelRepairID;
		}

	}

}
