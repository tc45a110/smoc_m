/**
 * @desc
 * 
 */
package com.protocol.proxy.manager;

import com.base.common.cache.CacheBaseService;
import com.base.common.constant.DynamicConstant;
import com.base.common.constant.FixedConstant;
import com.base.common.constant.InsideStatusCodeConstant;
import com.base.common.log.CategoryLog;
import com.base.common.util.DateUtil;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperCacheWorker;
import com.base.common.worker.SuperQueueWorker;
import com.huawei.insa2.comm.sgip.message.SGIPMessage;
import com.huawei.insa2.comm.sgip.message.SGIPReportMessage;

public class ReportWorkerManager extends SuperQueueWorker<SGIPMessage>{

	private static ReportWorkerManager manager = new ReportWorkerManager();
	
	public static ReportWorkerManager getInstance(){
		return manager;
	}
	
	public void process(SGIPMessage message){
		add(message);
	}
	
	private ReportWorkerManager(){
		//启动cpu的数量
		for(int i=0;i<FixedConstant.CPU_NUMBER;i++){
			ReportWorker reportWorker = new ReportWorker();
			reportWorker.setName(new StringBuilder("ReportWorker-").append(i+1).toString());
			reportWorker.start();
		}
		this.start();
	}
	
	@Override
	protected void doRun() throws Exception {
		Thread.sleep(FixedConstant.COMMON_MONITOR_INTERVAL_TIME);
		logger.info("状态报告缓存队列数量{}",size());
	}
	
	/**
	 * 状态报告：Report命令用于向SP发送一条先前的Submit命令的当前状态。
	 */
	class ReportWorker extends SuperCacheWorker{
		
		@Override
		protected void doRun() throws Exception {
			SGIPReportMessage reportMessage = (SGIPReportMessage)superQueue.take();
			CategoryLog.messageLogger.info(reportMessage.toString());
			
			String channelReportSRCID=null;
			String phoneNumber=reportMessage.getUserNumber();
			if (phoneNumber.length() > 11) {
				phoneNumber = phoneNumber.substring(phoneNumber
						.length() - 11);
			}
			String channelMessageID=reportMessage.getSubmitSequenceNumber();
			String sequenceID = String.valueOf(reportMessage.getSequenceId());
			
			processChannelReport(String.valueOf(reportMessage.getState()),String.valueOf(reportMessage.getErrorCode()),channelReportSRCID, channelMessageID, phoneNumber,sequenceID);
			
		}
		
		/**
		 * 处理状态报告
		 * @param statusCode
		 * @param channelReportSRCID
		 * @param channelMessageID
		 * @param phoneNumber
		 */
		private void processChannelReport(String state,String errorCode,String channelReportSRCID,String channelMessageID,String phoneNumber,String sequenceID){
			logger.info(
					new StringBuilder().append("回执信息")
					.append("{}phoneNumber={}")
					.append("{}channelMessageID={}")
					.append("{}channelReportSRCID={}")
					.append("{}state={}")
					.append("{}errorCode={}")
					.append("{}sequenceID={}")
					.toString(),
					FixedConstant.SPLICER,phoneNumber,
					FixedConstant.SPLICER,channelMessageID,
					FixedConstant.SPLICER,channelReportSRCID,
					FixedConstant.SPLICER,state,
					FixedConstant.SPLICER,errorCode,
					FixedConstant.SPLICER,sequenceID
					);
			
			BusinessRouteValue businessRouteValue = new BusinessRouteValue();
		
			businessRouteValue.setSubStatusCode("");
			businessRouteValue.setStatusCodeSource(FixedConstant.StatusReportSource.CHANNEL.name());
			//根据协议层设置或默认的成功状态码判断后设置成功码标识
			if(DynamicConstant.REPORT_SUCCESS_CODE.equals(state)){
				businessRouteValue.setSuccessCode(InsideStatusCodeConstant.SUCCESS_CODE);
				//state=0时以DELIVRD为标准
				businessRouteValue.setStatusCode(InsideStatusCodeConstant.StatusCode.DELIVRD.name());
			}else{
				businessRouteValue.setSuccessCode(InsideStatusCodeConstant.FAIL_CODE);
				//若errorCode=0则设置错误码
				if(DynamicConstant.REPORT_SUCCESS_CODE.equals(errorCode)){
					businessRouteValue.setStatusCode(InsideStatusCodeConstant.FAIL_CODE);
				}else{
					businessRouteValue.setStatusCode(errorCode);
				}
			}
			businessRouteValue.setChannelReportSRCID(channelReportSRCID);
			businessRouteValue.setChannelMessageID(channelMessageID);
			businessRouteValue.setPhoneNumber(phoneNumber);
			businessRouteValue.setChannelReportTime(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_MILLI));
			
			CacheBaseService.saveReportToMiddlewareCache(businessRouteValue);
		}
		
		public void exit(){
			//停止线程
			super.exit();
		}
		
	}

}


