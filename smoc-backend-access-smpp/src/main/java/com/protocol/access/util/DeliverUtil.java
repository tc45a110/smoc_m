/**
 * @desc
 * 
 */
package com.protocol.access.util;

import java.io.UnsupportedEncodingException;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.common.constant.FixedConstant;
import com.base.common.log.CategoryLog;
import com.base.common.manager.BusinessDataManager;
import com.base.common.util.DateUtil;
import com.protocol.access.smpp.sms.ByteBuffer;
import com.protocol.access.manager.ReportTimerTaskWorkerManager;
import com.protocol.access.manager.SessionManager;
import com.protocol.access.smpp.util.HexUtil;
import com.protocol.access.vo.Report;

public class DeliverUtil {
	private static final Logger logger = LoggerFactory
			.getLogger(DeliverUtil.class);
	
	/**
	 * 封装cmpp标准协议回执
	 * @param report
	 * @param version
	 * @return
	 */
	private static com.protocol.access.smpp.pdu.Deliver packageDeliver(Report report, int version){
		com.protocol.access.smpp.pdu.Deliver delive = new com.protocol.access.smpp.pdu.Deliver();
		
		String submitTime = DateUtil.format(
				DateUtil.parseDate(report.getSubmitTime(), DateUtil.DATE_FORMAT_COMPACT_STANDARD_MILLI),
				DateUtil.DATE_FORMAT_COMPACT_SECOND);
		String doneTime = DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_SECOND);
		
		delive.setSourceAddress(report.getPhoneNumber());
		delive.setDestinationAddress(report.getAccountSrcId()==null?"":report.getAccountSrcId());
		delive.setEsmClass((byte)4);
		
		delive.assignSequenceNumber();
		
		ByteBuffer message = new ByteBuffer();
		
		message.appendBytes("id:".getBytes());
		message.appendBytes(HexUtil.convertHexStringToBytes(report.getMessageId()));
		message.appendBytes(" sub:001 dlvrd:001 ".getBytes());
		message.appendBytes(new StringBuilder().append("submit date:").append(submitTime).append(" ").toString().getBytes());
		message.appendBytes(new StringBuilder().append("done date:").append(doneTime).append(" ").toString().getBytes());
		message.appendBytes(new StringBuilder().append("stat:").append(report.getStatusCode()).toString().getBytes());
		message.appendBytes(" err:000 Text:".getBytes());

		delive.setDataCoding((byte)1);
		delive.setSmLength((byte)message.getBuffer().length);
		try {
			delive.setShortMessageText(new String(message.getBuffer(),"US-ASCII"));
		} catch (UnsupportedEncodingException e) {
			logger.error("封装SMPP标准协议回执发生错误:"+e.getMessage(),e);
		}
		
		//记录状态报告的参数
		CategoryLog.messageLogger.info("SMPP_DELIVER{}{}",FixedConstant.LOG_SEPARATOR,delive.toDeliverStr(report.getMessageId()));
		return delive;
	}
	
	public static void sendReport(IoSession session,Report report){
		
		try {
			//设置推送次数
			report.setReportPushTimes(report.getReportPushTimes() + 1);
			//获取最大推送次数
			int maxPushTimes = BusinessDataManager.getInstance().getAccountReportRepeatPushTimes(report.getAccountId());
			
			if(session != null && !session.isClosing() && session.isConnected()){
				com.protocol.access.smpp.pdu.Deliver delive = packageDeliver(report, SessionManager
						.getInstance().getSessionVersion(session));
				
				//当小于推送次数则增加定时任务保证成功接收
				if( report.getReportPushTimes() < maxPushTimes) {
					ReportTimerTaskWorkerManager.getInstance().addReportTimeoutTask(report,String.valueOf(delive.getSequenceNumber()));
				}
				logger.info(new StringBuilder().append("推送状态报告开始")
						.append("{}client={}")
						.append("{}msgid={}")
						.append("{}mobile={}")
						.append("{}stat={}")
						.append("{}sessionID={}")
						.append("{}sequenceID={}")
						.append("{}pushTimes={}")
						.toString(),
						FixedConstant.LOG_SEPARATOR,report.getAccountId(),
						FixedConstant.LOG_SEPARATOR,report.getMessageId(),
						FixedConstant.LOG_SEPARATOR,report.getPhoneNumber(),
						FixedConstant.LOG_SEPARATOR,report.getStatusCode(),
						FixedConstant.LOG_SEPARATOR,session.getId(),
						FixedConstant.LOG_SEPARATOR,delive.header.getSequenceNo(),
						FixedConstant.LOG_SEPARATOR,report.getReportPushTimes()
						);
				long start = System.currentTimeMillis();
				
				WriteFuture future  = session.write(delive);
				int count =0;
				boolean result = future.isWritten();
				while(!result){
					result = future.isWritten();
					if(result || count >10){
						break;
					}else{
						Thread.sleep(1);
						++count;
					}
				}
				logger.info(new StringBuilder().append("推送状态报告结束")
						.append("{}client={}")
						.append("{}msgid={}")
						.append("{}mobile={}")
						.append("{}stat={}")
						.append("{}future={}")
						.append("{}sessionID={}")
						.append("{}sequenceID={}")
						.append("{}pushTimes={}")
						.append("{}耗时={}毫秒")
						.toString(),
						FixedConstant.LOG_SEPARATOR,report.getAccountId(),
						FixedConstant.LOG_SEPARATOR,report.getMessageId(),
						FixedConstant.LOG_SEPARATOR,report.getPhoneNumber(),
						FixedConstant.LOG_SEPARATOR,report.getStatusCode(),
						FixedConstant.LOG_SEPARATOR,future.isWritten(),
						FixedConstant.LOG_SEPARATOR,session.getId(),
						FixedConstant.LOG_SEPARATOR,delive.header.getSequenceNo(),
						FixedConstant.LOG_SEPARATOR,report.getReportPushTimes(),
						FixedConstant.LOG_SEPARATOR,(System.currentTimeMillis() - start));
			}else{
				logger.error(new StringBuilder().append("推送状态报告异常")
						.append("{}client={}")
						.append("{}msgid={}")
						.append("{}mobile={}")
						.append("{}stat={}")
						.toString(),
						FixedConstant.LOG_SEPARATOR,report.getAccountId(),
						FixedConstant.LOG_SEPARATOR,report.getMessageId(),
						FixedConstant.LOG_SEPARATOR,report.getPhoneNumber(),
						FixedConstant.LOG_SEPARATOR,report.getStatusCode());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
	}
}


