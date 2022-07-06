/**
 * @desc
 * @author ma
 * @date 2017年8月15日
 * 
 */
package com.protocol.proxy.util;

import java.util.Date;
import java.util.Map;

import com.base.common.constant.FixedConstant;
import com.base.common.util.ChannelMTUtil;
import com.base.common.util.LongSMSEncode6;
import com.huawei.insa2.comm.sgip.message.SGIPMessage;
import com.huawei.insa2.comm.sgip.message.SGIPSubmitMessage;

public class SgipUtil extends ChannelMTUtil{
	
	/**
	 * 封装cmpp消息，根据账号编码、通道编码、默认编码确定编码
	 * @param map
	 * @param content
	 * @param mobile
	 * @param spNumber
	 * @param isCMPP20Version
	 * @param messageFormat
	 * @return
	 */
	public static SGIPMessage[] packageSubmit(Map<String, String> map,
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

	private static SGIPMessage[] packageSingleSubmit(Map<String, String> map,
			String content, String mobile, String spNumber,int messageCoding) {
		
		String CorpId = map.get("corpId");
		String ServiceType = map.get("serviceType");
		String FeeType = map.get("feeType");
		int Priority = Integer.valueOf(map.get("priority"));
		int ReportFlag = Integer.valueOf(map.get("reportFlag"));
		int feeUserType = 2;
		if (map.get("feeUserType") != null) {
			feeUserType = Integer.valueOf(map.get("feeUserType"));
		}
		String feeTerminalId = "";
		if(map.get("feeTerminalId") != null){
			feeTerminalId = map.get("feeTerminalId");
		}
		String reserve = "";
		byte[] msgByte = null;
		try {
			msgByte = content.getBytes(charsetMap.get(String.valueOf(messageCoding)));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		int TP_pid = 0;
		int TP_udhi = 0;
		// 存活有效期，格式遵循SMPP3.3协议
		Date Valid_Time = new Date(System.currentTimeMillis()+SMS_VALID_TIME);
		// 定时发送时间
		Date At_Time = null;

		// 被计费用户的号码类型 0：真实号码；1：伪码
		int Fee_Terminal_Type = 0;
		// 接收短信的用户的号码类型，0：真实号码；1：伪码
		int Dest_Terminal_Type = 0;
		SGIPMessage[] submitParamArray = new SGIPMessage[1];
	
		//submitParamArray[0] = new SGIPSubmitMessage(SPNumber, ChargeNumber, UserNumber, CorpId, ServiceType, FeeType, FeeValue, GivenValue, AgentFlag, MorelatetoMTFlag, Priority, ExpireTime, ScheduleTime, ReportFlag, TP_pid, TP_udhi, MessageCoding, MessageType, MessageLen, MessageContent, reserve)
		

		return submitParamArray;
	}
	
	private static SGIPMessage[] packageMultipleSubmit(Map<String, String> map,
			String content, String mobile, String spNumber,int messageCoding) {

		String CorpId = map.get("corpId");
		String ServiceType = map.get("serviceType");
		String FeeType = map.get("feeType");
		int Priority = Integer.valueOf(map.get("priority"));
		int ReportFlag = Integer.valueOf(map.get("reportFlag"));
		int feeUserType = 2;
		if (map.get("feeUserType") != null) {
			feeUserType = Integer.valueOf(map.get("feeUserType"));
		}
		String feeTerminalId = "";
		if(map.get("feeTerminalId") != null){
			feeTerminalId = map.get("feeTerminalId");
		}
		String reserve = "";
		int TP_pid = 0;
		int TP_udhi = 1;
		// 存活有效期，格式遵循SMPP3.3协议
		Date Valid_Time = new Date(System.currentTimeMillis() + SMS_VALID_TIME);
		// 定时发送时间
		Date At_Time = null;

		// 被计费用户的号码类型 0：真实号码；1：伪码
		int Fee_Terminal_Type = 0;
		// 接收短信的用户的号码类型，0：真实号码；1：伪码
		int Dest_Terminal_Type = 0;
		byte[][] longSmsArray = null;
				
		longSmsArray = LongSMSEncode6.enCodeBytes(content,charsetMap.get(String.valueOf(messageCoding)));
		
		SGIPMessage[] submitParamArray = new SGIPMessage[longSmsArray.length];

		for (int i = 0; i < longSmsArray.length; i++) {		
			//submitParamArray[i] = new SGIPSubmitMessage(SPNumber, ChargeNumber, UserNumber, CorpId, ServiceType, FeeType, FeeValue, GivenValue, AgentFlag, MorelatetoMTFlag, Priority, ExpireTime, ScheduleTime, ReportFlag, TP_pid, TP_udhi, MessageCoding, MessageType, MessageLen, MessageContent, reserve)	
		}
		return submitParamArray;
	}
	
}
