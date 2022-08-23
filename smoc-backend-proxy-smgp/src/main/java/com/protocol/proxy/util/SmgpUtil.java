package com.protocol.proxy.util;

import java.util.Date;
import java.util.Map;

import com.base.common.constant.FixedConstant;
import com.base.common.util.ChannelMTUtil;
import com.base.common.util.LongSMSEncode6;
import com.huawei.insa2.comm.smgp.message.SMGPMessage;
import com.huawei.insa2.comm.smgp.message.SMGPSubmitMessage;
import com.huawei.insa2.comm.smgp.message.SMGPSubmitTLV;

public class SmgpUtil  extends ChannelMTUtil{
	
	final static int msgType = 6;
	
	final static String fixedFee = "0";
	
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
	public static SMGPMessage[] packageSubmit(Map<String, String> map,
			String content, String mobile, String spNumber,String messageFormat){
		
		int messageCoding = Integer.parseInt(DEFAULT_ENCODING);

		String format = ChannelMTUtil.getMessageCoding(messageFormat, map.get("channelFormat"));
		try {
			messageCoding = Integer.parseInt(format);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
		if(content.length() <= 70){
			return packageSingleSubmit(map,content,mobile,spNumber,messageCoding);
		}
		
		//当编码格式为15，内容长度大于70个字符时，需特殊处理
		if(messageCoding == 15){
			
			int messageByteLength = 0;
			try {
				messageByteLength = content.getBytes(charsetMap.get(String.valueOf(messageCoding))).length;
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
			
			//运营商：长短信不支持通过15编码方式发送
			if(messageByteLength > 140){
				logger.warn(
						new StringBuilder().append("内容和编码不一致：内容字节数大于140，提交编码格式为15")
						.append("{}phoneNumber={}")
						.append("{}messageContent={}")
						.append("{}messageContentLength={}")
						.append("{}messageByteLength={}")
						.toString(),
						FixedConstant.SPLICER,mobile,
						FixedConstant.SPLICER,content,
						FixedConstant.SPLICER,content.length(),
						FixedConstant.SPLICER,messageByteLength
						);
				return packageMultipleSubmit(map,content,mobile,spNumber,Integer.parseInt(DEFAULT_ENCODING));
			}
			
			return packageSingleSubmit(map,content,mobile,spNumber,messageCoding);
		}
		
		return packageMultipleSubmit(map,content,mobile,spNumber,messageCoding);
	}

	public static SMGPMessage[] packageSingleSubmit(Map<String, String> map,
			String content, String mobile, String spNumber, int messageCoding) {
		String serviceType = map.get("serviceType");
		String feeType = map.get("feeType");
		String feeCode = map.get("feeCode");
		int priority = Integer.valueOf(map.get("priority"));
		int reportFlag = Integer.valueOf(map.get("reportFlag"));
		String reserve = "";
		byte[] msgByte = null;
		try {
			msgByte = content.getBytes(charsetMap.get(String.valueOf(messageCoding)));
		} catch (Exception e) {
		}
		// 存活有效期，格式遵循SMPP3.3协议
		Date validTime = new Date(System.currentTimeMillis() + SMS_VALID_TIME);
		// 定时发送时间
		Date atTime = null;

		SMGPMessage[] submitParamArray = new SMGPMessage[1];

		submitParamArray[0] = new SMGPSubmitMessage(msgType, reportFlag,
				priority, serviceType, feeType, feeCode, fixedFee,
				messageCoding, validTime, atTime, spNumber, mobile,
				new String[] { mobile }, msgByte, reserve,new SMGPSubmitTLV());

		return submitParamArray;
	}

	public static SMGPMessage[] packageMultipleSubmit(Map<String, String> map,
			String content, String mobile, String spNumber, int messageCoding) {
		String serviceType = map.get("serviceType");
		String feeType = map.get("feeType");
		String feeCode = map.get("feeCode");
		int priority = Integer.valueOf(map.get("priority"));
		int reportFlag = Integer.valueOf(map.get("reportFlag"));
		String reserve = "";
		// 存活有效期，格式遵循SMPP3.3协议
		Date validTime = new Date(System.currentTimeMillis() + SMS_VALID_TIME);
		// 定时发送时间
		Date atTime = null;
		byte[][] longSmsArray = LongSMSEncode6.enCodeBytes(content,charsetMap.get(String.valueOf(messageCoding)));
		SMGPMessage[] submitParamArray = new SMGPMessage[longSmsArray.length];

		for (int i = 0; i < longSmsArray.length; i++) {
			SMGPSubmitTLV tlv = new SMGPSubmitTLV();
			tlv.b_cUdhi=true;
			tlv.cUdhi=(byte)1;
			tlv.b_cPkTotal= true;
			tlv.cPkTotal=(byte)(longSmsArray.length);
			tlv.b_cPkNumber=true;
			tlv.cPkNumber=(byte)(i+1);
			
			submitParamArray[i] = new SMGPSubmitMessage(msgType, reportFlag,
					priority, serviceType, feeType, feeCode, fixedFee,
					messageCoding, validTime, atTime, spNumber, mobile,
					new String[] { mobile }, longSmsArray[i], reserve,tlv);

		}
		return submitParamArray;
	}

}
