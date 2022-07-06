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

import com.base.common.dao.LavenderDBSingleton;
import com.base.common.log.CategoryLog;
import com.base.common.manager.SegmentProvinceCityManager.SystemSegmentProvinceCity;
import com.base.common.worker.SuperMapWorker;

public class SegmentProvinceCityManager extends SuperMapWorker<String, SystemSegmentProvinceCity>{
	
	private static SegmentProvinceCityManager manager = new SegmentProvinceCityManager();
	
	/**
	 * 通过手机号获取省份编码
	 * @param phoneNumber
	 * @return
	 */
	public String getProvinceCode(String phoneNumber){
		SystemSegmentProvinceCity systemSegmentProvinceCity = get(phoneNumber.substring(0,7));
		//避免空指针
		if(systemSegmentProvinceCity != null) {
			return systemSegmentProvinceCity.getProvinceCode();
		}
		return "";
	}

	/**
	 * 通过手机号获取省份名称
	 * @param phoneNumber
	 * @return
	 */
	public String getProvinceName(String phoneNumber){
		SystemSegmentProvinceCity systemSegmentProvinceCity = get(phoneNumber.substring(0,7));
		//避免空指针
		if(systemSegmentProvinceCity != null) {
			return systemSegmentProvinceCity.getProvinceName();
		}
		return "";
	}
	
	/**
	 * 通过手机号回去地市名称
	 * @param phoneNumber
	 * @return
	 */
	public String getCityName(String phoneNumber){
		SystemSegmentProvinceCity systemSegmentProvinceCity = get(phoneNumber.substring(0,7));
		//避免空指针
		if(systemSegmentProvinceCity != null) {
			return systemSegmentProvinceCity.getCityName();
		}
		return "";
	}
	
	private SegmentProvinceCityManager(){
		loadData();
		this.start();
	}
	
	public static SegmentProvinceCityManager getInstance(){
		return manager;
	}
	
	@Override
	public void doRun() throws Exception {
			Thread.sleep(INTERVAL);
			loadData();
	}
	
	/**
	 * 加载数据
	 */
	private void loadData(){
		long startTime = System.currentTimeMillis();
		Map<String, SystemSegmentProvinceCity> resultMap = loadDictionariesProvincesValue();
		if(resultMap != null){
			//将新获取的数据集赋值
			superMap = resultMap;
		}
		
		long endTime = System.currentTimeMillis();
		CategoryLog.commonLogger.info("size={},耗时={}",size(),(endTime-startTime));
	}
	
	/**
	 * 获取号段、省份编码、省份名称和地市区的对应关系数据
	 * 数据库异常时返回null
	 * @return
	 */
	private Map<String, SystemSegmentProvinceCity> loadDictionariesProvincesValue() {
		Map<String, SystemSegmentProvinceCity> resultMap = new HashMap<String,SystemSegmentProvinceCity>();
		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		sql.append("SELECT SEGMENT,PROVINCE_CODE,PROVINCE_NAME,CITY_NAME FROM smoc.system_segment_province_city");
		SystemSegmentProvinceCity systemSegmentProvinceCity = null;
		try {
			conn = LavenderDBSingleton.getInstance().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				systemSegmentProvinceCity = new SystemSegmentProvinceCity();
				systemSegmentProvinceCity.setSegment(rs.getString("SEGMENT"));
				systemSegmentProvinceCity.setProvinceCode(rs.getString("PROVINCE_CODE"));
				systemSegmentProvinceCity.setProvinceName(rs.getString("PROVINCE_NAME"));
				systemSegmentProvinceCity.setCityName(rs.getString("CITY_NAME"));
				resultMap.put(systemSegmentProvinceCity.getSegment(), systemSegmentProvinceCity);
			}

		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(),e);
			return null;
		} finally {
			LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
		}
		return resultMap;
	}
	
	class SystemSegmentProvinceCity {
		/**
		 * 号段
		 */
		private String segment;
		
		/**
		 * 省份编码
		 */
		private String provinceCode;
		
		/**
		 * 省份名称
		 */
		private String provinceName;
		
		/**
		 * 城市/地区
		 */
		private String cityName;

		public String getSegment() {
			return segment;
		}

		public void setSegment(String segment) {
			this.segment = segment;
		}

		public String getProvinceCode() {
			return provinceCode;
		}

		public void setProvinceCode(String provinceCode) {
			this.provinceCode = provinceCode;
		}

		public String getCityName() {
			return cityName;
		}

		public void setCityName(String cityName) {
			this.cityName = cityName;
		}

		public String getProvinceName() {
			return provinceName;
		}

		public void setProvinceName(String provinceName) {
			this.provinceName = provinceName;
		}

		@Override
		public String toString() {
			return "SystemSegmentProvinceCity [segment=" + segment + ", provinceCode=" + provinceCode
					+ ", provinceName=" + provinceName + ", cityName=" + cityName + "]";
		}
		
	}
	
}


