
package com.protocol.proxy.worker;

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
import com.huawei.insa2.comm.sgip.message.SGIPMessage;
import com.huawei.insa2.comm.sgip.message.SGIPReportMessage;

/**
 * 在SP和SMG的通信中，SMG用Deliver命向SP发送一条MO短消息
 */
public class DeliverWorker extends SuperQueueWorker<SGIPMessage>{
	private String channelID;
	
	public DeliverWorker(String channelID,String index) {
		this.channelID = channelID;
		init();
		this.setName(new StringBuilder(channelID).append("-").append(index).toString());
		this.start();
	}
	
	public void process(SGIPMessage message){
		add(message);
	}
	
	/**
	 * 初始化:检查通道表
	 */
	private void init(){
		
	}


	@Override
	protected void doRun() throws Exception {
		SGIPMessage message = take();
		CategoryLog.messageLogger.info(message.toString());
		int registeredDeliver = 0;
		//短信是否包含长短信头的标识
		int tpUdhi=0;
		int messageFormat=0;
		String channelReportSRCID=null;
		String phoneNumber=null;
		byte[] messageContent=null;
		String channelMessageID=null;
		String statusCode=null;
		String sequenceID=null;
		
		sequenceID = String.valueOf(message.getSequenceId());
		
		if(message instanceof SGIPReportMessage){
			SGIPReportMessage reportMessage = (SGIPReportMessage)message;
			phoneNumber = reportMessage.getUserNumber();
			channelMessageID = reportMessage.getSubmitSequenceNumber();
			reportMessage.getErrorCode();
			reportMessage.getState();
		}
		//状态报告
		if(registeredDeliver == 1){
			processChannelReport(statusCode, channelReportSRCID, channelMessageID, phoneNumber,sequenceID);
		}else if(registeredDeliver == 0){
			processChannelMO(tpUdhi, messageFormat, phoneNumber, channelReportSRCID, messageContent,sequenceID);
		}
		
	}
	
	/**
	 * 处理状态报告
	 * @param statusCode
	 * @param channelReportSRCID
	 * @param channelMessageID
	 * @param phoneNumber
	 */
	private void processChannelReport(String statusCode,String channelReportSRCID,String channelMessageID,String phoneNumber,String sequenceID){
		logger.info(
				new StringBuilder().append("回执信息")
				.append("{}channelID={}")
				.append("{}phoneNumber={}")
				.append("{}channelMessageID={}")
				.append("{}channelReportSRCID={}")
				.append("{}statusCode={}")
				.append("{}sequenceID={}")
				.toString(),
				FixedConstant.SPLICER,channelID,
				FixedConstant.SPLICER,phoneNumber,
				FixedConstant.SPLICER,channelMessageID,
				FixedConstant.SPLICER,channelReportSRCID,
				FixedConstant.SPLICER,statusCode,
				FixedConstant.SPLICER,sequenceID
				);
		
		BusinessRouteValue businessRouteValue = new BusinessRouteValue();
		
		businessRouteValue.setChannelID(channelID);
		businessRouteValue.setStatusCode(statusCode);
		businessRouteValue.setSubStatusCode("");
		businessRouteValue.setStatusCodeSource(FixedConstant.StatusReportSource.CHANNEL.name());
		//根据协议层设置或默认的成功状态码判断后设置成功码标识
		if(DynamicConstant.REPORT_SUCCESS_CODE.equals(statusCode)){
			businessRouteValue.setSuccessCode(InsideStatusCodeConstant.SUCCESS_CODE);
		}else{
			businessRouteValue.setSuccessCode(InsideStatusCodeConstant.FAIL_CODE);
		}
		businessRouteValue.setChannelReportSRCID(channelReportSRCID);
		businessRouteValue.setChannelMessageID(channelMessageID);
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
		logger.info(
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
		}else{
			ChannelMOManager.getInstance().add(channelMO);
		}
	
	}
	
	
	public void exit(){
		//停止线程
		super.exit();
	}
	
}


