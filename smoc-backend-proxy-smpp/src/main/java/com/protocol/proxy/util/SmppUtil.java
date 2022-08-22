package com.protocol.proxy.util;

import java.util.Arrays;
import java.util.Map;

import com.huawei.insa2.comm.smpp.message.SMPPMessage;
import com.huawei.insa2.comm.smpp.message.SMPPSubmitMessage;


public class SmppUtil extends ChannelMTUtil{
	
	/**
	 * 封装smgp消息，根据账号编码、通道编码、默认编码确定编码
	 * @param map
	 * @param content
	 * @param mobile
	 * @param spNumber
	 * @param isCMPP20Version
	 * @param messageFormat
	 * @return
	 */
	public static SMPPMessage[] packageSubmit(Map<String, String> map,
			String content, String mobile, String spNumber, String accountID){
		
		
		//默认编码1
		byte messageFormat = Byte.valueOf(DEFAULT_ENCODING);
		boolean isSupport7Bit = false;
		try {
			if(map.get("channelFormat") != null) {
				messageFormat = Byte.valueOf(map.get("channelFormat"));
			}
			if(map.get("support7Bit") != null) {
				isSupport7Bit = Boolean.valueOf(map.get("support7Bit"));
			}
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}

		if(isPureASCII(content)){
			//纯ACSII内容并且通道需要7bit，则将编码调整为0
			if(isSupport7Bit) {
				messageFormat = 0;
			}
			if(content.length() <= 160){
				return packageSingleSubmit(map, content, mobile, spNumber, messageFormat, isSupport7Bit);
			}else{
				return packageMultipleSubmit(map, content, mobile, spNumber, messageFormat, isSupport7Bit);
			}
		}else {
			//非纯ascii码
			messageFormat = 8;
			//编码
			byte[] msg = null;
			try {
				msg = content.getBytes(charsetMap.get(String.valueOf(messageFormat)));
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			//按照字节数组的长度判断
			if(msg.length <= 140){
				return packageSingleSubmit(map, content, mobile, spNumber, messageFormat, isSupport7Bit);
			}else{
				return packageMultipleSubmit(map, content, mobile, spNumber, messageFormat, isSupport7Bit);
			}
		}
	}

	public static SMPPMessage[] packageSingleSubmit(Map<String, String> map,
			String content, String mobile, String spNumber, byte messageCoding, boolean isSupport7Bit) {
		//对于普通短信该值为0
		byte esmClass = 0;
		
		String serviceType = "";
		if(map.get("ServiceType") != null){
			serviceType = map.get("ServiceType");
		}
		
		byte sourceAddrTon = 0;
		if(map.get("sourceAddrTon") != null){
			sourceAddrTon = Byte.parseByte(map.get("sourceAddrTon"));
		}
		
		byte sourceAddrNpi = 0;
		if(map.get("sourceAddrNpi") != null){
			sourceAddrNpi = Byte.parseByte(map.get("sourceAddrNpi"));
		}
		
		String sourceAddr = "";
		if(map.get("sourceAddr") != null){
			sourceAddr = map.get("sourceAddr");
		}
		
		byte destAddrTon = 0;
		if(map.get("destAddrTon") != null){
			destAddrTon = Byte.parseByte(map.get("destAddrTon"));
		}
		
		byte destAddrNpi = 0;
		if(map.get("destAddrNpi") != null){
			destAddrNpi = Byte.parseByte(map.get("destAddrNpi"));
		}
		
		byte protocolId = 0;
		if(map.get("protocolId") != null){
			protocolId = Byte.parseByte(map.get("protocolId"));
		}
		
		byte priorityFlag = 0;
		if(map.get("priorityFlag") != null){
			priorityFlag = Byte.parseByte(map.get("priorityFlag"));
		}
		
		String scheduleDeliveryTime = "";
		String validityPeriod = "";
		
		byte registeredDelivery = 1;
		if(map.get("registeredDelivery") != null){
			registeredDelivery = Byte.parseByte(map.get("registeredDelivery"));
		}
		
		byte replaceIfPresentFlag = 0;
		if(map.get("replaceIfPresentFlag") != null){
			replaceIfPresentFlag = Byte.parseByte(map.get("replaceIfPresentFlag"));
		}

		byte smDefaultMsgId = 0;
		if(map.get("smDefaultMsgId") != null){
			smDefaultMsgId = Byte.parseByte(map.get("smDefaultMsgId"));
		}

		SMPPMessage[] submitParamArray = new SMPPMessage[1];
		
		byte[] msg = null;
		try {
			if(isSupport7Bit){
				msg = LongSMSEncode6.gsm7BitEncode(content);
			}else{
				msg = content.getBytes(charsetMap.get(String.valueOf(messageCoding)));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
		byte length = (byte)msg.length;
		
		submitParamArray[0] = new SMPPSubmitMessage(serviceType, sourceAddrTon,
				sourceAddrNpi, sourceAddr, destAddrTon, destAddrNpi, 
				mobile, esmClass, protocolId, priorityFlag, 
				scheduleDeliveryTime, validityPeriod, registeredDelivery, 
				replaceIfPresentFlag, messageCoding, smDefaultMsgId,length,msg);

		return submitParamArray;
	}

	public static SMPPMessage[] packageMultipleSubmit(Map<String, String> map,
			String content, String mobile, String spNumber, byte messageCoding, boolean isSupport7Bit) {

		int headLength = 6;
		if(map.get("headLength")!=null && map.get("headLength").length() > 0){
			headLength = Integer.parseInt(map.get("headLength"));
		}
		
		String serviceType = "";
		if(map.get("ServiceType") != null){
			serviceType = map.get("ServiceType");
		}
		
		byte sourceAddrTon = 0;
		if(map.get("sourceAddrTon") != null){
			sourceAddrTon = Byte.parseByte(map.get("sourceAddrTon"));
		}
		
		byte sourceAddrNpi = 0;
		if(map.get("sourceAddrNpi") != null){
			sourceAddrTon = Byte.parseByte(map.get("sourceAddrNpi"));
		}
		
		String sourceAddr = "";
		if(map.get("sourceAddr") != null){
			serviceType = map.get("sourceAddr");
		}
		
		byte destAddrTon = 0;
		if(map.get("destAddrTon") != null){
			sourceAddrTon = Byte.parseByte(map.get("destAddrTon"));
		}
		
		byte destAddrNpi = 0;
		if(map.get("destAddrNpi") != null){
			sourceAddrTon = Byte.parseByte(map.get("destAddrNpi"));
		}
		//长短信默认为64
		
		byte esmClass = 64;
		if(map.get("esmClass") != null){
			esmClass = Byte.parseByte(map.get("esmClass"));
		}
		
		byte protocolId = 0;
		if(map.get("protocolId") != null){
			sourceAddrTon = Byte.parseByte(map.get("protocolId"));
		}
		
		byte priorityFlag = 0;
		if(map.get("priorityFlag") != null){
			sourceAddrTon = Byte.parseByte(map.get("priorityFlag"));
		}
		
		String scheduleDeliveryTime = "";
		String validityPeriod = "";
		
		byte registeredDelivery = 1;
		if(map.get("registeredDelivery") != null){
			sourceAddrTon = Byte.parseByte(map.get("registeredDelivery"));
		}
		
		byte replaceIfPresentFlag = 0;
		if(map.get("replaceIfPresentFlag") != null){
			sourceAddrTon = Byte.parseByte(map.get("replaceIfPresentFlag"));
		}
		
		if(map.get("replaceIfPresentFlag") != null){
			sourceAddrTon = Byte.parseByte(map.get("replaceIfPresentFlag"));
		}
		
		byte smDefaultMsgId = 0;
		if(map.get("smDefaultMsgId") != null){
			sourceAddrTon = Byte.parseByte(map.get("smDefaultMsgId"));
		}
		byte[][] longSmsArray = null;
		if (isSupport7Bit) {
			longSmsArray = LongSMSEncode6.gsmEncodeBytes(content, lengthMap.get(messageCoding), headLength);
		} else {
			longSmsArray = LongSMSEncode6.enCodeBytes(content, charsetMap.get(String.valueOf(messageCoding)),
					lengthMap.get(messageCoding), headLength);
		}
	
		SMPPMessage[] submitParamArray = new SMPPMessage[longSmsArray.length];

		for (int i = 0; i < longSmsArray.length; i++) {
			logger.debug(longSmsArray[i].length+":"+Arrays.toString(longSmsArray[i]));
			byte length = (byte)(longSmsArray[i].length);
			
			submitParamArray[i] = new SMPPSubmitMessage(serviceType, sourceAddrTon,
					sourceAddrNpi, sourceAddr, destAddrTon, destAddrNpi, 
					mobile, esmClass, protocolId, priorityFlag, 
					scheduleDeliveryTime, validityPeriod, registeredDelivery, 
					replaceIfPresentFlag, messageCoding, smDefaultMsgId,length, longSmsArray[i]);

		}
		return submitParamArray;
	}
	
	
	private static String CHARSET_ASCII = "US-ASCII";
	
	private static boolean isPureASCII (String content){
		try {
			if(content.equals(new String(content.getBytes(CHARSET_ASCII),CHARSET_ASCII)))
				return true;
		} catch (Exception e) {
		}
		return false;
	}

}
