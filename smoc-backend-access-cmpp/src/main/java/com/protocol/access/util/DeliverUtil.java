/**
 * @desc
 * 
 */
package com.protocol.access.util;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.common.constant.FixedConstant;
import com.base.common.log.CategoryLog;
import com.base.common.manager.BusinessDataManager;
import com.base.common.util.DateUtil;
import com.protocol.access.cmpp.CmppConstant;
import com.protocol.access.cmpp.sms.ByteBuffer;
import com.protocol.access.cmpp.sms.ShortMessage;
import com.protocol.access.manager.ReportTimerTaskWorkerManager;
import com.protocol.access.manager.SessionManager;
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
	private static com.protocol.access.cmpp.pdu.Deliver packageDeliver(Report report, int version){
		com.protocol.access.cmpp.pdu.Deliver delive;
		if (version == CmppConstant.VERSION2) {
			delive = new com.protocol.access.cmpp.pdu.Deliver20();
		} else {
			delive = new com.protocol.access.cmpp.pdu.Deliver();
		}
		String submitTime = DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_CMPP);
		String doneTime = DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_CMPP);

		delive.setMsgId(TypeConvert.long2byte(Long.parseLong(report.getMessageId())));
		delive.setDstId(report.getAccountSrcId()==null?"":report.getAccountSrcId());
		delive.setServiceId(report.getAccountBusinessCode());
		delive.setSrcTermId(report.getPhoneNumber());
		delive.setIsReport((byte) 1);
		delive.assignSequenceNumber();
		ShortMessage sm = new ShortMessage();
		ByteBuffer messageData = new ByteBuffer();
		messageData.appendBytes(TypeConvert.long2byte(Long.parseLong(report.getMessageId())), 8);
		messageData.appendString(report.getStatusCode(), 7);
		messageData.appendString(submitTime, 10);
		messageData.appendString(doneTime, 10);
		if (version == CmppConstant.VERSION2) {
			//2.0手机号占用字节数
			messageData.appendString(report.getPhoneNumber(), 21);
		}else{
			//3.0手机号占用字节数
			messageData.appendString(report.getPhoneNumber(), 32);
		}
		messageData.appendInt(30);
		sm.setMessage(messageData.getBuffer(), (byte) 00);
		delive.setSm(sm);
		delive.setLinkId("");
		
		//记录状态报告的参数
		CategoryLog.messageLogger.info(
				new StringBuilder().append("CMPP_DELIVER:")
				.append("Sequence_Id={}")
				.append("{}MsgId={}")
				.append("{}DestnationId={}")
				.append("{}ServiceId={}")
				.append("{}TpPid={}")
				.append("{}TpUdhi={}")
				.append("{}MsgFmt={}")
				.append("{}SrcterminalId={}")
				.append("{}SrcterminalType={}")
				.append("{}RegisteredDeliver={}")
				.append("{}MsgLength={}")
				.append("{}Stat={}")
				.append("{}SubmitTime={}")
				.append("{}DoneTime={}")
				.append("{}DestTerminalId={}")
				.append("{}SMSCSequence={}")
				.append("{}LinkID={}")
				.toString(),
				delive.getSequenceNumber(),
				FixedConstant.LOG_SEPARATOR,report.getMessageId(),
				FixedConstant.LOG_SEPARATOR,delive.getDstId(),
				FixedConstant.LOG_SEPARATOR,delive.getServiceId(),
				FixedConstant.LOG_SEPARATOR,delive.getTpPid(),
				FixedConstant.LOG_SEPARATOR,delive.getTpUdhi(),
				FixedConstant.LOG_SEPARATOR,sm.getMsgFormat(),
				FixedConstant.LOG_SEPARATOR,delive.getSrcTermId(),
				FixedConstant.LOG_SEPARATOR,delive.getSrcTermType(),
				FixedConstant.LOG_SEPARATOR,delive.getIsReport(),
				FixedConstant.LOG_SEPARATOR,sm.getLength(),
				FixedConstant.LOG_SEPARATOR,report.getStatusCode(),
				FixedConstant.LOG_SEPARATOR,submitTime,
				FixedConstant.LOG_SEPARATOR,doneTime,
				FixedConstant.LOG_SEPARATOR,report.getPhoneNumber(),
				FixedConstant.LOG_SEPARATOR,30,
				FixedConstant.LOG_SEPARATOR,delive.getLinkId()
				);
		return delive;
	}
	
	public static void sendReport(IoSession session,Report report){
		
		try {
			//设置推送次数
			report.setReportPushTimes(report.getReportPushTimes() + 1);
			//获取最大推送次数
			int maxPushTimes = BusinessDataManager.getInstance().getAccountReportRepeatPushTimes(report.getAccountId());
			//当小于推送次数则增加定时任务保证成功接收
			if( report.getReportPushTimes() < maxPushTimes) {
				ReportTimerTaskWorkerManager.getInstance().addReportTimeoutTask(report);
			}
			
			if(session != null && !session.isClosing() && session.isConnected()){
				com.protocol.access.cmpp.pdu.Deliver delive = packageDeliver(report, SessionManager
						.getInstance().getSessionVersion(session));
				logger.info(new StringBuilder().append("推送状态报告开始:")
						.append("client={}")
						.append("{}msgid={}")
						.append("{}mobile={}")
						.append("{}stat={}")
						.append("{}sessionID={}")
						.append("{}sequenceID={}")
						.append("{}pushTimes={}")
						.toString(),
						report.getAccountId(),
						FixedConstant.LOG_SEPARATOR,report.getMessageId(),
						FixedConstant.LOG_SEPARATOR,report.getPhoneNumber(),
						FixedConstant.LOG_SEPARATOR,report.getStatusCode(),
						FixedConstant.LOG_SEPARATOR,session.getId(),
						FixedConstant.LOG_SEPARATOR,delive.getSequenceNumber(),
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
				logger.info(new StringBuilder().append("推送状态报告结束:")
						.append("client={}")
						.append("{}msgid={}")
						.append("{}mobile={}")
						.append("{}stat={}")
						.append("{}future={}")
						.append("{}sessionID={}")
						.append("{}sequenceID={}")
						.append("{}pushTimes={}")
						.append("{}耗时={}毫秒")
						.toString(),
						report.getAccountId(),
						FixedConstant.LOG_SEPARATOR,report.getMessageId(),
						FixedConstant.LOG_SEPARATOR,report.getPhoneNumber(),
						FixedConstant.LOG_SEPARATOR,report.getStatusCode(),
						FixedConstant.LOG_SEPARATOR,future.isWritten(),
						FixedConstant.LOG_SEPARATOR,session.getId(),
						FixedConstant.LOG_SEPARATOR,delive.getSequenceNumber(),
						FixedConstant.LOG_SEPARATOR,report.getReportPushTimes(),
						FixedConstant.LOG_SEPARATOR,(System.currentTimeMillis() - start));
			}else{
				logger.error(new StringBuilder().append("推送状态报告异常:")
						.append("client={}")
						.append("{}msgid={}")
						.append("{}mobile={}")
						.append("{}stat={}")
						.toString(),
						report.getAccountId(),
						FixedConstant.LOG_SEPARATOR,report.getMessageId(),
						FixedConstant.LOG_SEPARATOR,report.getPhoneNumber(),
						FixedConstant.LOG_SEPARATOR,report.getStatusCode());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	
		/*com.protocol.access.cmpp.pdu.Deliver delive = null;
		// 当写入给client成功后，result 为 true
		try {
			if (!session.isClosing() && session.isConnected()) {
				
				delive = packageDeliver(report, SessionManager
						.getInstance().getSessionVersion(session));
				report.setReportPushTimes(report.getReportPushTimes() + 1);
				
				int pushTimes = BusinessDataManager.getInstance().getAccountReportRepeatPushTimes(report.getAccountId());
				//当小于推送次数则增加定时任务保证成功接收
				if( report.getReportPushTimes() < pushTimes) {
					ReportTimerTaskWorkerManager.getInstance().addReportTimeoutTask(report);
				}
				logger.info(new StringBuilder().append("推送状态报告开始:")
						.append("client={}")
						.append("{}msgid={}")
						.append("{}mobile={}")
						.append("{}stat={}")
						.append("{}sessionID={}")
						.append("{}sequenceID={}")
						.append("{}pushTimes={}")
						.toString(),
						report.getAccountId(),
						FixedConstant.LOG_SEPARATOR,report.getMessageId(),
						FixedConstant.LOG_SEPARATOR,report.getPhoneNumber(),
						FixedConstant.LOG_SEPARATOR,report.getStatusCode(),
						FixedConstant.LOG_SEPARATOR,session.getId(),
						FixedConstant.LOG_SEPARATOR,delive.getSequenceNumber(),
						FixedConstant.LOG_SEPARATOR,report.getReportPushTimes()
						);

				WriteFuture future = session.write(delive);
				long start = System.currentTimeMillis();
				future.awaitUninterruptibly();
				boolean isDone = future.isDone();
				logger.info(new StringBuilder().append("推送状态报告完成:")
						.append("client={}")
						.append("{}msgid={}")
						.append("{}mobile={}")
						.append("{}stat={}")
						.append("{}future={}")
						.append("{}isDone={}")
						.append("{}sessionID={}")
						.append("{}sequenceID={}")
						.append("{}pushTimes={}")
						.append("{}耗时={}毫秒")
						.toString(),
						report.getAccountId(),
						FixedConstant.LOG_SEPARATOR,report.getMessageId(),
						FixedConstant.LOG_SEPARATOR,report.getPhoneNumber(),
						FixedConstant.LOG_SEPARATOR,report.getStatusCode(),
						FixedConstant.LOG_SEPARATOR,future.isWritten(),
						FixedConstant.LOG_SEPARATOR,isDone,
						FixedConstant.LOG_SEPARATOR,session.getId(),
						FixedConstant.LOG_SEPARATOR,delive.getSequenceNumber(),
						FixedConstant.LOG_SEPARATOR,report.getReportPushTimes(),
						FixedConstant.LOG_SEPARATOR,(System.currentTimeMillis() - start));

			}else{
				logger.warn(new StringBuilder().append("连接已中断推送状态报告终止:")
						.append("client={}")
						.append("{}msgid={}")
						.append("{}mobile={}")
						.append("{}stat={}")
						.append("{}sessionID={}")
						.toString(),
						report.getAccountId(),
						FixedConstant.LOG_SEPARATOR,report.getMessageId(),
						FixedConstant.LOG_SEPARATOR,report.getPhoneNumber(),
						FixedConstant.LOG_SEPARATOR,report.getStatusCode(),
						FixedConstant.LOG_SEPARATOR,session.getId());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}*/
		
	}
}


