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
		int FeeType = Integer.parseInt(map.get("feeType"));
		String FeeValue = map.get("feeValue");
		String GivenValue = map.get("givenValue");
		int AgentFlag = Integer.valueOf(map.get("agentFlag"));
		int MorelatetoMTFlag = Integer.valueOf(map.get("morelatetoMTFlag"));
		int Priority = Integer.valueOf(map.get("priority"));
		Date ExpireTime = null;
		Date ScheduleTime = null;
		int ReportFlag = Integer.valueOf(map.get("reportFlag"));
		String reserve = "";
		int TP_pid = 0;
		int TP_udhi = 0;
		int MessageType = Integer.valueOf(map.get("messageType"));
		String ChargeNumber = map.get("chargeNumber");
		
		byte[] msgByte = null;
		try {
			msgByte = content.getBytes(charsetMap.get(String.valueOf(messageCoding)));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}

		SGIPMessage[] submitParamArray = new SGIPMessage[1];
	
		submitParamArray[0] = new SGIPSubmitMessage(spNumber, ChargeNumber, new String[]{mobile}, CorpId, ServiceType, FeeType, FeeValue, GivenValue, AgentFlag, MorelatetoMTFlag, Priority, ExpireTime, ScheduleTime, ReportFlag, TP_pid, TP_udhi, messageCoding, MessageType, msgByte.length, msgByte, reserve);
		

		return submitParamArray;
	}
	
	private static SGIPMessage[] packageMultipleSubmit(Map<String, String> map,
			String content, String mobile, String spNumber,int messageCoding) {

		String CorpId = map.get("corpId");
		String ServiceType = map.get("serviceType");
		int FeeType = Integer.parseInt(map.get("feeType"));
		String FeeValue = map.get("feeValue");
		String GivenValue = map.get("givenValue");
		int AgentFlag = Integer.valueOf(map.get("agentFlag"));
		int MorelatetoMTFlag = Integer.valueOf(map.get("morelatetoMTFlag"));
		int Priority = Integer.valueOf(map.get("priority"));
		Date ExpireTime = null;
		Date ScheduleTime = null;
		int ReportFlag = Integer.valueOf(map.get("reportFlag"));
		String reserve = "";
		int TP_pid = 0;
		int TP_udhi = 1;
		int MessageType = Integer.valueOf(map.get("messageType"));
		String ChargeNumber = map.get("chargeNumber");
		
		byte[][] longSmsArray = LongSMSEncode6.enCodeBytes(content,charsetMap.get(String.valueOf(messageCoding)));
		
		SGIPMessage[] submitParamArray = new SGIPMessage[longSmsArray.length];

		for (int i = 0; i < longSmsArray.length; i++) {		
			submitParamArray[i] = new SGIPSubmitMessage(spNumber, ChargeNumber, new String[]{mobile}, CorpId, ServiceType, FeeType, FeeValue, GivenValue, AgentFlag, MorelatetoMTFlag, Priority, ExpireTime, ScheduleTime, ReportFlag, TP_pid, TP_udhi, messageCoding, MessageType, longSmsArray[i].length, longSmsArray[i], reserve);	
		}
		return submitParamArray;
	}
	
}
