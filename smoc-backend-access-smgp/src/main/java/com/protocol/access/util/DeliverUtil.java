/**
 * @desc
 * 
 */
package com.protocol.access.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.base.common.constant.FixedConstant;
import com.base.common.log.CategoryLog;
import com.base.common.manager.BusinessDataManager;
import com.base.common.util.DateUtil;
import com.protocol.access.smgp.pdu.Deliver;
import com.protocol.access.smgp.sms.ByteBuffer;
import com.protocol.access.smgp.sms.ShortMessage;
import com.protocol.access.manager.ReportTimerTaskWorkerManager;
import com.protocol.access.manager.SessionManager;
import com.protocol.access.vo.Report;


public class DeliverUtil {
	private static final Logger logger = LoggerFactory.getLogger(DeliverUtil.class);
	
	/**
	 * 封装smgp标准协议回执
	 * @param report
	 * @param version
	 * @return
	 * @throws DecoderException 
	 */
	private static Deliver packageDeliver(Report report, int version) throws DecoderException{
		
        Deliver delive = new Deliver();
	
        ByteBuffer buffer = new ByteBuffer(Hex.decodeHex(report.getMessageId().toCharArray()));
		delive.setMsgID(buffer.getBuffer());
		delive.setIsReport((byte) 1);
		delive.setMsgFormat((byte) 0);
		delive.setRecvTime(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_SECOND));			
		delive.setSrcTermID(report.getPhoneNumber());		
		delive.setDestTermID(report.getAccountSrcId()==null?"":report.getAccountSrcId());
		delive.assignSequenceNumber();
		
		
		ByteBuffer message = new ByteBuffer();
		
		message.appendBytes("id:".getBytes());
		message.appendBytes(buffer.getBuffer());
		message.appendBytes("sSub:001 dlvrd:001".getBytes());
	
		String submitTime =DateUtil.format(DateUtil.parseDate(report.getSubmitTime(),DateUtil.DATE_FORMAT_COMPACT_STANDARD_MILLI),DateUtil.DATE_FORMAT_COMPACT_CMPP);
		String submitData = new StringBuffer().append("sSubmit_date:").append(submitTime).append("s").toString();
		message.appendBytes(submitData.getBytes());
		
		String doneTime =DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_CMPP);
		String doneData = new StringBuffer().append("Done_date:").append(doneTime).append("s").toString();
		message.appendBytes(doneData.getBytes());
		String stat = new StringBuffer().append("Stat:").append(report.getStatusCode()).toString();
		message.appendBytes(stat.getBytes());
		String err = new StringBuffer().append("sErr:").append(getErr(report.getStatusCode())).append("sText:007").append(report.getStatusCode()).toString();
			
		message.appendBytes(err.getBytes());
		ShortMessage sm1 = new ShortMessage();
		sm1.setMessage(message.getBuffer(), (byte) 00);
		logger.info("message:"+sm1.getMessage());
		delive.setSm(sm1);
		delive.setReserve("");

		return delive;
	}
	
	private static String getErr(String stat) {
		String err = "";
		if("DELIVRD".equals(stat)) {
			err = "000";
		}else if("UNDELIV".equals(stat)){
			err = "908";
		}else if("REJECTD".equals(stat)){
			err = "174";
		}else if("EXPIRED".equals(stat)){
			err = "702";
		}else {
			err = "999";
		}
		return err;
	}
	
	public static void sendReport(IoSession session,Report report){
		
		try {
			//设置推送次数
			report.setReportPushTimes(report.getReportPushTimes() + 1);
			//获取最大推送次数
			int maxPushTimes = BusinessDataManager.getInstance().getAccountReportRepeatPushTimes(report.getAccountId());
			//当小于推送次数则增加定时任务保证成功接收
			if( report.getReportPushTimes() < maxPushTimes){
				ReportTimerTaskWorkerManager.getInstance().addReportTimeoutTask(report);
			}
			
			if(session != null && !session.isClosing() && session.isConnected()){
				
				com.protocol.access.smgp.pdu.Deliver delive = packageDeliver(report, SessionManager
						.getInstance().getSessionVersion(session));
				//记录状态报告的参数
				CategoryLog.messageLogger.info("SMGP_DELIVER{}{}",FixedConstant.LOG_SEPARATOR,delive.dump());
				
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
						FixedConstant.LOG_SEPARATOR,delive.header.getSequenceID(),
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
						FixedConstant.LOG_SEPARATOR,delive.header.getSequenceID(),
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
	}
}


