/**
 * @desc
 * 
 */
package com.protocol.proxy.worker;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;

import com.base.common.cache.CacheBaseService;
import com.base.common.constant.DynamicConstant;
import com.base.common.constant.FixedConstant;
import com.base.common.constant.InsideStatusCodeConstant;
import com.base.common.manager.ChannelInfoManager;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperMapWorker;
import com.huawei.insa2.comm.cmpp.message.CMPPMessage;
import com.huawei.insa2.comm.cmpp.message.CMPPSubmitRepMessage;
import com.huawei.insa2.comm.cmpp30.message.CMPP30SubmitRepMessage;
import com.huawei.insa2.util.TypeConvert;

public class ResponseWorker extends SuperMapWorker<Integer, TimerTask>{
	
	private String channelID;
	private Timer timer;
	
	private BlockingQueue<CMPPMessage> responseQueue;
	
	

	ResponseWorker(String channelID,String index,BlockingQueue<CMPPMessage> responseQueue) {
		this.responseQueue = responseQueue;
		timer = new Timer();
		this.setName(new StringBuilder(channelID).append("-").append(index).toString());
		this.start();
	}
	
	/**
	 * 设置响应超时任务
	 * @param sequenceID
	 * @param businessRouteValue
	 */
	void setResponseTimeoutTask(int sequenceID,BusinessRouteValue businessRouteValue){
		TimerTask responseTimeoutTask = new ResponseTimeoutTask(sequenceID,businessRouteValue);
		add(sequenceID, responseTimeoutTask);
		//长链接：获取提交等待响应的超时时间，一般是通用时间，可以根据单个通道在通道扩展参数中设置responseTimeout
		long delay = ChannelInfoManager.getInstance().getResponseTimeout(channelID);
		timer.schedule(responseTimeoutTask, delay);
	}
	
	/**
	 * 判断未知响应是否超过滑动窗口大小
	 * @return
	 */
	boolean overGlideWindow(){
		return size() >= ChannelInfoManager.getInstance().getGlideWindowSize(channelID);
	}

	@Override
	protected void doRun() throws Exception {
		CMPPMessage message = responseQueue.take();
		int sequenceID = message.getSequenceId();
		ResponseTimeoutTask responseTimeoutTask = (ResponseTimeoutTask)remove(sequenceID);
		
		if(responseTimeoutTask == null){
	
			logger.info(new StringBuilder().append("响应信息")
					.append("{}sequenceID={}")
					.toString(),
					FixedConstant.SPLICER,sequenceID
					);
			return;
		}
		responseTimeoutTask.cancel();
		BusinessRouteValue businessRouteValue = responseTimeoutTask.getBusinessRouteValue();

		byte[] channelMessageID = null;
		String nextNodeErrorCode = "";
		if(message instanceof CMPPSubmitRepMessage){
			CMPPSubmitRepMessage responseMessage = (CMPPSubmitRepMessage) message;
			channelMessageID = responseMessage.getMsgId();
			nextNodeErrorCode = String.valueOf(responseMessage.getResult());
		}else if(message instanceof CMPP30SubmitRepMessage){
			CMPP30SubmitRepMessage responseMessage = (CMPP30SubmitRepMessage) message;
			channelMessageID = responseMessage.getMsgId();
			nextNodeErrorCode = String.valueOf(responseMessage.getResult());
		}
		//记录msgid、提交成功标识、提交响应码
		businessRouteValue.setChannelMessageID(String.valueOf(TypeConvert.byte2long(channelMessageID)));
		businessRouteValue.setNextNodeErrorCode(nextNodeErrorCode);
		
		if(DynamicConstant.RESPONSE_SUCCESS_CODE.equals(nextNodeErrorCode)){
			businessRouteValue.setNextNodeCode(InsideStatusCodeConstant.SUCCESS_CODE);
		}else{
			businessRouteValue.setNextNodeCode(InsideStatusCodeConstant.FAIL_CODE);
		}
		logger.info(
				new StringBuilder().append("响应信息")
				.append("{}accountID={}")
				.append("{}phoneNumber={}")
				.append("{}messageContent={}")
				.append("{}channelID={}")
				.append("{}channelTotal={}")
				.append("{}channelIndex={}")
				.append("{}sequenceID={}")
				.append("{}channelMessageID={}")
				.append("{}responseCode={}")
				.toString(),
				FixedConstant.SPLICER,businessRouteValue.getAccountID(),
				FixedConstant.SPLICER,businessRouteValue.getPhoneNumber(),
				FixedConstant.SPLICER,businessRouteValue.getMessageContent(),
				FixedConstant.SPLICER,businessRouteValue.getChannelID(),
				FixedConstant.SPLICER,businessRouteValue.getChannelTotal(),
				FixedConstant.SPLICER,businessRouteValue.getChannelIndex(),
				FixedConstant.SPLICER,sequenceID,
				FixedConstant.SPLICER,businessRouteValue.getChannelMessageID(),
				FixedConstant.SPLICER,businessRouteValue.getNextNodeErrorCode()
				);
		CacheBaseService.saveResponseToMiddlewareCache(businessRouteValue);
	}
	
	public void exit(){
		super.exit();
	}
	
	/**
	 * 在设置的超时时间内没有
	 */
	class ResponseTimeoutTask extends TimerTask {

		private int sequenceID;
		private BusinessRouteValue businessRouteValue;

		public ResponseTimeoutTask(int sequenceID,BusinessRouteValue businessRouteValue) {
			super();
			this.sequenceID = sequenceID;
			this.businessRouteValue = businessRouteValue;
		}

		@Override
		public void run() {
			try {
				remove(sequenceID);
				logger.info(
						new StringBuilder().append("提交无响应")
						.append("{}accountID={}")
						.append("{}phoneNumber={}")
						.append("{}messageContent={}")
						.append("{}channelID={}")
						.append("{}channelTotal={}")
						.append("{}channelIndex={}")
						.append("{}sequenceID={}")
						.toString(),
						FixedConstant.SPLICER,businessRouteValue.getAccountID(),
						FixedConstant.SPLICER,businessRouteValue.getPhoneNumber(),
						FixedConstant.SPLICER,businessRouteValue.getMessageContent(),
						FixedConstant.SPLICER,businessRouteValue.getChannelID(),
						FixedConstant.SPLICER,businessRouteValue.getChannelTotal(),
						FixedConstant.SPLICER,businessRouteValue.getChannelIndex(),
						FixedConstant.SPLICER,sequenceID
						);
				//未知
				businessRouteValue.setNextNodeCode(InsideStatusCodeConstant.UNKNOWN_CODE);
				businessRouteValue.setNextNodeErrorCode("");
				businessRouteValue.setChannelMessageID("");
				CacheBaseService.saveResponseToMiddlewareCache(businessRouteValue);
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
		}

		public BusinessRouteValue getBusinessRouteValue() {
			return businessRouteValue;
		}
		
	}
	
}


