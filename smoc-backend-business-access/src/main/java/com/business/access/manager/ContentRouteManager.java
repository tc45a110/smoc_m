package com.business.access.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.collections4.CollectionUtils;
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
					// 匹配内容包含
					if (!match(contentroute.getRouteContent(), content)) {
						return "";
					}

					// 匹配内容不包含
					if (StringUtils.isNotEmpty(contentroute.getRouteReverseContent())) {
						if (match(contentroute.getRouteReverseContent(), content)) {
							return "";
						}
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
								&& content.length() > contentroute.getMaxContent())) {
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
										
						if (!match(contentroute.getAreaCodes(), areaCode)) {
							return "";
						}
									
					return contentroute.getChannelID();

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
	public boolean match(String regex, String inputString) {
		return Pattern.matches(regex, inputString);
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
				"SELECT a.ACCOUNT_ID,a.CARRIER,a.AREA_CODES,a.ROUTE_CONTENT,a.ROUTE_REVERSE_CONTENT,a.MOBILE_NUM,a.MIN_CONTENT,a.MAX_CONTENT,");
		sql.append(
				"a.CHANNEL_ID,b.SUPPORT_AREA_CODES FROM smoc.config_route_content_rule a join smoc.config_channel_basic_info b  ");
		sql.append("on a.CHANNEL_ID=b.CHANNEL_ID where ROUTE_STATUS='1' and CHANNEL_STATUS='001'");

		Map<String, ContentRoute> contentRouteMap = new HashMap<String, ContentRoute>();

		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ContentRoute contentRoute = new ContentRoute();
				
                //路由内容包含
				contentRoute.setRouteContent(new StringBuffer().append(".*(").append(rs.getString("ROUTE_CONTENT"))
						.append(").*").toString());
				
				//路由内容不包含
				if (StringUtils.isNotEmpty(rs.getString("ROUTE_REVERSE_CONTENT"))) {
					contentRoute.setRouteReverseContent(new StringBuffer().append(".*(")
							.append(rs.getString("ROUTE_REVERSE_CONTENT")).append(").*").toString());
				}
				
				//路由手机号段
				if (StringUtils.isNotEmpty(rs.getString("MOBILE_NUM"))) {
					contentRoute.setMobileNum(new StringBuffer().append("^(").append(rs.getString("MOBILE_NUM"))
							.append(")\\d+").toString());
				}

				//路由业务区域
				//路由业务区域勾选
				if (StringUtils.isNotEmpty(rs.getString("AREA_CODES"))) {
					List<String> listA = Arrays.asList(rs.getString("AREA_CODES").split(","));
					List<String> listB = Arrays.asList(rs.getString("SUPPORT_AREA_CODES").split(","));

					contentRoute.setAreaCodes(StringUtils.join(CollectionUtils.intersection(listA, listB), "|"));
					//路由业务区域不勾选
				} else if (StringUtils.isEmpty(rs.getString("AREA_CODES"))) {
					contentRoute.setAreaCodes(rs.getString("SUPPORT_AREA_CODES").replaceAll(",", "|"));
				}

				contentRoute.setMinContent(rs.getInt("MIN_CONTENT"));
				contentRoute.setMaxContent(rs.getInt("MAX_CONTENT"));
				contentRoute.setChannelID(rs.getString("CHANNEL_ID"));

				if (StringUtils.isNotEmpty(rs.getString("ACCOUNT_ID"))
						&& StringUtils.isNotEmpty(rs.getString("CARRIER"))) {
					String key = new StringBuffer().append(rs.getString("ACCOUNT_ID")).append("&")
							.append(rs.getString("CARRIER")).toString();
					contentRouteMap.put(key, contentRoute);
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
		 * 内容包含
		 */
		private String routeContent;

		/**
		 * 内容不包含
		 */
		private String routeReverseContent;

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
		private String channelID;

		public String getAreaCodes() {
			return areaCodes;
		}

		public void setAreaCodes(String areaCodes) {
			this.areaCodes = areaCodes;
		}

		public String getRouteContent() {
			return routeContent;
		}

		public void setRouteContent(String routeContent) {
			this.routeContent = routeContent;
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

		public String getChannelID() {
			return channelID;
		}

		public void setChannelID(String channelID) {
			this.channelID = channelID;
		}

		public String getRouteReverseContent() {
			return routeReverseContent;
		}

		public void setRouteReverseContent(String routeReverseContent) {
			this.routeReverseContent = routeReverseContent;
		}

	}
}
