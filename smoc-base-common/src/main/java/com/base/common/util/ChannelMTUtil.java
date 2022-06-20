/**
 * @desc
 * 
 */
package com.base.common.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelMTUtil {
	protected static final Logger logger = LoggerFactory.getLogger(ChannelMTUtil.class);
	
	protected static long SMS_VALID_TIME = 0xa4cb800;
	
	//默认编码
	protected final static String DEFAULT_ENCODING = "8";
	
	protected static Map<String,String> charsetMap = new HashMap<String, String>();
	
	static{
		charsetMap.put("0", "ASCII");
		charsetMap.put("15", "GBK");
		charsetMap.put("9", "UTF-16BE");
		charsetMap.put("8", "UTF-16BE");
		charsetMap.put("24", "UTF-16BE");
	}
	
	private final static int SPNUMBER_LENGTH = 20;
	
	/**
	 * 标准协议需要通过客户的编码格式获取发送编码格式
	 * @param msgFormat
	 * @return
	 */
	public static String getMessageCoding(String messageFormat,String channelFormat){
		//如果通道指定了编码格式，按照通道的编码格式进行编码
		if(StringUtils.isNotEmpty(channelFormat)){
			return channelFormat;
		}
		//如果客户编码格式符合要求，则客户的编码格式
		if(StringUtils.isNotEmpty(messageFormat) && charsetMap.containsKey(messageFormat)){
			return messageFormat;
		}
		return DEFAULT_ENCODING;
	}
	
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


