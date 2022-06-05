/**
 * @desc
 * 
 */
package com.base.common.util;

import java.util.HashMap;
import java.util.Map;

public class ChannelMTUtil {
	protected static long SMS_VALID_TIME = 0xa4cb800;
	
	//默认编码
	protected final static int DEFAULT_ENCODING = 8;
	
	protected static Map<Integer,String> charsetMap = new HashMap<Integer, String>();
	
	static{
		charsetMap.put(15, "GBK");
		charsetMap.put(9, "UTF-16BE");
		charsetMap.put(8, "UTF-16BE");
		charsetMap.put(24, "UTF-16BE");
	}
	
	private final static int SPNUMBER_LENGTH = 20;
	
	// 防止spNumber长度超过20位
	private static String adaptationSPNumber(String spNumber,String length) {
		if(length!=null && length.length() >0){
			return adaptationSPNumber(spNumber,Integer.parseInt(length));
		}
		return adaptationSPNumber(spNumber,SPNUMBER_LENGTH);
	}
	
	private static String adaptationSPNumber(String spNumber,int length) {
		if (spNumber != null && spNumber.length() > length) {
			return spNumber.substring(0, length);
		}
		return spNumber;
	}
	
	/**
	 * 获取下发到通道的码号
	 * @param map
	 * @param extCode
	 * @return
	 */
	public static String getChannelSubmitSRCID(Map<String, String> map,String extCode){
		String spNumber = map.get("srcId");
		if (extCode != null && extCode.length() > 0) {
			spNumber = spNumber.concat(extCode);
		}
		spNumber = adaptationSPNumber(spNumber,map.get("srcIdLength"));
		return spNumber;
	}
	
}


