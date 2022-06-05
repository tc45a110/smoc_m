package com.base.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.base.common.log.CategoryLog;

public class ChannellnfoDAO {	
	/**
	 * 加载通道价格 数据库异常时返回null
	 * @return
	 */
	public static Map<String, Map<String, Double>> loadChannelPrice(){
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		sql.append("SELECT CHANNEL_ID,AREA_CODE,CHANNEL_PRICE FROM smoc.config_channel_price ");
		Map<String, Map<String, Double>> resultMap = new HashMap<String, Map<String, Double>>();

		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String channelId = rs.getString("CHANNEL_ID");
				String areaCode = rs.getString("AREA_CODE");
				double channelPrice = rs.getDouble("CHANNEL_PRICE");
				Map<String, Double> priceMap = resultMap.get(channelId);
				if (priceMap == null) {
					priceMap = new HashMap<String, Double>();
					resultMap.put(channelId, priceMap);
				}
				priceMap.put(areaCode,channelPrice);
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
	 * 通过通道id和业务区域获取价格   数据库异常时返回null
	 * @return
	 */
	public static String loadChannelPrice(String channelId, String areaCode) {
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		sql.append("SELECT CHANNEL_PRICE FROM smoc.config_channel_price where CHANNEL_ID=? and AREA_CODE=?");
	
		String channelPrice=null;
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());			
			pstmt.setString(1, channelId);
			pstmt.setString(2, areaCode);
			rs = pstmt.executeQuery();		
			while (rs.next()) {				
				channelPrice = String.valueOf(rs.getDouble("CHANNEL_PRICE"));
			}

		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(), e);
			return null;
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
		}
		return channelPrice;
	}
	

}
