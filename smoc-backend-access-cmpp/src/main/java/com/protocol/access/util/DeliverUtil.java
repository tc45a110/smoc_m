/**
 * @desc
 * 
 */
package com.protocol.access.util;

import com.protocol.access.manager.ReportTimerTaskWorkerManager;
import com.protocol.access.manager.SessionManager;
import com.protocol.access.vo.Report;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.protocol.access.cmpp.CmppConstant;
import com.protocol.access.cmpp.sms.ByteBuffer;
import com.protocol.access.cmpp.sms.ShortMessage;
import com.base.common.constant.FixedConstant;
import com.base.common.log.CategoryLog;
import com.base.common.util.DateUtil;

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
			messageData.appendString(report.getPhoneNumber(), 21);
		}else{
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
		String msgid = report.getMessageId();
		com.protocol.access.cmpp.pdu.Deliver delive = null;
		// 当写入给client成功后，result 为 true
		try {
			if (session != null && session.isConnected()) {

				delive = packageDeliver(report, SessionManager
						.getInstance().getSessionVersion(session));
				if(!report.getDbFlag()) {
					ReportTimerTaskWorkerManager.getInstance().addReportTimeoutTask(report);
				}
				
				session.write(delive);
				
				logger.info(new StringBuilder().append("状态报告推送成功:")
						.append("client={}")
						.append("{}msgid={}")
						.append("{}mobile={}")
						.append("{}stat={}").toString(),
						report.getAccountId(),
						FixedConstant.LOG_SEPARATOR,msgid,
						FixedConstant.LOG_SEPARATOR,report.getPhoneNumber(),
						FixedConstant.LOG_SEPARATOR,report.getStatusCode());
				

			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
	}
}


