 /**
d * @desc
 * 从通道表中按照优先级及时间先后获取数据，每次按照通道的速率进行获取，存入到队列中
 */
package com.protocol.proxy.worker;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.druid.util.Base64;
import com.base.common.cache.CacheBaseService;
import com.base.common.constant.DynamicConstant;
import com.base.common.constant.FixedConstant;
import com.base.common.constant.InsideStatusCodeConstant;
import com.base.common.log.CategoryLog;
import com.base.common.manager.ChannelInfoManager;
import com.base.common.manager.ChannelMOManager;
import com.base.common.manager.LongSMSMOManager;
import com.base.common.util.ChannelMOUtil;
import com.base.common.util.DateUtil;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.vo.ChannelMO;
import com.base.common.worker.SuperQueueWorker;
import com.huawei.insa2.comm.smpp.message.SMPPDeliverMessage;
import com.huawei.insa2.comm.smpp.message.SMPPMessage;
import com.protocol.proxy.util.HexUtil;

public class ReportWorker extends SuperQueueWorker<SMPPMessage>{
	private String channelID;
	
	public ReportWorker(String channelID,String index) {
		this.channelID = channelID;
		init();
		//先判断表是否存在，初始化时会建表
		this.setName(new StringBuilder("ReportWorker-").append(channelID).append("-").append(index).toString());
		this.start();
	}
	
	public void process(SMPPMessage message){
		add(message);
	}
	
	/**
	 * 初始化:检查通道表
	 */
	private void init(){
		
	}


	@Override
	protected void doRun() throws Exception {
		SMPPMessage message = poll();
		if(message == null){
			return;
		}
		CategoryLog.messageLogger.info(message.toString());
		int isDeliver = 0;
		//短信是否包含长短信头的标识
		int tpUdhi=0;
		int messageFormat=0;
		String channelReportSRCID=null;
		String phoneNumber=null;
		String subStatusCode = null;
		byte[] messageContent =null;
		byte[] channelMessageID=null;
		String statusCode=null;
		String sequenceID=null;
		
		sequenceID = String.valueOf(message.getSequenceId());
		
		SMPPDeliverMessage deliverMessage = (SMPPDeliverMessage)message;
		logger.info("处理状态报告：{}",deliverMessage.toString());
		isDeliver = deliverMessage.getEsmClass();
		messageContent = deliverMessage.getShortMessage();
		
		//状态报告
		if(isDeliver == 4){
			messageFormat =  deliverMessage.getDataCoding();
			//状态报告 source_address为发送手机号
			phoneNumber = deliverMessage.getSourceAddr();
			channelReportSRCID = deliverMessage.getDestinationAddr();
			
			Map<String,String> deliverParamsMap = getDeliverParams(messageContent);
			logger.info("deliverParamsMap={}",deliverParamsMap);
			channelMessageID = deliverMessage.getMessageId();
			statusCode = deliverParamsMap.get("stat");
			subStatusCode = deliverParamsMap.get("err");
			processChannelReport(statusCode, subStatusCode, channelReportSRCID, channelMessageID, phoneNumber,sequenceID);
		}else if(isDeliver == 0){
			messageFormat =  deliverMessage.getDataCoding();
			//状态报告 destination_address为发送手机号
			phoneNumber = deliverMessage.getDestinationAddr();
			channelReportSRCID = deliverMessage.getSourceAddr();
			
			processChannelMO(tpUdhi, messageFormat, phoneNumber, channelReportSRCID, messageContent,sequenceID);
		}
		
	}
	public static Map<String,String> getDeliverParams(byte [] messageContent){
		String content = new String(messageContent);
		Map<String,String> resultMap = new HashMap<String, String>();
		if(StringUtils.isEmpty(content)) {
			return resultMap;
		}
		for(String s : content.split(" ")) {
			if(StringUtils.isNotEmpty(s)) {
				String [] arrStr = s.split(":");
				if(arrStr.length == 1) {
					resultMap.put(s.split(":")[0].toLowerCase(), "");
				}else if(arrStr.length == 2){
					resultMap.put(s.split(":")[0].toLowerCase(), s.split(":")[1]);
				}
			}
		}
		return resultMap;
	}
	
	public byte[] getMsgID(byte[] messageContent) {
		byte[] msgid = new byte[9];
		System.arraycopy(messageContent, 3, msgid, 0, msgid.length);
		return msgid;
	}
	
	public String getStat(byte[] messageContent) {
		byte[] stat_b = new byte[7];
		System.arraycopy(messageContent, 88, stat_b, 0, stat_b.length);
		String stat = new String(stat_b);
		return stat;
	}
	
	public String getErr(byte[] messageContent) {
		byte[] stat_b = new byte[3];
		System.arraycopy(messageContent, 100, stat_b, 0, stat_b.length);
		String stat = new String(stat_b);
		return stat;
	}
	
	public Map<String,String> getParams(String content){
		Map<String,String> paramsMap = new HashMap<String, String>();
		String[] array = content.split(" ");
		for(String s : array){
			String[] params = s.split(":");
			if(params.length >= 2){
				paramsMap.put(params[0], params[1]);
			}
		}
		return paramsMap;
	}
	
	/**
	 * 处理状态报告
	 * @param statusCode
	 * @param channelReportSRCID
	 * @param channelMessageID
	 * @param phoneNumber
	 */
	private void processChannelReport(String statusCode, String subStatusCode,String channelReportSRCID,byte[] channelMessageID,String phoneNumber,String sequenceID){
		logger.info(
				new StringBuilder().append("回执信息")
				.append("{}channelID={}")
				.append("{}phoneNumber={}")
				.append("{}channelMessageID={}")
				.append("{}channelReportSRCID={}")
				.append("{}statusCode={}")
				.append("{}subStatusCode={}")
				.append("{}sequenceID={}")
				.toString(),
				FixedConstant.SPLICER,channelID,
				FixedConstant.SPLICER,phoneNumber,
				FixedConstant.SPLICER,HexUtil.byteToHex(channelMessageID),
				FixedConstant.SPLICER,channelReportSRCID,
				FixedConstant.SPLICER,statusCode,
				FixedConstant.SPLICER,subStatusCode,
				FixedConstant.SPLICER,sequenceID
				);
		
		BusinessRouteValue businessRouteValue = new BusinessRouteValue();
		
		businessRouteValue.setChannelID(channelID);
		businessRouteValue.setStatusCode(statusCode);
		businessRouteValue.setSubStatusCode(subStatusCode);
		businessRouteValue.setStatusCodeSource(FixedConstant.StatusReportSource.CHANNEL.name());
		//根据协议层设置或默认的成功状态码判断后设置成功码标识
		if(DynamicConstant.REPORT_SUCCESS_CODE.equals(statusCode)){
			businessRouteValue.setSuccessCode(InsideStatusCodeConstant.SUCCESS_CODE);
		}else{
			businessRouteValue.setSuccessCode(InsideStatusCodeConstant.FAIL_CODE);
		}
		businessRouteValue.setChannelReportSRCID(channelReportSRCID);
		businessRouteValue.setChannelMessageID(HexUtil.byteToHex(channelMessageID));
		businessRouteValue.setPhoneNumber(phoneNumber);
		businessRouteValue.setChannelReportTime(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_MILLI));
		
		CacheBaseService.saveReportToMiddlewareCache(businessRouteValue);
	}
	
	/**
	 * 处理上行信息
	 * @param tpUdhi
	 * @param messageFormat
	 * @param phoneNumber
	 * @param channelMOSRCID
	 * @param messageContent
	 */
	private void processChannelMO(int tpUdhi,int messageFormat,String phoneNumber,String channelMOSRCID,byte[] messageContent,String sequenceID){
		logger.info("上行信息{}{}{}{}{}{}{}{}{}{}{}{}{}{}",
				new StringBuilder().append("上行信息")
				.append("{}channelID={}")
				.append("{}phoneNumber={}")
				.append("{}channelMOSRCID={}")
				.append("{}messageContentBase64={}")
				.append("{}tpUdhi={}")
				.append("{}messageFormat={}")
				.append("{}sequenceID={}")
				.toString(),
				FixedConstant.SPLICER,channelID,
				FixedConstant.SPLICER,phoneNumber,
				FixedConstant.SPLICER,channelMOSRCID,
				FixedConstant.SPLICER,Base64.byteArrayToBase64(messageContent),
				FixedConstant.SPLICER,tpUdhi,
				FixedConstant.SPLICER,messageFormat,
				FixedConstant.SPLICER,sequenceID
				);
		
		String channelSRCID = ChannelInfoManager.getInstance().getChannelSRCID(channelID);
		ChannelMO channelMO = ChannelMOUtil.getMO(tpUdhi, messageFormat, phoneNumber, channelMOSRCID, messageContent,channelID,channelSRCID);
		//长短信需要进行合成处理
		if(channelMO.getMessageTotal() > 1){
			LongSMSMOManager.getInstance().add(channelMO);
		}else {
			ChannelMOManager.getInstance().put(channelMO.getBusinessMessageID(), channelMO);
		}
	}
	
	
	public void exit(){
		//停止线程
		super.exit();
	}
	
}


